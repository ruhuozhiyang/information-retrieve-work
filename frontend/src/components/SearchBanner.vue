<template>
  <div>
    <a-input-search
      placeholder="input search text"
      enter-button="Search"
      size="large"
      @search="onSearch"
    />
  </div>
</template>

<script>
import { Input } from 'ant-design-vue';
import Vue from 'vue';

const { Search } = Input;
Vue.component(Input.name, Input);
Vue.component(Search.name, Search);

export default {
    name: 'SearchBanner',
    data() {
        return {}
    },
    methods: {
        onSearch() {
        },
    },
    components: {}
}
</script>

<style>

</style>