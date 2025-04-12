<script setup lang="js">
import { FACETED_URL } from '@/assets/scripts/config';
import { addPlugin } from '@/assets/scripts/utils';
import ArianThread from '@/components/shared/ArianThread.vue';

addPlugin("/scripts/introjs/intro.js");
addPlugin("/scripts/dots.js");

$(document).ready(function () {
  $("#search").attr("action", FACETED_URL);


  $('form#search').click(function (e) {
    var request = $(".keyword").val();
    console.log('request' + request);
    if (request == "") {
      $('.message').show();
      e.stopPropagation();
      e.preventDefault();
    } else {
      $('.message').hide();
      saveRequest(request);
    }
  });

  function saveRequest(keyword) {
    $.ajax({
      type: 'post',
      data: 'p={m:"setQuickSearch",keyword:' + keyword + '}',
      url: 'ToolHistory',
      success: function (data) {
        $('.success').html(data);
      }
    });
  }

});

</script>

<template>
  <ArianThread>
    <template #baseText>
      Search
    </template>
    <template #nextText>
      Quick Search
    </template>
  </ArianThread>
  <div class="foowrap">
    <div class="canvas">
      <canvas style="width:100%;height:100%;"></canvas>
      <section class="centering-search">
        <div class="container-fluid Q-search">
          <div class="container delim">
            <div style="text-align: center">
              <div class="exp">
                <h4><b>Search and browse AgroLD</b></h4>
                <p>Search examples: ontological concepts - 'plant height' or 'regulation of gene expression'; gene names
                  -
                  'GRP2' or 'TCP12'.</p>
              </div>
            </div>
            <div id="sform">
              <center>
                <form id="search" action="" method="post" target="_blank">
                  <div class="col-lg-6">
                    <div class="input-group">
                      <input class="keyword form-control" name="q" type="text" placeholder="Search examples: Gene names -
                                            'GRP2' or 'TCP12' or Keywords 'plant height'" data-step="1"
                        data-intro="Type your expression and then ..." />
                      <span class="input-group-btn">
                        <input class="btn btn-secondary search-button" type="submit" value="Search" data-step="2"
                          data-intro="launch the search engine!" required />
                      </span>
                    </div>
                  </div>
                </form>
                <div class="error"></div>
                <div class="success"></div>
                <span style="margin-top:30px;color:red;display:none" class="message">Please enter a keword</span>
              </center>
            </div>
          </div>
        </div>
      </section><br />
    </div>
  </div>
</template>

<style scoped>
@import '@/assets/introjs/introjs.css';
@import '@/assets/css/search.css';

.arian-thread {
  height: 60px;
  padding: 10px;
  background: #f3f3f3;
  font-weight: bold;
  font-size: 25px;
  color: #b3b3b3;
  width: 100%;
  margin-top: 0rem;
}

.info_title {
  font-weight: bold;
  font-size: 25px;
  color: #b3b3b3;
}

.active-p {
  color: #858d85;
}

.foowrap {
  position: relative;
  min-height: 100%;
}

.Q-search {
  position: absolute !important;
  z-index: 9999;
  top: 50%;
  left: center;
  border: 1px solid silver;
  border-radius: 4px;
  transform: translateY(-50%);
  background: white;
  padding: 50px 0px;
  width: 100%;
}

.centering-search {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.search-button:hover {
  cursor: pointer;
  background: #86b817 !important;
  transition: 0.3s ease-in-out;
  color: white;
}

.form-control:focus-within {
  border: 1px solid #86b817;
  transition: 200ms ease-in-out;
}
</style>
