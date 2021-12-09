<template>
  <div>
		<div class="title">
			<SearchBanner :content="content" @requestNews="onSearch" />
		</div>
		<div class="result_tip">
			<span v-if="tool">找到约{{ news_count }}条结果（用时约{{ search_time }}秒）</span>
			<span v-else>
				<a-select default-value="r" style="width: 100px" @change="handleChange">
					<a-select-option value="r">按相关度</a-select-option>
					<a-select-option value="t">按时间</a-select-option>
					<a-select-option value="h">按热度</a-select-option>
				</a-select>
			</span>
			<a-button style="float: right;" @click="setTools" :type="tool ? '' : 'primary'">工具</a-button>
		</div>
		<div :style="{ width: '100%', overflow: 'hidden' }">
			<div class="result">
				<a-spin :spinning="loading" size="large">
					<a-list item-layout="vertical" :data-source="newsList" :split="false" :pagination="pagination">
						<a-list-item slot="renderItem" slot-scope="item">
							<!-- <a-card :hoverable="true" class="card"> -->
								<a style="color: lightgrey">{{ urlByLevel(item.url) }}</a>
								<div class="result_card">
									<a :href="item.url" class="a_style" v-html="item.title + ' -' + item.source_website"></a>
								</div>
								<div class="result_card" v-html="getSummary(item.time, item.summary)"></div>
								<div class="result_card1">
									热度:{{ item.heat }}
									<a style="float: right; color: lightgrey;">相似新闻></a>
								</div>
							<!-- </a-card> -->
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
import { List, Spin, Card, Button, Select } from 'ant-design-vue'
import SearchBanner from './common/SearchBanner.vue';
import Footer from './common/Footer.vue';
import axios from 'axios';
import { urlByLevel, getTime, r_history, g_t } from '../utils/utils';

const searchApi = '/api/search';
const { Item } = List;
const { Meta } = Item;
const { Option } = Select;
Vue.component(List.name, List);
Vue.component(Item.name, Item);
Vue.component(Meta.name, Meta);
Vue.component(Spin.name, Spin);
Vue.component(Card.name, Card);
Vue.component(Button.name, Button)
Vue.component(Select.name, Select)
Vue.component(Option.name, Option);


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
						this.pagination.current = page;
						this.getNews(this.content, page, this.sort)
					},
					pageSize: 10,
					current: 1,
					total: 0,
				},
				tool: true,
				sort: 'r',
			}
    },
    methods: {
			handleChange(s) {
				this.sort = s;
				this.getNews(this.content, 1, s)
			},
			setTools() {
				this.sort = 'r';
				this.tool = !this.tool;
			},
			getSummary(t, s) {
				let s1 = '';
				let t1 = '<span style="color: grey;">' + this.getTime(t) + '——' + '</span>';
				s1 = t1 + (s || '') + '...'
				return s1
			},
			getTime(t) {
				if (t.indexOf('-') > 0) {
					return getTime(t, '-')
				}
			},
			urlByLevel(url) {
				return urlByLevel(url)
			},
			onSearch(value, currentPage) {
				this.content = value;
				this.pagination.current = 1;
				this.getNews(value, currentPage, this.sort);
			},
			getNews(value, currentPage, s) {
				r_history('n_r', value);
				if (!s) {
					s = 'r'
				}
				this.loading = true;
				const params = {
					c: value,
					p: currentPage,
					s: s,
					t: g_t(),
				};
				axios.post(searchApi, params).then((res) => {
					this.loading = false;
					this.newsList = res.data.data.irEntities || [];
					this.news_count = res.data.data.count || 0;
					this.pagination.total = this.news_count;
					this.search_time = res.data.data.time ? res.data.data.time / 1000 : 0;
					// global.console.log(this.newsList)
				}).catch((err) => {
					this.loading = false;
					alert(err);
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
					this.getNews(content, 1)
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
	width: 700px;
	margin-top: 5px;
	margin-left: 150px;
}
.result_card {
	margin-top: 5px;
}
.result_card1 {
	margin-top: 10px;
}
</style>