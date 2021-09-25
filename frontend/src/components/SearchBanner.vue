<template>
  <div :class="tagsChoose">
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
    props: {
      content: String,
    },
    name: 'SearchBanner',
    data() {
        return {
          searchValue: '',
          tagsChoose: 'main',
        }
    },
    methods: {
        onSearch(value) {
          if (!value) {
            return
          }
          this.$router.push({ path: '/result', query: { content: this.searchValue }});
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
    mounted() {
      this.tagsChoose = this.$route.path === '/' ? 'main' : 'subPage';
    },
    watch: {
      content(value) {
        this.searchValue = value;
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
.subPage {
    text-align: center;
    width: 650px;
    height: 200px;
    margin: auto;
    margin-top: 20px;
    margin-left: 150px;
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
}
</style>