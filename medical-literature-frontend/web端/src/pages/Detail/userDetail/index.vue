<template>
    <el-container class="userDetail">
        <el-main>
            <iframe ref="mainiframe"  :src="url" style="height:100%;width:100%" ></iframe>
        </el-main>
        <el-footer>
            <el-button type="danger" @click="fileDownload(title, id)">下载</el-button>
            <el-button  @click="unlikeClick" class="iconfont icon-cai" :class="unlike ? 'active' : ''"></el-button>
            <el-button @click="likeClick" class="iconfont icon-dianzan" :class="like ? 'active' : ''"></el-button>
        </el-footer>
    </el-container>
</template>

<script>
import {ReqLikeHandle} from '@/api'
import { mapState } from 'vuex'
import { Message } from 'element-ui'
import { tokenCheck } from '@/utils'
export default {
    name:'userDetail',
    data(){
        return{
            like:false,
            unlike:false,
            id:this.$route.params.id,
            title:this.$route.params.title,
            likeFlag:this.$route.params.likeFlag
        }
    },
    computed:{
      ...mapState({
        url:state => state.Detail.likeUrl,
      })  
    },
    methods:{
        async likeClick(){
            let flag =  await tokenCheck()
            if(flag){
                if(this.like){
                    this.like = false
                    ReqLikeHandle(this.id,sessionStorage.getItem('userName'),-1)
                }else{
                    this.like = true
                    if(this.unlike){
                        this.unlike = false
                        ReqLikeHandle(this.id,sessionStorage.getItem('userName'),2)
                    }else{
                        ReqLikeHandle(this.id,sessionStorage.getItem('userName'),1)
                    }
                }
            }else{
                Message('请登录账号')
            }
        },
        async unlikeClick(){
            let flag =  await tokenCheck()
            if(flag){
                if(this.unlike){
                    this.unlike = false
                    ReqLikeHandle(this.id,sessionStorage.getItem('userName'),1)
                }else{
                    this.unlike = true
                    if(this.like){
                        this.like = false
                        ReqLikeHandle(this.id,sessionStorage.getItem('userName'),-2)
                    }else{
                        ReqLikeHandle(this.id,sessionStorage.getItem('userName'),-1)
                    }
                }
            }else{
                Message('请登录账号')
            }
        }
        
    },
    mounted(){
        if(!sessionStorage.getItem('userName')){
            Message('请登录账号')
            this.$router.push('./home')
        }
        this.$store.dispatch('getLikeDetail',{id:this.id})
        if(this.likeFlag == 1){
            this.like = true
        }else if(this.likeFlag == -1){
            this.unlike = true
        }
    }
}
</script>

<style lang="less">
.userDetail{
    margin-top: 10px;
    .el-main{
        height: 950px;
        overflow: hidden;
    }
    .el-footer{
        .el-button{
            float: right;
        }
        .el-button:nth-child(1){
            float: left;
            margin-left: 45%;
        }
        .active{
            background-color: skyblue;
        }
    }
}
</style>