<%-- 
    Document   : formsearch
    Created on : Jul 15, 2015, 3:40:50 PM
    Author     : tagny
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>AgroLD: Form-based search</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="yasqe/dist/yasqe.min.css" rel="stylesheet" type="text/css"/>
        <link href='http://cdn.jsdelivr.net/g/yasqe@2.2(yasqe.min.css),yasr@2.4(yasr.min.css)' rel='stylesheet' type='text/css'/>
        <link href="styles/search.css" rel="stylesheet" type="text/css">        
        <link href="styles/style1.css" rel="stylesheet" type="text/css"/>
        <link href="styles/menu1.css" rel="stylesheet" type="text/css"/> 
        <script src="swagger/lib/swagger-client.js" type="text/javascript"></script>
        <script src="//code.jquery.com/jquery-1.10.2.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script> 
        <script src="scripts/lib.js" type="text/javascript"></script>
        <script src="yasr/yasr.bundled.min.js" type="text/javascript"></script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="header.html"></jsp:include>
                <section>
                    <div class="info_title">Search > Advanced form-based search</div>
                    <div id="advanced-form" class="border-right">
                    <jsp:include page="WEB-INF/jspf/advancedForm.jsp"></jsp:include>
                    </div>
                    <div id="result-container">
                        <div id="overlay" style="display: none;"></div>
                    <%
                        String type = request.getParameter("type");
                        //String id = request.getParameter("id");
                        System.out.print("type="+ type);
                        
                        String uri = request.getParameter("uri");
                        //out.println("uri="+ uri);
                        
                        String keyword = request.getParameter("keyword");
                        
                        if (type != null) {
                    %>
                    <script>
                        var type = "<% out.print(type);%>";
                    </script>
                    <%
                        if (keyword != null) {
                    %>  
                    <script>
                        $(document).ready(function () {
                            var keyword = "<% out.print(keyword);%>";
                            $('#type option[value=' + type + ']').prop('selected', true);
                            $("input.keyword").val(keyword);
                        });
                    </script>
                    <span id="Search-info">Search <b><% out.print(type);%></b> with keyword <% out.print("\"<b> " + keyword + " </b>\"<br>");%></span><span id="pageBtns"></span>
                    <div id="result"></div>                    
                    <script src="scripts/search.js" type="text/javascript"></script>
                    <script type="text/javascript">
                        var type = "<% out.print(type);%>";
                        var keyword = "<% out.print(keyword);%>";
                        // display result of the search of key                        
                        $(document).ready(function () {
                            search(type, keyword, 0);
                        });
                    </script>

                    <%
                    } else if (uri != null) {
                        switch (type) {
                            case "gene":
                                // display the description of a gene
                    %>                    
                    <jsp:include page="WEB-INF/jspf/gene.jsp"></jsp:include>
                    <%
                                break;
                            case "protein":
                    %>                    
                    <jsp:include page="WEB-INF/jspf/protein.jsp"></jsp:include>                
                    <script>    <%
                                break;
                            case "qtl":
                    %>
                    <jsp:include page="WEB-INF/jspf/qtl.jsp"></jsp:include>
                    <%
                            break;
                        case "pathway":
                    %>
                    <jsp:include page="WEB-INF/jspf/pathway.jsp"></jsp:include>
                    <%
                            break;
                        case "ontology":
                    %>
                    <jsp:include page="WEB-INF/jspf/ontology.jsp"></jsp:include>
                    <%
                            break;
                                              }
                                          }
                                      }
                    %>
                    </script>
                </div>                
            </section>
            <div id="push"></div> <!--add the push div here -->
            <jsp:include page="footer.html"></jsp:include>
        </div>          
        <script>
            size1 = Math.round(pageSize / 3);
            size2 = Math.round(pageSize * 2 / 3);
            YASR.plugins.table.defaults.datatable["pageLength"] = pageSize;
            YASR.plugins.table.defaults.datatable["lengthMenu"] = [[size1, size2, pageSize, -1], [size1, size2, pageSize, "All"]];
            YASR.plugins.table.defaults.fetchTitlesFromPreflabel = false;
        </script>
    </body>
</html>
