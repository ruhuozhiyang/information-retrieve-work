<template>
  <div>
    <a-input-search
      :style="{ width: '600px', marginTop: '200px', marginLeft: '30%' }"
      placeholder="请输入"
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
        onSearch() {},
    },
    components: {}
}
</script>

<style>

</style>