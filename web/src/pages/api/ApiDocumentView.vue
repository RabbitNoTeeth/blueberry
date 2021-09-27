<template>
  <div class="main_content">
    <iframe :src="iframeUrl" width="100%" height="100%"></iframe>
  </div>
</template>

<script>
export default {
  name: "ApiDocumentView",
  data() {
    return {
    }
  },
  computed: {
    iframeUrl() {
      return this.$baseUrl + '/doc.html';
    }
  }
}
</script>

<style scoped>

</style>
