<template>
  <div>
		<div class="title">
			<SearchBanner :content="content" />
		</div>
    <div class="result">
			<a-spin :spinning="loading" size="large">
				<a-list item-layout="vertical" :data-source="newsList" :split="false">
					<a-list-item slot="renderItem" slot-scope="item">
						<a-card :hoverable="true" class="card">
							<a :href="item.url" style="text-decoration: underline;">{{ item.title }}</a>
						</a-card>
					</a-list-item>
				</a-list>
			</a-spin>
    </div>
  </div>
</template>

<script>
import Vue from 'vue';
import { List, Spin, Card } from 'ant-design-vue'
import SearchBanner from './SearchBanner.vue';
import axios from 'axios';

const searchApi = '/api/search';
const { Item } = List;
const { Meta } = Item;
Vue.component(List.name, List);
Vue.component(Item.name, Item);
Vue.component(Meta.name, Meta);
Vue.component(Spin.name, Spin);
Vue.component(Card.name, Card);

export default {
    name: 'SearchResult',
    data() {
        return {
            content: '',
            newsList: [],
            loading: false,
        }
    },
    methods: {
        getNews(value) {
            this.loading = true;
            const params = {
                content: value
            };
            axios.post(searchApi, params).then((res) => {
                this.loading = false;
                this.newsList = res.data.data || [];
                global.console.log(this.newsList)
            }).catch((err) => {
                this.loading = false;
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
.result {
	margin-left: 150px;
	width: 650px;
}
.card {
	height: 200px;
	border-radius: 10px;
}
</style>