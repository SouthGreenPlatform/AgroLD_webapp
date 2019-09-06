<%-- 
    Document   : HomeAuthenticated
    Created on : 12 juil. 2017, 10:46:28
    Author     : Jc
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <script></script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home Member</title>
        <jsp:include page="Includes/includes.jsp"></jsp:include>
        <jsp:include page="Includes/user.jsp"></jsp:include>
        </head>
        <body>
        <jsp:include page="Includes/header.jsp"></jsp:include>
            <div id="wrapper" class="toggled">

            <jsp:include page="Includes/sidebarUser.jsp"></jsp:include>
            <div class="container-fluid arian-thread">
                <div class="container">
                    <div class="row">
                        <div style="" class="col-2 lunch-menu d all-t-05">
                            <i class="fa fa-bars"></i>
                        </div>
                        <div style="" class="o-ap col-8 info_title">
                            <i class="fa fa-home"></i> <span class="active-p">Home</span>
                        </div>
                        <div style="" class="col-2">
                            
                        </div>
                        
                    </div>
                </div>
            </div>
            <div class="foowrap">
                <div class="container main">
                    <div id="static-container">

                    </div>
                    <div id="dynamic-container"></div>
                    <div id="al"></div>
                </div>
                <div class="jump-bot"></div>
            </div>
    </body>
    <script type="text/javascript">
        PageStarted = false;
        SideBar();

        $(window).resize(function () {
            console.log('largeur ecran :'  + window.innerWidth);
            if(window.innerWidth<=990){
                
                if($("#wrapper").hasClass("toggled"))
                    SideBar();
            }
            else if(window.innerWidth>991){
                
                if(!$("#wrapper").hasClass("toggled"))
                    SideBar();
            }
                    
        });
        
        StaticContent = {
            container: $('#static-container'),
            put: function (html) {
                this.container.html(html);
            },
            clear: function (f) {
                f();
            }
        };
        $('body .lunch-menu').click(function () {
            SideBar();
        });
        function SideBar(){
            $('.arian-thread .lunch-menu').toggleClass("d");
            //$('.arian-thread .info_title').toggleClass("col-sm-8");
            //$('.arian-thread .info_title').toggleClass("col-sm-10");
            $('.arian-thread .info_title').toggleClass("t-a-c");
            $("#wrapper").toggleClass("toggled");
        }
                $(window).resize();

    </script>
</html>