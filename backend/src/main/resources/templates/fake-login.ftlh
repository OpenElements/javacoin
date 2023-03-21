<!DOCTYPE html>
<html lang="en">
<head>
    <#include "head-meta.ftlh">
</head>
<body>
<#include "header.ftlh">

<main class="container mx-auto py-12">
  <form action="/onlogin" method="post">

    <p>
      <label>Private Key</label>
      <input type="password" name="privateKey" value="${privateKey!}"/>
    </p>

    <p>
      <label>Public Key</label>
      <input type="text" name="publicKey" value="${publicKey!}"/>
    </p>

    <p>
      <label>Client ID</label>
      <input type="text" name="clientId" value="${clientId!}"/>
    </p>
    <button>Send</button>
  </form>
</main>
</body>
</html>