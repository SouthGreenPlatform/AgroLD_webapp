<%-- Document : survey Created on : 15 sept. 2015, 23:55:36 Author : Gildas --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>AgroLD:Survey</title>
    <jsp:include page="includes.jsp"></jsp:include>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link href="styles/style1.css" rel="stylesheet" type="text/css" />
    <link href="styles/menu1.css" rel="stylesheet" type="text/css" />
    <link href="intro.js-1.0.0/introjs.css" rel="stylesheet" type="text/css" />
    <script src="intro.js-1.0.0/intro.js" type="text/javascript"></script>
    <link href="styles/search.css" rel="stylesheet" type="text/css" />
  </head>
  <body>
    <jsp:include page="header.jsp"></jsp:include>
    <section>
      <div class="container">
        <div class="info_title">Thank you for helping us!</div>
        <p>
          Please take the following survey. Your answers will help us improve
          the applicaton to suit your needs.
        </p>
        <!--div style="border:2px solid #666; border-radius:11px; padding:10px;"-->

        <center>
          <iframe
            id="form-iframe"
            src="https://docs.google.com/forms/d/1fFYqUdJluoAzuzdxAsRXey1RUgOgDztur4YKq5Llv1I/viewform?hl=en&embedded=true"
            style="
              margin: 0;
              width: 100%;
              height: 850px;
              border: none;
              overflow: hidden;
            "
            scrolling="yes"
          >
            Loading...
          </iframe>
        </center>
        <!--/div-->
      </div>
    </section>
    <br />
    <jsp:include page="footer.html"></jsp:include>
  </body>
  <script>
    $(document).ready(function () {
      $.ajax({
        type: "post",
        data: 'p={m:"setPageConsult",page:"survey"}',
        url: "ToolHistory",
        success: function (data) {
          $(".success").html(data);
        },
      });
    });
  </script>
</html>
