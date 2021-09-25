import VueRouter from "vue-router";
import SearchResult from '../components/SearchResult.vue';
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
];

const router = new VueRouter({
    routes
});

export default router;