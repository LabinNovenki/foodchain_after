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
    private static String contractCodeString = "0x608060405234801561001057600080fd5b5033600281905550610f3a806100276000396000f30060806040526004361061004c576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168063533d56e9146100515780636dc788341461008e575b600080fd5b34801561005d57600080fd5b5061007860048036036100739190810190610c25565b6100cd565b6040516100859190610dff565b60405180910390f35b34801561009a57600080fd5b506100b560048036036100b09190810190610be4565b6103b4565b6040516100c493929190610d93565b60405180910390f35b60006100d8826107da565b151561011e57600082908060018154018082558091505090600182039060005260206000200160009091929091909150908051906020019061011b929190610ac0565b50505b6001826040518082805190602001908083835b6020831015156101565780518252602082019150602081019050602083039250610131565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390206000018490806001815401808255809150509060018203906000526020600020016000909192909190915090805190602001906101c7929190610ac0565b50506001826040518082805190602001908083835b60208310151561020157805182526020820191506020810190506020830392506101dc565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020600101839080600181540180825580915050906001820390600052602060002001600090919290919091509080519060200190610272929190610ac0565b50506001826040518082805190602001908083835b6020831015156102ac5780518252602082019150602081019050602083039250610287565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390206002016001430190806001815401808255809150509060018203906000526020600020016000909192909190915055506001826040518082805190602001908083835b6020831015156103475780518252602082019150602081019050602083039250610322565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390206003014290806001815401808255809150509060018203906000526020600020016000909192909190915055506001430190509392505050565b60608060606103c1610b40565b60608060006103cf886107da565b1515610410576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161040790610ddf565b60405180910390fd5b6001886040518082805190602001908083835b6020831015156104485780518252602082019150602081019050602083039250610423565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390206080604051908101604052908160008201805480602002602001604051908101604052809291908181526020016000905b82821015610568578382906000526020600020018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156105545780601f1061052957610100808354040283529160200191610554565b820191906000526020600020905b81548152906001019060200180831161053757829003601f168201915b5050505050815260200190600101906104ac565b50505050815260200160018201805480602002602001604051908101604052809291908181526020016000905b82821015610651578382906000526020600020018054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561063d5780601f106106125761010080835404028352916020019161063d565b820191906000526020600020905b81548152906001019060200180831161062057829003601f168201915b505050505081526020019060010190610595565b505050508152602001600282018054806020026020016040519081016040528092919081815260200182805480156106a857602002820191906000526020600020905b815481526020019060010190808311610694575b505050505081526020016003820180548060200260200160405190810160405280929190818152602001828054801561070057602002820191906000526020600020905b8154815260200190600101908083116106ec575b50505050508152505093506020604051908101604052806000815250925060206040519081016040528060008152509150600090505b8360000151518110156107b8576107688385600001518381518110151561075957fe5b90602001906020020151610902565b92506107a9836040805190810160405280600181526020017f3b00000000000000000000000000000000000000000000000000000000000000815250610902565b92508080600101915050610736565b8284604001518560600151819150809050965096509650505050509193909250565b600080600090505b6000805490508110156108f7576000818154811015156107fe57fe5b9060005260206000200160405180828054600181600116156101000203166002900480156108635780601f10610841576101008083540402835291820191610863565b820191906000526020600020905b81548152906001019060200180831161084f575b5050915050604051809103902060001916836040518082805190602001908083835b6020831015156108aa5780518252602082019150602081019050602083039250610885565b6001836020036101000a03801982511681845116808217855250505050505090500191505060405180910390206000191614156108ea57600191506108fc565b80806001019150506107e2565b600091505b50919050565b60608060608060008087945086935083518551016040519080825280601f01601f1916602001820160405280156109485781602001602082028038833980820191505090505b509250600091505b84518210156109fd57848281518110151561096757fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f01000000000000000000000000000000000000000000000000000000000000000283838151811015156109c057fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a9053508180600101925050610950565b600090505b8351811015610ab2578381815181101515610a1957fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f0100000000000000000000000000000000000000000000000000000000000000028382875101815181101515610a7557fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a9053508080600101915050610a02565b829550505050505092915050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610b0157805160ff1916838001178555610b2f565b82800160010185558215610b2f579182015b82811115610b2e578251825591602001919060010190610b13565b5b509050610b3c9190610b69565b5090565b608060405190810160405280606081526020016060815260200160608152602001606081525090565b610b8b91905b80821115610b87576000816000905550600101610b6f565b5090565b90565b600082601f8301121515610ba157600080fd5b8135610bb4610baf82610e47565b610e1a565b91508082526020830160208301858383011115610bd057600080fd5b610bdb838284610ead565b50505092915050565b600060208284031215610bf657600080fd5b600082013567ffffffffffffffff811115610c1057600080fd5b610c1c84828501610b8e565b91505092915050565b600080600060608486031215610c3a57600080fd5b600084013567ffffffffffffffff811115610c5457600080fd5b610c6086828701610b8e565b935050602084013567ffffffffffffffff811115610c7d57600080fd5b610c8986828701610b8e565b925050604084013567ffffffffffffffff811115610ca657600080fd5b610cb286828701610b8e565b9150509250925092565b6000610cc782610e80565b808452602084019350610cd983610e73565b60005b82811015610d0b57610cef868351610d84565b610cf882610e96565b9150602086019550600181019050610cdc565b50849250505092915050565b6000610d2282610e8b565b808452610d36816020860160208601610ebc565b610d3f81610eef565b602085010191505092915050565b6000601182527fe5ba97e5aeb66964e697a0e69588efbc810000000000000000000000000000006020830152604082019050919050565b610d8d81610ea3565b82525050565b60006060820190508181036000830152610dad8186610d17565b90508181036020830152610dc18185610cbc565b90508181036040830152610dd58184610cbc565b9050949350505050565b60006020820190508181036000830152610df881610d4d565b9050919050565b6000602082019050610e146000830184610d84565b92915050565b6000604051905081810181811067ffffffffffffffff82111715610e3d57600080fd5b8060405250919050565b600067ffffffffffffffff821115610e5e57600080fd5b601f19601f8301169050602081019050919050565b6000602082019050919050565b600081519050919050565b600081519050919050565b6000602082019050919050565b6000819050919050565b82818337600083830152505050565b60005b83811015610eda578082015181840152602081019050610ebf565b83811115610ee9576000848401525b50505050565b6000601f19601f83011690509190505600a265627a7a72305820c5e1b4e89faf76b3c97c8589bb6646957b6c579085cb35daf79f6a24de8a14486c6578706572696d656e74616cf50037";
    private static byte[] contractCode = ByteUtils.hexStringToBytes(contractCodeString); //CreditManager

    //upgrade
    private static String contractUpdateCodeString = "60806040526004361061006d576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680631b3c4fab1461007257806357fce39d14610187578063af7c102c146101b2578063b2628df8146101f3578063d448601914610242575b600080fd5b34801561007e57600080fd5b50610087610291565b60405180868152602001806020018060200185151515158152602001848152602001838103835287818151815260200191508051906020019080838360005b838110156100e15780820151818401526020810190506100c6565b50505050905090810190601f16801561010e5780820380516001836020036101000a031916815260200191505b50838103825286818151815260200191508051906020019080838360005b8381101561014757808201518184015260208101905061012c565b50505050905090810190601f1680156101745780820380516001836020036101000a031916815260200191505b5097505050505050505060405180910390f35b34801561019357600080fd5b5061019c610337565b6040518082815260200191505060405180910390f35b3480156101be57600080fd5b506101dd60048036038101908080359060200190929190505050610341565b6040518082815260200191505060405180910390f35b3480156101ff57600080fd5b50610228600480360381019080803590602001909291908035906020019092919050505061035e565b604051808215151515815260200191505060405180910390f35b34801561024e57600080fd5b506102776004803603810190808035906020019092919080359060200190929190505050610523565b604051808215151515815260200191505060405180910390f35b6000606080600080606080600080600033905060c89250600091506040805190810160405280600781526020017f6a72626c6f636b0000000000000000000000000000000000000000000000000081525094506040805190810160405280601a81526020017f32303231303533316a72626c6f636b636f6e7261637463616c6c000000000000815250935082858584849950995099509950995050505050509091929394565b6000600254905090565b600060036000838152602001908152602001600020549050919050565b6000600254331415156103d9576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260118152602001807f5065726d697373696f6e2064656e69656400000000000000000000000000000081525060200191505060405180910390fd5b6000548260015401131580156103f457506001548260015401135b80156104005750600082135b1515610474576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600e8152602001807f496e76616c69642076616c75652100000000000000000000000000000000000081525060200191505060405180910390fd5b8160036000858152602001908152602001600020600082825401925050819055508160016000828254019250508190555081837f9a46fdc9277c739031110f773b36080a9a2012d0b3eca1f5ed8a3403973e05576001546040518080602001838152602001828103825260048152602001807f64656d6f000000000000000000000000000000000000000000000000000000008152506020019250505060405180910390a36001905092915050565b6000816003600033815260200190815260200160002054121515156105b0576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260138152602001807f62616c616e6365206e6f7420656e6f756768210000000000000000000000000081525060200191505060405180910390fd5b6000821380156105c257506000548213155b1515610636576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600e8152602001807f496e76616c69642076616c75652100000000000000000000000000000000000081525060200191505060405180910390fd5b81600360003381526020019081526020016000206000828254039250508190555081600360008581526020019081526020016000206000828254019250508190555060019050929150505600a165627a7a72305820929f39f5dfc978f05e029b986659fd7542e1009cbbb133b2bc009f8876b59c910029";
    private static byte[] contractUpdateCode = ByteUtils.hexStringToBytes(contractUpdateCodeString); //CreditManager



    /**
     * contract id
     */
    private static String callContractId = "wuda1626401312148";

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

    public static void callContractMakeComment(String comment,String bill, String shop_id) {
        EVMParameter parameters = new EVMParameter("make_comment(string,string,string)");
        parameters.addString(String.valueOf(comment));
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
        } else {
            byte[] output = callContractResult.getTransactionReceipt().getOutput();
            if (output == null) {
                exit("call make_comment function", "output failed");
            } else {
                // decode return values
                EVMOutput contractReturnValues = new EVMOutput(ByteUtils.toHexString(output));
                System.out.println("call callContractMakeComment success, response value: " + contractReturnValues.getUint());
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
                String comment_tmp = comments.replace(";", "#");
                String[] comment_list = comment_tmp.split("#");
                List block_list = contractReturnValues.getUintDynamicArray();
                List time_list = contractReturnValues.getUintDynamicArray();
                int len = comment_list.length;
                CommentInfo all_info[] = new CommentInfo[len];
                for (int index = 0; index < len; index++) {
                    String time_tmp = paserTime((BigInteger) time_list.get(index));
                    all_info[index] = new CommentInfo(comment_list[index], (BigInteger) block_list.get(index), time_tmp);
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



    public static void main(String[] args) throws Exception {
        //step 1:init mychain env.
        initMychainEnv();
        //step 2: init sdk client
        initSdk();

        //step 3 : deploy a contract using useridentity.
        //deployContract();
        //callContractMakeComment("好吃", "100kuai", "2");
        //callContractMakeComment("非常好吃", "20kuai", "2");
        callContractQueryComment("2");
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

        System.in.read();
        //step 5 : sdk shut down
        sdk.shutDown();
    }
}
