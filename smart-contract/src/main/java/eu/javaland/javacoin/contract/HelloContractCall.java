package eu.javaland.javacoin.contract;

import static eu.javaland.javacoin.contract.ContractConstants.HELLO_WORLD_CONTRACT_ID;

import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.PrivateKey;
import eu.javaland.javacoin.contract.generated.HelloWorldContract;
import eu.javaland.util.HederaNode;
import eu.javaland.util.HederaUtils;
import io.github.cdimascio.dotenv.Dotenv;
import java.math.BigInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.StaticGasProvider;

public class HelloContractCall {

    private final static Logger logger = LoggerFactory.getLogger(HelloContractCall.class);

    public static void main(final String[] args) throws Exception {
        final HederaNode node = HederaNode.TESTNET;
        final PrivateKey privateKey = PrivateKey.fromString(Dotenv.load().get("HEDERA_PRIVATE_KEY"));
        final ContractId contractId = HELLO_WORLD_CONTRACT_ID;

        final Web3j web3j = HederaUtils.createWeb3j(node);
        final Credentials credentials = HederaUtils.toCredentials(privateKey);

        final BigInteger gasPrice = BigInteger.valueOf(20_000_000_000_000L);
        final BigInteger gasLimit = BigInteger.valueOf(500_000L);
        final StaticGasProvider staticGasProvider = new StaticGasProvider(gasPrice, gasLimit);

        final HelloWorldContract statefulContract = HederaUtils.createContractWrapper(node,
                web3j,
                credentials,
                HelloWorldContract.class,
                contractId, staticGasProvider);

        logger.info("Greeting method result: {}", statefulContract.say_hello().send());
    }
}
