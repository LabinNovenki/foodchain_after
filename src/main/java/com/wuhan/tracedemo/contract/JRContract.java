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
    private static String contractCodeString = "0x608060405234801561001057600080fd5b5033600281905550611528806100276000396000f30060806040526004361061004c576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680634113c38c146100515780636dc788341461008e575b600080fd5b34801561005d57600080fd5b5061007860048036036100739190810190611100565b6100cf565b60405161008591906113ae565b60405180910390f35b34801561009a57600080fd5b506100b560048036036100b091908101906110bf565b610640565b6040516100c6959493929190611318565b60405180910390f35b60006100da82610c88565b15156101aa57600082908060018154018082558091505090600182039060005260206000200160009091929091909150908051906020019061011d929190610f6e565b505060006001836040518082805190602001908083835b6020831015156101595780518252602082019150602081019050602083039250610134565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051809103902060060160006101000a81548161ffff021916908361ffff1602179055505b6001826040518082805190602001908083835b6020831015156101e257805182526020820191506020810190506020830392506101bd565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020600001869080600181540180825580915050906001820390600052602060002001600090919290919091509080519060200190610253929190610f6e565b50506001826040518082805190602001908083835b60208310151561028d5780518252602082019150602081019050602083039250610268565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390206001018590806001815401808255809150509060018203906000526020600020016000909192909190915090805190602001906102fe929190610f6e565b50506001826040518082805190602001908083835b6020831015156103385780518252602082019150602081019050602083039250610313565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051809103902060030184908060018154018082558091505090600182039060005260206000209060209182820401919006909192909190916101000a81548160ff021916908360ff160217905550506001826040518082805190602001908083835b6020831015156103ec57805182526020820191506020810190506020830392506103c7565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051809103902060020183908060018154018082558091505090600182039060005260206000200160009091929091909150908051906020019061045d929190610f6e565b50506001826040518082805190602001908083835b6020831015156104975780518252602082019150602081019050602083039250610472565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390206004016001430190806001815401808255809150509060018203906000526020600020016000909192909190915055506001826040518082805190602001908083835b602083101515610532578051825260208201915060208101905060208303925061050d565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390206005014290806001815401808255809150509060018203906000526020600020016000909192909190915055508360ff166001836040518082805190602001908083835b6020831015156105ce57805182526020820191506020810190506020830392506105a9565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051809103902060060160008282829054906101000a900461ffff160192506101000a81548161ffff021916908361ffff16021790555060014301905095945050505050565b6060806060806060610650610fee565b60608060008061065f8b610c88565b15156106a0576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016106979061138e565b60405180910390fd5b60018b6040518082805190602001908083835b6020831015156106d857805182526020820191506020810190506020830392506106b3565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051809103902060e0604051908101604052908160008201805480602002602001604051908101604052809291908181526020016000905b828210156107f8578382906000526020600020018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156107e45780601f106107b9576101008083540402835291602001916107e4565b820191906000526020600020905b8154815290600101906020018083116107c757829003601f168201915b50505050508152602001906001019061073c565b50505050815260200160018201805480602002602001604051908101604052809291908181526020016000905b828210156108e1578382906000526020600020018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156108cd5780601f106108a2576101008083540402835291602001916108cd565b820191906000526020600020905b8154815290600101906020018083116108b057829003601f168201915b505050505081526020019060010190610825565b50505050815260200160028201805480602002602001604051908101604052809291908181526020016000905b828210156109ca578382906000526020600020018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156109b65780601f1061098b576101008083540402835291602001916109b6565b820191906000526020600020905b81548152906001019060200180831161099957829003601f168201915b50505050508152602001906001019061090e565b50505050815260200160038201805480602002602001604051908101604052809291908181526020018280548015610a4757602002820191906000526020600020906000905b82829054906101000a900460ff1660ff1681526020019060010190602082600001049283019260010382029150808411610a105790505b5050505050815260200160048201805480602002602001604051908101604052809291908181526020018280548015610a9f57602002820191906000526020600020905b815481526020019060010190808311610a8b575b5050505050815260200160058201805480602002602001604051908101604052809291908181526020018280548015610af757602002820191906000526020600020905b815481526020019060010190808311610ae3575b505050505081526020016006820160009054906101000a900461ffff1661ffff1661ffff168152505094506020604051908101604052806000815250935060206040519081016040528060008152509250600091505b846020015151821015610bcf57610b7f84866020015184815181101515610b7057fe5b90602001906020020151610db0565b9350610bc0846040805190810160405280600181526020017f2300000000000000000000000000000000000000000000000000000000000000815250610db0565b93508180600101925050610b4d565b600090505b846020015151811015610c5657610c0683866000015183815181101515610bf757fe5b90602001906020020151610db0565b9250610c47836040805190810160405280600181526020017f2300000000000000000000000000000000000000000000000000000000000000815250610db0565b92508080600101915050610bd4565b828486608001518760a00151886060015182925081915080905099509950995099509950505050505091939590929450565b600080600090505b600080549050811015610da557600081815481101515610cac57fe5b906000526020600020016040518082805460018160011615610100020316600290048015610d115780601f10610cef576101008083540402835291820191610d11565b820191906000526020600020905b815481529060010190602001808311610cfd575b5050915050604051809103902060001916836040518082805190602001908083835b602083101515610d585780518252602082019150602081019050602083039250610d33565b6001836020036101000a0380198251168184511680821785525050505050509050019150506040518091039020600019161415610d985760019150610daa565b8080600101915050610c90565b600091505b50919050565b60608060608060008087945086935083518551016040519080825280601f01601f191660200182016040528015610df65781602001602082028038833980820191505090505b509250600091505b8451821015610eab578482815181101515610e1557fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f0100000000000000000000000000000000000000000000000000000000000000028383815181101515610e6e57fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a9053508180600101925050610dfe565b600090505b8351811015610f60578381815181101515610ec757fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f0100000000000000000000000000000000000000000000000000000000000000028382875101815181101515610f2357fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a9053508080600101915050610eb0565b829550505050505092915050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610faf57805160ff1916838001178555610fdd565b82800160010185558215610fdd579182015b82811115610fdc578251825591602001919060010190610fc1565b5b509050610fea9190611030565b5090565b60e060405190810160405280606081526020016060815260200160608152602001606081526020016060815260200160608152602001600061ffff1681525090565b61105291905b8082111561104e576000816000905550600101611036565b5090565b90565b600082601f830112151561106857600080fd5b813561107b611076826113f6565b6113c9565b9150808252602083016020830185838301111561109757600080fd5b6110a283828461149b565b50505092915050565b60006110b7823561148e565b905092915050565b6000602082840312156110d157600080fd5b600082013567ffffffffffffffff8111156110eb57600080fd5b6110f784828501611055565b91505092915050565b600080600080600060a0868803121561111857600080fd5b600086013567ffffffffffffffff81111561113257600080fd5b61113e88828901611055565b955050602086013567ffffffffffffffff81111561115b57600080fd5b61116788828901611055565b9450506040611178888289016110ab565b935050606086013567ffffffffffffffff81111561119557600080fd5b6111a188828901611055565b925050608086013567ffffffffffffffff8111156111be57600080fd5b6111ca88828901611055565b9150509295509295909350565b60006111e28261143c565b8084526020840193506111f483611422565b60005b828110156112265761120a8683516112fa565b6112138261145d565b91506020860195506001810190506111f7565b50849250505092915050565b600061123d82611447565b80845260208401935061124f8361142f565b60005b8281101561128157611265868351611309565b61126e8261146a565b9150602086019550600181019050611252565b50849250505092915050565b600061129882611452565b8084526112ac8160208601602086016114aa565b6112b5816114dd565b602085010191505092915050565b6000601182527fe5ba97e5aeb66964e697a0e69588efbc810000000000000000000000000000006020830152604082019050919050565b61130381611477565b82525050565b61131281611481565b82525050565b600060a0820190508181036000830152611332818861128d565b90508181036020830152611346818761128d565b9050818103604083015261135a81866111d7565b9050818103606083015261136e81856111d7565b905081810360808301526113828184611232565b90509695505050505050565b600060208201905081810360008301526113a7816112c3565b9050919050565b60006020820190506113c360008301846112fa565b92915050565b6000604051905081810181811067ffffffffffffffff821117156113ec57600080fd5b8060405250919050565b600067ffffffffffffffff82111561140d57600080fd5b601f19601f8301169050602081019050919050565b6000602082019050919050565b6000602082019050919050565b600081519050919050565b600081519050919050565b600081519050919050565b6000602082019050919050565b6000602082019050919050565b6000819050919050565b600060ff82169050919050565b600060ff82169050919050565b82818337600083830152505050565b60005b838110156114c85780820151818401526020810190506114ad565b838111156114d7576000848401525b50505050565b6000601f19601f83011690509190505600a265627a7a7230582017193c86a02196d881968d123e9a075294dd4a7fae41a69c66110b8467d7f6c26c6578706572696d656e74616cf50037";
    private static byte[] contractCode = ByteUtils.hexStringToBytes(contractCodeString); //CreditManager

    //upgrade
    private static String contractUpdateCodeString = "60806040526004361061006d576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680631b3c4fab1461007257806357fce39d14610187578063af7c102c146101b2578063b2628df8146101f3578063d448601914610242575b600080fd5b34801561007e57600080fd5b50610087610291565b60405180868152602001806020018060200185151515158152602001848152602001838103835287818151815260200191508051906020019080838360005b838110156100e15780820151818401526020810190506100c6565b50505050905090810190601f16801561010e5780820380516001836020036101000a031916815260200191505b50838103825286818151815260200191508051906020019080838360005b8381101561014757808201518184015260208101905061012c565b50505050905090810190601f1680156101745780820380516001836020036101000a031916815260200191505b5097505050505050505060405180910390f35b34801561019357600080fd5b5061019c610337565b6040518082815260200191505060405180910390f35b3480156101be57600080fd5b506101dd60048036038101908080359060200190929190505050610341565b6040518082815260200191505060405180910390f35b3480156101ff57600080fd5b50610228600480360381019080803590602001909291908035906020019092919050505061035e565b604051808215151515815260200191505060405180910390f35b34801561024e57600080fd5b506102776004803603810190808035906020019092919080359060200190929190505050610523565b604051808215151515815260200191505060405180910390f35b6000606080600080606080600080600033905060c89250600091506040805190810160405280600781526020017f6a72626c6f636b0000000000000000000000000000000000000000000000000081525094506040805190810160405280601a81526020017f32303231303533316a72626c6f636b636f6e7261637463616c6c000000000000815250935082858584849950995099509950995050505050509091929394565b6000600254905090565b600060036000838152602001908152602001600020549050919050565b6000600254331415156103d9576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260118152602001807f5065726d697373696f6e2064656e69656400000000000000000000000000000081525060200191505060405180910390fd5b6000548260015401131580156103f457506001548260015401135b80156104005750600082135b1515610474576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600e8152602001807f496e76616c69642076616c75652100000000000000000000000000000000000081525060200191505060405180910390fd5b8160036000858152602001908152602001600020600082825401925050819055508160016000828254019250508190555081837f9a46fdc9277c739031110f773b36080a9a2012d0b3eca1f5ed8a3403973e05576001546040518080602001838152602001828103825260048152602001807f64656d6f000000000000000000000000000000000000000000000000000000008152506020019250505060405180910390a36001905092915050565b6000816003600033815260200190815260200160002054121515156105b0576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260138152602001807f62616c616e6365206e6f7420656e6f756768210000000000000000000000000081525060200191505060405180910390fd5b6000821380156105c257506000548213155b1515610636576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600e8152602001807f496e76616c69642076616c75652100000000000000000000000000000000000081525060200191505060405180910390fd5b81600360003381526020019081526020016000206000828254039250508190555081600360008581526020019081526020016000206000828254019250508190555060019050929150505600a165627a7a72305820929f39f5dfc978f05e029b986659fd7542e1009cbbb133b2bc009f8876b59c910029";
    private static byte[] contractUpdateCode = ByteUtils.hexStringToBytes(contractUpdateCodeString); //CreditManager



    /**
     * contract id
     */
    private static String callContractId = "wuda1626571152069";

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

    public static int callContractMakeComment(String commentid, String comment,String bill, String shop_id, int star) {
        EVMParameter parameters = new EVMParameter("make_comment(string,string,uint8,string,string)");
        parameters.addString(String.valueOf(commentid));
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
                String commentids = contractReturnValues.getString();
                String[] commentid_list = commentids.split("#");
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
                    all_info[index] = new CommentInfo(commentid_list[index], comment_list[index], (BigInteger) block_list.get(index), time_tmp, star_tmp.intValue());
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
//        step 1:init mychain env.
        initMychainEnv();
//        step 2: init sdk client
        initSdk();

//        step 3 : deploy a contract using useridentity.
//        deployContract();
        callContractMakeComment("0x7123516235", "好吃", "100kuai", "2", 3);
        callContractMakeComment("023889767897","非常好吃", "20kuai", "2", 4);
        callContractQueryComment("2");
//        step 4 callContract.
//        String testAccount = "jraccount_1622277417038";
//        callContractIssueCredit(testAccount,800);
//        callContractQueryCredit(testAccount);
//        callContractTransferCredit(testAccount,100);
//
//        upgrade contract 调用
//        updateContractDemo();
//        callContractGetParamsTest();
//
//
//        freezeContract调用
//        freezeContractTest();
//        callContractQueryCredit(account);
//
//        unFreeze调用
//        unFreezeContractTest();

        System.in.read();
//        step 5 : sdk shut down
        sdk.shutDown();
    }
}
