<template>
  <div class="main">
		<div>
			<span>News热搜</span>
			<a :style="{ float: 'right' }" @click="changeBatch">
				<a-icon type="sync" />
				换一换
			</a>
		</div>
  </div>
</template>

<script>
import Vue from 'vue';
import { Icon } from 'ant-design-vue';
Vue.component(Icon.name, Icon);

export default {
    name: 'HotSearch',
    data() {
        return {}
    },
    methods: {
			changeBatch() {
				global.console.log('换一换');
			},
		},
    components: {},
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
		background-color:aqua;
}
</style>