<%@ page pageEncoding="UTF-8" %>
<%@ page import="javax.servlet.http.HttpSession"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <!--fiveico-->
        <link rel="icon" href="images/logo_min.png" />
        <link rel="icon" type="image/png" href="images/logo_min.png" />
        <title>Login - AgroLD</title>
        <link type="text/css" rel="stylesheet" href="form.css" />
        <link type="text/css" rel="stylesheet" href="styles/login.css" />
        <!-- Jquery baby -->
        <script type="text/javascript" src="scripts/jquery-3.2.1.min.js"></script
        <!-- Bootstrap -->
        <link rel="stylesheet" type="text/css" href="styles/bootstrap/css/bootstrap-grid.min.css"/>
        <link rel="stylesheet" type="text/css" href="styles/bootstrap/css/bootstrap.css"/>
        <link rel="stylesheet" type="text/css" href="styles/font-awesome/css/font-awesome.min.css"/>
        <script type="text/javascript" src="styles/bootstrap/js/bootstrap.min.js"></script>
    </head>            
    <body class="o-login">
        <div class="container-fluid">
            <div class="container">
                <div class="row">
                    <div class="col-sm-6 col-md-5">
                        <div class="account-wall">
                            <div id="my-tab-content" class="tab-content">
                                <div class="tab-pane active" id="login">
                                    <img class="profile-img" src="images/logo-login.png" alt="agrold logo">
                                    <form class="form-signin" action="" method="post">
                                        <input type="email" id="email" name="email" value="${utilisateur.email}" size="20" maxlength="60" class="form-control" placeholder="Username" required autofocus>
                                        <input type="password" class="form-control" placeholder="Password" id="motdepasse" name="motdepasse" value="" size="20" maxlength="20" required>
                                        <input type="submit" class="btn btn-lg btn-default btn-block" value="Sign In" />
                                    </form>
                                    <div id="tabs" data-tabs="tabs">
                                        <p class="text-center"><a href="Register">Need an Account?</a></p>
                                        <div class="o-co-error">
                                            <span style="text-align:center;padding:25px;color:red;" class="erreur">${param.error==1?'User id or password is incorrect':''}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </<div>
    </body>
</html>