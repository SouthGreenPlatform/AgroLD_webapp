/* First stage */

var nos = 0;
$(document).ready(function () {
    $("#search").attr("action", FACETED_URL);
    $(".regular").slick({
        /*scrollOverflow: true,
         slidesNavigation: true,
         navigation: true,
         verticalCentered: false,
         */
        autoplay: true,
        autoplaySpeed: 6000,
        pauseOnHover: false,
        pauseOnFocus: false,
        //scrollBar: false,
        // 				    autoScrolling: false,
        /*
         */
    });
    if (window.innerWidth > 970) {

        $('#fullPage').fullpage({

            /*
             scrollOverflow: false,
             */
            fitToSection: false,
            hybrid: true,
            normalScrollElement: '#section1',
            touchSensitivity: 20,
            normalScrollElementTouchThreshold: 20,
            bigSectionsDestination: null,
            scrollOverflow: true,

            /*
             paddingBottom: 70,
             slidesNavigation: true,
             navigation: true,
             autoScrolling: false,
             scrollBar: false,
             verticalCentered: false,
             anchors:['home', 'kurse', 'news', 'agb', 'jobs', 'impressum'],
             afterRender: function(){
             //alert('hello - i am done!');
             //$.fn.fullpage.setAllowScrolling(false);
             },
             afterLoad: function(anchorLink, index){
             //alert(anchorLink);
             //Tell GA where we go...
             ga('set', 'page', '/' + anchorLink );
             ga('send', 'pageview');
             }*/
            //verticalCentered: false*/*/*/*/*/*/
        });

        /* Correction d'un conflit entre le fait que fullPage.js veuille le centrer 
         verticalement et qu'on le veuille sticky */

        $('#section1 .fp-tableCell').removeClass('fp-tableCell').addClass('bug-rport-1').attr('style', 'height: auto;');
        $('.titled-section').attr('style', 'padding-top: 70px;');
        //$('#section0 .fp-tableCell').removeClass('fp-tableCell').addClass('bug-rport-1').attr('style','height: auto;');

        /* Gestion du scroll de la partie #section1 à #section0, lorsqu'on scroll vers le
         haut on réimplémente le comportement par défaut de fullPage.js */

        distance = 99999;
        $(window).on('scroll', function () {
            var scrollTop = $(window).scrollTop();
            var elementOffset = $('#section1').offset().top;
            // var headerNav = $('#header').offset().top;

            if ((elementOffset - scrollTop) > 0 && distance <= (elementOffset - scrollTop)) {
                $.fn.fullpage.moveSectionUp();
            }
            distance = (elementOffset - scrollTop);
            //                    $('.static-j').attr('style', 'top:100px;');

        });
    }

    /* Second stage */


    var width = window.innerWidth;
    var height = window.innerHeight;
    var max = [[1200, 798], [1000, 900], [828, 827], [2000, 660], [768, 2000]];

    function sp() {
        width = parseInt(window.innerWidth);
        height = parseInt(window.innerHeight);
        var i;
        for (i = 0; i < max.length; i++)
            if (width <= max[i][0] && height <= max[i][1]) {
                $('.slider').addClass("hideMe");
                i = -1;
                console.log('break : {' + width + ' x ' + height + '}');
                break;
            }
        if (i == max.length)
            $('.slider').removeClass("hideMe");

    }

    $(window).resize(function () {
        sp();
    });

    sp();

});