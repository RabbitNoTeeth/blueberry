<template>
  <q-dialog v-model="show" persistent>
    <q-card style="min-width: 300px">
      <q-card-section class="row items-center">
        <q-icon name="warning" class="text-red" style="font-size: 2em;" />
        <span class="q-ml-sm">是否删除以下通道 ?</span>
      </q-card-section>
      <q-card-section class="row items-center">
        <div style="padding-left: 50px">
          <p>
            <span>●&nbsp;</span>
            <span>{{ data.id }} - {{ data.name }}</span>
          </p>
        </div>
      </q-card-section>
      <q-card-actions align="right">
        <q-btn size="sm" label="确定" color="primary" @click="submit"/>
        <q-btn size="sm" flat label="取消" color="primary" @click="close" />
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script>

export default {
  name: "ChannelRemoveConfirm",
  data() {
    return {
      show: true
    }
  },
  props: {
    data: {
      type: Object
    }
  },
  computed: {
    deviceId() {
      return this.$route.params.deviceId
    },
    deviceType() {
      return this.$route.params.deviceType
    }
  },
  methods: {
    close() {
      this.$emit('close')
    },
    submit() {
      const app = this;
      const url = '/api/v1/channel/delete';
      app.$axios.post(url, {
        id: app.data.id,
        deviceId: app.deviceId
      })
        .then(res => {
          if (res.data.success) {
            app.$emit('success');
            app.$q.notify({
              type: 'positive',
              position: 'top',
              message: '删除成功'
            });
          } else {
            app.$q.notify({
              type: 'warning',
              position: 'top',
              message: '删除失败: ' + res.data.message
            });
          }
        })
        .catch(e => {
          app.$q.notify({
            type: 'negative',
            position: 'top',
            message: '删除异常. ' + e
          });
        })
    }
  }
}
</script>

<style scoped>

</style>
