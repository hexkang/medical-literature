<template>
  <div >
    <div class="line"></div>
    <el-menu
    :default-active="activeIndex"
    mode="horizontal"
    router
    @select="handleSelect"
    text-color="#121212"
    active-text-color="#6EAEC2">
        <el-menu-item >
          <img src="@/assets/logo.png" alt="" style="width: 100%;height: 95%;">
        </el-menu-item>
        <el-menu-item index="/home" >首页</el-menu-item>
        <el-menu-item index="/sendFile" >上传文件</el-menu-item>
        <el-menu-item index="/userDetail" v-show="$route.path=='/userDetail'">文件详情</el-menu-item>
        <el-menu-item index="/searchDetail" v-show="$route.path=='/searchDetail'">文件详情</el-menu-item>
        <el-menu-item index="/about" >关于</el-menu-item>
        <el-menu-item  
        style="float: right;font-size: 18px;color:#409eff; " @click.native="login" v-if="!this.name" >
          登录/注册</el-menu-item>
          <el-menu-item  
        style="float: right;font-size: 18px;color:#409eff; "  index="/person"  v-else>
          {{this.name}}</el-menu-item>
    </el-menu>
    <Login></Login>
    <Register></Register>
    <findPass></findPass>
  </div>
</template>

<script>

export default {
    name:'topNav',
    data() {
      return {
        name:'',
        activeIndex: this.$route.path,
        outerVisible: false
      };
    },
    methods: {
      login(){
        this.activeIndex = "/home"
        if(!this.name){
          this.$bus.$emit('login')
        }
      },
      handleSelect (index) {
        this.activeIndex = index 
        if(!index){
          this.$router.push('/home')
          this.activeIndex = "/home"
        }
      }
    },
    watch:{
      $route(){
        this.handleSelect(this.$route.path)
      }
    },
    mounted(){
      this.$bus.$on('userName',(name)=>{
        this.name = name
      })
      this.name =  sessionStorage.getItem('userName')
    }
}
</script>

<style >

</style>