<template>
  <div>
    <el-container class="thesisItemContainer">
        <el-container class="thesisItem" v-for="(item, index) in fileList" :key="item.id">
            <el-header height="">
                <div>
                    <span class="title">{{ item.title }}</span>
                    <el-button @click="unlikeClick(index, item.id)" class="iconfont icon-cai"
                        :class="unLike[index] === 1 ? 'active' : ''"></el-button>
                    <el-button @click="likeClick(index, item.id)" class="iconfont icon-dianzan"
                        :class="like[index] === 1 ? 'active' : ''"></el-button>
                    <div class="info">
                        <span class="author">作者：{{ item.authors }}</span>
                    </div>
                </div>
            </el-header>
            <el-main>
                <span class="thesisItemContent">{{ item.content }}</span>
            </el-main>
            <el-footer height="">
                <span class="score">得分情况：
                    总分：{{item.score.relevance+item.score.rate+item.score.like}}
                     内容相关性：{{ item.score.relevance }}
                    文献点击率：{{ item.score.rate }}
                    用户反馈：{{ item.score.like }}</span>
                <el-button type="danger" @click="fileDownload(item.title, item.id)">下载</el-button>
                <el-button type="primary" @click="goDetail(item.title, item.id,index)">
                    预览</el-button>
            </el-footer>
        </el-container>
        <el-pagination background layout="prev, pager, next" :total="total" @current-change="changePage" :page-size="5" :current-page.sync="page">
        </el-pagination>
    </el-container>
  </div>
</template>

<script>
import { ReqLikeHandle } from '@/api'
import { Message } from 'element-ui'
import { tokenCheck } from '@/utils'
import { mapState } from 'vuex'
export default {
    props:{
        'likeFlag':{
            type:Number,
            default:0
        },
        'fileList':{
            type:Array
        },
        "searchParam":{
            type:String
        },
        "total":{
            type:Number
        }
    },
    name: 'thesis',
    data() {
        return {
            page: 1
        }
    },
    computed:{
        ...mapState({
            like:state => state.Home.like || new Array(5).fill(1),
            unLike:state => state.Home.unlike || new Array(5).fill(0)
        })
    },
    methods: {
        async likeClick(index, id) {
            let flag = await tokenCheck()
            if (flag) {
                if (this.like[index]) {
                    this.like.splice(index, 1, 0)
                    ReqLikeHandle(id, sessionStorage.getItem('userName'), -1)
                } else {
                    this.like.splice(index, 1, 1)
                    if (this.unLike[index]) {
                        this.unLike.splice(index, 1, 0)
                        ReqLikeHandle(id, sessionStorage.getItem('userName'), 2)
                    } else {
                        ReqLikeHandle(id, sessionStorage.getItem('userName'), 1)
                    }
                }
                this.$forceUpdate()
            } else {
                Message('请登录账号')
            }
        },
        async unlikeClick(index, id) {
            let flag = await tokenCheck()
            if (flag) {
                if (this.unLike[index]) {
                    this.unLike.splice(index, 1, 0)
                    ReqLikeHandle(id, sessionStorage.getItem('userName'), 1)
                } else {
                    this.unLike.splice(index, 1, 1)
                    if (this.like[index]) {
                        this.like.splice(index, 1, 0)
                        ReqLikeHandle(id, sessionStorage.getItem('userName'), -2)
                    } else {
                        ReqLikeHandle(id, sessionStorage.getItem('userName'), -1)
                    }
                }
                this.$forceUpdate()
            } else {
                Message('请登录账号')
            }
        },
        async goDetail(title,id,index) {
            let likeFlag = 0
            if(this.like[index]){
                likeFlag = 1
            }
            if(this.unLike[index]){
                likeFlag = -1
            }
            let flag = await tokenCheck()
            let modeFlag = this.likeFlag
            if (flag) {
                if(modeFlag || this.searchParam==''){
                    this.$router.push({name:'userDetail',params:{id,title,likeFlag}})
                }else{
                    this.$router.push({name:'searchDetail',params:{id,title,likeFlag,keyWord:this.searchParam}})
                }
            } else {
                Message('请登录账号')
            }
        },
        async changePage(val) {
            let self = this
            let flag = await tokenCheck()
            if (flag) {
                self.page = val
                this.$bus.$emit('changePage',val)
                if(this.likeFlag ==0){
                    this.$store.dispatch('getFileList', { keyWord: self.searchParam, page: val,username:sessionStorage.getItem('userName') })
                }else{
                    this.$store.dispatch('getLikeFileList', {username:sessionStorage.getItem('userName'),page:val})
                }
            } else {
                this.page = 1
                Message('请登录账号')
            }
        }
    },
    mounted(){
    }
}
</script>

<style lang="less">
.thesisItemContainer {
    margin-top: 10px;
    border: 1px solid #409eff;
    flex-direction: column;

    .thesisItem {
        color: #121212;
        box-sizing: border-box;
        border: 1px solid #eee;
        margin: 10px 10px 10px 10px;

        .el-header {
            min-height: 60px;
            line-height: 60px;
            flex-direction: row;

            .title {
                font-size: 22px;
            }

            .el-button {
                float: right;
                margin: 10px 10px;
            }

            .active {
                background-color: skyblue;
            }

            .info {
                line-height: 40px;

                .author {
                    margin-left: 15px;
                    font-size: 16px;
                    color: #161823;
                }

                .page {
                    margin-left: 30px;
                    font-size: 16px;
                    color: #161823;
                }
            }
        }

        .el-main {
            .thesisItemContent {
                line-height: 40px;
                font-size: 16px;
            }
        }

        .el-footer {
            padding: 10px 20px;

            .score {
                color: #808080;
                line-height: 40px;
            }

            .el-button {
                float: right;
                margin: 0 10px;
            }
        }
    }

    .el-pagination {
        margin: 0 auto 10px;
    }
}
</style>