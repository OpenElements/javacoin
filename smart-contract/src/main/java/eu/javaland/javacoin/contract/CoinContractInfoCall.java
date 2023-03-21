package eu.javaland.javacoin.contract;

import static eu.javaland.javacoin.contract.ContractConstants.COIN_CONTRACT_ID;
import static eu.javaland.javacoin.contract.util.Converters.coinFromUint;
import static eu.javaland.util.HederaUtils.hBarFromUint;

import com.hedera.hashgraph.sdk.PrivateKey;
import eu.javaland.javacoin.contract.generated.JavaCoinContract;
import eu.javaland.util.HederaNode;
import eu.javaland.util.HederaUtils;
import io.github.cdimascio.dotenv.Dotenv;
import java.math.BigInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.StaticGasProvider;

public class CoinContractInfoCall {

    private static final Logger logger = LoggerFactory.getLogger(CoinContractInfoCall.class);

    public static void main(final String[] args) throws Exception {
        final HederaNode node = HederaNode.TESTNET;
        final PrivateKey privateKey = PrivateKey.fromString(Dotenv.load().get("HEDERA_PRIVATE_KEY"));

        final Web3j web3j = HederaUtils.createWeb3j(node);
        final Credentials credentials = HederaUtils.toCredentials(privateKey);

        final BigInteger gasPrice = BigInteger.valueOf(20_000_000_000_000L);
        final BigInteger gasLimit = BigInteger.valueOf(500_000L);
        final StaticGasProvider staticGasProvider = new StaticGasProvider(gasPrice, gasLimit);

        final JavaCoinContract contract = HederaUtils.createContractWrapper(
                node,
                web3j,
                credentials,
                JavaCoinContract.class,
                COIN_CONTRACT_ID,
                staticGasProvider);

        logger.info("Total Supply: {}", coinFromUint(contract.totalSupply().send()));
        logger.info("Price: {}", hBarFromUint(contract.getCoinPrice().send()));
    }
}
