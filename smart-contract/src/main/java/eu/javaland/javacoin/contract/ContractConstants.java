package eu.javaland.javacoin.contract;

import com.hedera.hashgraph.sdk.ContractId;

public interface ContractConstants {

    ContractId HELLO_WORLD_CONTRACT_ID = ContractId.fromString("0.0.3814940");

    ContractId COIN_CONTRACT_ID = ContractId.fromString("0.0.3879597");
}
