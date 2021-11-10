<template>
  <div>
      <div id="app">
        <img
            :style="{ height: '100px' }"
            alt="News logo"
            src="../assets/logo.png"
        >
      </div>
      <SearchBanner />
      <HotSearch />
      <router-view />
  </div>
</template>

<script>
import SearchBanner from './common/SearchBanner.vue';
import HotSearch from './HotSearch.vue';

export default {
    name: 'Home',
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