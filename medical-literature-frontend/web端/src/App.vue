<template>
    <div class="bg">
      <div id="app">
          <topNav v-show="$route.meta.isShow" class="topNav"></topNav>
          <router-view class="main"></router-view>
      </div>
    </div>
</template>

<script>
import topNav from './components/topNav/topNav.vue';
import  sendFile  from './pages/sendFile/index.vue';

export default {
  name: 'App',
  components: {
    topNav,
    sendFile
  }
}
</script>

<style lang="less">
@import url('//at.alicdn.com/t/c/font_3869234_wc7gn65vgyj.css');
*{
  margin: 0;
  padding: 0;
}

body{
  font-family: 'ADR';
  height: 100vh;
  position: relative;
  .bg{
    width: 100%;
    height: 100%;
    // background: skyblue;
    #app{
      position: relative;
      left: 20%;
      width: 60%;
      height: 100%;
      .clearfix:after {
        content: " ";
        display: block;
        clear: both;
      }
      .topNav{
        position:fixed;
        top:0;
        display: block;
        height: 5%;
        z-index: 10;
        width: 60%;
        display: block;
      }
      .main{
        position: relative;
        top: 8%;
      }
    }
  }
}

</style>
