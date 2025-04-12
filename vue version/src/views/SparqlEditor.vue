<script setup lang="js">
import { addPlugin } from '@/assets/scripts/utils';
import { A_GRO_LD_API_JSON_URL, WEB_APP_URL, FACETED_URL, SPAR_QL_ENDPOINT_URL, DEFAULT_API_FORMAT } from '../assets/scripts/config';
import ArianThread from '@/components/shared/ArianThread.vue';

const defaultQuery = `
PREFIX agrold:<http://www.southgreen.fr/agrold/>
SELECT * 
WHERE{
  GRAPH ?graph {
  ?subject ?property ?object.
}
filter(REGEX(?graph, CONCAT("^", str(agrold:))))
} 
LIMIT 10`;

const variables = `
  const A_GRO_LD_API_JSON_URL  = "${A_GRO_LD_API_JSON_URL}";
  const SPAR_QL_ENDPOINT_URL = "${SPAR_QL_ENDPOINT_URL}";
  const WEB_APP_URL = "${WEB_APP_URL}";
  const FACETED_URL = "${FACETED_URL}";
  const DEFAULT_API_FORMAT = "${DEFAULT_API_FORMAT}";
  const SPARQL_ENDPOINT = "${SPAR_QL_ENDPOINT_URL}";
  `;
// Add plugins
addPlugin("/scripts/localStorage.js");
addPlugin("/scripts/introjs/intro.js");
addPlugin("/scripts/lib.js", variables);
addPlugin("/sparql-editor/main1.js");
addPlugin("/sparql-editor/main2.js");
addPlugin("/scripts/querypatterns.js");
addPlugin("/sparql-editor/yasr.bundled.min.js");
addPlugin("/sparql-editor/yasqe.bundled.min.js");
addPlugin("/sparql-editor/main3.js");
addPlugin("/sparql-editor/d3.v3.min.js");
addPlugin("/sparql-editor/d3sparql.js");
addPlugin("/sparql-editor/dom-to-image.min.js");
addPlugin("/sparql-editor/graphPlugin.js");

</script>

<template>
  <ArianThread>
    <template #baseText>
      Search
    </template>
    <template #nextText>
      SPARQL Query Editor
    </template>
  </ArianThread>

  <div class="foowrap">
    <div class="d-flex flex-column align-items-center justify-content-center ml-3 mr-3">
      <span>
        Select a sample query and run it. The sample query could be used to modify the parameters
        accordingly.
        Alternatively, enter SPARQL code in the query box below.
        <button href="javascript:void(0);" onclick="javascript:introJs().setOption('showProgress', true).start();"
          class="yasrbtn" style="background-color: #00B5AD!important; color: white; font-weight: bold">
          Watch how!
        </button>
      </span>
      <hr />
    </div>
    <div class="container-fluid only-queries">
      <div id=" main" style="overflow:auto;">
        <div id="sparql">
          <div id="cmd-container" data-step="6" data-intro="Hand over to see what shortcuts are available">
            <b id="cmds">KEYBOARD COMMANDS</b>
            <ul id="cmds" style="display: none; font-weight: bold">
              <li><code>[Ctrl|Cmd]-Space</code>: Trigger Autocompletion</li>
              <li><code>[Ctrl|Cmd]-D</code> and <code>[Ctrl|Cmd]-D</code>: Delete current/selected
                line(s)</li>
              <li><code>[Ctrl|Cmd]-/</code>: Comment or uncomment current/selected line(s)</li>
              <li><code>[Ctrl|Cmd]-Alt-Down</code>: Copy line down</li>
              <li><code>[Ctrl|Cmd]-Alt-Up</code>: Copy line up</li>
              <li><code>[Ctrl|Cmd]-Shift-F</code>: Auto-format/indent selected lines</li>
              <li><code>[Ctrl|Cmd]-]</code>: Indent current/selected line(s) more</li>
              <li><code>[Ctrl|Cmd]-[</code>: Indent current/selected line(s) less</li>
              <li><code>[Ctrl|Cmd]-S</code>: Save current query in local storage</li>
              <li><code>[Ctrl|Cmd]-Enter</code>: Execute Query</li>
              <li><code>F11</code>: Set query editor full-screen (or leave full-screen)</li>
              <li><code>Esc</code>: Leave full-screen</li>
            </ul>
          </div>
          <div id="parameters">
          </div>
          <form action="http://agrold.southgreen.fr/sparql" method="get" data-step="2"
            data-intro="watch & edit its query here!">
            <label for="query"><b style="font-size: 15px">Query Text</b></label><br />
            <textarea rows="15" cols="76" name="query" id="query" onchange="format_select(this)"
              onkeyup="format_select(this)"></textarea>
            <hr />
            <table width="100%">
              <tbody>
                <tr>
                  <td style="background-color: #d1d1d1">
                    <label for="timeout" class="n">Execution timeout</label>
                    <input name="timeout" class="yasrbtn" id="timeout" type="text" value="20000"
                      onchange="//setTimeout(this)" style="width:70px" /> milliseconds
                    <span class="info"><i>(values less than 1000 are ignored)</i></span>
                  </td>
                  <td align="right" style="background-color: #f7f7f7" data-step="5"
                    data-intro="or download directly your results in the format of your choice">
                    <label for="format" class="n">Results Format</label>
                    <select name="format" id="format" onchange="format_change(this)">
                      <option value="auto">Auto</option>
                      <option value="text/html">HTML</option>
                      <option value="application/vnd.ms-excel">Spreadsheet</option>
                      <option value="application/sparql-results+xml">XML</option>
                      <option value="application/sparql-results+json">JSON</option>
                      <option value="application/javascript">Javascript</option>
                      <option value="text/turtle">Turtle</option>
                      <option value="application/rdf+xml" selected="selected">RDF/XML</option>
                      <option value="text/plain">N-Triples</option>
                      <option value="text/csv">CSV</option>
                      <option value="text/tab-separated-values">TSV</option>
                    </select>
                    <input type="submit" class="yasrbtn" value="Download Results" />
                  </td>
                </tr>
              </tbody>
            </table>
          </form>
          <div>
            <table width="100%">
              <tbody>
                <tr>
                  <td align="left" data-step="7" data-intro="You can save your query in a file and then ... "
                    style="background-color: #f7f7f7">Filename to Save As:
                    <input id="inputFileNameToSaveAs" value="query.sparql"></input>
                    <button class="yasrbtn"
                      onclick="saveTextAsFile(document.getElementById('inputFileNameToSaveAs').value);">Save
                      Query</button>
                  </td>
                  <td align="right" data-step="8"
                    data-intro=" Load it (or any other text file containing a sparql query) later"
                    style="background-color: #d1d1d1">
                    <input type="file" id="fileToLoad" class="yasrbtn">
                    <button class="yasrbtn" onclick="loadFileAsText(document.getElementById('fileToLoad').files[0]);">
                      Load Selected Query File
                    </button>
                  </td>
                </tr>
                <tr id="historyRow" class="d-none">
                  <td>
                    <button id="btnHistory" class="yasrbtn" data-toggle="modal" data-target="#historyModal">
                      Select a previous query
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        <div id="patternslist" data-step="1" data-intro="Select a <b>question</b> here and then ...">
          <b style="font-size: 15px">Query Patterns</b>
        </div>
      </div>
      <div class="container-rst" style="width: 100%">
        <div id="yasr" data-step="4" data-intro="watch your results ... ">
          <div class="info_title" style="font-size: 19px">Results</div>
          <canvas id="hiddenCanvas"></canvas>
        </div>
        <div id="push"></div>
      </div>
    </div>
  </div>

  <div class="modal fade" id="historyModal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h4 class="modal-title">Pick a query</h4>
          <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
              aria-hidden="true">&times;</span></button>
        </div>
        <div class="modal-body text-left" id="history"></div>
      </div>
    </div>
  </div>
  <div class="jump-bot"></div>
  <div class="debugme"></div>
</template>

<style>
@import '@/assets/sparql-editor/yasqe.min.css';
@import '@/assets/sparql-editor/yasr.min.css';
@import '@/assets/introjs/introjs.css';
@import '@/assets/sparql-editor/main.css';

.only-queries {
  width: 1300px !important;
}
</style>