<template>
  <div class="main_content">
    <q-table
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
            placeholder="设备ID"
            clearable
            style="width: 100px; margin-right: 5px"
            v-model="searchParams.id">
          </el-input>
          <el-input
            size="mini"
            placeholder="设备名称"
            clearable
            style="width: 100px; margin-right: 5px"
            v-model="searchParams.name">
          </el-input>
          <el-select
            v-model="searchParams.type"
            placeholder="设备类型"
            clearable
            size="mini"
            style="width: 100px; margin-right: 5px"
          >
            <el-option label="GB" value="GB"/>
            <el-option label="RTSP" value="RTSP"/>
          </el-select>
          <el-select
            v-model="searchParams.online"
            placeholder="在线状态"
            clearable
            size="mini"
            style="width: 100px; margin-right: 5px"
          >
            <el-option label="在线" :value="1"/>
            <el-option label="离线" :value="0"/>
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
            label="通道"
            @click="openChannelList(props.row)"
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
    <device-form v-if="showForm" :form-data="formData" @close="formClose" @success="formSuccess"></device-form>
    <device-remove-confirm v-if="showConfirm" :data="removeData" @close="removeClose"
                           @success="removeSuccess"></device-remove-confirm>
  </div>
</template>

<script>
import DeviceForm from "pages/device/DeviceForm";
import DeviceRemoveConfirm from "pages/device/DeviceRemoveConfirm";

export default {
  name: "DeviceList",
  components: {DeviceRemoveConfirm, DeviceForm},
  data() {
    return {
      data: [],
      columns: [
        {name: 'index', field: 'index', label: '#', align: 'left'},
        {name: 'id', field: 'id', label: '设备ID', align: 'left'},
        {name: 'type', field: 'type', label: '设备类型', align: 'left'},
        {name: 'name', field: 'name', label: '设备名称', align: 'left'},
        {name: 'manufacturer', field: 'manufacturer', label: '生产商', align: 'left'},
        {name: 'model', field: 'model', label: '型号', align: 'left'},
        {name: 'online', field: 'online', label: '状态', align: 'left', format: (val, row) => val === 1 ? '在线' : '离线'},
        // {name: 'createdAt', field: 'createdAt', label: '注册时间', align: 'left'},
        {name: 'lastKeepaliveAt', field: 'lastKeepaliveAt', label: '最后心跳', align: 'left'},
        {name: 'operations', field: 'operations', label: '操作', align: 'left'}
      ],
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
        name: null,
        type: null,
        online: null
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
      app.$axios.get('/api/v1/device/page', {
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
              message: '设备列表加载失败: ' + res.data.message
            });
          }
        })
        .catch(e => {
          app.$q.notify({
            type: 'negative',
            position: 'top',
            message: '设备列表加载异常. ' + e
          });
        })
    },
    openChannelList(device) {
      this.$router.push({path: '/channels/' + device.type + '/' + device.id})
    },
    refreshClick() {
      this.queryList();
    },
    resetClick() {
      this.searchParams = {
        id: null,
        name: null,
        online: null,
        type: null
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
