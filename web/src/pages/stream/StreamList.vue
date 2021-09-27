<template>
  <div class="q-pa-md row items-start q-gutter-md">
    <template v-for="(stream, index) in data">
      <q-card :key="index" style="width: 240px">
        <q-img
          :src="getImgUrl(stream)"
          basic
          :ratio="4/3"
          class="img_snap"
          @click="snapClick(stream)"
        >
          <div class="absolute-bottom text-subtitle2 text-center">
            {{ stream.id }}
          </div>
        </q-img>
      </q-card>
    </template>
    <stream-viewer2 v-if="showStreamView" :stream-id="currentStream.id" :flv-url="currentStream.flvUrl" @close="streamViewClose"></stream-viewer2>
  </div>
</template>

<script>
import StreamViewer2 from "pages/stream/StreamViewer2";
export default {
  name: "StreamList",
  components: {StreamViewer2},
  data() {
    return {
      data: [],
      showStreamView: false,
      currentStream: false,
      refreshInterval: null
    }
  },
  mounted() {
    const app = this;
    app.queryStreamList();
    if (app.refreshInterval) {
      clearInterval(app.refreshInterval);
    }
    app.refreshInterval = setInterval(() => app.queryStreamList(), 10000);
  },
  destroyed() {
    const app = this;
    if (app.refreshInterval) {
      clearInterval(app.refreshInterval);
    }
  },
  methods: {
    queryStreamList() {
      const app = this;
      app.$axios.get('/api/v1/video-stream/list', {
        params: {}
      })
        .then(res => {
          if (res.data.success) {
            app.data = res.data.data;
          } else {
            app.$q.notify({
              type: 'warning',
              position: 'top',
              message: '视频列表加载失败: ' + res.data.message
            });
          }
        })
        .catch(e => {
          app.$q.notify({
            type: 'negative',
            position: 'top',
            message: '视频列表加载异常. ' + e
          });
        })
    },
    snapClick(stream) {
      this.currentStream = stream;
      this.showStreamView = true;
    },
    streamViewClose() {
      this.showStreamView = false;
    },
    getImgUrl(stream) {
      return this.$baseUrl + "/api/v1/video-stream/snap/" + stream.snap + "?st=" + Date.parse(new Date());
    }
  }
}
</script>

<style scoped>

.img_snap {

}

.img_snap:hover {
  cursor: pointer;
}

</style>
