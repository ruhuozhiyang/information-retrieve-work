<template>
  <div>
    <div class="main">
      <div>
        <span :style="{ fontSize: '18px' }">热搜</span>
        <a :style="{ float: 'right' }" @click="changeBatch">
          <a-icon type="sync" />
          <span :style="{ fontSize: '15px', marginLeft: '5px' }">换一换</span>
        </a>
      </div>
      <a-list :loading="list_load" :split="false" size="small" item-layout="horizontal" :data-source="hot_news">
        <a-list-item v-if="index <= h_n_range[1] && index >= h_n_range[0]" slot="renderItem" slot-scope="item, index">
          <a-list-item-meta>
            <a slot="title" :href="item.url">
              <span :style="get_r_s(index)">{{ index + 1 }}</span>
              {{ item.title }}
            </a>
          </a-list-item-meta>
          <a slot="actions" style="color: lightgrey;">{{ item.heat }}</a>
        </a-list-item>
      </a-list>
    </div>
  </div>
</template>

<script>
import Vue from 'vue';
import { Icon, List } from 'ant-design-vue';
import axios from 'axios';

const { Item } = List;
const { Meta } = Item;
Vue.component(Icon.name, Icon);
Vue.component(List.name, List);
Vue.component(Item.name, Item);
Vue.component(Meta.name, Meta);

const hostSearchApi = '/api/hot-search';

export default {
    name: 'HotSearch',
    data() {
      return {
        hot_news: [],
        list_load: false,
        h_n_range: [0, 9],
      }
    },
    methods: {
      get_r_s(r) {
        let re = { marginRight: '5px', color: 'grey', fontSize: '18px' };
        if (r === 0) {
          re.color = 'red';
        } else if (r === 1) {
          re.color = 'orange';
        } else if (r === 2) {
          re.color = '#faa90e';
        }
        return re;
      },
			changeBatch() {
        let l = (this.h_n_range[0] + 10) % 50;
        let r = (this.h_n_range[1] + 10) % 50;
        this.h_n_range = [l, r];
			},
      getHotSearch() {
        this.list_load = true;
        axios.get(hostSearchApi).then((res) => {
          if (res.data.success) {
            const r = res.data.data || [];
            this.hot_news = r;
          }
          this.list_load = false;
        }).catch((err) => {
          alert(err);
          this.list_load = false;
        });
      },
		},
    components: {},
    mounted() {
      this.getHotSearch(); //加载热搜新闻
    },
}
</script>

<style scoped>
.main {
  width: 650px;
  height: 300px;
  margin: auto;
  margin-top: 280px;
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color:white;
}
</style>