<template>
  <div class="main_content">
    <q-table
      :data="data"
      :columns="columns"
      row-key="field"
      style="height: 100%"
      virtual-scroll
      :pagination="{ rowsPerPage: 0 }"
    >
      <template v-slot:top>
        <div>
          <el-input
            size="mini"
            placeholder="算法名称"
            clearable
            style="width: 100px; margin-right: 5px"
            v-model="searchParams.arithmeticName">
          </el-input>
          <el-input
            size="mini"
            placeholder="设备ID"
            clearable
            style="width: 150px; margin-right: 5px"
            v-model="searchParams.deviceId">
          </el-input>
          <el-input
            size="mini"
            placeholder="通道ID"
            clearable
            style="width: 150px; margin-right: 5px"
            v-model="searchParams.channelId">
          </el-input>
          <el-select
            v-model="searchParams.hasQualityError"
            placeholder="是否质量异常"
            clearable
            size="mini"
            style="width: 150px; margin-right: 5px"
          >
            <el-option label="是" :value="1"/>
            <el-option label="否" :value="0"/>
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
            color="primary"
            size="xs"
            label="查看详情"
            style="margin-right: 5px"
            @click="onSnapshotClick(props.row)"
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
    <video-quality-detect-record-snapshot-viewer v-if="showSnapshotView" :record="currentRecord"
                                                 @close="onSnapshotViewClose"></video-quality-detect-record-snapshot-viewer>
  </div>
</template>

<script>

import VideoQualityDetectRecordSnapshotViewer
  from "pages/videoqualitydetect/record/VideoQualityDetectRecordSnapshotViewer";

export default {
  name: "VideoQualityDetectRecordList",
  components: {VideoQualityDetectRecordSnapshotViewer},
  data() {
    return {
      data: [],
      columns: [
        {name: 'index', field: 'index', label: '#', align: 'left'},
        {name: 'arithmeticName', field: 'arithmeticName', label: '算法名称', align: 'left'},
        {name: 'deviceId', field: 'deviceId', label: '设备ID', align: 'left'},
        {name: 'channelId', field: 'channelId', label: '通道ID', align: 'left'},
        {
          name: 'hasError',
          field: 'hasError',
          label: '检测是否成功',
          align: 'left',
          format: (val, row) => val === 0 ? '是' : '否'
        },
        {
          name: 'hasQualityError',
          field: 'hasQualityError',
          label: '是否质量异常',
          align: 'left',
          format: (val, row) => val === 1 ? '是' : '否'
        },
        {name: 'createdAt', field: 'createdAt', label: '创建时间', align: 'left'},
        {name: 'operations', field: 'operations', label: '操作', align: 'left'}
      ],
      refreshInterval: null,
      currentRecord: null,
      showSnapshotView: false,
      pagination: {
        page: 1,
        pageSize: 15,
        pageSizes: [10, 15, 20, 30, 40, 50],
        total: 0
      },
      searchParams: {
        arithmeticName: null,
        deviceId: null,
        channelId: null,
        hasQualityError: null
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
      app.$axios.get('/api/v1/video-quality-detect/arithmetic/record/page', {
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
              message: '检测记录列表加载失败: ' + res.data.message
            });
          }
        })
        .catch(e => {
          app.$q.notify({
            type: 'negative',
            position: 'top',
            message: '检测记录列表加载异常. ' + e
          });
        })
    },
    refreshClick() {
      this.queryList();
    },
    resetClick() {
      this.searchParams = {
        arithmeticName: null,
        deviceId: null,
        channelId: null,
        hasQualityError: null
      };
      this.queryList();
    },
    onSnapshotClick(data) {
      this.currentRecord = data;
      this.showSnapshotView = true;
    },
    onSnapshotViewClose() {
      this.showSnapshotView = false;
      this.currentRecord = null;
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
