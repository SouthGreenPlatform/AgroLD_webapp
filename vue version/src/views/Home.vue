<script setup lang="js">

import { addPlugin } from '@/assets/scripts/utils';
import { A_GRO_LD_API_JSON_URL, WEB_APP_URL, FACETED_URL, SPAR_QL_ENDPOINT_URL, DEFAULT_API_FORMAT } from '../assets/scripts/config';


const variables = `
  const A_GRO_LD_API_JSON_URL  = "${A_GRO_LD_API_JSON_URL}";
  const SPAR_QL_ENDPOINT_URL = "${SPAR_QL_ENDPOINT_URL}";
  const WEB_APP_URL = "${WEB_APP_URL}";
  const FACETED_URL = "${FACETED_URL}";
  const DEFAULT_API_FORMAT = "${DEFAULT_API_FORMAT}";
  const SPARQL_ENDPOINT = "${SPAR_QL_ENDPOINT_URL}";
  `;
// Add plugins
addPlugin("/scripts/home1.js", variables);

</script>

<template>
  <div id="fullPage" style="overflow: auto;">
    <div id="section0" class="section- introIndex-">
      <div class="home-page">
        <div class="static-j-">
          <div class="container">
            <div class="bug-rport-2">
              <div class="primary-a col-md-12 col-lg-12 brise">
                <center>
                  <!--<div class="primary-a col-sm-12 col-lg-6">-->
                  <h1>Agronomic Linked Data</h1>
                  <h3>The RDF Knowledge-based Database for plant molecular networks</h3>
                  <br>
                </center>

              </div>
              <div id="sform">
                <center>
                  <form id="search" method="post" target="_blank">
                    <div class="col-lg-6">
                      <div class="input-group">
                        <!--input type="text" class="form-control" placeholder="Search for..."-->
                        <input class="keyword form-control" name="q" type="text"
                          placeholder="examples: GRP2 or TCP12 or plant height" data-step="1"
                          data-intro="Type your expression and then ..." />
                        <span class="input-group-btn">
                          <input class="btn btn-secondary" type="submit" value="Search" data-step="2"
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
        </div>

      </div>
    </div>
    <div id="section1" class="fp-normal-scroll section sumaryService">
      <div class="titled-section t-s-1 container-fluid titled-section-ref">
        <h2>Discover our services</h2>
      </div>
      <div class="centering-fx">
        <div class="framed-child-">
          <div class="container">
            <div class="card-deck">
              <div class="col-md-6 col-lg-3 col-xs-2">
                <div class="card card-outline-success mb-3 text-center c1">
                  <div class="card-block">
                    <div class="delim-tc">
                      <h4 class="card-title">Quick Search</h4>
                    </div>
                    <blockquote class="card-blockquote">
                      <p>Search with keywords and browse AgroLD Knowledge Base</p>
                      <footer><a href="/quick-search" class="btn btn-outline-success">Use this tool</a></footer>
                    </blockquote>
                  </div>
                </div>
              </div>
              <div class="col-md-6 col-lg-3 col-xs-2">
                <div class="card card-outline-success mb-3 text-center c2">
                  <div class="card-block">
                    <div class="delim-tc">
                      <h4 class="card-title">Advanced Search</h4>
                    </div>
                    <blockquote class="card-blockquote">
                      <p>Search with keywords, browse, and get answers to some biological questions</p>
                      <footer><a href="/advanced-search" class="btn btn-outline-success">Use this tool</a></footer>
                    </blockquote>
                  </div>
                </div>
              </div>
              <div class="col-md-6 col-lg-3 col-xs-2">
                <div class="card card-outline-success mb-3 text-center c3">
                  <div class="card-block">
                    <div class="delim-tc">
                      <h4 class="card-title">Explore Relationships</h4>
                    </div>
                    <blockquote class="card-blockquote">
                      <p>Search easily existing relationships between entities</p>
                      <footer><a href="/relfinder" class="btn btn-outline-success">Use this tool</a></footer>
                    </blockquote>
                  </div>
                </div>
              </div>
              <div class="col-md-6 col-lg-3 col-xs-2">
                <div class="card card-outline-success mb-3 text-center c4">
                  <div class="card-block">
                    <div class="delim-tc">
                      <h4 class="card-title">SPARQL Query Editor</h4>
                    </div>
                    <blockquote class="card-blockquote">
                      <p>Edit and submit your SPARQL Queries to the sparql endpoint of AgroLD located</p>
                      <footer><a href="/sparql-editor" class="btn btn-outline-success">Use this tool</a></footer>
                    </blockquote>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

</template>

<style>
/* @import '@/assets/css/hp.css'; */
/* 
#fullPage {
  width: 100rem !important;
  height: 100%;
  margin-top: -12rem;
} */

h1 {
  color: white;
}

#fullPage {
  /* border: 4px solid black; */
  height: 100% !important;
  /* top: 12.6rem; */
  position: fixed !important;
  /* padding: 0; */
  /* text-align: center; */
  width: 100% !important;
  /* line-height: 30px; */
  /* background-color: #f6f6f6; */
  bottom: 0;
  left: 0;
}

.jumbotron {
  height: calc(100vh);
}


#section0 {
  position: relative;
  /* border: 2px solid white; */
  width: 100%;
  /* margin-top: 15rem; */
  background: url('/images/banner-img.jpg') 50% !important;

  /* background-position: center; */
  background-size: cover;
  padding: 4rem !important;
  padding-top: 12rem !important;
  height: 65% !important;
  overflow: auto;
}

#section1 {
  position: relative;
  left: 0;
  /* margin-bottom: 0.5rem; */
  height: 100% !important;
  width: 100% !important;
  overflow: auto;
  padding-bottom: 5rem !important;
  /* padding-bottom: 4rem !important; */
}

@media (max-width: 950px) {
  #section1 {
    overflow: auto;
    /* padding-bottom: 40rem; */
  }
}


#app {
  /* max-width: none; */
  /* padding-top: 0rem !important; */
  /* margin-left: -4rem !important; */
}

/* 
.router-view {
  padding-top: 0rem !important;
  padding-bottom: 0rem !important;
} */

/* .footer {
  display: none !important;
} */

.footer-home {
  padding-bottom: 5% !important;
}

.centering-fx {
  position: relative;
  margin-top: 10% !important;
  /* padding-bottom: 0rem !important; */
}

.home-set {
  margin-top: 8% !important;
}

.titled-section-ref {
  padding-top: 2rem !important;
}
</style>
