<!DOCTYPE html>
<html lang="en">
<head>
    <#include "head-meta.ftlh">
</head>
<body>
<#include "header.ftlh">

<main class="container mx-auto py-12">
  <section>
    <h2>Coin stock</h2>
    <p>
      <span><b>Amount:</b></span> <span>${coinAmount!UNKNOWN}</span>
    </p>
    <p>
      <span><b>Coins in pool:</b></span> <span>${coinsInPool!UNKNOWN}</span>
    </p>
    <p>
      <span><b>My coins:</b></span> <span>${coins!UNKNOWN}</span>
    </p>
    <p>
      <span><b>My HBAR:</b></span> <span>${hbar!UNKNOWN}</span>
    </p>
    <p>
      <span><b>coin price:</b></span> <span>${coinPrice!UNKNOWN}</span>
    </p>
  </section>

  <section>
    <h2>Buy coin</h2>
    <form action="/onbuy" method="post">
      <p>
        <label>Amount</label>
        <input name="amount" type="text"/>
      </p>
      <button>Buy</button>
    </form>
  </section>
  <section>
    <h2>Sell coin</h2>
    <form action="/onsell" method="post">
      <p>
        <label>Amount</label>
        <input name="amount" type="text"/>
      </p>
      <button>Sell</button>
    </form>
  </section>
</main>
</body>
</html>