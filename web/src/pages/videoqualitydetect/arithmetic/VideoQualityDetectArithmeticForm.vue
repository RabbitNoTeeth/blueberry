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
          <q-select
            filled
            v-model="formData_.a"
            :options="supportedArithmetics"
            option-value="code"
            option-label="name"
            label="选择算法 *"
            emit-value
            map-options
            lazy-rules
            v-if="formData === null"
            :rules="[val => val !== null && val !== '' || '请选择算法']"
          />
          <q-input
            filled
            v-model="formData_.code"
            label="算法编码 *"
            lazy-rules
            readonly
            :rules="[ val => val && val.length > 0 || '请选择算法']"
          />
          <q-input
            filled
            v-model="formData_.name"
            label="算法名称 *"
            lazy-rules
            readonly
            :rules="[ val => val && val.length > 0 || '请选择算法']"
          />
          <q-input
            filled
            type="number"
            v-model="formData_.priority"
            label="优先级 *"
            lazy-rules
            :rules="[ val => val !== null && val !== '' || '请输入优先级']"
          />
          <q-field filled label="应用设备" stack-label>
            <template v-slot:control>
              <div class="self-center full-width no-outline" tabindex="0">
                <q-radio v-model="formData_.applyAll" :val="1" label="所有设备" color="teal"/>
                <q-radio v-model="formData_.applyAll" :val="0" label="指定设备" color="red"/>
              </div>
            </template>
          </q-field>
          <q-field v-if="showDeviceSelect" filled label="选择设备" stack-label>
            <template v-slot:control>
              <div class="self-center full-width no-outline" tabindex="0">
                <q-tree class="col-12 col-sm-6"
                        :nodes="applicableDeviceList"
                        node-key="code"
                        tick-strategy="leaf"
                        :ticked.sync="appliedDeviceList"
                        default-expand-all
                >
                  <template v-slot:default-header="prop">
                    <div class="row items-center">
                      <div>{{ prop.node.name }}</div>
                    </div>
                  </template>
                </q-tree>
              </div>
            </template>
          </q-field>
          <q-input
            v-model="formData_.settings"
            filled
            type="textarea"
            label="参数设置"
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
  name: "VideoQualityDetectArithmeticForm",
  data() {
    return {
      show: true,
      loading: false,
      supportedArithmetics: [],
      formData_: null,
      applicableDeviceList: [],
      appliedDeviceList: []
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
      return formData ? '编辑算法' : '新建算法';
    },
    showDeviceSelect() {
      const formData = this.formData_;
      return formData.applyAll !== undefined && formData.applyAll !== null && formData.applyAll === 0
    }
  },
  watch: {
    'formData_.a': function (val) {
      if (val) {
        const split = val.split(':');
        this.formData_.code = split[0];
        this.formData_.name = split[1];
      }
    }
  },
  created() {
    const formData = this.formData;
    this.formData_ = formData ? {...formData} : {applyAll: 1}
  },
  mounted() {
    this.querySupportedArithmetics();
    this.queryApplicableDeviceList();
    this.queryAppliedDeviceList();
  },
  methods: {
    close() {
      this.$emit('close')
    },
    onSubmit() {
      const app = this;
      const url = this.formData ? '/api/v1/video-quality-detect/arithmetic/update' : '/api/v1/video-quality-detect/arithmetic/add';
      if (app.formData_.applyAll === 0) {
        app.formData_.applyDevices = app.appliedDeviceList.join(',');
      }
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
    },
    querySupportedArithmetics() {
      const app = this;
      app.$axios.get('/api/v1/video-quality-detect/arithmetic/supportedArithmetics')
        .then(res => {
          if (res.data.success) {
            app.supportedArithmetics = res.data.data.map(i => {
              return {
                code: i,
                name: i
              }
            })
          } else {
            app.$q.notify({
              type: 'warning',
              position: 'top',
              message: '支持的算法列表加载失败: ' + res.data.message
            });
          }
        })
        .catch(e => {
          app.$q.notify({
            type: 'negative',
            position: 'top',
            message: '支持的算法列表加载异常. ' + e
          });
        })
    },
    queryApplicableDeviceList() {
      const app = this;
      app.$axios.get('/api/v1/video-quality-detect/arithmetic/applicableDeviceList')
        .then(res => {
          if (res.data.success) {
            app.applicableDeviceList = res.data.data;
          } else {
            app.$q.notify({
              type: 'warning',
              position: 'top',
              message: '可应用设备列表加载失败: ' + res.data.message
            });
          }
        })
        .catch(e => {
          app.$q.notify({
            type: 'negative',
            position: 'top',
            message: '可应用设备列表加载异常. ' + e
          });
        })
    },
    queryAppliedDeviceList() {
      const app = this;
      app.$axios.get('/api/v1/video-quality-detect/arithmetic/appliedDeviceList', {
        params: {
          id: app.formData_.id
        }
      })
        .then(res => {
          if (res.data.success) {
            app.appliedDeviceList = res.data.data;
          } else {
            app.$q.notify({
              type: 'warning',
              position: 'top',
              message: '已应用设备列表加载失败: ' + res.data.message
            });
          }
        })
        .catch(e => {
          app.$q.notify({
            type: 'negative',
            position: 'top',
            message: '已应用设备列表加载异常. ' + e
          });
        })
    }
  }
}
</script>

<style scoped>

</style>
