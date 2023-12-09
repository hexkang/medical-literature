<template>
	<view class="index">

		<view class="search">
			<uni-search-bar @confirm="search" :focus="true" v-model="searchValue" @clear="clear">
			</uni-search-bar>
		</view>


		<view class="paperList">
			<view class="paperItem" v-for="(item,index) in fileList" :key="item.id">
				<header>
					<text class="tit">{{item.title}}</text>
					<text>作者：{{item.authors}}</text>
				</header>
				<main>
					<text>{{ item.content }}</text>
				</main>
				<footer>
					<text> 得分情况：总分：{{item.score.relevance+item.score.rate+item.score.like}}
						内容相关性：{{ item.score.relevance }} 文献点击率：{{item.score.rate}} 用户反馈：{{item.score.like}}</text>
					<button class="iconfont icon-dianzan" @click="likeClick(index,item.id)"
						:class="like[index]?'active':''"></button>
					<button class="iconfont icon-cai" @click="unlikeClick(index,item.id)"
						:class="unLike[index]?'active':''"></button>
					<button type="warn" @click="download(item.id,item.title)">下载</button>
					<button type="primary" @click="goKeyPage(item.id,item.title,index)">预览</button>
				</footer>
			</view>
			<u-toast ref="uToast"></u-toast>
		</view>

		<view class="isOver" v-if="flag">
			------已显示全部------
		</view>
		<u-back-top :scroll-top="scrollTop" top="0"></u-back-top>
	</view>
</template>

<script>
	import {
		ReqGetFileList,
		ReqLikeHandle,
		ReqCheck,
		ReqGetUrl,
		ReqDownload
	} from '../../utils/request/index.js'
	export default {
		data() {
			return {
				searchValue: '',
				like: [],
				unLike: [],
				fileList: [],
				pageIndex: 1,
				total: 0,
				flag: false,
				searchKey: '',
				scrollTop: 0
			}
		},
		onPageScroll(e) {
			this.scrollTop = e.scrollTop;
		},
		methods: {
			async search(res) {
				this.searchKey = res.value
				this.fileList = []
				this.like = []
				this.unLike = []
				uni.setStorageSync('searchVal', this.searchValue)
				let token = uni.getStorageSync('token')
				if (token) {
					this.getFileList(this.searchKey, 1, uni.getStorageSync('userName'))
				} else {
					uni.showToast({
						title: '请登录账号：',
						icon: 'none'
					})
				}

			},
			clear(res) {
				res.value = ''
			},
			async likeClick(index, id) {
				let res = await this.checkUser()
				if (res) {
					if (this.like[index]) {
						this.like.splice(index, 1, 0)
						ReqLikeHandle(id, uni.getStorageSync('userName'), -1)
					} else {
						this.like.splice(index, 1, 1)
						if (this.unLike[index]) {
							this.unLike.splice(index, 1, 0)
							ReqLikeHandle(id, uni.getStorageSync('userName'), 2)
						} else {
							ReqLikeHandle(id, uni.getStorageSync('userName'), 1)
						}
					}
				} else {
					uni.showToast({
						title: '请登录账号：',
						icon: 'none'
					})
				}
			},
			async unlikeClick(index, id) {
				let res = await this.checkUser()
				if (res) {
					if (this.unLike[index]) {
						this.unLike.splice(index, 1, 0)
						ReqLikeHandle(id, uni.getStorageSync('userName'), 1)
					} else {
						this.unLike.splice(index, 1, 1)
						if (this.like[index]) {
							this.like.splice(index, 1, 0)
							ReqLikeHandle(id, uni.getStorageSync('userName'), -2)
						} else {
							ReqLikeHandle(id, uni.getStorageSync('userName'), -1)
						}
					}
				} else {
					uni.showToast({
						title: '请登录账号',
						icon: 'none'
					})
				}
			},
			async goDetail(id) {
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
							url: '/pages/Detail/Detail?url=' + this.agreementUrl + '#page=1'
						}) //这里的url指向你刚创建的filePreviwe.vue页面
					},
					fail: err => {
						console.log(err)
					}
				})
			},
			async download(id, title) {
				let res = await ReqDownload(id)
				var blob = new Blob([res]);
				var url = window.URL.createObjectURL(blob);
				var a = document.createElement("a");
				a.style.display = 'none'
				a.target = '_blank'
				document.body.appendChild(a)
				a.href = url;
				a.download = title + '.pdf';
				a.click();
			},
			goKeyPage(id, title, index) {
				if (this.searchKey) {
					let likeFlag = 0
					if (this.like[index]) {
						likeFlag = 1
					}
					if (this.unLike[index]) {
						likeFlag = -1
					}
					uni.navigateTo({
						url: `/pages/keyPage/keyPage?id=${id}&title=${title}&likeFlag=${likeFlag}&keyWord=${this.searchKey}`,
					})
				} else {
					this.goDetail(id)
				}

			},
			async getFileList(keyword, page) {
				let self = this
				let res = await ReqGetFileList(keyword, page, uni.getStorageSync('userName') || '')
				if (res.code == 0) {
					for (let i = 0; i < res.data.thesisList.length; i++) {
						self.fileList.push(res.data.thesisList[i])
						if (res.data.thesisList[i].like == 1) {
							self.like.push(1)
							self.unLike.push(0)
						} else if (res.data.thesisList[i].like == -1) {
							self.like.push(0)
							self.unLike.push(1)
						} else {
							self.like.push(0)
							self.unLike.push(0)
						}
					}
					self.total = res.data.total
				}
			},

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
			}
		},
		onBackPress() {
			// #ifdef APP-PLUS
			plus.key.hideSoftKeybord();
			// #endif
		},
		onHide() {
			this.searchValue = '',
				this.like = [],
				this.unLike = [],
				this.fileList = [],
				this.pageIndex = 1,
				this.total = 0,
				this.flag = false,
				this.searchKey = '',
				this.scrollTop = 0
		},
		onShow() {
			this.searchValue = uni.getStorageSync('searchVal')
			this.getFileList(this.searchValue || '', 1, uni.getStorageSync('userName') || undefined)
			// window.onbeforeunload = function (e) {
			// 	var storage = window.localStorage;
			// 	storage.clear()
			//   }
		},
		onReachBottom() {
			if (this.fileList.length == this.total) {
				return this.flag = true
			}
			this.pageIndex++,
				this.getFileList(this.searchKey, this.pageIndex)
		},
	}
</script>

<style lang="scss">
	.index {
		.paperList {
			.paperItem {
				border: 1px solid #eee;
				margin: 5px;

				header {
					line-height: 40rpx;
					font-size: 30rpx;
					text-align: center;
					margin-bottom: 10rpx;
					color: #808080;

					text {
						display: block;
					}

					.tit {
						font-size: 36rpx;
						line-height: 60rpx;
						color: #121212;
					}
				}

				main {
					line-height: 40rpx;
					font-size: 28rpx;
				}

				footer {
					margin: 10rpx 0;
					height: 150rpx;

					text {
						display: block;
						font-size: 26rpx;
						line-height: 40rpx;
					}

					.iconfont {
						float: left;
					}

					.active {
						background-color: skyblue;
					}

					button {
						width: 125rpx;
						height: 60rpx;
						margin: 8rpx;
						font-size: 24rpx;
						display: inline-block;
						float: right;
					}
				}
			}
		}

		.isOver {
			width: 100%;
			height: 100rpx;
			line-height: 100rpx;
			text-align: center;
			background-color: #eee;
			font-size: 32rpx;
		}
	}
</style>