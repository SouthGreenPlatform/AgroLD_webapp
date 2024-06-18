<%-- 
        Document   : advancedForm
        Created on : Sep 8, 2015, 2:21:47 PM
        Author     : tagny
--%>
<div class="text-center mb-5">
    <!--ul>
    <li>ontological concepts: 'plant height' or 'regulation of gene expression'</li>
    <li>Gene: keyword 'stachyose' or name 'TCP2'.</li>
    <li>Pathway:  keywords 'fermentation' or 'acetate' or 'cytokinins'</li>
    <li>protein:  name 'TBP1', keyword 'qtl'</li>
    <li>QTL: name 'BNL6.32' or keyword 'trait'</li>
    </ul-->
Some examples:<br/>
ontological concepts: 'plant height' or 'regulation of gene expression'.<br/>
    Gene: keywords 'stachyose', 'protein_coding', 'qtl', 'Constitutive flowering repressor', 'fungal growth'; or name 'TCP2'.<br/>
    Pathway:  keywords 'fermentation' or 'acetate' or 'cytokinins'.<br/>
    protein:  name 'TBP1', keyword 'qtl'.<br/>
    QTL: name 'BNL6.32' or keyword 'trait'.<br/>
</div>

<!--<center>
    <div class="container">
        <form id="sform">
           
                <option value="" default>--- Select a type* ---</option>
                <option value="gene">Gene</option>
                <option value="protein">Protein</option>
                <option value="qtl">QTL</option>
                <option value="pathway">Pathway</option>
                <option value="ontology">Ontology</option>
            </select>
            <input id="input" class="keyword" name="keyword" type="text" autofocus placeholder="Type here ..." style="display: inline"/>
            <input id="submit" class="yasrbtn" type="submit" value="Search" style="display: inline"/>
        </form>
    </div>
</center>-->
<!-- TEST -->
<div class="Q-Search A-Search">
    <div class="container delim">
        <div class="row">
            <div class="col-lg-12">
                <span style="color:#ff0000" id="message"></span>
                <div class="input-group">
                    <div class="input-group-btn">
                        <button type="button" id="afft" value="" class="btn btn-secondary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            Filter by
                        </button>
                        <div class="dropdown-menu cht">
                            <a class="dropdown-item" href="#gene">Gene</a></li>
                            <a class="dropdown-item" href="#protein">Protein</a></li>
                            <a class="dropdown-item" href="#qtl">QTL</a></li>
                            <a class="dropdown-item" href="#pathway">Pathway</a></li>
                            <a class="dropdown-item" href="#ontology">Ontology</a></li>
                        </div>
                    </div>
                    <input id="keyword" class="keyword" name="keyword" type="text" autofocus placeholder="Search term...">
                    <span class="input-group-btn">
                        <button class="btn btn-primary" id="jcb" class="yasrbtn" value="Search" style="border-radius: 0 5px 5px 0;">
                            <svg color="white" xmlns="http://www.w3.org/2000/svg" width="1rem" height="1rem" viewBox="0 0 512 512">
                                <!--Font Awesome Free 6.5.2 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2024 Fonticons, Inc. -->
                                <path fill="currentColor" d="M416 208c0 45.9-14.9 88.3-40 122.7L502.6 457.4c12.5 12.5 12.5 32.8 0 45.3s-32.8 12.5-45.3 0L330.7 376c-34.4 25.2-76.8 40-122.7 40C93.1 416 0 322.9 0 208S93.1 0 208 0S416 93.1 416 208zM208 352a144 144 0 1 0 0-288 144 144 0 1 0 0 288z"/>
                            </svg>
                            Search terms
                        </button>
                        <button 
                            class="btn btn-info d-none" data-toggle="collapse"
                            id="historyAdvanced" class="yasrbtn" aria-expanded="false"
                            data-target="#historyAdvancedList" aria-controls="#historyAdvancedList"
                            style="border-radius: 0 0 5px;"
                        >
                            History
                        </button>
                    </span>
                </div>
            </div>
        </div>
        <div class="col">
            <div class="collapse" id="historyAdvancedList">
                <hr/>
                <div id="historyAdvancedListPush" class="grid-3-rows">
                    
                </div>
            </div>   
        </div>
    </div>
</div>

<script>
if (consentedToTreatment("advancedSearch.history")) {
    document.getElementById("historyAdvanced")?.classList.remove("d-none");

    const elt = document.getElementById("jcb");
    if (elt) elt.style.borderRadius = "0 5px 0 0";    
}
</script>

<script>

    var ht = '${param.ht}' ; // Set ht Value from GET request
    var hk = '${param.hk}' ; // Set hk Value from GET request
    var HISTORY = false;
    
    var SearchContext = {
        type: null,
        uri: null,
        keyword: null,
        ACTIVE: null // future CURRENT_RESULT
    };
    var ModalContext = {
        type: null,
        uri: null,
        id: null,
        keyword: null,
        ACTIVE: null,
        clean: function () {
            this.type = null;
            this.uri = null;
            this.id = null;
            this.keyword = null;
            this.ACTIVE = null;
        },
        notFound: function () {
            this.clean();
            $('.modal-result').html('<h1 class="nfound">No data found :(</h1>');
        }
    }
    var FOCUS_KEYWORD = 0;

    $('input[type=text]#keyword').on('keydown', function (e) {
        if (e.which == 13) {
            $('#jcb').click();
        }
    });
    $('#jcb').click(function (e) {
        e.stopPropagation();
        e.preventDefault();
        /* Keyword ok ? */
        if ($('#afft').attr('value') === "") {
            $("#message").html('Select a type please');
        } else {
            if (!($('#advanced-form').attr('class').includes('searched')))
                $('#advanced-form').addClass('searched');
            $("#message").html('');
            checkForm();
            if(!HISTORY)
                saveRequest();
            else
                HISTORY=false;

            saveRequestInLocalStorage(SearchContext.type, SearchContext.keyword)

            search(SearchContext.type, SearchContext.keyword, 0);
        }
    });
    /* Switch pour �muler un select:option */
    $(document).ready(function (e) {
        $('.cht a').click(function (e) {
            e.preventDefault();
            var param = $(this).attr("href").replace("#", "");
            var concept = $(this).text();
            $('#afft').text(concept);
            $('#afft').val(param);
        });
        // On va regarder si l'uilisateur consulte son historique de recherche avanc�e
        if(hk !== '' && ht!==''){
            HISTORY = true;
            $('#keyword').attr('value',hk);
            $('#afft').text(ht);
            $('#afft').val(ht);
            $('#jcb').click();
        }
    });
    //récupération de l'historique des recherches quand il est affiché
    document.getElementById("historyAdvanced").onclick = () => {
        const elt = document.getElementById("historyAdvancedListPush")
        
        if (elt?.children.length > 0) {
            elt.replaceChildren([])
            return
        }

        JSON.parse(
            localStorageGet("advancedSearch.history") ?? "[]"
        ).forEach(({ type, keyword }) => {
            const card = document.createElement("div")
            card.classList.add("card", "card-body", "mb-3")

            const header = document.createElement("div")
            header.classList.add("card-header")
            header.textContent = type

            const select = document.createElement("button")
            select.classList.add("btn", "btn-outline-light", "btn-sm", "float-right")
            select.textContent = "Select"
            select.onclick = () => {
                $('#afft').text(type);
                $('#afft').val(type);
                $('#keyword').val(keyword);
                $('#historyAdvanced').click();
            }

            const body = document.createElement("div")
            body.classList.add("card-body", "p-2")

            const text = document.createElement("p")
            text.classList.add("card-text", "text-center")
            text.textContent = keyword

            body.appendChild(text)
            card.appendChild(header)
            card.appendChild(body)  
            header.appendChild(select)    
            elt.appendChild(card)
        })
        
    }

    /* R�cup�ration des �l�ments du formulaire de recherche */
    function checkForm() {
        SearchContext.type = $('#afft').attr('value');
        SearchContext.keyword = $('#keyword').val();
    }
    function saveRequest(){
        $.ajax({
            type:'post',
            data:'p={m:"setAdvancedSearch",type:"'+SearchContext.type+'",keyword:"'+SearchContext.keyword+'"}',
            url:'ToolHistory',
            success:function(data){
                $('.success').html(data);
            }                                                
        });
    }
    function saveRequestInLocalStorage(type, keyword) {
        const history = JSON.parse(
            localStorageGet("advancedSearch.history") ?? "[]"
        );

        entry = { type, keyword: keyword.trim() }

        if (!history.find(e => e.type === entry.type && e.keyword === entry.keyword)) {
            history.unshift(entry)

            localStorageSet(
                "advancedSearch.history",
                JSON.stringify(history)
            );            
        }

    }

</script>