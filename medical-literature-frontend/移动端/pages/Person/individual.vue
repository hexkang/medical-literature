<template>
	<view class="container">
		<view class="userInfo">
			<view class="img">
				<image src="@/static/person-active.png" alt=""></image>
			</view>
			<view class="name">
				{{username}}
			</view>
			<button type="primary" @click="changePass">修改密码</button>
			<button type="danger" @click="out">退出登录</button>
		</view>
		<view>
			<u-modal :show="show" title="修改密码" :showCancelButton='true' :showConfirmButton="true" closeOnClickOverlay
				@confirm="confirmChangePass" @cancel="cancelChangePass">
				<view class="slot-content">
					<u--form labelPosition="left" :model="changeInfo" :rules="rules" ref="changePass" >
						<u-form-item label="原密码" prop="oldpass" borderBottom ref="item1" labelWidth="100px">
							<u--input v-model="changeInfo.oldpass" border="none" password clearable ></u--input>
						</u-form-item>
						<u-form-item label="新密码" prop="newpass" borderBottom ref="item1" labelWidth="100px">
							<u--input v-model="changeInfo.newpass" border="none"password clearable></u--input>
						</u-form-item>
						<u-form-item label="确认密码" prop="newpass" borderBottom ref="item1" labelWidth="100px">
							<u--input v-model="changeInfo.checkpass" border="none" password clearable></u--input>
						</u-form-item>
					</u--form>
				</view>
			</u-modal>
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
					<text> 得分情况：总分：{{item.score.relevance+item.score.rate+item.score.like}} 内容相关性：{{ item.score.relevance }} 文献点击率：{{item.score.rate}}
						用户反馈：{{item.score.like}}</text>
					<button class="iconfont icon-dianzan" @click="likeClick(index,item.id)"
						:class="like[index]?'active':''"></button>
					<button class="iconfont icon-cai" @click="unlikeClick(index,item.id)"
						:class="unLike[index]?'active':''"></button>
					<button type="warn" @click="download(item.id,item.title)">下载</button>
					<button type="primary" @click="goDetail(item.id,item.title)">预览</button>
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
		ReqGetLikeFileList,
		ReqLikeHandle,
		ReqCheck,
		ReqGetUrl,
		ReqDownload,
		ReqChangePass
	} from '@/utils/request/index.js'
	export default {
		name: 'individual',
		props: ['username'],
		data() {
			return {
				passReg : /^(?=.*\d)(?=.*[a-zA-Z])(?=.*[^\da-zA-Z\s]).{6,}$/,
				scrollTop: 0,
				fileList: [],
				pageIndex: 1,
				flag: false,
				like: [],
				unLike: [],
				total: 0,
				scrollTop: 0,
				show: false,
				changeInfo:{
					oldpass:'',
					newpass:'',
					checkpass:''
				},
				mountedFlag : false,
				rules: {
					oldpass: [{
						type: 'string',
						required: true,
						trigger: ['blur', 'change']
					}],
					newpass: [{
						type: 'string',
						required: true,
						trigger: ['blur', 'change']
					}],
					checkpass: [{
						type: 'string',
						required: true,
						trigger: ['blur', 'change']
					}],
				},
			}
		},
		onPageScroll(e) {
			this.scrollTop = e.scrollTop;
		},
		methods: {
			out() {
				uni.removeStorageSync('token')
				uni.removeStorageSync('userName')
				this.$emit('out')
			},
			async getFileList(username, page) {
				let self = this
				let res = await ReqGetLikeFileList(username, page)
				if (res.code == 0) {
					for (let i = 0; i < res.data.thesisList.length; i++) {
						self.fileList.push(res.data.thesisList[i])
						self.like.push(1)
						self.unLike.push(0)
					}
					self.total = res.data.total
				}
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
				}
			},
			async unlikeClick(index) {
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
			async download(id,title) {
				let res = await ReqDownload(id)
				var blob = new Blob([res]);
				var url = window.URL.createObjectURL(blob);
				var a = document.createElement("a");
				a.style.display = 'none'
				a.target = '_blank'
				document.body.appendChild(a)
				a.href = url;
				a.download = title+'.pdf';
				a.click();
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
			},
			changePass() {
				this.show = true
			},
			async confirmChangePass() {
				if(this.changeInfo.checkpass!=this.changeInfo.newpass){
					uni.$u.toast('请确认密码相同')
				}else if(!this.passReg.test(this.changeInfo.newpass)){
					uni.$u.toast('新密码至少包含字母、数字、特殊字符，6位以上')
				}
				else{
					let res = await ReqChangePass(this.username,this.changeInfo.oldpass,this.changeInfo.newpass)
					if(res.code == 0){
						for(let i in this.changeInfo){
							this.changeInfo[i] = ''
						}
						uni.$u.toast('密码修改成功')
						this.show = false
					}else{
						for(let i in this.changeInfo){
							this.changeInfo[i] = ''
						}
						uni.$u.toast('请输入正确的原密码')
						this.show = false
					}
				}
			},
			cancelChangePass() {
				for(let i in this.changeInfo){
					this.changeInfo[i] = ''
				}
				this.show = false
			}
		},
		onReachBottom() {
			if (this.fileList.length <= this.total) {
				return this.flag = true
			}
			this.pageIndex++,
				this.getFileList(this.username, this.pageIndex)
		},
		mounted() {
			if(!this.mountedFlag){
				this.getFileList(this.username, this.pageIndex)
				this.mountedFlag = true
			}
		},
		activated() {
			this.getFileList(this.username, this.pageIndex)
		},
		deactivated() {
			this.fileList = []
			this.like = []
			this.unLike = []
		}
	}
</script>

<style lang="scss">
	.container {
		display: flex;
		flex-direction: column;
		align-items: center;
		height: 100vh;

		.userInfo {
			border-bottom: 1px solid #eee;
			display: flex;
			flex-direction: column;
			align-items: center;
			width: 720rpx;

			.img {
				width: 200rpx;
				height: 200rpx;

				image {
					width: 100%;
					height: 100%;
				}
			}

			.name {
				color: darkcyan;
			}

			button {
				margin-bottom: 10rpx;
			}
		}

		.paperList {
			.paperItem {
				border: 1px solid #eee;
				margin: 5px;
				width: 750rpx;
				overflow: hidden;

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
					word-wrap: break-word;
				}

				footer {
					margin-top: 10rpx;
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