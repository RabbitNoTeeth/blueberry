<template>
  <div class="main_content">
    <q-table
      class="table_full"
      virtual-scroll
      :virtual-scroll-sticky-size-start="48"
      :data="data"
      :columns="columns"
      row-key="field"
      :pagination="{ rowsPerPage: 0 }"
    >
      <template v-slot:top>
        <div>
          <q-btn
            color="teal"
            size="sm"
            label="新建"
            style="margin-right: 5px"
            @click="addClick"
          />
        </div>
        <div>
          <el-input
            size="mini"
            placeholder="名称"
            clearable
            style="width: 100px; margin-right: 5px"
            v-model="searchParams.name">
          </el-input>
          <el-select
            v-model="searchParams.enable"
            placeholder="状态"
            clearable
            size="mini"
            style="width: 100px; margin-right: 5px"
          >
            <el-option label="启用" :value="1"/>
            <el-option label="停用" :value="0"/>
          </el-select>
          <el-select
            v-model="searchParams.applyAll"
            placeholder="应用设备"
            clearable
            size="mini"
            style="width: 100px; margin-right: 5px"
          >
            <el-option label="所有设备" :value="1"/>
            <el-option label="指定设备" :value="0"/>
          </el-select>
          <q-btn
            color="primary"
            size="sm"
            label="查询"
            style="margin-right: 5px"
            @click="refreshClick"
          />
          <q-btn
            color="primary"
            size="sm"
            outline
            label="重置"
            @click="resetClick"
          />
        </div>
      </template>
      <template v-slot:body-cell-operations="props">
        <q-td :props="props">
          <q-btn
            color="secondary"
            size="xs"
            label="启用"
            v-if="props.row.enable === 0"
            @click="resumeClick(props.row)"
            style="margin-right: 5px"
          />
          <q-btn
            color="orange"
            size="xs"
            label="停用"
            v-if="props.row.enable === 1"
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
      <template v-slot:bottom>
        <el-pagination
          @size-change="onSizeChange"
          @current-change="onPageChange"
          :current-page="pagination.page"
          :page-sizes="pagination.pageSizes"
          :page-size="pagination.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total">
        </el-pagination>
      </template>
    </q-table>
    <video-quality-detect-arithmetic-form v-if="showForm" :form-data="formData" @close="formClose"
                                          @success="formSuccess"></video-quality-detect-arithmetic-form>
    <video-quality-detect-arithmetic-remove-confirm v-if="showConfirm" :data="removeData" @close="removeClose"
                                                    @success="removeSuccess"></video-quality-detect-arithmetic-remove-confirm>
    <video-quality-detect-arithmetic-pause-confirm v-if="showPause" :data="pauseData" @close="pauseClose"
                                                   @success="pauseSuccess"></video-quality-detect-arithmetic-pause-confirm>
    <video-quality-detect-arithmetic-resume-confirm v-if="showResume" :data="resumeData" @close="resumeClose"
                                                    @success="resumeSuccess"></video-quality-detect-arithmetic-resume-confirm>
  </div>
</template>

<script>
import VideoQualityDetectArithmeticForm from "pages/videoqualitydetect/arithmetic/VideoQualityDetectArithmeticForm";
import VideoQualityDetectArithmeticRemoveConfirm
  from "pages/videoqualitydetect/arithmetic/VideoQualityDetectArithmeticRemoveConfirm";
import VideoQualityDetectArithmeticPauseConfirm
  from "pages/videoqualitydetect/arithmetic/VideoQualityDetectArithmeticPauseConfirm";
import VideoQualityDetectArithmeticResumeConfirm
  from "pages/videoqualitydetect/arithmetic/VideoQualityDetectArithmeticResumeConfirm";

export default {
  name: "VideoQualityDetectArithmeticList",
  components: {
    VideoQualityDetectArithmeticResumeConfirm,
    VideoQualityDetectArithmeticPauseConfirm,
    VideoQualityDetectArithmeticRemoveConfirm,
    VideoQualityDetectArithmeticForm
  },
  data() {
    return {
      data: [],
      columns: [
        {name: 'index', field: 'index', label: '#', align: 'left'},
        {name: 'code', field: 'code', label: '编码', align: 'left'},
        {name: 'name', field: 'name', label: '名称', align: 'left'},
        {name: 'priority', field: 'priority', label: '优先级', align: 'left'},
        {name: 'settings', field: 'settings', label: '参数设置', align: 'left'},
        {name: 'enable', field: 'enable', label: '启用状态', align: 'left', format: (val, row) => val === 1 ? '启用' : '停用'},
        {
          name: 'applyAll',
          field: 'applyAll',
          label: '应用设备',
          align: 'left',
          format: (val, row) => val === 1 ? '所有设备' : '指定设备'
        },
        {name: 'operations', field: 'operations', label: '操作', align: 'left'}
      ],
      refreshInterval: null,
      showForm: false,
      formData: null,
      showConfirm: false,
      removeData: null,
      showPause: false,
      pauseData: null,
      showResume: false,
      resumeData: null,
      pagination: {
        page: 1,
        pageSize: 15,
        pageSizes: [10, 15, 20, 30, 40, 50],
        total: 0
      },
      searchParams: {
        name: null,
        enable: null,
        applyAll: null
      }
    }
  },
  mounted() {
    const app = this;
    app.queryList();
    if (app.refreshInterval) {
      clearInterval(app.refreshInterval);
    }
    app.refreshInterval = setInterval(() => app.queryList(), 10000);
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
      app.$axios.get('/api/v1/video-quality-detect/arithmetic/page', {
        params: {
          page: app.pagination.page,
          pageSize: app.pagination.pageSize,
          ...app.searchParams
        }
      })
        .then(res => {
          if (res.data.success) {
            app.pagination.total = res.data.data.total;
            app.data = res.data.data.data;
            for (let i = 0; i < app.data.length; i++) {
              app.data[i].index = i + 1;
            }
          } else {
            app.$q.notify({
              type: 'warning',
              position: 'top',
              message: '算法列表加载失败: ' + res.data.message
            });
          }
        })
        .catch(e => {
          app.$q.notify({
            type: 'negative',
            position: 'top',
            message: '算法列表加载异常. ' + e
          });
        })
    },
    refreshClick() {
      this.queryList();
    },
    resetClick() {
      this.searchParams = {
        name: null,
        enable: null,
        applyAll: null
      };
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
    },
    onPageChange(val) {
      this.pagination.page = val;
      this.queryList();
    },
    onSizeChange(val) {
      this.pagination.pageSize = val;
      this.queryList();
    }
  }
}
</script>

<style scoped>

</style>
