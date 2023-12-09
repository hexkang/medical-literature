<template>
	<view>
		<view class="pageList">
			<header class="title">
				{{title}}
				<view class="btnGroup">
					<button class="iconfont icon-dianzan" @click="likeClick()"
						:class="likeFlag==1?'active':''"></button>
					<button class="iconfont icon-cai" @click="unlikeClick()"
						:class="likeFlag==-1?'active':''"></button>
				</view>
			</header>
			<view class="pageItem" v-for="(pageItem,index) in pageList" :key="pageItem.page">
				<header>
					<text class="page">页码：{{pageItem.page}}</text>
					<view class="preview">
						<button type="primary" @click="goDetail(id,pageItem.page)">预览</button>
					</view>
				</header>
				<hr>
				<br>
				<main >
					<text v-for="(item) in pageItem.content" class="content">内容：{{ item }}</text>
				</main>
			</view>
		</view>
	</view>
</template>

<script>
	import {ReqGetDetail,ReqCheck,ReqLikeHandle,ReqGetUrl} from '../../utils/request/index.js'
	export default {
		data() {
			return {
				id:0,
				title:'',
				likeFlag:0,
				pageList:[]
			}
		},
		methods: {
			async checkUser() {
				let self = this
				let token = uni.getStorageSync('token')
				if (token) {
					return true
				} else {
					self.$refs.uToast.show({
						title: '请登录账号',
						type: 'warning'
					})
					return false
				}
			},
			async likeClick() {
				let self = this
				let res = await this.checkUser()
				if (res) {
					if (this.likeFlag == 1) {
						this.likeFlag = 0
						ReqLikeHandle(self.id, uni.getStorageSync('userName'),-1)
					} else {
						if (this.likeFlag == -1) {
							ReqLikeHandle(self.id,uni.getStorageSync('userName'), 2)
						} else {
							ReqLikeHandle(self.id,uni.getStorageSync('userName'), 1)
						}
						this.likeFlag = 1
					}
				}
			},
			async unlikeClick() {
				let self = this
				let res = await this.checkUser()
				if (res) {
					if (this.likeFlag == -1) {
						this.likeFlag = 0
						ReqLikeHandle(self.id,uni.getStorageSync('userName'), 1)
					} else {
						if (this.likeFlag == 1) {
							ReqLikeHandle(self.id, uni.getStorageSync('userName'),-2)
						} else {
							ReqLikeHandle(self.id, uni.getStorageSync('userName'),-1)
						}
						this.likeFlag = -1
					}
				}
			},
			async goDetail(id,page) {
				let res = await ReqGetUrl(id)
				let Url = res.data.url
				uni.request({
					url: Url,
					responseType: 'arraybuffer', //这里记得设置响应数据格式，不然预览的pdf是空白
					success: (response) => {
						if (!response) {
							uni.showToast({
								title: "协议预览失败",
								duration: 2000
							});
						}
						let pdfData = response.data; //pdfData是后端返回的文件流
						let blob = new Blob([pdfData], {
							type: 'application/pdf;charset=UTF-8'
						})
						pdfData = window.URL.createObjectURL(blob) //创建预览路径
						this.agreementUrl = encodeURIComponent(pdfData)
						uni.navigateTo({
							url: '/pages/Detail/Detail?url=' + this.agreementUrl + '#page=' +page
						}) //这里的url指向你刚创建的filePreviwe.vue页面
					},
					fail: err => {
						console.log(err)
					}
				})
			},
			async getKeyPage(id,keyWord){
				let res = await ReqGetDetail(id,keyWord)
				this.pageList = res.data.keyPage
			}
		},
		onLoad:function(options) {
			this.id=options.id
			this.title = options.title
			this.likeFlag = options.likeFlag
			this.keyWord = options.keyWord
			this.getKeyPage(this.id,this.keyWord)
		}
	}
</script>

<style lang="scss">
.pageList{
	margin: 10rpx;
	overflow: hidden;
	.title{
		text-align: center;
		font-size: 50rpx;
		line-height: 100rpx;
		display: block;
	}
	.btnGroup{
		height: 100rpx;
		button {
			width: 125rpx;
			height: 60rpx;
			margin: 8rpx;
			font-size: 24rpx;
			display: inline-block;
			text-align: center;
		}
	}
	
	.pageItem{
		margin: 5px;
		border: 1px solid #eee;
		position: relative;
		.preview{
			button {
				width: 125rpx;
				height: 60rpx;
				font-size: 24rpx;
				position: absolute;
				top:10rpx;
				right: 10rpx;
			}
		}
		.page{
			height: 80rpx;
			line-height: 80rpx;
			font-size: 40rpx;
		}
		.content{
			line-height: 45rpx;
			font-size: 28rpx;
			display: block;
		}
	}
	.active {
		background-color: skyblue;
	}
}
</style>
