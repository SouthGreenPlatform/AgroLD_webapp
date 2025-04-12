<script lang="js" setup>

$(document).ready(function () {

    function hideFooterForHomePage(elementId) {
        const url = window.location.pathname.split("/").pop();
        if (url.length == 0) {
            $('.hexagone').addClass('hexagone-no-filtered');
            return true;
        }
        return false;
    }

    function scrollBarVerticalActive() {
        return $(document).height() > $(window).height();
    }

    function displayElementOnScroll(elementId) {
        if (scrollBarVerticalActive()) {
            $(window).scroll(function () {
                const documentHeight = $(document).height();
                const windowHeight = $(window).height();
                const scrollPosition = $(window).scrollTop();
                const appearancePosition = documentHeight - windowHeight - 100;
                if (scrollPosition >= appearancePosition) {
                    $(elementId).fadeIn('slow');
                } else {
                    $(elementId).fadeOut('slow');
                }
            });
        } else {
            $(elementId).css({
                'display': 'block'
            });
        }
    }

    $(window).resize(function () {
        displayElementOnScroll('#footer');
    });
    displayElementOnScroll('#footer');
    hideFooterForHomePage('#footer');
});
</script>

<template>
    <footer class="footer" id="footer">
        <div class="container">
            <div class="col-md-12 col-lg-12 resolve-grid">
                <div class="col">
                    <a href="http://www.ibc-montpellier.fr/wp/wp5">
                        <div class="hexagone"><img src="/images/IBC.png" alt="IBC"></div>
                    </a>
                </div>
                <div class="col">
                    <a href="http://www.cirad.fr">
                        <div class="hexagone"><img alt="CIRAD" src="/images/CIRAD.png"></div>
                    </a>
                </div>
                <div class="col">
                    <a href="http://www.ird.fr">
                        <div class="hexagone"><img src="/images/IRD.png" alt="IRD"></div>
                    </a>
                </div>
                <div class="col">
                    <a href="https://www.umontpellier.fr">
                        <div class="hexagone"><img src="/images/UM.png" alt="Univ. Montpellier"></div>
                    </a>
                </div>
                <div class="col">
                    <a href="https://www.france-bioinformatique.fr">
                        <div class="hexagone"><img src="/images/IFB.png" alt="INRIA"></div>
                    </a>
                </div>

                <div class="col">
                    <a href="http://www.southgreen.fr">
                        <div class="hexagone"><img src="/images/southgreen.png" alt="INRIA"></div>
                    </a>
                </div>

            </div>
        </div>
        <div class="copyright">
            &COPY; AgroLD 2019
            <button data-target="#labelstoreModal" data-toggle="modal" type="button"
                class="btn m-2 btn-sm btn-outline-light">
                Change your consents
            </button>
        </div>

    </footer>
</template>

<style lang="css" scoped>
footer {
    height: auto;
    z-index: 1000;
    display: none;
}

footer.footer {
    position: fixed !important;
    padding: 0;
    text-align: center;
    width: 100% !important;
    line-height: 30px;
    background-color: #f6f6f6;
    bottom: 0;
    left: 0;
}

footer .resolve-grid {
    display: -webkit-box;
    display: -webkit-flex;
    display: -ms-flexbox;
    display: flex;
    -webkit-flex-flow: row wrap;
    -ms-flex-flow: row wrap;
    flex-flow: row wrap;
    padding: 10px 0;
}

footer a {
    display: inline-block;
    position: relative;
    margin: 3px auto;
    text-align: center;
}

footer img {
    width: 100%;
    margin: none;
    border: none;
}

.hexagone {
    position: relative;
    width: 60px;
    height: 60px;
    border: dashed 1px white;
    margin: 0;
    border-radius: 50%;
    -webkit-filter: grayscale(1);
    -webkit-filter: grayscale(100%);
    filter: gray;
    filter: grayscale(100%);
    background: #fff;
}

.hexagone-no-filtered {
    -webkit-filter: grayscale(0);
    -webkit-filter: grayscale(0%);
    filter: none;
}

.hexagone:hover {
    -webkit-filter: grayscale(0);
    -webkit-filter: grayscale(0%);
    filter: none;
    filter: grayscale(0%);
}

.copyright {
    color: #626060;
    background: rgba(210, 210, 210, 0.3);

}

footer a {
    margin: 0 5%;
}
</style>