<template>
  <div class="main_content">
    <q-table
      class="table_full"
      virtual-scroll
      :virtual-scroll-sticky-size-start="48"
      :data="data"
      :columns="columns"
      row-key="field"
      hide-bottom
      :pagination="pagination"
    >
      <template v-slot:top-left>
        <span>定时任务列表</span>
      </template>
      <template v-slot:top-right>
        <q-btn
          color="primary"
          size="xs"
          label="新建"
          @click="addClick"
        />
        <q-btn flat color="primary" label="" icon="refresh" @click="refreshClick"/>
      </template>
      <template v-slot:body-cell-operations="props">
        <q-td :props="props">
          <q-btn
            color="secondary"
            size="xs"
            label="启动"
            v-if="props.row.state === 'PAUSED'"
            @click="resumeClick(props.row)"
            style="margin-right: 5px"
          />
          <q-btn
            color="orange"
            size="xs"
            label="暂停"
            v-if="props.row.state === 'NORMAL'"
            @click="pauseClick(props.row)"
            style="margin-right: 5px"
          />
          <q-btn
            color="brown-5"
            size="xs"
            label="编辑"
            @click="editClick(props.row)"
            style="margin-right: 5px"
          />
          <q-btn
            color="red"
            size="xs"
            label="删除"
            @click="removeClick(props.row)"
            style="margin-right: 5px"
          />
        </q-td>
      </template>
    </q-table>
    <schedule-form v-if="showForm" :form-data="formData" @close="formClose" @success="formSuccess"></schedule-form>
    <schedule-remove-confirm v-if="showConfirm" :data="removeData" @close="removeClose" @success="removeSuccess"></schedule-remove-confirm>
    <schedule-pause-confirm v-if="showPause" :data="pauseData" @close="pauseClose" @success="pauseSuccess"></schedule-pause-confirm>
    <schedule-resume-confirm v-if="showResume" :data="resumeData" @close="resumeClose" @success="resumeSuccess"></schedule-resume-confirm>
  </div>
</template>

<script>
import ScheduleForm from "pages/schedule/ScheduleForm";
import ScheduleRemoveConfirm from "pages/schedule/ScheduleRemoveConfirm";
import SchedulePauseConfirm from "pages/schedule/SchedulePauseConfirm";
import ScheduleResumeConfirm from "pages/schedule/ScheduleResumeConfirm";
export default {
  name: "ScheduleList",
  components: {ScheduleResumeConfirm, SchedulePauseConfirm, ScheduleRemoveConfirm, ScheduleForm},
  data() {
    return {
      data: [],
      columns: [
        {name: 'index', field: 'index', label: '#', align: 'left'},
        {name: 'name', field: 'name', label: '任务名称', align: 'left'},
        {name: 'group', field: 'group', label: '任务组', align: 'left'},
        {name: 'description', field: 'description', label: '任务描述', align: 'left'},
        // {name: 'jobClassName', field: 'jobClassName', label: 'job-class', align: 'left'},
        // {name: 'cronExpression', field: 'cronExpression', label: 'cron', align: 'left'},
        // {name: 'misfireInstruction', field: 'misfireInstruction', label: '补偿模式', align: 'left'},
        // {name: 'priority', field: 'priority', label: '权重', align: 'left'},
        {name: 'state', field: 'state', label: '状态', align: 'left', format: (val, row) => val === "PAUSED" ? '暂停' : '运行中'},
        {name: 'previousFireTime', field: 'previousFireTime', label: '上次执行', align: 'left'},
        {name: 'nextFireTime', field: 'nextFireTime', label: '下次执行', align: 'left'},
        {name: 'operations', field: 'operations', label: '操作', align: 'left'}
      ],
      pagination: {
        rowsPerPage: 0
      },
      refreshInterval: null,
      showForm: false,
      formData: null,
      showConfirm: false,
      removeData: null,
      showPause: false,
      pauseData: null,
      showResume: false,
      resumeData: null
    }
  },
  mounted() {
    const app = this;
    app.queryList();
    if (app.refreshInterval) {
      clearInterval(app.refreshInterval);
    }
    app.refreshInterval = setInterval(() => app.queryList(), 5000);
  },
  destroyed() {
    const app = this;
    if (app.refreshInterval) {
      clearInterval(app.refreshInterval);
    }
  },
  methods: {
    queryList() {
      const app = this;
      app.$axios.get('/api/v1/quartz/job/list', {
        params: {}
      })
        .then(res => {
          if (res.data.success) {
            app.data = res.data.data;
            for (let i = 0; i < app.data.length; i++) {
              app.data[i].index = i + 1;
            }
          } else {
            app.$q.notify({
              type: 'warning',
              position: 'top',
              message: '定时任务列表加载失败: ' + res.data.message
            });
          }
        })
        .catch(e => {
          app.$q.notify({
            type: 'negative',
            position: 'top',
            message: '定时任务列表加载异常. ' + e
          });
        })
    },
    refreshClick() {
      this.queryList();
    },
    addClick() {
      this.formData = null;
      this.showForm = true;
    },
    editClick(data) {
      this.formData = data;
      this.showForm = true;
    },
    formClose() {
      this.formData = null;
      this.showForm = false;
    },
    formSuccess() {
      this.formClose();
      this.queryList();
    },
    removeClose() {
      this.removeData = null;
      this.showConfirm = false;
    },
    removeClick(data) {
      this.removeData = data;
      this.showConfirm = true;
    },
    removeSuccess() {
      this.removeClose();
      this.queryList();
    },
    pauseClose() {
      this.pauseData = null;
      this.showPause = false;
    },
    pauseClick(data) {
      this.pauseData = data;
      this.showPause = true;
    },
    pauseSuccess() {
      this.pauseClose();
      this.queryList();
    },
    resumeClose() {
      this.resumeData = null;
      this.showResume = false;
    },
    resumeClick(data) {
      this.resumeData = data;
      this.showResume = true;
    },
    resumeSuccess() {
      this.resumeClose();
      this.queryList();
    }
  }
}
</script>

<style scoped>

</style>
