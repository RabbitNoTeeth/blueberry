<template>
  <q-dialog v-model="show" persistent transition-show="scale" transition-hide="scale">
    <q-card style="min-width: 800px">
      <q-card-section class="row items-center q-pb-none">
        <div class="text-h6">{{streamId}}</div>
        <q-space/>
        <q-btn icon="close" flat round dense v-close-popup @click="close"/>
      </q-card-section>
      <q-card-section>
        <flv-player v-if="flvUrl" :flv-url="flvUrl"></flv-player>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

<script>
import FlvPlayer from "components/FlvPlayer";

export default {
  name: "StreamViewer2",
  components: {FlvPlayer},
  data() {
    return {
      show: true,
    }
  },
  props: {
    streamId: {
      type: String
    },
    flvUrl: {
      type: String
    }
  },
  mounted() {
  },
  methods: {
    close() {
      this.$emit('close')
    }
  }
}
</script>

<style scoped>

</style>
