<template>
  <div>
      <div class="title">
        <SearchBanner :content="content" />
      </div>
      <div>
        <a-list item-layout="horizontal" :data-source="newsList">
            <a-list-item slot="renderItem" slot-scope="item">
                <a-list-item-meta>
                    <a slot="title" :href="item.url">{{ item.title }}</a>
                </a-list-item-meta>
            </a-list-item>
        </a-list>
      </div>
  </div>
</template>

<script>
import Vue from 'vue';
import { List } from 'ant-design-vue'
import SearchBanner from './SearchBanner.vue';
import axios from 'axios';

const searchApi = '/api/search';
const { Item } = List;
const { Meta } = Item;
Vue.component(List.name, List);
Vue.component(Item.name, Item);
Vue.component(Meta.name, Meta);

export default {
    name: 'SearchResult',
    data() {
        return {
            content: '',
            newsList: [],
        }
    },
    methods: {
        getNews(value) {
            const params = {
                content: value
            };
            axios.post(searchApi, params).then((res) => {
                this.newsList = res.data.data || [];
                global.console.log(this.newsList)
            }).catch((err) => {
                global.console.log(err);
            });
        },
    },
    components: {
        SearchBanner
    },
    mounted() {
        if (this.$route.query) {
            const { content } = this.$route.query;
            this.content = content;
            this.getNews(content)
        }
    },
}
</script>

<style scoped>
.title {
    height: 80px;
    width: 100%;
    border-bottom: 0.05rem solid rgb(230, 230, 230);
}
</style>