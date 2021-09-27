<template>
  <div class="main_content">
    <q-card style="width: 100%; height: 100%">
      <q-card-section>
        <div class="row" style="width: 100%;font-size: 18px;">
          <div>请上传需要检测的图片</div>
        </div>
      </q-card-section>
      <q-card-section>
        <el-upload
          class="avatar-uploader"
          :action="imageUploadUrl"
          :show-file-list="false"
          :multiple="false"
          :on-success="onImageUploadSuccess"
          :before-upload="onBeforeUpload"
          accept=".jpg,.png">
          <img v-if="imageUrl" :src="imageUrl" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
      </q-card-section>
      <q-card-section v-if="detectRes">
        <div class="row" style="width: 100%;font-size: 18px;font-weight: bold">
          <div>检测结果:&nbsp;&nbsp;&nbsp;&nbsp;</div>
          <div style="color: red" v-if="detectRes.hasQualityError">{{detectRes.qualityError}}</div>
          <div style="color: green" v-else>正常</div>
        </div>
      </q-card-section>
    </q-card>
  </div>
</template>

<script>
export default {
  name: "VideoQualityDetectManualView",
  data() {
    return {
      imageUrl: null,
      detectRes: null,
      loading: null
    }
  },
  computed: {
    imageUploadUrl() {
      return this.$baseUrl + '/api/v1/video-quality-detect/manual/upload';
    }
  },
  methods: {
    onImageUploadSuccess(res, file) {
      this.imageUrl = URL.createObjectURL(file.raw);
      this.detectRes = res.data;
      this.loading.close();
      this.loading = null;
    },
    onBeforeUpload(file) {
      this.detectRes = null;
      this.loading = this.$loading({
        lock: true,
        text: '图片上传检测中',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
    }
  }
}
</script>

<style>

.avatar-uploader .el-upload {
  border: 2px dashed #606266;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 60px;
  color: #606266;
  width: 600px;
  height: 600px;
  line-height: 600px;
  text-align: center;
}

.avatar {
  width: 600px;
  height: 600px;
  display: block;
}

</style>
