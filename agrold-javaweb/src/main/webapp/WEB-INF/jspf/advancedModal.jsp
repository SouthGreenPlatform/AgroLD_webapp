<%-- 
    Document   : advancedModal
    Created on : 25 juil. 2017, 14:17:22
    Author     : Jc
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>


<div class="modal fade" id="result-modal">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Modal title</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="mban adv">
                <div class="subtitle"></div>
                <div class="desc"></div>
                <div class="mnvi"></div>
            </div>
            <div class="modal-body">
                <div class="uri-md"></div>
                <div class="modal-result"></div>
                <div class="modal-wait"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>
<script>
    $.noConflict(); // TODO: JQuery is used twice or more, leading to conflicts
                    // Remove this line when the issue is fixed
    /***************************************************************/
    /*********************  Specific modal code ********************/
    /***************************************************************/
    var MODAL_OPEN = 0;
    /**
     * On ferme le modal
     */
    $('#result-modal').on('hidden.bs.modal', function () {
        MODAL_OPEN = 0;
        for(var i in ModalContext.ACTIVE)
            if (ModalContext.ACTIVE.hasOwnProperty(i) && typeof ModalContext.ACTIVE[i] === 'number')
                ModalContext.ACTIVE[i] = 0;
        
    });
    /* On veut consulter un résultat */
    $('body').on('click', '.mdpre', function (e) {
        console.log('####--- MODAL ---####');
        console.log('# -- Click event DISPLAY');
        /* garde la page courante */
        e.preventDefault();
        /* Injection des résultats */
        ModalContext.uri = decodeURIComponent(e.target.id);
        initModalContent(String($(this).attr('name')));
        /* Récupération de l'uri */
        /* lancement du modal ssi pas ouvert */
        if (++MODAL_OPEN < 2) {
            $('body #result-modal').modal();
            console.log('************ # CACHE URI ' + ModalContext.uri);
        } else
            SearchContext.uri = decodeURIComponent(e.target.id);
        e.preventDefault();
    });

    function initModalContent(n) {
        console.log('####--- MODAL ---####');
        console.log('# -- initModalContent()');
        // On récupère le type du select au travers du context de recherche 
        if (MODAL_OPEN == 0)
            ModalContext.ACTIVE = window[SearchContext.type];
        else
            ModalContext.ACTIVE = window[n];
        WaitingModal(0);
        // Le context actif injecte son HTML { gene, protein, ... }
        $('.modal-result').html(ModalContext.ACTIVE.html);
        //graphicalInitialisation();
        window.swagger = new SwaggerClient({
            url
        }).then(client => {
            console.log("# -- Call swagger for MODAL");
            ModalContext.ACTIVE.getDescription(ModalContext.uri);
            console.log('#### --- ## --- ####')
        });
    }
    function JSON_DEBUG(json) {
        for (var i in json) {
            console.log('- ' + i + ' : ' + json[i]);
            if (json[i].length)
                JSON_DEBUG(json[i]);
        }
    }
    function invoke(functionName, arg) {
        console.log('####--- MODAL ---####');
        console.log("CALL INVOKE : " + functionName, );

        window["ModalContext"]["ACTIVE"][functionName](arg);
    }
    /* Lance l'attente graphique du modal */
    function WaitingModal(sands) {
        if (sands === 0) {
            $('.modal-title').html("");
            $('.subtitle').html("");
            $('.desc').html("");
            $('.uri-md').html("");
            $('.modal-result').html('');
            $('.modal-wait').html('<div class="loader loader-double is-active"></div>');
        } else {
            $('.modal-wait').html('');
        }
    }
    initModalEvent();
    function initModalEvent() {
        $('body').on('click', '.nav-link', function (e) {
            /* Suppression de l'événement */
            $(this).attr('onclick', '');
            /* Effets de navigation */
            $('.nav-link.active').removeClass('active');
            $(this).addClass('active');
            var tabs = document.getElementsByClassName('o-panel');
            var i = 0;
            /* On cache les autres panels */
            for (i = 0; i < tabs.length; i++) {
                if ($(tabs[i]).attr('id').includes(e.target.id)){
                    $(tabs[i]).show();
//                    $('body .modal-pagination-top .o-pa-'+(e.target.id)).show();
//                    $('body .modal-pagination-bottom .o-pa-'+(e.target.id)).show();
                }else{
                    $(tabs[i]).hide();
//                    $('body .modal-pagination-top .o-pa-'+(e.target.id)).hide();
//                    $('body .modal-pagination-bottom .o-pa-'+(e.target.id)).hide();
                }
            }
        });
    }
    function graphicalInitialisation(){
        var tmp = $('.modal-result nav.advs').clone();
        $('nav.advs').remove();
        $('.mnvi').html(tmp);
    }
</script>