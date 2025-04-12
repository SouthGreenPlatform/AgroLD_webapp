<script setup lang="js">
$(document).ready(function (e) {

  var SearchContext = {
    type: null,
    uri: null,
    keyword: null,
    ACTIVE: null // future CURRENT_RESULT
  };

  $('input[type=text]#keyword').on('keydown', function (e) {
    if (e.which == 13) {
      $('#jcb').click();
    }
  });
  $('#jcb').click(function (e) {
    e.stopPropagation();
    e.preventDefault();
    if ($('#afft').attr('value') === "") {
      $("#message").html('Select a type please');
    } else {
      if (!($('#advanced-form').attr('class').includes('searched')))
        $('#advanced-form').addClass('searched');
      $("#message").html('');
      checkForm();
      search(SearchContext.type, SearchContext.keyword, 0);
    }
  });

  $('.cht a').click(function (e) {
    e.preventDefault();
    e.stopPropagation();
    var param = $(this).attr("href").replace("#", "");
    var concept = $(this).text();
    $('#afft').text(concept);
    $('#afft').val(param);
  });

  // functions 
  function checkForm() {
    SearchContext.type = $('#afft').attr('value');
    SearchContext.keyword = $('#keyword').val();
  }

});

</script>

<template>
  <div class="text-center mb-5">
    Some examples:<br />
    ontological concepts: 'plant height' or 'regulation of gene expression'.<br />
    Gene: keywords 'stachyose', 'protein_coding', 'qtl', 'Constitutive flowering repressor', 'fungal growth'; or name
    'TCP2'.<br />
    Pathway: keywords 'fermentation' or 'acetate' or 'cytokinins'.<br />
    protein: name 'TBP1', keyword 'qtl'.<br />
    QTL: name 'BNL6.32' or keyword 'trait'.<br />
  </div>
  <div class="Q-Search A-Search">
    <div class="container delim">
      <div class="row">
        <div class="col-lg-12">
          <span style="color:#ff0000" id="message"></span>
          <div class="input-group">
            <div class="input-group-btn">
              <button type="button" id="afft" value="" class="btn btn-secondary dropdown-toggle" data-toggle="dropdown"
                aria-haspopup="true" aria-expanded="false">
                Filter by
              </button>
              <div class="dropdown-menu cht">
                <a class="dropdown-item" href="#gene">Gene</a>
                <a class="dropdown-item" href="#protein">Protein</a>
                <a class="dropdown-item" href="#qtl">QTL</a>
                <a class="dropdown-item" href="#pathway">Pathway</a>
                <a class="dropdown-item" href="#ontology">Ontology</a>
              </div>
            </div>
            <input id="keyword" class="keyword" name="keyword" type="text" autofocus placeholder="Search term...">
            <span class="input-group-btn">
              <button class="btn btn-primary yasrbtn" id="jcb" value="Search" style="border-radius: 0 5px 5px 0;">
                <svg color="white" xmlns="http://www.w3.org/2000/svg" width="1rem" height="1rem" viewBox="0 0 512 512">
                  <!--Font Awesome Free 6.5.2 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2024 Fonticons, Inc. -->
                  <path fill="currentColor"
                    d="M416 208c0 45.9-14.9 88.3-40 122.7L502.6 457.4c12.5 12.5 12.5 32.8 0 45.3s-32.8 12.5-45.3 0L330.7 376c-34.4 25.2-76.8 40-122.7 40C93.1 416 0 322.9 0 208S93.1 0 208 0S416 93.1 416 208zM208 352a144 144 0 1 0 0-288 144 144 0 1 0 0 288z" />
                </svg>
                Search terms
              </button>
              <button class="btn btn-info" data-toggle="collapse" id="historyAdvanced" aria-expanded="false"
                data-target="#historyAdvancedList" aria-controls="#historyAdvancedList" style="border-radius: 0 0 5px;">
                History
              </button>
            </span>
          </div>
        </div>
      </div>
      <div class="col">
        <div class="collapse" id="historyAdvancedList">
          <hr />
          <div id="historyAdvancedListPush" class="grid-3-rows">

          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
@import '@/assets/css/advSearch.css';
</style>
