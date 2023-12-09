<template>
  <el-container class="uploadFile">
    <el-main>
      <el-upload drag :action="uploadURL" 
      :file-list="fileList" 
      :auto-upload="true" 
      :on-change="handleChange"
      :before-upload = "handleCheck"
      :multiple="multiple" 
      :on-remove = "handleRemove"
      :on-success = "onSuccess"
      ref="upload"
      accept=".pdf">
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传pdf文件</div>
      </el-upload>
    </el-main>
  </el-container>
</template>

<script>
import { Message } from 'element-ui';
import { ReqCheck } from '@/api'
export default {
  name: 'sendFile',
  data() {
    return {
      fileList: [],
      multiple: true,
      uploadURL: 'http://43.139.73.93:8081/api/uploadFile'
    }
  },
  methods: {
    handleChange(file, fileList) {
        this.fileList = fileList;
    },
    handleRemove(file, fileList) {
        this.fileList = fileList;
    },
    onSuccess(response, file, fileList){
      if(!response){
        Message({
          message:'上传失败',
          type:'warning'
        })
      }else{
        if(response.code === 0){
          Message({
            message:'上传成功',
            type:'success'
          })
        }else{
          Message({
            message:'上传失败',
            type:'warning'
          })
        }
      }
    },
    handleCheck(){
      let token = sessionStorage.getItem('token')
      if(token){
        return true
      }else{
        Message('请登录账号')
        return false
      }
    }
  }
}
</script>

<style  lang="less">
.uploadFile {
  border: 2px solid #eee;
  margin-top: 10px;

  .el-upload-list__item {
    font-size: 20px;
  }

  .el-upload {
    display: block;
    margin-top: 20px;
    width: 100%;
    height: 300px;

    .el-upload-dragger {
      height: 100%;
      width: 100%;
    }
  }

  .el-icon-upload {
    display: block;
    padding-top: 50px;
  }

}
</style>