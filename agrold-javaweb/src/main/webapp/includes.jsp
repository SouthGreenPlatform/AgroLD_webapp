<%-- 
    Document   : about
    Created on : Jun 29, 2015, 11:55:56 AM
    Author     : tagny
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Tether -->
<script src="https://npmcdn.com/tether@1.2.4/dist/js/tether.min.js"></script>
<!-- Jquery baby -->
<script type="text/javascript" src="scripts/jquery-3.2.1.min.js"></script>
<!-- Bootstrap -->
<link rel="stylesheet" type="text/css" href="styles/bootstrap/css/bootstrap-reboot.min.css">

<link href="styles/style1.css" rel="stylesheet" type="text/css"/>
<link href="styles/sp.css" rel="stylesheet" type="text/css"/>
<link href="styles/menu.css" rel="stylesheet" type="text/css"/>

<!--link href="styles/menu1.css" rel="stylesheet" type="text/css"/-->
<link rel="stylesheet" type="text/css" href="styles/bootstrap/css/bootstrap-grid.min.css">
<link rel="stylesheet" type="text/css" href="styles/font-awesome/css/font-awesome.min.css">
<script type="text/javascript" src="styles/bootstrap/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css" href="styles/bootstrap/css/bootstrap.css">

<!--Inject system parameters inside js-->
<script type="text/javascript">
    // this functions is used parse system parameters inside javascript
    // indeed we can't use jsp tags inside javascript, the result comes as raw text
    // so we need to parse it if it is equals to null
    function parseJsp(text) {
        return text === "null" ? null : text;
    }

    const system_sparqlendpoint = parseJsp('<%= System.getProperty("agrold.sparql_endpoint", "http://sparql.southgreen.fr") %>')
    const system_faceted_search_url = parseJsp('<%= System.getProperty("agrold.faceted_search_url", "http://sparql.southgreen.fr/faceted") %>')
</script>
<script type="text/javascript" src="config/config.js"></script>

<link rel="icon" href="images/logo_min.png" />
<link rel="icon" type="image/png" href="images/logo_min.png" />
</head>
</html>