<%--    
    Document   : user
    Created on : 14 août 2017, 15:45:18
    Author     : Jc
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
    .fadeAppear {
        animation: fadein 1s;
        -moz-animation: fadein 1s; /* Firefox */
        -webkit-animation: fadein 1s; /* Safari and Chrome */
        -o-animation: fadein 1s; /* Opera */
    }
    @keyframes fadein {
        from {
            opacity:0;
        }
        to {
            opacity:1;
        }
    }
    @-moz-keyframes fadein { /* Firefox */
        from {
            opacity:0;
        }
        to {
            opacity:1;
        }
    }
    @-webkit-keyframes fadein { /* Safari and Chrome */
        from {
            opacity:0;
        }
        to {
            opacity:1;
        }
    }
    @-o-keyframes fadein { /* Opera */
        from {
            opacity:0;
        }
        to {
            opacity: 1;
        }
    }
    /* The switch - the box around the slider */
    .switch {
        position: relative;
        display: inline-block;
        width: 60px;
        height: 34px;
    }

    /* Hide default HTML checkbox */
    .switch input {display:none;}

    /* The slider */
    .slider {
        position: absolute;
        cursor: pointer;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background-color: #ccc;
        -webkit-transition: .4s;
        transition: .4s;
    }

    .slider:before {
        position: absolute;
        content: "";
        height: 26px;
        width: 26px;
        left: 4px;
        bottom: 4px;
        background-color: white;
        -webkit-transition: .4s;
        transition: .4s;
    }

    input:checked + .slider {
        background-color: #2196F3;
    }

    input:focus + .slider {
        box-shadow: 0 0 1px #2196F3;
    }

    input:checked + .slider:before {
        -webkit-transform: translateX(26px);
        -ms-transform: translateX(26px);
        transform: translateX(26px);
    }

    /* Rounded sliders */
    .slider.round {
        border-radius: 34px;
    }

    .slider.round:before {
        border-radius: 50%;
    } 
</style>


<script type="text/javascript">

    var Util = {
        specialParsing: function (param) {
            var tab = [];
            tab = param.split(/,|:/);
            var json = '';
            for (var i = 0; i < tab.length; i++) {
                console.log(i + ' : ' + tab[i]);
            }
            for (var i = 0; i < tab.length; i++) {
                if ((i % 2) == 0)
                    json += '"' + tab[i] + '"';
                else if (isNaN(parseInt(tab[i])))
                    json += ':"' + tab[i] + '"';
                else
                    json += ':' + tab[i];
                if (i != tab.length - 1 && (i % 2) == 1)
                    json += ',';
                console.log(json);
            }
            return json;
        },
        tableChecked:function(table){
            var result = {};
            var i = 0;
            console.log('on est passé par ici');
            $('body tbody#'+ table + ' tr td input[type=checkbox]').each(function(e){
                if($(this).is(':checked'))
                    result[i++]=$(this).attr('value');
            });
            if(typeof result[0] == 'undefined')
                result[0]="null";
            return result;
        }, 
        setCheckable:function(o){
            $('body '+o).click(function () {
                var ID_TBODY = $(this).closest('table').find('tbody').attr('id');
                if ($(this).is(':checked')) {
                    $('body #' + ID_TBODY + " input[type=checkbox]").each(function () {
                        $(this).prop('checked', true);
                    });
                } else {
                    $('body #' + ID_TBODY + " input[type=checkbox]").each(function () {
                        $(this).prop('checked', false);
                    });

                }
            });
        },
        paginator:function(o){
            var paginators = {
                userListFutureAdmin:'getFutureAdmin',
                userListManager:'getUserManager'
            };
            var TARGET_HTML = o.f;
            var FUNCTION_NAME = paginators[TARGET_HTML];
            var OFFSET_PAGE = o.offset;
            var BUTTON_CONTEXT = $(o.btn);
            var PREVIOUS_VALUE = BUTTON_CONTEXT.closest('ul').find('.active').attr('value') ;
            
            BUTTON_CONTEXT.closest('.pagination').find('.active').toggleClass('active');
            BUTTON_CONTEXT.parent().toggleClass('active');

            console.log("[^] Paginator ");
            console.log('[^] TARGET_HTML : ' + TARGET_HTML);
            console.log('[^] FUNCTION_NAME : ' + FUNCTION_NAME);
            console.log('[^] OFFSET_PAGE : ' + OFFSET_PAGE);
            
            $.ajax({
                    type: 'post',
                    data: 'f=' + FUNCTION_NAME + '&p={"limit":"10","offset":"' + OFFSET_PAGE + '"}',
                    success: function (data) {
                        var tbody = $.parseHTML(data);
                        tbody = $(tbody).find('tbody').html();
//                        .find('tbody').html();
//                        $('#'+TARGET_HTML).html(tbody);
                        $('#'+TARGET_HTML).html(tbody);
                    },
                    error: function (data) {
                        
                        console.log('Erreur de pagination :"(' + data);
                    }
                });            
        }
    };
    function ArianT(icon, Text) {
        $('.info_title i').attr('class', icon);
        $('body span.active-p').html(Text);
    }
    var struct = {
        <c:if test="${sessionScope.sessionUtilisateur.getRight()==0}">
        <jsp:include page="admin.js"></jsp:include>
        </c:if>
        home: {
            call: 'getHome',
            callback: function (data) {
                ArianT('fa fa-home', 'Home');
                $('#dynamic-container').html(data.data);
                if (UserInfo.fname.value === '' && UserInfo.name.value === '') {
                    console.log("nom et fnom vide")
                    var message = '<div class="alert alert-success alert-dismissible fade show" role="alert">\
                                <button type="button" class="close closeinfo" data-dismiss="alert" aria-label="Close">\
                                    <span aria-hidden="true">&times;</span>\
                                </button>\
                                <strong>Hello ${ sessionScope.sessionUtilisateur.email }</strong> it\'s your first time in AgroLD ? Please complete yours informations <strong><a href="javascript:void(0)" onclick="$(\'.o-settings-parent\').click();$(\'#getUserInfo\').click();"> here</a></strong>\
                           </div>';
                    StaticContent.put(message);
                    console.log(message);
                    console.log($('body #static-conatiner'));
                }
                Page.style();
            },
            p: false
        },
        getAdvancedSearch: {
            call: 'onHistory',
            callback: function (data) {
                ArianT($('#getAdvancedSearch i').attr('class'), 'History > Advanced Search');
                $('#dynamic-container').html(data.data);
                Page.style();
                console.log("User > getAdvancedSearch");
            },
            m: 'getAdvancedSearch',
            p: false
        },
        getSparqlEditor: {
            call: 'onHistory',
            callback: function (data) {
                ArianT($('#getSparqlEditor i').attr('class'), 'History > Sparql Editor');
                $('#dynamic-container').html(data.data);
                Page.style();
                console.log("User > getSparqlEditor");
            },
            m: 'getSparqlEditor',
            p: false
        },
        getQuickSearch: {
            call: 'onHistory',
            callback: function (data) {
                ArianT($('#getQuickSearch i').attr('class'), 'History > Quick Search');
                $('#dynamic-container').html(data.data);
                Page.style();
                console.log("User > getQuickSearch");
            },
            m: 'getQuickSearch',
            p: false
        },
        getUserInfo: {
            call: 'onUserInfo',
            callback: function (data) {
                ArianT($('#getUserInfo i').attr('class'), 'Settings > User');
                $('#dynamic-container').html(data.data);
                Page.style();
                console.log("User > getUserInfoUser");
            },
            m: 'getUserInfo',
            p: false
        },
        getHistorySettings: {
            call: 'onHistorySettings',
            callback: function (data) {
                ArianT($('#getHistorySettings i').attr('class'), 'Settings > History');
                $('#dynamic-container').html(data.data);
                Page.style();
                console.log("User > getHistorySettings");
            },
            m: 'getHistorySettings',
            p: false
        },
        contactAdmin: {
            call: 'onContactAdmin',
            callback: function (data) {
                ArianT($('#contactAdmin i').attr('class'), 'Home');
                $('#dynamic-container').html(data.data);
                Page.style();
                console.log("User > contactAdmin");
            },
            m: 'contactAdmin',
            p: false
        },
        deleteQuickSearch: {
            call: 'onHistory',
            callback: function (data) {
                console.log('lol' + data.param);
                $('body #qs-entry-' + data.param.id).remove();
                console.log("Delete entry in QuickSearch History");
            },
            controller: function (o) {
                return Util.specialParsing(o.attr('value'));
            },
            m: 'deleteQuickSearch',
            p: true
        },
        deleteAdvancedSearch: {
            call: 'onHistory',
            callback: function (data) {
                console.log('lol' + data.param);
                $('body #ads-entry-' + data.param.id).remove();
                console.log("Delete entry in AdvancedSearch History");
            },
            controller: function (o) {
                return Util.specialParsing(o.attr('value'));
            },
            m: 'deleteAdvancedSearch',
            p: true
        },
        deleteSparqleditor: {
            call: 'onHistory',
            callback: function (data) {
                console.log('lol' + data.param);
                $('body #sp-entry-' + data.param.id).remove();
                console.log("Delete entry in SparQLEditor History");
            },
            controller: function (o) {
                return Util.specialParsing(o.attr('value'));
            },
            m: 'deleteSparqlEditor',
            p: true
        },
        settingsQuickSearch: {
            call: 'onHistorySettings',
            callback: function (data) {
                console.log('lol' + data.param);
                console.log("Settings changed");
            },
            controller: function (o) {
                console.log("!H : " + (o.is(':checked') == true ? 1 : 0));
                var req = 'id:' + (o.is(':checked') == true ? '1' : '0');
                return req;
            },
            m: 'settingsQuickSearch',
            p: true
        },
        settingsAdvancedSearch: {
            call: 'onHistorySettings',
            callback: function (data) {
                console.log('lol' + data.param);
                console.log("Settings changed");
            },
            controller: function (o) {
                console.log("!H : " + "ADVANCED SEARCH");
                var req = 'id:' + (o.is(':checked') == true ? '1' : '0');
                return req;
            },
            m: 'settingsAdvancedSearch',
            p: true
        },
        settingsSparqlEditor: {
            call: 'onHistorySettings',
            callback: function (data) {
                console.log('lol' + data.param);
                console.log("Settings changed");
            },
            controller: function (o) {
                console.log("!H : " + (o.is(':checked') == true ? 1 : 0));
                var req = 'id:' + (o.is(':checked') == true ? '1' : '0');
                return req;
            },
            m: 'settingsSparqlEditor',
            p: true
        },
        settingsUserInfo: {
            call: 'onUserInfo',
            cache: false,
            callback: function (data) {
                $('#getUserInfo').click();

                if (this.cache)
                    StaticContent.clear(function () {
                        $('.closeinfo').click();
                    });
            },
            controller: function (o) {
                var col = {};
                $('body input[type=text].info-user').each(function (index) {
                    col[$(this).attr('id')] = $(this).val();
                });
                var response = JSON.stringify(col).replace('{', '').replace('}', '');
                console.log(response);
                if (col.name == '' || col.fname == '')
                    this.cache = true;
                return response;
            },
            m: 'updateUserInfo',
            p: true
        },
        getContact: {
            call: 'getContact',
            p: false,
            callback: function (data) {
                ArianT($('#getContact i').attr('class'), 'Contact');
                $('#dynamic-container').html(data.data);
                Page.style();


            }
        }
    };
    var Page = {
        start: function () {
            // this.loadStaticMessage();
            this.loadDynamicPage('f=home');
            $('#home').click();
            console.log('Page Start');
        },
        loadDynamicPage: function (target) {
            /* On charge l'appel des pages via le menu */
            /* Trigger sur la classe o-mi */
            $('body').on('click', '.o-mi', function (e) {
                if($(this).hasClass('no-bubling')) e.stopPropagation();
                var id = $(this).attr('id');
                var call = (typeof window['struct'][id]['call'] !== 'undefined' ? window['struct'][id]['call'] : ''); // Méthode / Objet à appeler sur le serveur
                var method = (typeof window['struct'][id]['m'] !== 'undefined' ? 'm:"' + window['struct'][id]['m'] + '"' : '');   // Le nom de la méthode
                var param = (window['struct'][id]['p'] ? window['struct'][id]['controller']($(this)) : '');   // Les paramètres de la méthode sont sur le tag > value
                if (method !== "" && param !== "")
                    method += ',';
                // Voir avec l'accès par val() en plus, non actif pour le moment

                /* call peut être   : 1) la méthode à appeler de l'utilisateur (côté serveur) > m est donc vide
                 : 2) le nom d'une invokation, Ex : user.historique.afficheAdvancedSearch();
                 Dans ce cas on précise la méthode d'invocation (côté serveur) Ex: call:onHistorique Cf UserDAOImpl_java.onHistorique.invoke(m:getAdvancedSearch)
                 On précise aussi la méthode à appeler sur l'objet emboîté (côté serveur) Ex: m:getAdvancedSearch
                 */
                console.log('--------------------------------------');
                console.log('DEBUG : ');
                console.log('CALL : ' + call + ', id : ' + id);
                console.log('METHOD : ' + method + ', PARAM : ' + param);
                console.log('--------------------------------------');
                $.ajax({
                    type: 'post',
                    data: 'f=' + call + '&p={' + method + param + '}',
                    success: function (data) {
                        if (param !== '') {
                            jev = '{' + param + '}';
                            console.log(jev);
                            var parsed = JSON.parse(jev);
                        }
                        if (window['struct'][id]['callback'] !== 'undefined')
                            window['struct'][id]['callback']({data: data, id: id, param: parsed});
                        console.log(' [HTTP/1.1 200 OK] ');
                    },
                    error: function (data) {
                        $('body #static-container').html("Erreur !  , ID:" + id);
                        console.log('Pas succès');
                    }
                });
            });
        },
        style: function () {
            $('#dynamic-container').addClass('fadeAppear');
            setTimeout(function () {
                $('#dynamic-container').removeClass('fadeAppear');
            }, 2000);
        }
    };
    $(document).ready(function () {
        Page.start();
    });

</script>