<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.lang.reflect.Field" %>
<!DOCTYPE html>
<html>
<head>
    <title>AgroLD: Form-based search V2</title> 

        <link rel="shortcut icon" href="image/favicon.ico" > <!-- favicon added -->               
        <link rel="icon" href="images/logo_min.png" />
        <link rel="icon" type="image/png" href="images/logo_min.png" />
        <!-- Les includes -->
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="styles/advSearch.css">
        <link rel="stylesheet" type="text/css" href="styles/css-loader.css">
        <link href="sparqleditor/yasr.min.css" rel="stylesheet" type="text/css"/>
        <script src='sparqleditor/yasr.bundled.min.js'></script>               
        
        <jsp:include page="includes.jsp"></jsp:include>
        <link href="styles/search.css" rel="stylesheet" type="text/css">        		
        <script src="scripts/URI.js"></script>
        <script src="scripts/lib.js" type="text/javascript"></script>
        <script src="swagger/lib/swagger-client.js" type="text/javascript"></script>
        <link href="https://cdn.datatables.net/1.10.18/css/jquery.dataTables.min.css" rel="stylesheet" type="text/css">        
        <script src="https://cdn.datatables.net/1.10.18/js/jquery.dataTables.min.js" type="text/javascript"></script>
        <style>
            #graphViewResult {
                width: inherit;
                height: 800px;
                position: relative;
                top: 0px;
                left: 0px;
            }
        </style>
    </head>
    <body>
        <div class="debug">
        </div>
        <jsp:include page="header.jsp"></jsp:include>
            <div class="container-fluid arian-thread">
                <div class="info_title">
                    <div class="container pos-l">Search > <span class="active-p">Advanced form-based search</span></div>
                </div>
            </div>
            <div class="foowrap">
                <section>
                    <div id="advanced-form" class="border-right">
                    <jsp:include page="WEB-INF/jspf/advancedForm.jsp"></jsp:include>
                    <jsp:include page="WEB-INF/jspf/advancedModal.jsp"></jsp:include>
                    </div>
                    <div id="result-container">
                        <div class="container">

                        </div>
                        <div class="container">
                      <div id="result"></div>                    
                        <script src="scripts/search.js" type="text/javascript"></script>

                    </div>


                </div>

                <!-- Les differents types dispo dans le select -->
                <jsp:include page="WEB-INF/jspf/protein.jsp"></jsp:include>                
                <jsp:include page="WEB-INF/jspf/qtl.jsp"></jsp:include>
                <jsp:include page="WEB-INF/jspf/pathway.jsp"></jsp:include>
                <jsp:include page="WEB-INF/jspf/gene.jsp"></jsp:include>
                <jsp:include page="WEB-INF/jspf/ontology.jsp"></jsp:include>

                    <!-- Appel via l'historique -->


            </div>                
        </section>
        <div id="push"></div> <!--add the push div here -->
        <div style="height:50px;width:100%;"></div>
    </div>          
<jsp:include page="footer.html"></jsp:include>
<script>
        size1 = Math.round(DEFAULT_PAGE_SIZE / 3);
        size2 = Math.round(DEFAULT_PAGE_SIZE * 2 / 3);
        YASR.plugins.table.defaults.datatable["pageLength"] = DEFAULT_PAGE_SIZE;
        YASR.plugins.table.defaults.datatable["lengthMenu"] = [[size1, size2, DEFAULT_PAGE_SIZE, -1], [size1, size2, DEFAULT_PAGE_SIZE, "All"]];
        YASR.plugins.table.defaults.fetchTitlesFromPreflabel = false;
</script>
</body>
<script>
    $(document).ready(function () {
        $.ajax({
            type: 'post',
            data: 'p={m:"setPageConsult",page:"advancedSearch"}',
            url: 'ToolHistory',
            success: function (data) {
                $('.success').html(data);
            }
        });
    });
</script>
</html>
       
            