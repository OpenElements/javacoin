<!DOCTYPE html>
<html lang="en">
<head>
    <#include "head-meta.ftlh">
</head>
<body>
<#include "header.ftlh">

<main class="container mx-auto py-12">

  <section>
    <h2>JavaLand Coin Management</h2>
    <p>
      <span><b>Total supply:</b></span> <span>${coinAmount!UNKNOWN}</span>
    </p>
    <p>
      <span><b>Coins in pool:</b></span> <span>${coinsInPool!UNKNOWN}</span>
    </p>
    <p>
      <span><b>My coins:</b></span> <span>${coins!UNKNOWN}</span>
    </p>
    <p>
      <span><b>Coin price:</b></span> <span>${coinPrice!UNKNOWN}</span>
    </p>
    <form action="/onmint" method="post">
      <p>
        <label for="mint-amount">Amount</label>
        <input id="mint-amount" name="amount" type="text"/>
      </p>
      <button>Mint</button>
    </form>
    <form action="/onburn" method="post">
      <p>
        <label for="burn-amount">Amount</label>
        <input id="burn-amount" name="amount" type="text"/>
      </p>
      <button>Burn</button>
    </form>
  </section>

  <section>
    <h2>Hbar Balance Management</h2>
    <p>
      <span><b>Treasury:</b></span> <span>${hbarInPool!UNKNOWN}</span>
    </p>
    <p>
      <span><b>My balance:</b></span> <span>${hbar!UNKNOWN}</span>
    </p>
    <form action="/ondeposit" method="post">
      <p>
        <label for="deposit-amount">Amount</label>
        <input id="deposit-amount" name="amount" type="text"/>
      </p>
      <button>Deposit</button>
    </form>
    <form action="/onwithdraw" method="post">
      <p>
        <label for="withdraw-amount">Amount</label>
        <input id="withdraw-amount" name="amount" type="text"/>
      </p>
      <button>Withdraw</button>
    </form>
  </section>

</main>
</body>
</html>