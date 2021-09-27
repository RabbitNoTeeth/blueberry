<template>
  <q-dialog v-model="show" persistent transition-show="scale" transition-hide="scale">
    <q-card style="min-width: 800px;">
      <q-card-section class="row items-center q-pb-none">
        <div class="text-h6">检测详情</div>
        <q-space/>
        <q-btn icon="close" flat round dense v-close-popup @click="close"/>
      </q-card-section>
      <q-card-section class="row items-center q-pb-none">
        <div class="row" v-if="record.arithmeticCode.indexOf('ANGLE_CHANGE') > -1" style="width: 100%;height: 100%">
          <div class="col" v-bind:key="index" v-for="(url, index) in getImgUrls()">
            <q-img
              :src="url"
              basic
              :ratio="4/3"
            >
            </q-img>
          </div>
        </div>
        <div class="row" v-else style="width: 100%;height: 100%">
          <q-img
            :src="getImgUrl()"
            basic
            :ratio="4/3"
          >
          </q-img>
        </div>
      </q-card-section>
      <q-card-section class="row items-center q-pb-none">
        <div class="row" style="width: 100%">
          <div class="col">
            <p>算法名称：{{ record.arithmeticName }}</p>
          </div>
          <div class="col">
            <p>算法编码：{{ record.arithmeticCode }}</p>
          </div>
        </div>
        <div class="row" style="width: 100%">
          <div class="col">
            <p>设备ID：{{ record.deviceId }}</p>
          </div>
          <div class="col">
            <p>通道ID：{{ record.channelId }}</p>
          </div>
        </div>
        <div class="row" style="width: 100%;margin-bottom: 5px">
          <div>视频快照：</div>
          <div>
            <p v-bind:key="index" v-for="(item, index) in record.imagePath.split(',')">{{item}}</p>
          </div>
        </div>
        <div class="row" style="width: 100%">
          <div>检测结果：</div>
        </div>
        <div class="row" style="width: 100%">
          <pre style="overflow-x: auto; background-color: #e0e0e0;width: 100%">{{ JSON.stringify(JSON.parse(record.detail), null, 2) }}</pre>
        </div>
      </q-card-section>
      <q-card-section class="row items-center q-pb-none">
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

<script>

export default {
  name: "VideoQualityDetectRecordSnapshotViewer",
  components: {},
  data() {
    return {
      show: true,
    }
  },
  props: {
    record: {
      type: Object
    }
  },
  mounted() {
  },
  methods: {
    close() {
      this.$emit('close')
    },
    getImgUrl() {
      return this.$baseUrl + "/api/v1/video-quality-detect/arithmetic/record/snap?id=" + this.record.id + "&st=" + Date.parse(new Date());
    },
    getImgUrls() {
      return this.record.imagePath.split(",").map(path => this.$baseUrl + "/api/v1/video-quality-detect/arithmetic/record/snapByPath?path=" + path + "&st=" + Date.parse(new Date()));
    }
  }
}
</script>

<style scoped>

</style>
