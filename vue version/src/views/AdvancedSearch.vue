<script setup lang="js">
import { addPlugin } from '@/assets/scripts/utils';
import AdvancedSearchForm from '@/components/advanced-search/AdvancedSearchForm.vue';
import AdvancedSearchModal from '@/components/advanced-search/AdvancedSearchModal.vue';
import { A_GRO_LD_API_JSON_URL, WEB_APP_URL, FACETED_URL, SPAR_QL_ENDPOINT_URL, DEFAULT_API_FORMAT } from '../assets/scripts/config';
import ArianThread from '@/components/shared/ArianThread.vue';


const variables = `
  const A_GRO_LD_API_JSON_URL  = "${A_GRO_LD_API_JSON_URL}";
  const SPAR_QL_ENDPOINT_URL = "${SPAR_QL_ENDPOINT_URL}";
  const WEB_APP_URL = "${WEB_APP_URL}";
  const FACETED_URL = "${FACETED_URL}";
  const DEFAULT_API_FORMAT = "${DEFAULT_API_FORMAT}";
  const SPARQL_ENDPOINT = "${SPAR_QL_ENDPOINT_URL}";
  `;
// Add plugins
addPlugin("/scripts/URI.js");
addPlugin("/sparql-editor/yasr.bundled.min.js");
addPlugin("/knetmaps/dist/js/knetmaps-lib.min.js");
addPlugin("/swagger/lib/swagger-client.js");
addPlugin("/scripts/lib.js", variables);
addPlugin("/scripts/search.js");
addPlugin("/knetmaps/dist/js/knetmaps.js");
addPlugin("/scripts/knetmaps_adaptator.js");
addPlugin("/scripts/jquery.dataTables.min.js");
addPlugin("/scripts/advanced-search/gene.js");
addPlugin("/scripts/advanced-search/ontology.js");
addPlugin("/scripts/advanced-search/pathway.js");
addPlugin("/scripts/advanced-search/protein.js");
addPlugin("/scripts/advanced-search/qtl.js");
setTimeout(() => {
  const DEFAULT_PAGE_SIZE = 30;
  const size1 = Math.round(DEFAULT_PAGE_SIZE / 3);
  const size2 = Math.round(DEFAULT_PAGE_SIZE * 2 / 3);
  YASR.plugins.table.defaults.datatable["pageLength"] = DEFAULT_PAGE_SIZE;
  YASR.plugins.table.defaults.datatable["lengthMenu"] = [[size1, size2, DEFAULT_PAGE_SIZE, -1], [size1, size2, DEFAULT_PAGE_SIZE, "All"]];
  YASR.plugins.table.defaults.fetchTitlesFromPreflabel = false;
}, 500);

</script>

<template>
  <ArianThread>
    <template #baseText>
      Search
    </template>
    <template #nextText>
      Advanced form-based search
    </template>
  </ArianThread>
  <div class="foowrap">
    <section>
      <div id="advanced-form" class="border-right">
        <AdvancedSearchForm />
        <AdvancedSearchModal />
        <!-- <jsp:include page="WEB-INF/jspf/advancedForm.jsp"></jsp:include>
                    <jsp:include page="WEB-INF/jspf/advancedModal.jsp"></jsp:include> -->
      </div>
      <div id="result-container">
        <div class="container">
        </div>
        <div class="container">
          <div id="as-result"></div>
        </div>
      </div>
      <!-- Les differents types dispo dans le select -->
      <!-- <Protein />
      <Qtl />
      <Pathway />
      <Gene />
      <Ontology /> -->
    </section>
    <div id="push"></div> <!--add the push div here -->
    <div style="height:50px;width:100%;"></div>
  </div>
</template>

<style scoped>
@import '@/assets/knetmaps/css_demo/index-style.css';
@import '@/assets/knetmaps/dist/css/knetmaps.css';
@import 'https://fonts.googleapis.com/css?family=Kanit|Play';
@import '@/assets/css/css-loader.css';
@import '@/assets/css/search.css';
@import '@/assets/css/jquery.dataTables.min.css';
@import '@/assets/css/advSearch.css';
/* @import '@/assets/sparql-editor/yasr.min.css'; */


#graphViewResult {
  width: inherit;
  height: 800px;
  position: relative;
  top: 0px;
  left: 0px;
}

.foowrap {
  position: relative;
  min-height: 100%;
}
</style>
