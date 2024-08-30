<%-- 
    Document   : about
    Created on : Jun 29, 2015, 11:55:56 AM
    Author     : tagny
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
${pageContext.ELContext.importHandler.importClass('agrold.config.PropertiesBean')}
<!--Inject system properties inside js-->
<script type="text/javascript">
    // this functions is used parse system parameters inside javascript
    // indeed we can't use jsp tags inside javascript, the result comes as raw text
    // so we need to parse it if it is equals to null
    function parseJsp(text) {
        return text === "null" ? null : text;
    }

    const system_sparqlendpoint = parseJsp('${PropertiesBean.getSparqlEndpoint()}')
    const system_faceted_search_url = parseJsp('${PropertiesBean.getFacetedSearchURL()}')
</script>
<script type="text/javascript" src="config/config.js"></script>
