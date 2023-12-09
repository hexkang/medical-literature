<template>
    <el-container class="uploadFile">
      <el-main>
        <el-upload 
        class="upload-demo"
        drag
        ref="upload"
        :action="uploadAction"
        :auto-upload="autoUpload"
        :on-remove="handleRemove"
        :on-change="beforeUpload"
        :on-preview="Preview"
        :data="extraParam"
        :headers="headers"
        :limit="fileSizeLimit"
        :file-list="fileList"
        :on-exceed="handleExceed"
        multiple
        accept=".pdf">
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
          <div class="el-upload__tip" slot="tip">只能上传pdf文件</div>
        </el-upload>
        
      </el-main>
      <el-footer>
        <el-button type="primary" @click="submitUpload">上传</el-button>
        <el-button type="primary" @click="delFile">重置</el-button>
      </el-footer>
    </el-container>
  </template>
  
  <script>
  import { Message } from 'element-ui';
  import axios from 'axios';
  export default{
    name: 'FileUpload',
    data() {
      return {
        promptMessage: '只能上传xls/xlsx文件',
        /* 文件上传路径 */
        // uploadAction: 'http://localhost:8081/api/uploadFile',
        uploadAction: '/api/uploadFile',
        /* 文件类型 */
        fileType: '.pdf',
        /* 限制最大文件上传数 */
        fileSizeLimit:  5,
        /* 上传时附带的额外参数，返回是一个对象 */
        extraParam: {},
        /* 已上传的文件列表 */
        fileList: [],
        /* 请求头 */
        headers: {
          'strainpreservation': this.getToken()
        },
        /* 是否在选取文件后立即进行上传 */
        autoUpload: false,
      }
    },
    methods:{
      getToken(){
        return sessionStorage.getItem('token')
      },
      /* 文件列表移除文件成功时的钩子 */
      handleRemove(file, fileList) {
        this.fileList = fileList
        return Message(`已成功移除"${file.name}"文件`)
      },
      /* 上传文件之前的钩子 因设置了auto-upload为false，如果使用before-upload，虽有提示，但是还是会请求服务器*/
      beforeUpload(file,fileList) {
        let token = this.getToken()
        if(!token){
          fileList.pop()
          return Message('请登录账号')
        }
        // 1、判断文件名是否重复，不允许上传相同文件
        let existFile = fileList.slice(0, fileList.length - 1).find(f => f.name === file.name)
        if(existFile){
          fileList.pop()
          this.Message(file.name+" 文件已存在！")
        }
        // 2、获取文件后缀
        fileList.forEach(everyFile => {
          const fileType = everyFile.name.substring(everyFile.name.lastIndexOf('.'))
          if(this.fileType.search(fileType) === -1){
             fileList.pop()
             Message({message:"上传文件的类型不正确"+"文件类型必须为" + this.fileType + '',type:'error'})
          }
        })
        this.fileList = fileList;
      },
      /* 文件超出个数限制时的钩子 */
      handleExceed(files, fileList) {
        Message({message:`当前限制选择`+ this.fileSizeLimit +`个文件，本次选择了 ${files.length} 个文件，已超出了文件最大上传个数`,type:'error'})
      },
      /* 确定上传 */
      submitUpload() {
        if(this.fileList.length == 0){
          Message('请添加文件')
        }else{
          this.upload(this.uploadAction,this.fileList).then(res => {
            this.fileList = []
          });
        }
      },
      upload(api, file) {
          let files = new FormData()
          file.forEach((everyFile) => {
            files.append("files",everyFile)
          })
          let raxios = axios.create()
          raxios.interceptors.request.use(config=>{
            console.log(config)
          })
          return raxios.post(api, files,{headers: {
            'Content-Type': 'multipart/form-data'
          }}).then(res=>{
            if(res.data.code == 0){
              Message({message:'上传成功',type:'success'})
            }else{
              Message({message:'文件上传失败1',type:"error"})
            }
          }).catch(e=>{console.log(e);Message({message:'文件上传失败2',type:"error"})})
      },
      delFile(){
        this.fileList = []
      },
      Preview(file){
        let url = URL.createObjectURL(file.raw)
        window.open(url, "_blank");
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
  
    .el-footer {
      text-align: center;
    }
  }
  </style>