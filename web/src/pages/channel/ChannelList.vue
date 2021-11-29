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
            v-if="deviceType !== 'GB'"
            @click="addClick"
          />
        </div>
        <div>
          <el-input
            size="mini"
            placeholder="通道ID"
            clearable
            style="width: 100px; margin-right: 5px"
            v-model="searchParams.id">
          </el-input>
          <el-input
            size="mini"
            placeholder="通道名称"
            clearable
            style="width: 100px; margin-right: 5px"
            v-model="searchParams.name">
          </el-input>
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
            style="margin-right: 5px"
            @click="resetClick"
          />
          <q-btn
            color="primary"
            size="sm"
            outline
            label="返回设备列表"
            @click="backClick"
          />
        </div>
      </template>
      <template v-slot:body-cell-operations="props">
        <q-td :props="props">
          <q-btn
            v-if="props.row.parental === 1"
            color="primary"
            size="xs"
            label="子目录"
            style="margin-right: 5px"
          />
          <q-btn
            v-if="props.row.parental === 0 && props.row.status === 'ON'"
            color="secondary"
            size="xs"
            label="预览"
            style="margin-right: 5px"
            @click="playClick(props.row.id)"
          />
          <q-btn
            color="brown-5"
            size="xs"
            label="编辑"
            style="margin-right: 5px"
            @click="editClick(props.row)"
          />
          <q-btn
            color="red"
            size="xs"
            label="删除"
            @click="removeClick(props.row)"
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
    <stream-view v-if="showStreamView" :device-id="deviceId" :channel-id="currentChannelId"
                 @close="streamViewClose"></stream-view>
    <channel-form v-if="showForm" :form-data="formData" @close="formClose" @success="formSuccess"></channel-form>
    <channel-remove-confirm v-if="showConfirm" :data="removeData" @close="removeClose"
                            @success="removeSuccess"></channel-remove-confirm>
  </div>
</template>

<script>
import StreamView from "pages/channel/StreamViewer";
import ChannelForm from "pages/channel/ChannelForm";
import ChannelRemoveConfirm from "pages/channel/ChannelRemoveConfirm";

export default {
  name: "ChannelList",
  components: {ChannelRemoveConfirm, ChannelForm, StreamView},
  data() {
    return {
      data: [],
      currentChannelId: null,
      showStreamView: false,
      refreshInterval: null,
      showForm: false,
      formData: null,
      showConfirm: false,
      removeData: null,
      pagination: {
        page: 1,
        pageSize: 15,
        pageSizes: [10, 15, 20, 30, 40, 50],
        total: 0
      },
      searchParams: {
        id: null,
        name: null
      }
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
      const app = this;
      return '设备[' + app.deviceId + '] - 通道列表';
    },
    columns() {
      const deviceType = this.deviceType;
      if (deviceType === 'RTSP') {
        return [
          {name: 'index', field: 'index', label: '#', align: 'left'},
          {name: 'id', field: 'id', label: '通道ID', align: 'left'},
          {name: 'name', field: 'name', label: '名称', align: 'left'},
          {name: 'manufacturer', field: 'manufacturer', label: '生产商', align: 'left'},
          {name: 'model', field: 'model', label: '型号', align: 'left'},
          {name: 'rtsp', field: 'rtsp', label: 'rtsp', align: 'left'},
          {
            name: 'online',
            field: 'online',
            label: '状态',
            align: 'left',
            format: (val, row) => val === 'ONLINE' ? '在线' : '离线'
          },
          {name: 'operations', field: 'operations', label: '操作', align: 'left'}
        ]
      } else {
        return [
          {name: 'index', field: 'index', label: '#', align: 'left'},
          {name: 'id', field: 'id', label: '通道ID', align: 'left'},
          {name: 'name', field: 'name', label: '名称', align: 'left'},
          {name: 'manufacturer', field: 'manufacturer', label: '生产商', align: 'left'},
          {name: 'model', field: 'model', label: '型号', align: 'left'},
          {name: 'civilCode', field: 'civilCode', label: '行政区域', align: 'left'},
          {
            name: 'parental',
            field: 'parental',
            label: '是否有子目录',
            align: 'left',
            format: (val, row) => val === 1 ? '是' : '否'
          },
          {
            name: 'status',
            field: 'status',
            label: '状态',
            align: 'left',
            format: (val, row) => val === 'ON' ? '在线' : '离线'
          },
          {name: 'operations', field: 'operations', label: '操作', align: 'left'}
        ]
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
      app.$axios.get('/api/v1/channel/page', {
        params: {
          deviceId: app.deviceId,
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
              message: '通道列表加载失败: ' + res.data.message
            });
          }
        })
        .catch(e => {
          app.$q.notify({
            type: 'negative',
            position: 'top',
            message: '通道列表加载异常. ' + e
          });
        })
    },
    backClick() {
      this.$router.push({path: '/device/list'})
    },
    refreshClick() {
      this.queryList();
    },
    resetClick() {
      this.searchParams = {
        id: null,
        name: null
      };
      this.queryList();
    },
    playClick(channelId) {
      this.currentChannelId = channelId;
      this.showStreamView = true;
    },
    streamViewClose() {
      this.showStreamView = false;
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
