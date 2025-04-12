<script setup lang="ts">
import ArianThread from './ArianThread.vue';


</script>

<template>
  <nav class="navbar fixed-top navbar-toggleable-md navbar-light bg-faded">
    <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse"
      data-target="#navbarTogglerDemo02" aria-controls="navbarTogglerDemo02" aria-expanded="false"
      aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="container">
      <a class="navbar-brand" href="/">
        <!-- <span class="ribbon">LOCAL</span> -->
        <img src="/images/v5.png">
      </a>
      <div class="collapse navbar-collapse" id="navbarTogglerDemo02">
        <ul class="navbar-nav mr-auto mt-2 mt-md-0">
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" data-toggle="dropdown"
              aria-haspopup="true" aria-expanded="false">
              Search
            </a>
            <div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
              <a class="dropdown-item" href="advanced-search" title="Multiservice Knowledge aggregator">Advanced
                Search</a>
              <a class="dropdown-item" href="rel-finder" title="Interactive Relationships Discovery in RDF Data">Explore
                Relationships</a>
              <a class="dropdown-item" href="sparql-editor" title="SPARQL Query">SPARQL Query</a>
            </div>
          </li>
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" data-toggle="dropdown"
              aria-haspopup="true" aria-expanded="false">
              Help
            </a>
            <div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
              <a class="dropdown-item" href="documentation">Documentation</a>
              <a class="dropdown-item" href="quick-search" title="Faceted Search">Faceted Search</a>
              <a class="dropdown-item" href="api-doc" title="AgroLD API documentation">AgroLD API</a>
            </div>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="about">About</a>
          </li>
          <li nav-item dropdown></li>
        </ul>
        <div class="form-inline my-2 my-lg-0">
          <a href="https://twitter.com/agro_ld?ref_src=twsrc%5Etfw" class="twitter-follow-button" data-size="large"
            data-show-screen-name="false" data-show-count="false">Follow @agro_ld</a>
          <a class="nav-link btn btn-outline-secondary nohover" href="survey"><i
              class="fa fa-star-o"></i>&nbsp;&nbsp;Please send us your feedback!</a>
        </div>
      </div>
    </div>
  </nav>

</template>

<style scoped>
nav {
  font-size: 1rem;
  padding: 1rem 0;
  border-bottom: 1px solid greenyellow;
}
</style>
