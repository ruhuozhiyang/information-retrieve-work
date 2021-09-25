<template>
  <div>
      <div id="app">
        <img
            :style="{ height: '100px' }"
            alt="News logo"
            src="./assets/logo.png"
        >
      </div>
      <SearchBanner />
      <HotSearch />
  </div>
</template>

<script>
import SearchBanner from './components/SearchBanner.vue';
import HotSearch from './components/HotSearch.vue';

export default {
    name: 'index',
    components: {
        SearchBanner,
        HotSearch,
    },
}
</script>

<style scoped>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  margin-top: 100px;
}
</style>