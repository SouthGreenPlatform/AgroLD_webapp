<%@ page pageEncoding="UTF-8" %>
<%@ page import="javax.servlet.http.HttpSession"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8"/>
        <title>Register - AgroLD</title>
        <link type="text/css" rel="stylesheet" href="form.css" />
        <link type="text/css" rel="stylesheet" href="styles/login.css"/>
        <style>
            p.erreur ul li{ padding:25px;color:red;}
        </style>
        <!-- Jquery baby -->
        <script type="text/javascript" src="scripts/jquery-3.2.1.min.js"></script>
        <!-- Bootstrap -->
        <link rel="stylesheet" type="text/css" href="styles/bootstrap/css/bootstrap-grid.min.css"/>
        <link rel="stylesheet" type="text/css" href="styles/bootstrap/css/bootstrap.css"/>
        <link rel="stylesheet" type="text/css" href="styles/font-awesome/css/font-awesome.min.css"/>
        <!--fiveico-->
        <link rel="icon" href="images/logo_min.png" />
        <link rel="icon" type="image/png" href="images/logo_min.png" />
    </head>
    <body class="o-register">
        <div class="container-fluid">
            <div class="container">
                <div class="row">
                    <div class="col-sm-6 col-md-5">
                        <div class="account-wall">
                            <div id="my-tab-content" class="tab-content">
                                <div class="tab-pane active" id="login">
                                    <img class="profile-img" src="images/logo-login.png" alt="Agrold logo">

                                    <p class="erreur" style="padding:0 25px 0 25px;color:red;">${form.resultat}</p>
                                    <form class="form-signin" method="post" action="Register">
                                        <input type="email" id="email" name="email" value="${utilisateur.email}" size="20" maxlength="60" class="form-control" placeholder="Username" required autofocus>
                                        <input type="password" class="form-control" placeholder="Password" id="motdepasse" name="password1" value="" size="20" maxlength="20" required>
                                        <input type="password" class="form-control" placeholder="Confirm password" id="motdepasse" name="password2" value="" size="20" maxlength="20" pattern="{8}" required>
                                        <input type="submit" class="btn btn-lg btn-default btn-block" value="Register" />
                                    </form>
                                    <div id="tabs" data-tabs="tabs">
                                        <p class="text-center"><a href="Login">Login</a></p>
                                        <div class="o-co-error">
                                            <span class="erreur">${form.erreurs['motdepasse']}</span>
                                        </div>
                                    </div>
                                    <script>
                                        if($('#res').attr('class') === 'ok'){
                                            $('form').html('');
                                            $('div.tabs').html('');
                                            $('.good').text('Register sucess, you will be redirected in 3 secondes');
                                            setTimeout(function() {
                                                window.location.href = WEBAPPURL + '/Login';
                                            }, 2200);
                                        }
                                    </script>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </<div>
    </body>
    
</html>