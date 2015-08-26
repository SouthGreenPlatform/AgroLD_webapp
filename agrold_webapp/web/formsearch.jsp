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
        <link href="styles/style1.css" rel="stylesheet" type="text/css"/>
        <link href="styles/menu1.css" rel="stylesheet" type="text/css"/> 
        <script src="swagger/lib/swagger-client.js" type="text/javascript"></script>
        <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
        <script src="//code.jquery.com/jquery-1.10.2.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script> 
        <script>
            var eltTypeKeys = ["gene", "protein", "qtl"];
            var eltTypeValues = ["Gene", "Protein", "QTL"];
            var eltTypeService = ["api/1.0/genes.json", "api/1.0/proteins.json", "api/1.0/qtls.json"];
            var sTypeElts;
            var input;
            function generateForm() {
                // automatic generation of the form                                
                var div = document.getElementById("advanced-form");
                var f = document.createElement("form");
                f.setAttribute('method', "post");
                f.setAttribute('action', "#");
                f.setAttribute('id', 'sform');
                var s = document.createElement("select"); //input element, text
                s.setAttribute('name', "elementType");
                s.setAttribute('class', "target");
                //s.setAttribute('onChange', "getGenes()");
                var o;
                for (i = 0; i < eltTypeKeys.length; i++) {
                    o = document.createElement("option");
                    o.setAttribute('value', eltTypeKeys[i]);
                    o.innerHTML = eltTypeValues[i];
                    s.appendChild(o);
                }
                s.selectedIndex = 1;
                sTypeElts = s;
                f.appendChild(s);
                var ip = document.createElement("input"); //input element, text
                ip.setAttribute('type', "text");
                ip.setAttribute('name', "label");
                ip.setAttribute('id', "eltTags");
                ip.setAttribute('placeholder', "Type it's code/name...");
                input = ip;
                f.appendChild(ip);
                f.appendChild(document.createElement("br"));
                /*var sb = document.createElement("input");
                 sb.setAttribute('type', "submit");
                 sb.setAttribute('value', "Search!");
                 //sb.setAttribute('onclick', "getGenes()");
                 f.appendChild(sb);*/
                div.appendChild(f);
            }
        </script>
        <script>
            var availableUri = [];
            var availableDescription = [];
            var availableElts;

            // initialize swagger, point to a resource listing
            window.swagger = new SwaggerClient({
                url: "http://volvestre.cirad.fr:8080/aldp/swagger/agrold.json",
                success: function () {
                    // upon connect, fetch a pet and set contents to element "mydata"
                    swagger.apis.gene.getgenes({_format: "json"}, {responseContentType: 'application/json'}, function (data) {
                        //document.getElementById("mydata").innerHTML = JSON.stringify(data.obj);
                    });
                }
            });

            $(document).ready(function () {
                $(".target").change(function () {
                    input.setAttribute('placeholder', "Please wait a bit...");
                    //input.setAttribute('disabled', "true");
                    document.getElementById("eltTags").value = "";
                    swagger.apis.protein.getproteins({}, {responseContentType: 'application/json'}, function (data) {
                        while (availableDescription.length > 0) {
                            availableDescription.pop();
                            availableUri.pop();
                        }
                        var json = data.obj;
                        availableElts = json;
                        console.log("JSON Data: " + json["head"]["vars"]);
                        nbResults = Object.keys(json.results.bindings).length;
                        for (i = 0; i < nbResults; i++) {
                            //availableDescription.push({value: json.results.bindings[i][json.head.vars[0]].value, label: json.results.bindings[i][json.head.vars[0]].value, desc: json.results.bindings[i][json.head.vars[1]].value});

                            availableDescription.push({label: json.results.bindings[i][json.head.vars[1]].value, idx: i});
                            availableUri.push(json.results.bindings[i][json.head.vars[0]].value);
                            //console.log(availableDescription);
                        }
                        //console.log(availableDescription);  

                        input.setAttribute('placeholder', "Type it's code/name...");
                        //input.setAttribute('disabled', "false");
                    });
                });

                $("#eltTags").autocomplete({
                    source: availableDescription,
                    minLength: 3,
                    select: function (event, ui) { // lors de la sélection d'une proposition
                        //$('#description').append(ui.item.desc); // on ajoute la description de l'objet dans un bloc
                        //alert(ui.item.idx); // n° de l'item sélectionné                        
                        //document.getElementById("result").innerHTML = "<hr><b>URI:</b> " + availableUri[ui.item.idx];
                        //document.getElementById("result").innerHTML += "<br><b>Description:</b> " + availableDescription[ui.item.idx].label;
                        document.getElementById("result").innerHTML = "<h4><b>" + eltTypeValues[sTypeElts.selectedIndex] + ": </b>" + availableElts.results.bindings[ui.item.idx][availableElts["head"]["vars"][0]].value + "</h4>";
                        /*for (i = 0; i < availableElts["head"]["vars"].length; i++) {
                         document.getElementById("result").innerHTML += "<b>" + availableElts["head"]["vars"][i] + ":</b> " + availableElts.results.bindings[ui.item.idx][availableElts["head"]["vars"][i]].value + "<br>";
                         }*/
                        $.getJSON("/agrold/api/res/description.json?uri=" + availableElts.results.bindings[ui.item.idx][availableElts["head"]["vars"][0]].value, function (json2) {
                            console.log(json2);
                            nbResults = Object.keys(json2.results.bindings).length;
                            for (i = 0; i < nbResults; i++) {
                                document.getElementById("result").innerHTML += "<b>" + json2.results.bindings[i].property.value + ":</b> " + json2.results.bindings[i].object.value + "<br>";
                            }
                        });
                    }
                });
            });</script>
        <style>
            #container .advanced-form{
                width: 50%;
            }
            .advanced-form #sform{
                position:fixed;
                top:10%;
                left:6%;
                width:75%;                  
            }
            #sform input[type="text"]{
                width: 45%;
                margin-left: 5pt;
            }
            #sform input, #sform select{
                font-size: 110%;
                margin-bottom: 5pt;
            }      
            #sform select{
                padding: 5pt;                
                width: 15%;
            }
            #sform input[type="submit"]{
                width: 60%;
            }
            /*.border-right{
                border-right: 1px solid black;
            }
            .border-right {
                border-right: 1px solid black;
            }
            #result{
                position: relative;
                
            }*/
        </style>       
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="header.html"></jsp:include>
                <section>
                    <h3>Search > Advanced search</h3>
                    <div id="advanced-form" class="border-right">
                    </div>
                    <div id="result">

                    </div>
                    <script>
                        generateForm();
                    </script>  
                </section><br>
            <jsp:include page="footer.html"></jsp:include>
        </div>
    </body>
</html>
