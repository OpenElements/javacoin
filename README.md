# The Javacoin tutorial

This project is a tutorial for the [Hedera hashgraph](https://hedera.com) and Java. The tutorials gives an overview how
smart contracts can be deployed and used on the Hedera hashgraph.

## Prerequisites

The tutorial is based on [Java 17](https://adoptium.net/marketplace/?version=17). No additional tools are needed to
execute the tutorials. Maven is used as buildtool for the project but since the Maven wrapper is part of the project a
local Maven installation is not needed.

Once you have checked out the project you can build it by running the following command from the root folder of the
project:

```bash
./mvnw verify
```

## Internals

The tutorial is using the [Hedera Java SDK](LINK) and [web3j](LINK). All functionality that is generic (like calling a
smart contract) is done by using web3j that acts as a generic Java library to interact with public ledgers like Hedera
or Ethereum. The Hedera Java SDK is used to interact with the Hedera network if special functionality is needed or not
implemented by web3j.

## Tutorials

The tutorials are split into different parts. The goal is to understand what a smart contract is, how it is deployed in
a public ledger and how it can be used. Next to this we will show how crypto currency can be created and used on top of
smart contracts.

### 1. Hedera hashgraph

Hedera provides 3 different Networks: The Mainnet, the Testnet and the Previewnet:

- The Hedera **Mainnet** (short for main network) is where applications are run in production, with transaction fees
  paid in HBAR. Transactions are submitted to the Hedera mainnet by any application or retail user; they're
  automatically consensus timestamped and fairly ordered. All nodes of the main network are run by Council members of
  the Hedera Foundation like Google, IBM, Deutsche Telekom, and others.
- The Hedera **Testnet** is a public network where developers can test their applications and smart contracts. The
  testnet is a fully functional Hedera network, with all the same features as the mainnet. The testnet is a great place
  to test your application before deploying it to the mainnet. The testnet is also a great place to test your smart
  contracts before deploying them to the mainnet.
- The Hedera **Previewnet** is a public network where developers can test their applications and smart contracts. The
  previewnet is a fully functional Hedera network, with all the same features as the mainnet.The Hedera previewnet is
  designed to offer developers in the Hedera community early exposure to features coming down the pipe. It’s not always
  stable and accounts / data are likely to be lost when the network codebase is upgraded.

In the tutorial we will use the **Testnet**. To use the testnet you need to create an account on
the [Hedera portal](https://portal.hedera.com). A step by step guide can be
found [here](https://help.hedera.com/hc/en-us/articles/360000664678-How-do-I-create-an-account-on-the-Hedera-testnet-).

Once an application is ready you will normally deploy it on Mainnet. A step by step guide can be
found [here](https://help.hedera.com/hc/en-us/articles/360000664618-Can-I-deploy-my-application-on-the-Hedera-mainnet-).

**NOTE:** You can even setup a network on your local machine. This is super useful for testing and development. You can
run integration tests automatically against such local network or use it to test your smart contracts when you are
offline and can not use the testnet. The local network is open source and created based on Docker containers. You can
find the source code on [GitHub](https://github.com/hashgraph/hedera-local-node).

### 2. Writing a Smart Contracts

The first part of the tutorial is about smart contracts. We will show how a smart contract can be created by using
[Solidity](https://docs.soliditylang.org) and how it can be deployed on the Hedera hashgraph.

A smart contract is a computer program that is running on a public ledger. One objective of smart contracts is the
reduction of need for trusted intermediators since the code is running in the ledger and can not be mutated by any
party.

The smart contracts that we will use are all written in [Solidity](https://soliditylang.org). Solidity is the most
common language for creating smart contracts. Smart contracts are compiled
to [EVM](https://ethereum.org/en/developers/docs/evm/) bytecode that can be executed on the Ethereum Virtual Machine.
You can think about this as a virtual machine that is running on the public ledger. The EVM bytecode is then deployed on
the public ledger and can be executed by anyone. The Hedera hashgraph is using the same EVM bytecode as Ethereum. This
is why we can use the same smart contracts on both ledgers and interact with them by using web3j.

A "Hello World" smart contract is shown below:

```solidity 
// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract MyContract {
    function helloWorld() public pure returns (string memory) {
        return "Hello, World!";
    }
}
```

A full documentation of the Solidity langauge can be found on
the [official documentation](https://docs.soliditylang.org). Therefore we won't go into detail about the language in
this tutorial.

Such contract can be added to the `src/main/solidity` folder of the `smart-contract` module of the project. All
contracts will be compiled by doing a maven build:

```bash
./mvnw verify
```

The maven build will not only compile the smart contracts but also generate the Java wrapper classes that can be used to
interact with the smart contracts. Internally the Solidity compiler is used that will create 2 outputs for each
contract:

- a .bin file that contains the EVM bytecode of the contract (see `target/generated-sources/bin/**.bin`)
- a .json file that contains the ABI of the contract (see `target/generated-sources/abi/**.json`)

The ABI contains the public interface (the API) of the smart contract and is used to create the Java wrapper. All
wrapper classes are generated in the `target/generated-sources/java` folder.

The generated Java wrapper classes can be used to interact with the smart contract. Before we have a look at how we can
use the smart contract we need to deploy it on the Hedera hashgraph. To deploy a smart contract we need to upload the
compiled binary of the contract to the Hedera network.

### 3. Deploying a Smart Contract

The smart contract that we have created in the previous step can be deployed on the Hedera hashgraph. To do so we need
to authorize ourselves against the network. This is done by using a Hedera account that we have created. The account
information can all be found at the [Hedera portal](https://portal.hedera.com). The account information must be stored
in the `.env` file in the root folder of the project. Once the information is copied the file looks like this (all keys
are invalid samples):

```bash
HEDERA_ACCOUNT_ID=0.0.53854625
HEDERA_PRIVATE_KEY=3030020100300706052b8104000a04220420c23ff08c429aa5a1d80bb300f436dd89adc5a4aa9a4544d7f3b00b2045c6cc37
HEDERA_PUBLIC_KEY=302d300706052b8104000a03220003cd09e0aaafe0d4a7c602f581aa00202c5aa4ffbae9b96f479fc1db36f4594a17
```

In Java we use the [java-dotenv](https://github.com/cdimascio/java-dotenv) library to read the environment variables
from the `.env` file. The following code shows how we can read the account information from the `.env` file:

```java
final String accountIdValue=Dotenv.load().get("HEDERA_ACCOUNT_ID");
final String privateKeyValue=Dotenv.load().get("HEDERA_PRIVATE_KEY");
```

The project contrains the `ContractDeploymentUtils` class that contains all the generic functionality to deploy a smart
contract. The following sample shows how it can be used to deploy a smart contract:

```java
final String contractName="HelloWorldContract";
final String accountIdValue=Dotenv.load().get("HEDERA_ACCOUNT_ID");
final String privateKeyValue=Dotenv.load().get("HEDERA_PRIVATE_KEY");

final AccountId accountId=AccountId.fromString(accountIdValue);
final PrivateKey privateKey=PrivateKey.fromString(privateKeyValue);
final byte[]byteCode=ContractDeploymentUtils.readBin(contractName);

        ContractDeploymentUtils.deploy(HederaNode.TESTNET,accountId,privateKey,byteCode);
```

By executing the code the compiled smart contract will be deployed on the Hedera testnet. The id of the deployed smart
contract will be printed to the console:

```bash
contract successfully created with FileId '0.0.3818143' and ContractId '0.0.3216587'
```

Once this is done we can interact with the contract by using its unique id.

### 4. Interacting with a Smart Contract

The smart contract that we have deployed in the previous step can be interacted with by using the generated Java
wrapper. To do so we need to store the id of the deployed smart contract. The `ContractConstants` interface can be used
to store the id of the deployed smart contracts.

To interact with the smart contract we will use the wrapper class that has been generated by the maven build. The
wrapper class provides Java functions for each function of the smart contract. By doing so it is really easy to call a
method. But before we can do that we need to introduce gas and gas price. Gas is the fee that is paid to the network to
execute functionality like calling a function of a smart contract. The gas price is the price that is paid for each unit
of gas. Internally the EVM has a gas price defined for each operation (like a simple multiplication). Whenever we call a
smart contract function we need to pay the gas price for each operation that is executed. To not become bankrupt by
calling a function that has a bug and ends in an infinite loop we need to limit the amount of gas that can be used to
execute the function. With web3j we do this by defining a GasProvider. The following code shows how we can define such
provider in a simple way:

```java
final BigInteger gasPrice=BigInteger.valueOf(20_000_000_000_000L);
final BigInteger gasLimit=BigInteger.valueOf(500_000L);
final StaticGasProvider staticGasProvider=new StaticGasProvider(gasPrice,gasLimit);
```

Now that we have definded the gas limit we have everything together to call the function of the deployed smart contract.
The following code shows how we can call the `say_hello` method of the smart contract on testnet:

```java
final HederaNode node=HederaNode.TESTNET;
final PrivateKey privateKey=PrivateKey.fromString(Dotenv.load().get("HEDERA_PRIVATE_KEY"));
final ContractId contractId=HELLO_WORLD_CONTRACT_ID;

final Web3j web3j=HederaUtils.createWeb3j(node);
final Credentials credentials=HederaUtils.toCredentials(privateKey);

final BigInteger gasPrice=BigInteger.valueOf(20_000_000_000_000L);
final BigInteger gasLimit=BigInteger.valueOf(500_000L);
final StaticGasProvider staticGasProvider=new StaticGasProvider(gasPrice,gasLimit);

final HelloWorldContract statefulContract=HederaUtils.createContractWrapper(node,
        web3j,
        credentials,
        HelloWorldContract.class,
        contractId,staticGasProvider);

        logger.info("Greeting method result: {}",statefulContract.say_hello().send());
```

### 5. Developing the smart contract for the JavaLandCoin 

To create a token, we have to implement a smart contract that implements the `ERC20` interface.
Luckily there are already implementations available that we can use.
We derive our smart contract from the `ERC20` contract from Open Zeppelin and only have to provide functionality that is specific to our token.
The following code shows the outline:

```solidity
// SPDX-License-Identifier: Apache-2.0
pragma solidity ^0.8.0;

import "./contracts/token/ERC20/ERC20.sol";

contract JavaCoinContract is ERC20 {

    address payable _owner;
    address payable _treasury;
    uint256 _coinScale;
    uint256 constant _hBarScale = 10 ** 8;

    uint256 _price;

    constructor(uint256 initialSupply) ERC20("JavaLand Coin", "JC") { /* ... */ }

    function _recalculatePrice() private { /* ... */ }

    function getCoinPrice()  public view returns (uint) { /* ... */ }

    function getCoinsForAccount() public view returns (uint) { /* ... */ }

    function buyCoins(uint count) public payable { /* ... */ }

    function sellCoins(uint count) public { /* ... */ }

    function withdrawHBars(uint amount) public { /* ... */ }

    function mintCoins(uint amount) public { /* ... */ }

    function burnCoins(uint amount) public { /* ... */ }

    receive() external payable {}

    fallback() external payable {}
}
```

The constructor of the smart contract takes the initial supply of the token as a parameter.
The constructor of the `ERC20` contract is called with the name and the symbol of the token. 
This will initialize the token.

In this workshop we will focus on the `buyCoins` and `sellCoins` methods.
The `buyCoins` method takes the number of coins that should be bought as a parameter.
It is payable, which means that we can send Hbars to the contract when calling the method.
The general outline of the method is the following:

```solidity
    function buyCoins(uint count) public payable {
        // check input parameters (count > 0 and count < 5% of total supply)
        // TODO
  
        uint total = count * _price / _coinScale;

        // check that user has enough HBars
        // TODO

        // check that pool contains enough JavaLandCoins
        // TODO

        // recalculate price
        _recalculatePrice();

        // transfer HBars to contract
        // TODO
  
        // transfer JavaLandCoins to user
        // TODO
    }
```

When developing smart contracts, we always want to follow the Checks-Effects-Interactions design pattern.
That means, we first check the input parameters and initial state, then we change the state of the contract and finally we interact with other contracts.
The commands that you will need are listed below:

```solidity
// Check conditions
require(bool condition, string message);

// Address of the sender
msg.address

// Get the Hbar balance of an account
address.balance

// Transfer Hbars
(bool success, ) = address.call{value: amount}("");
require(success, "Transfer failed.”);

// Get the total amount of JavaLandCoins
totalSupply();

// Get balance of JavaLandCoins
balanceOf(address account);

// Transfer JavaLandCoins
_transfer(address from, address to, uint amount)
```

The `sellCoins` method is the opposite of the `buyCoins` method.

If you are not able to finish the last tasks on time, don't worry.
Developing smart contracts is tough. 😉
You can take a look at the solution [here](TODO).
