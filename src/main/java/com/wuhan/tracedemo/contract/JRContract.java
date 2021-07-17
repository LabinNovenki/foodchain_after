package com.wuhan.tracedemo.contract;

import com.alipay.mychain.sdk.api.MychainClient;
import com.alipay.mychain.sdk.api.env.ClientEnv;
import com.alipay.mychain.sdk.api.env.ISslOption;
import com.alipay.mychain.sdk.api.env.SignerOption;
import com.alipay.mychain.sdk.api.env.SslBytesOption;
import com.alipay.mychain.sdk.api.logging.AbstractLoggerFactory;
import com.alipay.mychain.sdk.api.logging.ILogger;
import com.alipay.mychain.sdk.api.utils.ConfidentialUtil;
import com.alipay.mychain.sdk.api.utils.Utils;
import com.alipay.mychain.sdk.common.VMTypeEnum;
import com.alipay.mychain.sdk.crypto.MyCrypto;
import com.alipay.mychain.sdk.crypto.keyoperator.Pkcs8KeyOperator;
import com.alipay.mychain.sdk.crypto.keypair.Keypair;
import com.alipay.mychain.sdk.crypto.signer.SignerBase;
import com.alipay.mychain.sdk.domain.account.Identity;
import com.alipay.mychain.sdk.errorcode.ErrorCode;
import com.alipay.mychain.sdk.message.transaction.AbstractTransactionRequest;
import com.alipay.mychain.sdk.message.transaction.TransactionReceiptResponse;
import com.alipay.mychain.sdk.message.transaction.confidential.ConfidentialRequest;
import com.alipay.mychain.sdk.message.transaction.contract.CallContractRequest;
import com.alipay.mychain.sdk.message.transaction.contract.DeployContractRequest;
import com.alipay.mychain.sdk.type.BaseFixedSizeUnsignedInteger;
import com.alipay.mychain.sdk.utils.ByteUtils;
import com.alipay.mychain.sdk.utils.IOUtil;
import com.alipay.mychain.sdk.utils.RandomUtil;
import com.alipay.mychain.sdk.vm.EVMOutput;
import com.alipay.mychain.sdk.vm.EVMParameter;
import com.wuhan.tracedemo.entity.CommentInfo;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * @author Chris
 * @date 2021/5/29 18:22
 * @Email:gang.wu@nexgaming.com
 */
public class JRContract {
    private static String contractCodeString = "0x608060405234801561001057600080fd5b50336002819055506112ba806100276000396000f30060806040526004361061004c576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806344c3a384146100515780636dc788341461008e575b600080fd5b34801561005d57600080fd5b5061007860048036036100739190810190610ed3565b6100ce565b6040516100859190611140565b60405180910390f35b34801561009a57600080fd5b506100b560048036036100b09190810190610e92565b610593565b6040516100c594939291906110bf565b60405180910390f35b60006100d982610a62565b15156101a957600082908060018154018082558091505090600182039060005260206000200160009091929091909150908051906020019061011c929190610d48565b505060006001836040518082805190602001908083835b6020831015156101585780518252602082019150602081019050602083039250610133565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051809103902060050160006101000a81548161ffff021916908361ffff1602179055505b6001826040518082805190602001908083835b6020831015156101e157805182526020820191506020810190506020830392506101bc565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020600001859080600181540180825580915050906001820390600052602060002001600090919290919091509080519060200190610252929190610d48565b50506001826040518082805190602001908083835b60208310151561028c5780518252602082019150602081019050602083039250610267565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051809103902060020184908060018154018082558091505090600182039060005260206000209060209182820401919006909192909190916101000a81548160ff021916908360ff160217905550506001826040518082805190602001908083835b602083101515610340578051825260208201915060208101905060208303925061031b565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390206001018390806001815401808255809150509060018203906000526020600020016000909192909190915090805190602001906103b1929190610d48565b50506001826040518082805190602001908083835b6020831015156103eb57805182526020820191506020810190506020830392506103c6565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390206003016001430190806001815401808255809150509060018203906000526020600020016000909192909190915055506001826040518082805190602001908083835b6020831015156104865780518252602082019150602081019050602083039250610461565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390206004014290806001815401808255809150509060018203906000526020600020016000909192909190915055508360ff166001836040518082805190602001908083835b60208310151561052257805182526020820191506020810190506020830392506104fd565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051809103902060050160008282829054906101000a900461ffff160192506101000a81548161ffff021916908361ffff160217905550600143019050949350505050565b6060806060806105a1610dc8565b60608060006105af89610a62565b15156105f0576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016105e790611120565b60405180910390fd5b6001896040518082805190602001908083835b6020831015156106285780518252602082019150602081019050602083039250610603565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051809103902060c0604051908101604052908160008201805480602002602001604051908101604052809291908181526020016000905b82821015610748578382906000526020600020018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156107345780601f1061070957610100808354040283529160200191610734565b820191906000526020600020905b81548152906001019060200180831161071757829003601f168201915b50505050508152602001906001019061068c565b50505050815260200160018201805480602002602001604051908101604052809291908181526020016000905b82821015610831578382906000526020600020018054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561081d5780601f106107f25761010080835404028352916020019161081d565b820191906000526020600020905b81548152906001019060200180831161080057829003601f168201915b505050505081526020019060010190610775565b505050508152602001600282018054806020026020016040519081016040528092919081815260200182805480156108ae57602002820191906000526020600020906000905b82829054906101000a900460ff1660ff16815260200190600101906020826000010492830192600103820291508084116108775790505b505050505081526020016003820180548060200260200160405190810160405280929190818152602001828054801561090657602002820191906000526020600020905b8154815260200190600101908083116108f2575b505050505081526020016004820180548060200260200160405190810160405280929190818152602001828054801561095e57602002820191906000526020600020905b81548152602001906001019080831161094a575b505050505081526020016005820160009054906101000a900461ffff1661ffff1661ffff168152505093506020604051908101604052806000815250925060206040519081016040528060008152509150600090505b836000015151811015610a36576109e6838560000151838151811015156109d757fe5b90602001906020020151610b8a565b9250610a27836040805190810160405280600181526020017f2300000000000000000000000000000000000000000000000000000000000000815250610b8a565b925080806001019150506109b4565b828460600151856080015186604001518292508191508090509750975097509750505050509193509193565b600080600090505b600080549050811015610b7f57600081815481101515610a8657fe5b906000526020600020016040518082805460018160011615610100020316600290048015610aeb5780601f10610ac9576101008083540402835291820191610aeb565b820191906000526020600020905b815481529060010190602001808311610ad7575b5050915050604051809103902060001916836040518082805190602001908083835b602083101515610b325780518252602082019150602081019050602083039250610b0d565b6001836020036101000a0380198251168184511680821785525050505050509050019150506040518091039020600019161415610b725760019150610b84565b8080600101915050610a6a565b600091505b50919050565b60608060608060008087945086935083518551016040519080825280601f01601f191660200182016040528015610bd05781602001602082028038833980820191505090505b509250600091505b8451821015610c85578482815181101515610bef57fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f0100000000000000000000000000000000000000000000000000000000000000028383815181101515610c4857fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a9053508180600101925050610bd8565b600090505b8351811015610d3a578381815181101515610ca157fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f0100000000000000000000000000000000000000000000000000000000000000028382875101815181101515610cfd57fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a9053508080600101915050610c8a565b829550505050505092915050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610d8957805160ff1916838001178555610db7565b82800160010185558215610db7579182015b82811115610db6578251825591602001919060010190610d9b565b5b509050610dc49190610e03565b5090565b60c0604051908101604052806060815260200160608152602001606081526020016060815260200160608152602001600061ffff1681525090565b610e2591905b80821115610e21576000816000905550600101610e09565b5090565b90565b600082601f8301121515610e3b57600080fd5b8135610e4e610e4982611188565b61115b565b91508082526020830160208301858383011115610e6a57600080fd5b610e7583828461122d565b50505092915050565b6000610e8a8235611220565b905092915050565b600060208284031215610ea457600080fd5b600082013567ffffffffffffffff811115610ebe57600080fd5b610eca84828501610e28565b91505092915050565b60008060008060808587031215610ee957600080fd5b600085013567ffffffffffffffff811115610f0357600080fd5b610f0f87828801610e28565b9450506020610f2087828801610e7e565b935050604085013567ffffffffffffffff811115610f3d57600080fd5b610f4987828801610e28565b925050606085013567ffffffffffffffff811115610f6657600080fd5b610f7287828801610e28565b91505092959194509250565b6000610f89826111ce565b808452602084019350610f9b836111b4565b60005b82811015610fcd57610fb18683516110a1565b610fba826111ef565b9150602086019550600181019050610f9e565b50849250505092915050565b6000610fe4826111d9565b808452602084019350610ff6836111c1565b60005b828110156110285761100c8683516110b0565b611015826111fc565b9150602086019550600181019050610ff9565b50849250505092915050565b600061103f826111e4565b80845261105381602086016020860161123c565b61105c8161126f565b602085010191505092915050565b6000601182527fe5ba97e5aeb66964e697a0e69588efbc810000000000000000000000000000006020830152604082019050919050565b6110aa81611209565b82525050565b6110b981611213565b82525050565b600060808201905081810360008301526110d98187611034565b905081810360208301526110ed8186610f7e565b905081810360408301526111018185610f7e565b905081810360608301526111158184610fd9565b905095945050505050565b600060208201905081810360008301526111398161106a565b9050919050565b600060208201905061115560008301846110a1565b92915050565b6000604051905081810181811067ffffffffffffffff8211171561117e57600080fd5b8060405250919050565b600067ffffffffffffffff82111561119f57600080fd5b601f19601f8301169050602081019050919050565b6000602082019050919050565b6000602082019050919050565b600081519050919050565b600081519050919050565b600081519050919050565b6000602082019050919050565b6000602082019050919050565b6000819050919050565b600060ff82169050919050565b600060ff82169050919050565b82818337600083830152505050565b60005b8381101561125a57808201518184015260208101905061123f565b83811115611269576000848401525b50505050565b6000601f19601f83011690509190505600a265627a7a72305820d66d2f6217b56c40442c762059001ae3b1f4516dcdb4fe3c4b09032cfe8e95636c6578706572696d656e74616cf50037";
    private static byte[] contractCode = ByteUtils.hexStringToBytes(contractCodeString); //CreditManager

    //upgrade
    private static String contractUpdateCodeString = "60806040526004361061006d576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680631b3c4fab1461007257806357fce39d14610187578063af7c102c146101b2578063b2628df8146101f3578063d448601914610242575b600080fd5b34801561007e57600080fd5b50610087610291565b60405180868152602001806020018060200185151515158152602001848152602001838103835287818151815260200191508051906020019080838360005b838110156100e15780820151818401526020810190506100c6565b50505050905090810190601f16801561010e5780820380516001836020036101000a031916815260200191505b50838103825286818151815260200191508051906020019080838360005b8381101561014757808201518184015260208101905061012c565b50505050905090810190601f1680156101745780820380516001836020036101000a031916815260200191505b5097505050505050505060405180910390f35b34801561019357600080fd5b5061019c610337565b6040518082815260200191505060405180910390f35b3480156101be57600080fd5b506101dd60048036038101908080359060200190929190505050610341565b6040518082815260200191505060405180910390f35b3480156101ff57600080fd5b50610228600480360381019080803590602001909291908035906020019092919050505061035e565b604051808215151515815260200191505060405180910390f35b34801561024e57600080fd5b506102776004803603810190808035906020019092919080359060200190929190505050610523565b604051808215151515815260200191505060405180910390f35b6000606080600080606080600080600033905060c89250600091506040805190810160405280600781526020017f6a72626c6f636b0000000000000000000000000000000000000000000000000081525094506040805190810160405280601a81526020017f32303231303533316a72626c6f636b636f6e7261637463616c6c000000000000815250935082858584849950995099509950995050505050509091929394565b6000600254905090565b600060036000838152602001908152602001600020549050919050565b6000600254331415156103d9576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260118152602001807f5065726d697373696f6e2064656e69656400000000000000000000000000000081525060200191505060405180910390fd5b6000548260015401131580156103f457506001548260015401135b80156104005750600082135b1515610474576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600e8152602001807f496e76616c69642076616c75652100000000000000000000000000000000000081525060200191505060405180910390fd5b8160036000858152602001908152602001600020600082825401925050819055508160016000828254019250508190555081837f9a46fdc9277c739031110f773b36080a9a2012d0b3eca1f5ed8a3403973e05576001546040518080602001838152602001828103825260048152602001807f64656d6f000000000000000000000000000000000000000000000000000000008152506020019250505060405180910390a36001905092915050565b6000816003600033815260200190815260200160002054121515156105b0576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260138152602001807f62616c616e6365206e6f7420656e6f756768210000000000000000000000000081525060200191505060405180910390fd5b6000821380156105c257506000548213155b1515610636576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600e8152602001807f496e76616c69642076616c75652100000000000000000000000000000000000081525060200191505060405180910390fd5b81600360003381526020019081526020016000206000828254039250508190555081600360008581526020019081526020016000206000828254019250508190555060019050929150505600a165627a7a72305820929f39f5dfc978f05e029b986659fd7542e1009cbbb133b2bc009f8876b59c910029";
    private static byte[] contractUpdateCode = ByteUtils.hexStringToBytes(contractUpdateCodeString); //CreditManager



    /**
     * contract id
     */
    private static String callContractId = "wuda1626502918027";

    private static final String account = "chrisblocktest";
    private static Identity userIdentity;
    private static Keypair userKeypair;

    /**
     * sdk client
     */
    private static MychainClient sdk;

    /**
     * client key password
     */
    private static String keyPassword = "Local#123";
    /**
     * user password
     */
    private static String userPassword = "Local#123";
    /**
     * host ip
     */

    private static String host = "47.103.163.48";

    /**
     * server port
     */
    private static int port = 18130;
    /**
     * trustCa password.
     */
    private static String trustStorePassword = "mychain";
    /**
     * mychain environment
     */
    private static ClientEnv env;
    /**
     * mychain is tee Chain
     */
    private static boolean isTeeChain = false;
    /**
     * tee chain publicKeys
     */
    private static List<byte[]> publicKeys = new ArrayList<byte[]>();
    /**
     * tee chain secretKey
     */
    private static String secretKey = "123456";


    public static void initMychainEnv() throws IOException {
        // any user key for sign message
        String userPrivateKeyFile = "user.key";
        userIdentity = Utils.getIdentityByName(account);
        Pkcs8KeyOperator pkcs8KeyOperator = new Pkcs8KeyOperator();
        userKeypair = pkcs8KeyOperator.load(IOUtil.inputStreamToByte(JRContract.class.getClassLoader().getResourceAsStream(userPrivateKeyFile)), userPassword);

        // use publicKeys by tee
        if (isTeeChain) {
            Keypair keypair = new Pkcs8KeyOperator()
                    .loadPubkey(
                            IOUtil.inputStreamToByte(JRContract.class.getClassLoader().getResourceAsStream("test_seal_pubkey.pem")));
            byte[] publicKeyDer = keypair.getPubkeyEncoded();
            publicKeys.add(publicKeyDer);
        }

        env = buildMychainEnv();
        ILogger logger = AbstractLoggerFactory.getInstance(JRContract.class);
        env.setLogger(logger);
    }

    private static ClientEnv buildMychainEnv() throws IOException {
        InetSocketAddress inetSocketAddress = InetSocketAddress.createUnresolved(host, port);
        String keyFilePath = "client.key";
        String certFilePath = "client.crt";
        String trustStoreFilePath = "trustCa";
        // build ssl option
        ISslOption sslOption = new SslBytesOption.Builder()
                .keyBytes(IOUtil.inputStreamToByte(JRContract.class.getClassLoader().getResourceAsStream(keyFilePath)))
                .certBytes(IOUtil.inputStreamToByte(JRContract.class.getClassLoader().getResourceAsStream(certFilePath)))
                .keyPassword(keyPassword)
                .trustStorePassword(trustStorePassword)
                .trustStoreBytes(
                        IOUtil.inputStreamToByte(JRContract.class.getClassLoader().getResourceAsStream(trustStoreFilePath)))
                .build();

        List<InetSocketAddress> socketAddressArrayList = new ArrayList<InetSocketAddress>();
        socketAddressArrayList.add(inetSocketAddress);

        List<SignerBase> signerBaseList = new ArrayList<SignerBase>();
        SignerBase signerBase = MyCrypto.getInstance().createSigner(userKeypair);
        signerBaseList.add(signerBase);
        SignerOption signerOption = new SignerOption();
        signerOption.setSigners(signerBaseList);
        return ClientEnv.build(socketAddressArrayList, sslOption, signerOption);
    }

    public static void initSdk() {
        sdk = new MychainClient();
        boolean initResult = sdk.init(env);
        if (!initResult) {
            exit("initSdk", "sdk init failed.");
        }else{
            System.out.println("sdk init success");
        }
    }

    private static String getErrorMsg(int errorCode) {
        int minMychainSdkErrorCode = ErrorCode.SDK_INTERNAL_ERROR.getErrorCode();
        if (errorCode < minMychainSdkErrorCode) {
            return ErrorCode.valueOf(errorCode).getErrorDesc();
        } else {
            return ErrorCode.valueOf(errorCode).getErrorDesc();
        }
    }

    private static void exit(String tag, String msg) {
        exit(String.format("%s error : %s ", tag, msg));
    }

    private static void exit(String msg) {
        System.out.println(msg);
        System.exit(0);
    }

    private static void signRequest(AbstractTransactionRequest request) {
        // sign request
        long ts = sdk.getNetwork().getSystemTimestamp();
        request.setTxTimeNonce(ts, BaseFixedSizeUnsignedInteger.Fixed64BitUnsignedInteger
                .valueOf(RandomUtil.randomize(ts + request.getTransaction().hashCode())), true);
        request.complete();
        sdk.getConfidentialService().signRequest(env.getSignerOption().getSigners(), request);
    }

    private static void deployContract() {
        EVMParameter contractParameters = new EVMParameter();
        String contractId = "wuda" + System.currentTimeMillis();

        // build DeployContractRequest
        DeployContractRequest request = new DeployContractRequest(userIdentity,
                Utils.getIdentityByName(contractId), contractCode, VMTypeEnum.EVM,
                contractParameters, BigInteger.ZERO);

        TransactionReceiptResponse deployContractResult;
        if (isTeeChain) {
            signRequest(request);
            // generate transaction key
            byte[] transactionKey = ConfidentialUtil.keyGenerate(secretKey,
                    request.getTransaction().getHash().getValue());

            ConfidentialRequest confidentialRequest = new ConfidentialRequest(request, publicKeys, transactionKey);
            deployContractResult = sdk.getConfidentialService().confidentialRequest(confidentialRequest);
        } else {
            deployContractResult = sdk.getContractService().deployContract(request);
        }

        // deploy contract
        if (!deployContractResult.isSuccess()
                || deployContractResult.getTransactionReceipt().getResult() != 0) {
            exit("deployContract",
                    getErrorMsg((int) deployContractResult.getTransactionReceipt().getResult()));
        } else {
            System.out.println("deploy contract success.contact id is " + contractId);
        }
    }
/*######################################################*/
    private static String paserTime(BigInteger time){
        System.setProperty("user.timezone", "Asia/Shanghai");
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(tz);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String times = format.format(new Date(time.longValue()));
        //System.out.print("日期格式---->" + times);
        return times;
    }

    public static int callContractMakeComment(String comment,String bill, String shop_id, int star) {
        EVMParameter parameters = new EVMParameter("make_comment(string,uint8,string,string)");
        parameters.addString(String.valueOf(comment));
        parameters.addInt(BigInteger.valueOf(Integer.valueOf(star)));
        parameters.addString(String.valueOf(bill));
        parameters.addString(String.valueOf(shop_id));


        // build CallContractRequest
        CallContractRequest request = new CallContractRequest(userIdentity,
                Utils.getIdentityByName(callContractId), parameters, BigInteger.ZERO, VMTypeEnum.EVM);

        TransactionReceiptResponse callContractResult;
        if (isTeeChain) {
            signRequest(request);
            // generate transaction key
            byte[] transactionKey = ConfidentialUtil.keyGenerate(secretKey,
                    request.getTransaction().getHash().getValue());

            ConfidentialRequest confidentialRequest = new ConfidentialRequest(request, publicKeys, transactionKey);

            callContractResult = sdk.getConfidentialService().confidentialRequest(confidentialRequest);
        } else {
            callContractResult = sdk.getContractService().callContract(request);
        }

        if (!callContractResult.isSuccess() || callContractResult.getTransactionReceipt().getResult() != 0) {
            System.out.println("callContract Error :"  + getErrorMsg((int) callContractResult.getTransactionReceipt().getResult()));
            return -1;
        } else {
            byte[] output = callContractResult.getTransactionReceipt().getOutput();
            if (output == null) {
                exit("call make_comment function", "output failed");
                return -1;
            } else {
                // decode return values
                EVMOutput contractReturnValues = new EVMOutput(ByteUtils.toHexString(output));
                BigInteger block = contractReturnValues.getUint();
                System.out.println("call callContractMakeComment success, response value: " + block);
                return block.intValue();
            }
        }
    }

    public static <string> CommentInfo[] callContractQueryComment(String shop_id) {
        EVMParameter parameters = new EVMParameter("query_comment(string)");
        parameters.addString(String.valueOf(shop_id));
        // build CallContractRequest
        CallContractRequest request = new CallContractRequest(userIdentity,
                Utils.getIdentityByName(callContractId), parameters, BigInteger.ZERO, VMTypeEnum.EVM);

        TransactionReceiptResponse callContractResult;
        if (isTeeChain) {
            signRequest(request);
            // generate transaction key
            byte[] transactionKey = ConfidentialUtil.keyGenerate(secretKey,
                    request.getTransaction().getHash().getValue());

            ConfidentialRequest confidentialRequest = new ConfidentialRequest(request, publicKeys, transactionKey);

            callContractResult = sdk.getConfidentialService().confidentialRequest(confidentialRequest);
        } else {
            callContractResult = sdk.getContractService().callContract(request);
        }

        if (!callContractResult.isSuccess() || callContractResult.getTransactionReceipt().getResult() != 0) {
            System.out.println("callContract Error :"  + getErrorMsg((int) callContractResult.getTransactionReceipt().getResult()));
        } else {
            byte[] output = callContractResult.getTransactionReceipt().getOutput();
            if (output == null) {
                exit("call QueryComment function", "output failed");
            } else {
                // decode return values
                EVMOutput contractReturnValues = new EVMOutput(ByteUtils.toHexString(output));
                String comments = contractReturnValues.getString();
                //String comment_tmp = comments.replace(";", "#");
                String[] comment_list = comments.split("#");
                List block_list = contractReturnValues.getUintDynamicArray();
                List time_list = contractReturnValues.getUintDynamicArray();
                List star_list = contractReturnValues.getUintDynamicArray();
                int len = comment_list.length;
                CommentInfo all_info[] = new CommentInfo[len];
                for (int index = 0; index < len; index++) {
                    String time_tmp = paserTime((BigInteger) time_list.get(index));
                    BigInteger star_tmp = (BigInteger) star_list.get(index);
                    all_info[index] = new CommentInfo(comment_list[index], (BigInteger) block_list.get(index), time_tmp, star_tmp.intValue());
                }
                for (int index = 0; index < all_info.length; index++) {
                    all_info[index].display();
                }
                /*
                System.out.println( String.format("call callContractQueryComment function, comments:" +  comments));
                System.out.println( String.format("call callContractQueryComment function, comments:" +  comment_tmp));
                System.out.println( String.format("call callContractQueryComment function, comments:" +  comment_list.length));
                System.out.println( String.format("call callContractQueryComment function, blocks:" +  block_list));
                System.out.println( String.format("call callContractQueryComment function, times:" +  time_list));
                */
                return all_info;
            }
        }
        return  null;
    }


    /*#############################################################*/



//    public static void main(String[] args) throws Exception {
        //step 1:init mychain env.
//        initMychainEnv();
        //step 2: init sdk client
//        initSdk();

        //step 3 : deploy a contract using useridentity.
        //deployContract();
//        callContractMakeComment("好吃", "100kuai", "2", 3);
//        callContractMakeComment("非常好吃", "20kuai", "2", 4);
//        callContractQueryComment("2");
        //step 4 callContract.
        //String testAccount = "jraccount_1622277417038";
        //callContractIssueCredit(testAccount,800);
        //callContractQueryCredit(testAccount);
        //callContractTransferCredit(testAccount,100);

        //upgrade contract 调用
        //updateContractDemo();
        //callContractGetParamsTest();


        //freezeContract调用
        //freezeContractTest();
        //callContractQueryCredit(account);

        //unFreeze调用
        //unFreezeContractTest();

//        System.in.read();
        //step 5 : sdk shut down
//        sdk.shutDown();
//    }
}
