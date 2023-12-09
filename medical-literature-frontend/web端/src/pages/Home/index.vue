<template>
    <div>
        <el-input v-model="input" placeholder="请输入内容" class="search">
            <el-button type="primary" icon="el-icon-search" slot="append" id="search" @click="searchFile">搜索</el-button>
        </el-input>
        <div class="tabs" v-if="searchParam === ''">
            <span>快捷导航：</span>
            <button round v-for="item in tabList" :key="item.name" class="tab">
                <a :href="item.url" target="_blank">
                    <img :src="item.imgSrc" alt="">
                    <span>{{ item.name }}</span>
                </a>
            </button>
            <button class="tab last-tab" disabled>
                <span>···</span>
            </button>
        </div>
        <thesis :fileList="fileList" :total="total" :searchParam="searchParam" v-if="searchParam != ''"></thesis>
        <el-container class="rank" v-else>
            <el-header class="rankTop" height="40px">
                <i class="el-icon-link"></i>
                <span>榜单</span>
            </el-header>
            <el-main class="rankMain">
                <el-container class="rankingList">
                    <el-header class="rankingName">
                        搜索热词榜
                    </el-header>
                    <el-main>
                        <span v-for="(item, index) in wordRank" :key="index" class="rankingWord">{{ index + 1 + '.' + item }}</span>
                    </el-main>
                </el-container>
                <el-container class="rankingList">
                    <el-header class="rankingName">
                        文献欢迎榜
                    </el-header>
                    <el-main>
                        <span v-for="(item, index) in thesisRank" :key="index"
                            class="rankingWord">{{ index + 1 + '.' + item }}</span>
                    </el-main>
                </el-container>
            </el-main>
        </el-container>
    </div>
</template>

<script>
import { mapState } from 'vuex'
import { tokenCheck } from '@/utils'
import { Message } from 'element-ui'
export default {
    name: 'Home',
    data() {
        return {
            input: '',
            searchParam: '',
            page: 1,
            tabList: [
                {
                    url: "https://www.dxy.cn/bbs/newweb/pc/home",
                    imgSrc: "https://img1.dxycdn.com/2019/0815/117/3362630565823635438-2.png",
                    name: "丁香园论坛"
                },
                {
                    url: "https://www.evdo.vip/web/",
                    imgSrc: "https://www.evdo.vip/web/top_logo_evdo.png",
                    name: "医维度"
                },
                {
                    url: "https://www.clinicalkey.com/#!/",
                    imgSrc: "https://cn-cdn.clinicalkey.com/ui-builds/ui-origin/master-2263/apple-touch-icon-114x114.png",
                    name: "clinicalkey"
                },
                {
                    url: "https://www.cnki.net/",
                    imgSrc: "https://ts3.cn.mm.bing.net/th?id=ODLS.a1cbf817-d191-4d81-9645-0a1b367e8930&w=32&h=32&qlt=90&pcl=fffffa&o=6&pid=1.2",
                    name: "中国知网"
                }
            ]
        }
    },
    computed: {
        ...mapState({
            fileList: state => state.Home.fileList,
            total: state => state.Home.total,
            wordRank: state => state.Rank.wordRank,
            thesisRank: state => state.Rank.thesisRank,
        })
    },
    methods: {
        async searchFile() {
            let flag = await tokenCheck()
            if (flag) {
                this.searchParam = this.input
                this.$store.dispatch('getFileList', { keyWord: this.searchParam, page: this.page, username: sessionStorage.getItem('userName') || '' })
            } else {
                Message('请登录账号')
            }
        }
    },
    mounted() {
        this.$store.dispatch('getRank')
        this.$on('changePage', (val) => {
            this.page = val
        })
    },
    beforeDestroy() {
        this.$store.dispatch('clearLike')
    }
}
</script>

<style lang="less">
.search {
    margin-top: 10px;

    #search {
        background: #409eff;
        color: #fff;
    }
}

.tabs {
    height: 30px;
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    margin: 1%;
    line-height: 30px;
    font-size: 20px;

    .tab {
        margin-left: 5px;
        margin-right: 13px;
        background: #fff;
        border: 0.5px solid #eee;
        box-sizing: border-box;
        padding-right: 8px;
        border-radius: 30px;
        height: 30px;
        display: flex;
        flex-direction: row;
        justify-content: center;

        &:hover {
            background-color: rgba(64, 158, 255, .5);
        }

        a {
            height: 30px;
            display: flex;
            flex-direction: row;
            justify-content: center;
            text-decoration: none;

            img {
                width: 28px;
                height: 28px;
                border-radius: 50%;
                margin-right: 2px;
            }

            span {
                font-family: "KT";
                line-height: 30px;
                font-weight: 600;
                font-size: 16px;
                color: #121212;
            }
        }
    }
    .last-tab{
        padding-left: 8px;
        border-radius: 50%;
        line-height: 30px;
        &:hover{
            background: none;
        }
    }
}

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

.rank {
    margin: 10px;

    .rankTop {
        font-size: 32px;
        line-height: 50px;
        color: #409eff;
    }

    .rankMain {
        flex-direction: row;
        display: flex;

        .rankingList {
            background: linear-gradient(90deg, #fff, hsla(22, 63%, 92%, .6));
            width: 45%;
            margin-right: 3%;
            font-size: 32px;
            color: skyblue;
            line-height: 60px;
            display: flex;
            flex-direction: column;

            box-shadow: 0 1px 2px -2px rgba(39, 66, 107, .1), 0 3px 6px 0 rgba(39, 66, 107, .1), 0 5px 12px 4px rgba(39, 66, 107, .1);

            .rankingWord {

                white-space: nowrap;
                /*设置文本超出元素宽度部分隐藏*/
                overflow-x: hidden;
                flex-shrink: 0;
                /*设置文本超出部分用省略号显示*/
                text-overflow: ellipsis;
                display: block;
                color: #121212;
                font-size: 20px;
                line-height: 40px;
            }
        }
    }
}</style>