<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
import Header from './components/shared/Header.vue';
import Footer from './components/shared/Footer.vue';
</script>

<template>
  <div class="container app-container">

    <div class="row header-view">
      <Header />
    </div>
    <div class="row router-view">
      <RouterView />
    </div>
    <div class="row">
      <Footer />
    </div>
  </div>



  <!-- <img alt="Vue logo" class="logo" src="@/assets/logo.svg" width="125" height="125" /> -->

  <!-- <div class="wrapper">
      <div class="nav">
        <RouterLink to="/">Home</RouterLink>
        <RouterLink to="/advanced-search">Advanced Search</RouterLink>
        <RouterLink to="/sparql-editor">Sparql Editor</RouterLink>
        <RouterLink to="/documentation">Documentation</RouterLink>
        <RouterLink to="/quick-search">Quick Search</RouterLink>
        <RouterLink to="/api-doc">Api Doc</RouterLink>
        <RouterLink to="/about">About</RouterLink>
        <RouterLink to="/survey">Survey</RouterLink>
      </div>
    </div> -->

</template>

<style scoped>
h1 {
  color: black !important;
}

.router-view {
  width: 100% !important;
  padding-top: 5rem;
  padding-bottom: 8rem;
}
</style>
