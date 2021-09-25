<template>
  <div>
      <div class="title">
        <SearchBanner :content="content" />
      </div>
  </div>
</template>

<script>
import SearchBanner from './SearchBanner.vue';

export default {
    name: 'SearchResult',
    data() {
        return {
            content: '',
        }
    },
    methods: {},
    components: {
        SearchBanner
    },
    mounted() {
        if (this.$route.query) {
            const { content } = this.$route.query;
            this.content = content;
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