import VueRouter from "vue-router";
import SearchResult from '../components/SearchResult.vue';
import SearchResultM from '../components/SearchResultMobile.vue';
import Home from '../components/Home';

const routes = [
	{
		path: '/',
		component: Home,
	},
	{
		path: '/result',
		component: SearchResult
	},
	{
		path: '/result_m',
		component: SearchResultM
	},
];

const router = new VueRouter({
    routes
});

export default router;