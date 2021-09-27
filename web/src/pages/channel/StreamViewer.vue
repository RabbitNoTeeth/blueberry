<template>
  <q-dialog v-model="show" persistent transition-show="scale" transition-hide="scale">
    <q-card style="min-width: 800px">
      <q-card-section class="row items-center q-pb-none">
        <div class="text-h6">预览</div>
        <q-space/>
        <q-btn icon="close" flat round dense v-close-popup @click="close"/>
      </q-card-section>
      <q-card-section>
        <flv-player v-if="flvUrl" :flv-url="flvUrl"></flv-player>
        <q-inner-loading :showing="loading">
          <q-spinner-gears size="50px" color="primary"/>
        </q-inner-loading>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

<script>
import FlvPlayer from "components/FlvPlayer";

export default {
  name: "StreamViewer",
  components: {FlvPlayer},
  data() {
    return {
      show: true,
      loading: false,
      flvUrl: null
    }
  },
  props: {
    deviceId: {
      type: String
    },
    channelId: {
      type: String
    }
  },
  mounted() {
    this.queryPlayUrl();
  },
  methods: {
    queryPlayUrl() {
      const app = this;
      app.loading = true;
      app.$axios.get('/api/v1/video-stream/play', {
        params: {
          deviceId: app.deviceId,
          channelId: app.channelId
        }
      })
        .then(res => {
          if (res.data.success) {
            app.loading = false;
            app.flvUrl = res.data.data.flv;
          } else {
            app.loading = false;
            app.$q.notify({
              type: 'warning',
              position: 'top',
              message: '视频点播失败: ' + res.data.message
            });
          }
        })
        .catch(e => {
          app.$q.notify({
            type: 'negative',
            position: 'top',
            message: '视频点播异常. ' + e
          });
        })
    },
    close() {
      this.flvUrl = null;
      this.$emit('close')
    }
  }
}
</script>

<style scoped>

</style>
