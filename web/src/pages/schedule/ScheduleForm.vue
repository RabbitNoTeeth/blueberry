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
            v-model="formData_.name"
            label="任务名称 *"
            lazy-rules
            :readonly="formData !== null"
            :rules="[ val => val && val.length > 0 || '请输入任务名称']"
          />
          <q-input
            filled
            v-model="formData_.group"
            label="任务组 *"
            lazy-rules
            :rules="[ val => val && val.length > 0 || '请输入任务组']"
          />
          <q-input
            v-model="formData_.description"
            filled
            type="textarea"
            label="描述 *"
            lazy-rules
            :rules="[ val => val && val.length > 0 || '请输入任务描述']"
          />
          <q-input
            filled
            v-model="formData_.jobClassName"
            label="job-class *"
            lazy-rules
            :rules="[ val => val && val.length > 0 || '请输入job-class']"
          />
          <q-input
            filled
            v-model="formData_.cronExpression"
            label="cron表达式 *"
            lazy-rules
            :rules="[ val => val && val.length > 0 || '请输入cron表达式']"
          />
          <q-input
            filled
            type="number"
            v-model="formData_.priority"
            label="任务权重 *"
            lazy-rules
            :rules="[val => val !== null && val !== '' || '请输入任务权重']"
          />
          <q-select
            filled
            v-model="formData_.misfireInstruction"
            :options="misfireInstructions"
            option-value="value"
            option-label="label"
            label="补偿模式"
            emit-value
            map-options
            lazy-rules
            :rules="[val => val !== null && val !== '' || '请选择补偿模式']"
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
  name: "ScheduleForm",
  data() {
    return {
      show: true,
      loading: false,
      misfireInstructions: [
        {label: '不进行补偿', value: 2},
        {label: '只补偿一次', value: 1},
        {label: '补偿所有', value: -1}
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
      return formData ? '编辑定时任务' : '新建定时任务';
    }
  },
  created() {
    const formData = this.formData;
    this.formData_ = formData ? {...formData} : {priority: 5, misfireInstruction: 2}
  },
  mounted() {
  },
  methods: {
    close() {
      this.$emit('close')
    },
    onSubmit() {
      const app = this;
      const url = app.formData ? '/api/v1/quartz/job/update' : '/api/v1/quartz/job/add';
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
      this.formData_ = {priority: 5, misfireInstruction: 2}
    }
  }
}
</script>

<style scoped>

</style>
