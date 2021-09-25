<template>
  <div class="main">
    <a-input-search
      v-model="searchValue"
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
import axios from 'axios';

const searchApi = '/api/search';
const { Search } = Input;
Vue.component(Input.name, Input);
Vue.component(Search.name, Search);

export default {
    name: 'SearchBanner',
    data() {
        return {
          searchValue: ''
        }
    },
    methods: {
        onSearch(value) {
          const params = {
            content: value
          };
          axios.post(searchApi, params).then((res) => {
            global.console.log(res);
          }).catch((err) => {
            global.console.log(err);
          });
        },
    },
    components: {}
}
</script>

<style scoped>
.main {
    text-align: center;
    width: 650px;
    height: 200px;
    margin: auto;
    margin-top: 200px;
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
}
</style>