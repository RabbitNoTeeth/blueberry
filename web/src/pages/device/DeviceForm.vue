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
            label="设备ID *"
            lazy-rules
            :readonly="formData !== null"
            :rules="[ val => val && val.length > 0 || '请输入设备ID']"
          />
          <q-select
            filled
            v-model="formData_.type"
            :options="types"
            option-value="value"
            option-label="label"
            label="设备类型 *"
            emit-value
            map-options
            lazy-rules
            :rules="[val => val !== null && val !== '' || '请选择设备类型']"
          />
          <q-input
            filled
            v-model="formData_.name"
            label="设备名称 *"
            lazy-rules
            :rules="[ val => val && val.length > 0 || '请输入设备名称']"
          />
          <q-input
            filled
            v-model="formData_.remoteIp"
            label="remoteIp *"
            lazy-rules
            :rules="[ val => val && val.length > 0 || '请输入remoteIp']"
          />
          <q-input
            filled
            type="number"
            v-model="formData_.remotePort"
            label="remotePort *"
            lazy-rules
            :rules="[ val => val !== null && val !== '' || '请输入remotePort']"
          />
          <q-input
            filled
            v-model="formData_.manufacturer"
            label="生产商"
          />
          <q-input
            filled
            v-model="formData_.model"
            label="型号"
          />
          <q-input
            filled
            v-model="formData_.firmware"
            label="固件版本"
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
  name: "DeviceForm",
  data() {
    return {
      show: true,
      loading: false,
      types: [
        {label: 'RTSP', value: 'RTSP'}
      ],
      formData_: null
    }
  },
  props: {
    formData: {
      type: Object
    }
  },
  computed: {
    title() {
      const formData = this.formData;
      return formData ? '编辑设备' : '新建设备';
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
      const url = this.formData ? '/api/v1/device/update' : '/api/v1/device/add';
      app.$axios.post(url, app.formData_)
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
