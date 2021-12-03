<template>
  <div :class="tagsChoose">
    <a-auto-complete
      v-model="searchValue"
      style="width: 100%"
      placeholder="请输入"
      size="large"
      :data-source="auto_complete_data"
      @search="getComplete"
    >
      <a-input @pressEnter="onSearch">
        <a-button
          slot="suffix"
          style="margin-right: -12px"
          size="large"
          type="primary"
          @click="onSearch"
        >
          <a-icon type="search" />
        </a-button>
      </a-input>
    </a-auto-complete>
  </div>
</template>

<script>
import { Input, AutoComplete } from 'ant-design-vue';
import Vue from 'vue';

const { Search } = Input;
Vue.component(Input.name, Input);
Vue.component(Search.name, Search);
Vue.component(AutoComplete.name, AutoComplete)

export default {
    props: {
      content: String,
    },
    name: 'SearchBanner',
    data() {
        return {
          searchValue: '',
          tagsChoose: 'main',
          auto_complete_data: [],
        }
    },
    methods: {
      getComplete(v) {
        this.auto_complete_data = !v ? [] : [v, v.repeat(2), v.repeat(3)];
      },
      onSearch() {
        if (!this.searchValue) {
          return
        }
        if (this.tagsChoose === 'main') {
          this.$router.push({ path: '/result', query: { content: this.searchValue }});
        } else if (this.tagsChoose === 'subPage') {
          this.$emit('requestNews', this.searchValue, 1);
        }          
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
    width: 700px;
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