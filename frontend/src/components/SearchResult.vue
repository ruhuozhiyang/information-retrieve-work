<template>
  <div>
	<div class="title">
		<SearchBanner :content="content" />
	</div>
	<div class="result_tip">
		找到约
		{{ news_count }}
		条结果（用时约
		{{ search_time }}
		秒）
	</div>
	<div :style="{ width: '100%', overflow: 'hidden' }">
		<div class="result">
			<a-spin :spinning="loading" size="large">
				<a-list item-layout="vertical" :data-source="newsList" :split="false" :pagination="pagination">
					<a-list-item slot="renderItem" slot-scope="item">
						<a-card :hoverable="true" class="card">
							<a :href="item.url" class="a_style" v-html="item.title"></a>
						</a-card>
					</a-list-item>
				</a-list>
			</a-spin>
		</div>
		<div class="right_module">
			<a-card class="right_card" title="相关搜索">
				搜索1
				搜索2
			</a-card>
		</div>
	</div>
	<Footer />
  </div>
</template>

<script>
import Vue from 'vue';
import { List, Spin, Card } from 'ant-design-vue'
import SearchBanner from './common/SearchBanner.vue';
import Footer from './common/Footer.vue';
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
						news_count: 20000,
						search_time: 0.35,
						pagination: {
							onChange: (page) => {
								global.console.log(page);
							},
							pageSize: 10,
							current: 1,
							total: 100,
						}
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
        SearchBanner,
		Footer,
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
	width: 700px;
	float: left;
}
.right_module {
	width: 30%;
	float: left;
}
.right_card {
	margin-left: 15%;
	border-radius: 10px;
	width: 100%;
	margin-top: 10px;
}
.card {
	height: 200px;
	border-radius: 10px;
}
.a_style {
	text-decoration: underline;
	font-size: 18px;
}
.result_tip {
	margin-left: 150px;
}
</style>