<template>
    <div>
        <el-container class="searchDetail">
            <el-header class="searchTop" height =''>
                <div class="title">{{ title }}</div>
                <div class="btnGroup">
                    <el-button type="danger" @click="fileDownload(title, id)">下载</el-button>
                    <el-button @click="likeClick" class="iconfont icon-dianzan" :class="like ? 'active' : ''"></el-button>
                    <el-button  @click="unlikeClick" class="iconfont icon-cai" :class="unlike ? 'active' : ''"></el-button>
                </div>
            </el-header>
            <el-main class="keyPage">
                <el-container v-for="(pageItem, index) in keyPage" :key="index" class="pageItem">
                    <el-header class="page" height="">
                        页数：{{ pageItem.page }} 
                        <el-button icon="el-icon-view" @click="preView(pageItem.page)">预览</el-button>
                    </el-header>
                    <el-main class="content">
                        <div v-for="(item, index) in pageItem.content" :key="index" class="contentItem">
                            具体段落：{{ item }}
                        </div>
                    </el-main>
                </el-container>
            </el-main>
        </el-container>
    </div>
</template>

<script>
import {ReqLikeHandle} from '@/api'
import { mapState } from 'vuex'
import { Message } from 'element-ui'
import { tokenCheck } from '@/utils'
export default {
    name:'searchDetail',
    data(){
        return{
            like:false,
            unlike:false,
            id:this.$route.params.id,
            title:this.$route.params.title,
            likeFlag:this.$route.params.likeFlag,
            keyWord:this.$route.params.keyWord,
            centerDialogVisible: false,
            showUrl:''
        }
    },
    computed:{
        ...mapState({
            keyPage:state=>state.Detail.keyPage,
            fileUrl:state => state.Detail.serachUrl,
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
        },
        async preView(page){
            
            this.showUrl = this.fileUrl + '#page='+page
            let a = document.createElement('a')
            a.style.display = 'none'
            a.target = '_blank'
            a.href = this.showUrl;
            a.click()
        }
    },
    mounted(){
        if(!sessionStorage.getItem('userName')){
            Message('请登录账号')
            this.$router.push('./home')
        }
       
        if(this.id){
            sessionStorage.setItem('keyId',this.id)
            sessionStorage.setItem('title',this.title)
            sessionStorage.setItem('likeFlag',this.likeFlag)
            sessionStorage.setItem('keyWord',this.keyWord)
        }else{
            this.id = sessionStorage.getItem('keyId')
            this.title = sessionStorage.getItem('title')
            this.likeFlag = sessionStorage.getItem('likeFlag')
            this.keyWord = sessionStorage.getItem('keyWord')
        }
        if(this.likeFlag == 1){
            this.like = true
        }else if(this.likeFlag == -1){
            this.unlike = true
        }
        this.$store.dispatch('getSerachDetail',{id:this.id,keyWord:this.keyWord})
    }
}
</script>

<style lang="less">
.searchDetail{
    margin-top: 10px;
    border: 1px solid #409eff;
    flex-direction: column;
    color: #121212;
    .searchTop{
        .title{
            text-align: center;
            font-size: 38px;
            height: 60px;
            line-height: 60px;
        }
        .btnGroup{
            .el-button{
                float: left;
            }
            .el-button:nth-child(1){
                float: right;
            }
            .active{
                background-color: skyblue;
            }
        }
    }
    .pageItem{
        color: #121212;
        box-sizing: border-box;
        border: 1px solid #eee;
        margin: 10px 10px 10px 10px;
        .page{
            margin-top:5px ;
            font-size: 22px;
            height: 40px;
            line-height: 40px;
            .el-button{
                font-size: 16px;
                float: right;
            }
        }
        .content{
            .contentItem{
                padding-bottom: 5px;
                line-height: 40px;
                font-size: 16px;
            }
        }
    }
}
</style>