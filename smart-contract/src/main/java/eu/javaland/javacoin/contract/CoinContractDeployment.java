package eu.javaland.javacoin.contract;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.ContractFunctionParameters;
import com.hedera.hashgraph.sdk.PrivateKey;
import eu.javaland.javacoin.contract.util.ContractDeploymentUtils;
import eu.javaland.util.HederaNode;
import io.github.cdimascio.dotenv.Dotenv;
import java.math.BigInteger;

public class CoinContractDeployment {

    private static final BigInteger SCALE = BigInteger.valueOf(1_000_000_000_000_000_000L);
    private static final int INITIAL_SUPPLY = 1000;

    public static void main(final String[] args) throws Exception {

        final String contractName = "JavaCoinContract";
        final String accountIdValue = Dotenv.load().get("HEDERA_ACCOUNT_ID");
        final String privateKeyValue = Dotenv.load().get("HEDERA_PRIVATE_KEY");

        final AccountId accountId = AccountId.fromString(accountIdValue);
        final PrivateKey privateKey = PrivateKey.fromString(privateKeyValue);
        final byte[] byteCode = ContractDeploymentUtils.readBin(contractName);

        final ContractFunctionParameters parameters = new ContractFunctionParameters()
                .addUint256(BigInteger.valueOf(INITIAL_SUPPLY).multiply(SCALE));

        ContractDeploymentUtils.deploy(HederaNode.TESTNET, accountId, privateKey, byteCode, parameters);
    }
}
