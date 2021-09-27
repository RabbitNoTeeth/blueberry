<template>
  <q-dialog v-model="show" persistent transition-show="scale" transition-hide="scale">
    <q-card>
      <q-card-section class="row items-center q-pb-none">
        <div class="text-h6">{{ title }}</div>
        <q-space/>
        <q-btn icon="close" flat round dense v-close-popup @click="close"/>
      </q-card-section>
      <q-card-section>
        <q-form
          @submit="onSubmit"
          @reset="onReset"
          class="q-gutter-md"
          style="width: 500px"
        >
          <q-input
            filled
            v-model="formData_.id"
            label="通道ID *"
            lazy-rules
            :readonly="formData !== null"
            :rules="[ val => val && val.length > 0 || '请输入通道ID']"
          />
          <q-input
            filled
            v-model="formData_.name"
            label="通道名称 *"
            lazy-rules
            :rules="[ val => val && val.length > 0 || '请输入通道名称']"
          />
          <q-input
            filled
            v-model="formData_.rtsp"
            label="rtsp *"
            v-if="deviceType === 'RTSP'"
            lazy-rules
            :rules="[ val => val && val.length > 0 || '请输入rtsp']"
          />
          <div style="text-align: right">
            <q-btn label="提交" type="submit" color="primary"/>
            <q-btn label="重置" type="reset" color="primary" flat class="q-ml-sm"/>
          </div>
        </q-form>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

<script>

export default {
  name: "ChannelForm",
  data() {
    return {
      show: true,
      formData_: null
    }
  },
  props: {
    formData: {
      type: Object
    }
  },
  computed: {
    deviceId() {
      return this.$route.params.deviceId
    },
    deviceType() {
      return this.$route.params.deviceType
    },
    title() {
      const formData = this.formData;
      return formData ? '编辑通道' : '新建通道';
    }
  },
  created() {
    const formData = this.formData;
    this.formData_ = formData ? {...formData} : {}
  },
  mounted() {
  },
  methods: {
    close() {
      this.$emit('close')
    },
    onSubmit() {
      const app = this;
      const url = app.formData ? '/api/v1/channel/update' : '/api/v1/channel/add';
      app.$axios.post(url, {
        ...app.formData_,
        deviceId: app.deviceId
      })
        .then(res => {
          if (res.data.success) {
            app.$emit('success');
            app.$q.notify({
              type: 'positive',
              position: 'top',
              message: app.formData ? '更新成功' : '添加成功'
            });
          } else {
            app.$q.notify({
              type: 'warning',
              position: 'top',
              message: (app.formData ? '更新失败' : '添加失败') + ': ' + res.data.message
            });
          }
        })
        .catch(e => {
          app.$q.notify({
            type: 'negative',
            position: 'top',
            message: (app.formData ? '更新异常' : '添加异常') + '. ' + e
          });
        })
    },
    onReset() {
      this.formData_ = {}
    }
  }
}
</script>

<style scoped>

</style>
