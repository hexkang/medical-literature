<template>
	<view class="container" :style="( isuserFocus || isemailFocus)?containerHeight.focus:containerHeight.blur">
		<view class="input-area">
			<view class="user-icon">
				<image :src="userImg" :style="isDisabledBtn?'opacity:0.5':'opacity:1'"></image>
			</view>
			<!-- 用户名输入框 -->
			<view class="input-text" :style="isuserFocus?input_boder_style.focus:input_boder_style.blur">
				<view :style="isuserFocus?label_style.focus:label_style.blur">
					帐 号
				</view>

				<u-input :focus="un" v-model="userfindPassInfo.userName" type="text" :height="100" placeholder="" :custom-style="findPassStyle"
				 :clearable="false" @focus="userFocus" @blur="userBlur" />

				<view class="clear" @click="clearInput('userName')" v-show="userfindPassInfo.userName!=='' && isuserFocus">
					<image :src="clearImg" class="img"></image>
				</view>
			</view>
			
			<view class="input-text" :style="isemailFocus?input_boder_style.focus:input_boder_style.blur">
				<view :style="isemailFocus?label_style.focus:label_style.blur">
					邮 箱
				</view>
			
				<u-input :focus="email" v-model="userfindPassInfo.email" type="text"  :height="100"
				 placeholder=""  :custom-style="findPassStyle" :clearable="false" @focus="emailFocus" @blur="emailBlur" />
			
				<view class="clear" @click="clearInput('email')" v-show="userfindPassInfo.email!=='' && isemailFocus">
					<image :src="clearImg" class="img"></image>
				</view>
			</view>
			
			

			<view class="btn">
				<u-button size="default" :loading="isfindPassing" :ripple="true" :custom-style="isDisabledBtn?findPass_btn_style.disabled:findPass_btn_style.able"
				   @click="findPass">{{isfindPassing?'':'确认找回'}}</u-button>
			</view>
			
			<view class="btn">
				<u-button size="default"  @click="goLogin" :custom-style="findPass_btn_style.able">返回登录界面</u-button>
			</view>
			
			
			<u-toast ref="uToast"></u-toast>
		</view>
		<view class="copy-right">代码写不队出品</view>
	</view>
</template>

<script>
	import {ReqFindPass} from '@/utils/request/index.js'
	export default {
		name:'findPass',
		data() {
			return {
				// ------------- 其他 -------------
				userfindPassInfo: {
					userName: '',
					email:''
				},

				// 整体容器高度，单位 rpx

				// containerHeight: 1000,
				containerHeight: {
					focus: 'height:1100rpx;transition:0.2s',
					blur: 'height:1300rpx;transition:0.2s'
				},

				// 获取焦点时，整体上移的动画效果
				animationData: {},

				// 登录界面的用户图标
				userImg: '../../static/person-active.png',

				// 是否正在清除
				isClearing: false,

				// ------------- 输入框 -------------
				pdMaxLength: 10,
				// 输入框是否获取到焦点
				isuserFocus: false,
				isemailFocus:false,

				// 输入框自定义样式
				findPassStyle: {
					"padding-left": "40rpx",
					// 使光标消失（iOS无效果）
					"color": "transparent",
					"text-shadow": "0 0 0 #000",
					"border":"none"
				},

				// 清除按钮图标
				clearImg: '../../static/clear.png',

				// 输入框 label 样式
				label_style: {
					focus: 'width:120rpx;display:flex;justify-content:center;color:#FF5242;font-weight:bold;transform:scale(1.1)',
					blur: 'width:120rpx;display:flex;justify-content:center;color:#aaaaaa;transform:scale(1)'
				},

				input_boder_style: {
					focus: 'border-bottom: 1px solid #FF5242;',
					blur: 'border-bottom: 1px solid #e4e4e4;'
				},

				// ------------- 登录按钮 -------------
				// 登录按钮自定义样式
				findPass_btn_style: {
					able: {
						"width": "100%",
						"height": "100rpx",
						"border-radius": "20rpx",
						"border": "#e4e4e4",
						"background-color": "#FF5242",
						"color": "#fff"
					},
					disabled: {
						"width": "100%",
						"height": "100rpx",
						"border-radius": "20rpx",
						"border": "#e4e4e4",
						"background-color": "#FF5242",
						"color": "#fff",
						"opacity": "0.5"
					}
				},
				
				send_btn_style: {
					able: {
						"width": "100%",
						"height": "100rpx",
						"border-radius": "20rpx",
						"border": "#e4e4e4",
						"background-color": "#FF5242",
						"color": "#fff"
					},
					disabled: {
						"width": "100%",
						"height": "100rpx",
						"border-radius": "20rpx",
						"border": "#e4e4e4",
						"background-color": "#FF5242",
						"color": "#fff",
						"opacity": "0.5"
					}
				},

				// 点击登录按钮后，接口返回数据前，对该操作上锁
				isfindPassing: false,

				un: false,
				email:false,

				isDisabledBtn: true,
				isCodeBtn : true,
				timeStr: '',
				emailReg : /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/
			}
		},
		methods: {
			userFocus() {
				// 是否在焦点上
				this.isuserFocus = true
			},
			userBlur() {
				setTimeout(() => {
					this.isuserFocus = false
				}, 1)
			},
			
			emailFocus() {
				this.isemailFocus = true
			},
			emailBlur() {
				// 失去焦点事件先于清除事件触发，因此让其延迟即可先触发 clearInput 事件
				setTimeout(() => {
					this.isemailFocus = false
				}, 1)
			},
			// 清除 input 内容
			clearInput(value) {
				switch (value) {
					case 'userName':
							this.userfindPassInfo.userName = ''
							// 清空内容之后保持焦点
							this.un = false
							this.$nextTick(() => {
								this.un = true
							})
						break
					case 'email':
							this.userfindPassInfo.email = ''
							this.email = false
							this.$nextTick(() => {
								this.email = true
							})
						break
				}
			},
			
			goLogin(){
				this.$emit('goLogin')
			},
			async findPass() {
				// 密码正确
				this.isfindPassing = true
				
				let res = await ReqFindPass(this.userfindPassInfo.userName,this.userfindPassInfo.email)

				if(res.code == 0 ){
					this.isfindPassing = false
					this.$refs.uToast.show({
						title: '密码已发送至邮箱',
						type: 'success'
					})
					this.$emit('goLogin')
				}else{
					this.$refs.uToast.show({
						title: '账号和邮箱不对应',
						type: 'danger'
					})
				}
			}
		},
		watch: {
			userfindPassInfo: {
				handler(newVal, oldVal) {
					if ((newVal.userName !== '') && 
					(newVal.password !== '')&&
					(newVal.checkpass !== '')&&
					(this.emailReg.test(newVal.email) ) &&
					(newVal.code !== '' && newVal.code == this.codeCheck)
					) {
						this.isDisabledBtn = false
					} else {
						this.isDisabledBtn = true
					}
				},
				deep: true
			}
		}
	}
</script>

<style lang="scss">
	$screen-height:1334rpx;

	.container {
		display: flex;
		flex-wrap: wrap;
		justify-content: center;
		align-items: flex-end;
		width: 100%;


		.input-area {
			width: 80%;

			.user-icon {
				width: 100%;
				display: flex;
				justify-content: center;

				>image {
					width: 150rpx;
					height: 150rpx;
				}
			}

			.input-text {
				display: flex;
				flex-wrap: nowrap;
				align-items: center;
				margin-top: 20rpx;

				.clear {
					height: 100rpx;
					display: flex;
					align-items: center;

					.img {
						height: 36rpx;
						width: 36rpx;
					}
				}
			}

			.btn {
				margin-top: 40rpx;

				.findPass-btn {
					width: 100%;
					border-radius: 20rpx;
					border: $uni-color-primary;
					background-color: $uni-color-primary;
					color: $uni-text-color-inverse;
				}
			}
		}

		.copy-right {
			// bottom: 100rpx;
			bottom: 0;
			width: 100%;
			color: $uni-text-color-grey;
			text-align: center;
			font-size: 24rpx;
		}
	}
</style>
