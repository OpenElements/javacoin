package eu.javaland.javacoin.backend.coin;

import static eu.javaland.javacoin.contract.util.Converters.coinFromUint;
import static eu.javaland.javacoin.contract.util.Converters.coinToUint;
import static eu.javaland.util.HederaUtils.hBarFromUint;
import static eu.javaland.util.HederaUtils.hBarToUint;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.Hbar;
import eu.javaland.javacoin.contract.generated.JavaCoinContract;
import eu.javaland.util.HederaFunctions;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

@Service
public class CoinContractService {

    @Value("${hedera.contracts.javaCoinContract.address}")
    private String javaCoinContractAddress;

    private final Client client;
    private final JavaCoinContract contract;

    @Autowired
    public CoinContractService(final Client client, final JavaCoinContract contract) {
        this.client = Objects.requireNonNull(client, "client must not be null");
        this.contract = Objects.requireNonNull(contract, "contract must not be null");
    }

    public BigDecimal getCoinAmount() {
        try {
            final BigInteger result = contract.totalSupply().send();
            return coinFromUint(result);
        } catch (final Exception e) {
            throw new RuntimeException("Error while calling smart contract", e);
        }
    }

    public BigDecimal getCoinsInPool() {
        try {
            final BigInteger result = contract.balanceOf(ContractId.fromString(javaCoinContractAddress).toSolidityAddress()).send();
            return coinFromUint(result);
        } catch (final Exception e) {
            throw new RuntimeException("Error while calling smart contract", e);
        }
    }

    public Hbar getCoinPrice() {
        try {
            final BigInteger result = contract.getCoinPrice().send();
            return hBarFromUint(result);
        } catch (final Exception e) {
            throw new RuntimeException("Error while calling smart contract", e);
        }
    }

    public BigDecimal getCoinsForAccount() {
        try {
            final BigInteger result = contract.getCoinsForAccount().send();
            return coinFromUint(result);
        } catch (final Exception e) {
            throw new RuntimeException("Error while calling smart contract", e);
        }
    }

    public Hbar getHbarInPool() {
        try {
            return HederaFunctions.getBalanceOfContract(client, ContractId.fromString(javaCoinContractAddress));
        } catch (final Exception e) {
            throw new RuntimeException("Error while calling smart contract", e);
        }
    }

    public Hbar getHBarForAccount() {
        try {
            return HederaFunctions.getBalanceOfAccount(client, client.getOperatorAccountId());
        } catch (final Exception e) {
            throw new RuntimeException("Error while calling smart contract", e);
        }
    }

    public void buyCoins(final BigDecimal amount) {
        try {
            final BigDecimal price = getCoinPrice().getValue();
            final BigInteger total = coinToUint(amount.multiply(price));
            final BigInteger realAmount = coinToUint(amount);

            final TransactionReceipt receipt = contract.buyCoins(realAmount, total).send();
            if (!receipt.isStatusOK()) {
                throw new IllegalStateException("Status of receipt: " + receipt.getStatus());
            }
        } catch (final Exception e) {
            throw new RuntimeException("Error while calling smart contract", e);
        }
    }

    public void sellCoins(final BigDecimal amount) {
        try {
            final BigInteger realAmount = coinToUint(amount);

            final TransactionReceipt receipt = contract.sellCoins(realAmount).send();
            if (!receipt.isStatusOK()) {
                throw new IllegalStateException("Status of receipt: " + receipt.getStatus());
            }
        } catch (final Exception e) {
            throw new RuntimeException("Error while calling smart contract", e);
        }
    }

    public void mintCoins(final BigDecimal amount) {
        try {
            final BigInteger realAmount = coinToUint(amount);

            contract.mintCoins(realAmount).send();
        } catch (final Exception e) {
            throw new RuntimeException("Error while calling smart contract", e);
        }
    }

    public void burnCoins(final BigDecimal amount) {
        try {
            final BigInteger realAmount = coinToUint(amount);

            contract.burnCoins(realAmount).send();
        } catch (final Exception e) {
            throw new RuntimeException("Error while calling smart contract", e);
        }
    }

    public void depositHbars(final BigDecimal amount) {
        try {
            HederaFunctions.transferHbar(
                    client,
                    client.getOperatorAccountId(),
                    AccountId.fromString(javaCoinContractAddress),
                    Hbar.from(amount));
        } catch (final Exception e) {
            throw new RuntimeException("Error while calling smart contract", e);
        }
    }

    public void withdrawHbars(final BigDecimal amount) {
        try {
            final BigInteger realAmount = hBarToUint(Hbar.from(amount));

            contract.withdrawHBars(realAmount).send();
        } catch (final Exception e) {
            throw new RuntimeException("Error while calling smart contract", e);
        }
    }

}
