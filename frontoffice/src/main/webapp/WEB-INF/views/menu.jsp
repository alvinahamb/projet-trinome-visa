<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Menu</title>
    <link rel="stylesheet" href="<%= $assets("/assets/css/menu.css") %>">
</head>
<body>
    <div class="menu-container">
        <div class="eyebrow">Frontoffice VISA</div>
        <h1>Gestion de vos demandes VISA</h1>
        <p class="subtitle">Design noir et blanc, propre, rapide et adapté aux usages quotidiens.</p>
        <div class="actions">
            <a href="<%= $route("home/home") %>" class="btn">Menu</a>
        </div>
    </div>
</body>
</html>
