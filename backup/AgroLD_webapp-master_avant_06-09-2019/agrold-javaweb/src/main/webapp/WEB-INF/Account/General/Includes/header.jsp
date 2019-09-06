<div id="header">
    <nav class="navbar fixed-top navbar-toggleable-md navbar-light bg-faded">
        <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarTogglerDemo02" aria-controls="navbarTogglerDemo02" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="container">
            <a class="navbar-brand" href="http://agrold.southgreen.fr/"><img src="images/v5_v2.png"></a>

            <div class="collapse navbar-collapse" id="navbarTogglerDemo02">
                <ul class="navbar-nav mr-auto mt-2 mt-md-0">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            Search
                        </a>
                        <div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                            <a class="dropdown-item" href="quicksearch.jsp" title="Keyword-based Search">Quick Search</a>
                            <a class="dropdown-item" href="advancedSearch.jsp" title="Multiservice Knowledge aggregator">Advanced Search</a>
                            <a class="dropdown-item" href="relfinder.jsp" title="Interactive Relationships Discovery in RDF Data">Explore Relationships</a>
                            <a class="dropdown-item" href="sparqleditor.jsp" title="SPARQL Query">SPARQL Query</a>
                        </div>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            Help
                        </a>
                        <div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                            <a class="dropdown-item" href="documentation.jsp">Documentation</a>
                            <a class="dropdown-item" href="api-doc.jsp" title="AgroLD API documentation">AgroLD API</a>
                        </div>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link_2" href="about.jsp">About</a>
                    </li>
                </ul>
                <div class="form-inline my-2 my-lg-0">
                    <a class="nav-link btn btn-outline-secondary nohover" href="survey.jsp"><i class="fa fa-star-o"></i>&nbsp;&nbsp;Please send us your feedback!</a>
                </div>
                <div class="form-inline my-2 my-lg-0 user-nav">
                    <% if(session.getAttribute("sessionUtilisateur")!= null){%>
                    <a class="nav-link btn btn-outline-secondary" href="Logout"><i class="fa fa-power-off"></i>&nbsp;&nbsp;Logout</a>
                    <%}else{%>
                    <a class="nav-link btn btn-outline-secondary" href="Login"><i class="fa fa-user-circle-o"></i>&nbsp;&nbsp;Login</a>
                    <a class="nav-link btn btn-outline-secondary" href="Register"><i class="fa fa-sign-in"></i>&nbsp;&nbsp;Register</a>
                    <%}%>
                </div>
            </div>
        </div>
    </nav>
</div>
