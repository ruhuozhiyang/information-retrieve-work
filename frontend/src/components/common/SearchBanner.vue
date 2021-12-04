<template>
  <div :class="tagsChoose">
    <a-auto-complete
      v-model="searchValue"
      style="width: 100%"
      placeholder="请输入"
      size="large"
      option-label-prop="value"
      :open="drop_open"
      @search="get_complete"
      @focus="get_history"
      @blur="drop_open = false"
      @select="drop_open = false"
      @change="drop_open = true"
    >
      <template slot="dataSource" v-if="!a_c">
        <a-select-opt-group v-for="c_group in auto_complete_data" :key="c_group.title">
          <span slot="label">
            {{ c_group.title }}
          </span>
          <a-select-option v-for="e in c_group.children" :key="e" :value="e">
            <span @click.stop>
              {{ e }}
              <a-icon style="float: right;" type="close" @click="r_h_item(e)" />
            </span>
          </a-select-option>
        </a-select-opt-group>
      </template>
      <template slot="dataSource" v-else>
        <a-select-option v-for="e in auto_complete_data" :key="e" :value="e">{{ e }}</a-select-option>
      </template>
      <a-input @pressEnter="onSearch" @click.stop>
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
import { Input, AutoComplete, Select } from 'ant-design-vue';
import Vue from 'vue';
import { g_history, m_history } from '../../utils/utils';

const { Search } = Input;
const { OptGroup, Option } = Select;
Vue.component(Input.name, Input);
Vue.component(Search.name, Search);
Vue.component(AutoComplete.name, AutoComplete)
Vue.component(Select.name, Select)
Vue.component(Option.name, Option)
Vue.component(OptGroup.name, OptGroup)

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
      drop_open: false,
      a_c: false, // 是否是自动补齐
    }
  },
  methods: {
    g_s_h() {
      this.a_c = false;
      if (!g_history('n_r')) {
        return [];
      }
      let t = [];
      let m = { title: '搜索记录', children: g_history('n_r')};
      t.push(m);
      return t;
    },
    r_h_item(i) {
      m_history('n_r', i);
      this.auto_complete_data = this.g_s_h();
    },
    get_history() {
      this.drop_open = true;
      this.auto_complete_data = this.g_s_h();
    },
    get_complete(v) {
      this.a_c = true;
      this.auto_complete_data = !v ? [] : [v, v.repeat(2), v.repeat(3)];
    },
    onSearch() {
      this.auto_complete_data = [];
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