<template>
  <div :class="tagsChoose">
    <a-auto-complete
      v-model="searchValue"
      style="width: 100%;"
      size="large"
      option-label-prop="value"
      :open="drop_open"
      @search="get_complete"
      @focus="get_history"
      @blur="drop_open = false"
      @select="c_s"
    >
      <template slot="dataSource" v-if="!a_c">
        <a-select-opt-group v-for="c_group in auto_complete_data" :key="c_group.title" :value="c_group.title">
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
        <a-select-option v-for="e in auto_complete_data" :key="e + ''">
          <span v-html="e"></span>
        </a-select-option>
      </template>
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
import { Input, AutoComplete, Select } from 'ant-design-vue';
import Vue from 'vue';
import { g_history, m_history, f_d, h_l_a_o } from '../../utils/utils';
import axios from 'axios';

const { Search } = Input;
const { OptGroup, Option } = Select;
Vue.component(Input.name, Input);
Vue.component(Search.name, Search);
Vue.component(AutoComplete.name, AutoComplete)
Vue.component(Select.name, Select)
Vue.component(Option.name, Option)
Vue.component(OptGroup.name, OptGroup)

const c_p_api = '/api/complete-predict';

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
      pre_predict: {},
    }
  },
  methods: {
    c_s(v) {
      this.drop_open = false;
      if (v.indexOf("<font color='red'>") > -1) {
        this.searchValue = v.replace("<font color='red'>", '').replace("</font>", '');
      }
    },
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
    get_high_light(c, a) {
      return h_l_a_o(c, a, "<font color='red'>", "</font>");
    },
    get_complete(v) {
      if (!v) {
        this.drop_open = false;
        return
      }
      this.drop_open = true;
      this.a_c = true;
      if (h_l_a_o(v, g_history('n_r')).length > 0) {
        this.auto_complete_data = this.get_high_light(v, g_history('n_r'));
        return
      }
      let p = this.pre_predict[v];
      if (p) {
        this.auto_complete_data = this.get_high_light(v, p);
      } else {
        axios.get(c_p_api, { params: { q: v }}).then((r) => {
          this.auto_complete_data = r.data.data ? this.get_high_light(v, f_d(r.data.data)) : [];
          this.pre_predict[v] = this.auto_complete_data;
        }).catch((err) => {
          alert(err)
        });
      }
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