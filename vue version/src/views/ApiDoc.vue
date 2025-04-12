<script setup lang="js">
import ArianThread from '@/components/shared/ArianThread.vue';
import { A_GRO_LD_API_JSON_URL } from '../assets/scripts/config';
import { addPlugin } from '@/assets/scripts/utils';

// plugins added 
addPlugin("/api-js/swagger-ui-bundle.js");
addPlugin("/api-js/swagger-ui-standalone-preset.js");

// swagger documentation api
window.onload = function () {
  // Begin Swagger UI call region
  const ui = SwaggerUIBundle({
    dom_id: "#swagger-ui",
    deepLinking: true,
    presets: [SwaggerUIBundle.presets.apis, SwaggerUIStandalonePreset],
    plugins: [
      //SwaggerUIBundle.plugins.DownloadUrl
    ],
    layout: "BaseLayout", // StandalonLayout : display the topbar
    validatorUrl: "https://validator.swagger.io/validator",
    url: A_GRO_LD_API_JSON_URL, // AGROLDAPIJSONURL,
    onComplete: function () {
      banInjector();
    },
  });

  // End Swagger UI call region
  window.ui = ui;
};

function banInjector() {
  $("#inject-info").remove(); // remove swagger top banner
}

</script>

<template>
  <ArianThread>
    <template #baseText>
      Help
    </template>
    <template #nextText>
      AgroLD API
    </template>
  </ArianThread>
  <div class="foowrap">
    <div id="inject-info" class="container-fluid swag-swag">
      <div class="container">
        <div class="centering-fix">
          <div class="txt-i">
            <div class="col-sm-6">
              <div id="pop-i"></div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="swagger-section">
      <section>
        <div id="message-bar" class="swagger-ui-wrap" style="display: none">
          &nbsp;
        </div>
        <div id="swagger-ui-container" class="swagger-ui-wrap"></div>
      </section>
    </div>
    <div id="swagger-ui">
      <section data-reactroot="" class="swagger-ui swagger-container"></section>
    </div>
  </div>
</template>

<style>
@import '@/assets/api-css/swagger-ui.css';

.arian-thread {
  height: 60px;
  padding: 10px;
  background: #f3f3f3;
  font-weight: bold;
  font-size: 25px;
  color: #b3b3b3;
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
</style>
