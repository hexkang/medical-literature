<template>
	<view class="container" :style="(ispdFocus || isuserFocus ||iscodeFocus || ispd2Focus || isemailFocus)?containerHeight.focus:containerHeight.blur">
		<view class="input-area">
			<view class="user-icon">
				<image :src="userImg" :style="isDisabledBtn?'opacity:0.5':'opacity:1'"></image>
			</view>
			<!-- 用户名输入框 -->
			<view class="input-text" :style="isuserFocus?input_boder_style.focus:input_boder_style.blur">
				<view :style="isuserFocus?label_style.focus:label_style.blur">
					帐 号
				</view>

				<u-input :focus="un" v-model="userRegisterInfo.userName" type="text" :height="100" placeholder="" :custom-style="registerStyle"
				 :clearable="false" @focus="userFocus" @blur="userBlur" />

				<view class="clear" @click="clearInput('userName')" v-show="userRegisterInfo.userName!=='' && isuserFocus">
					<image :src="clearImg" class="img"></image>
				</view>
			</view>
			<!-- 密码输入框 -->
			<view class="input-text" :style="ispdFocus?input_boder_style.focus:input_boder_style.blur">
				<view :style="ispdFocus?label_style.focus:label_style.blur">
					密 码
				</view>

				<u-input :focus="pd" v-model="userRegisterInfo.password" type="password" :password-icon="false" :height="100"
				 placeholder="" :maxlength="pdMaxLength" :custom-style="registerStyle" :clearable="false" @focus="pdFocus" @blur="pdBlur" />

				<view class="clear" @click="clearInput('password')" v-show="userRegisterInfo.password!=='' && ispdFocus">
					<image :src="clearImg" class="img"></image>
				</view>
			</view>
			
			<view class="input-text" :style="ispd2Focus?input_boder_style.focus:input_boder_style.blur">
				<view :style="ispd2Focus?label_style.focus:label_style.blur">
				确认密码
				</view>
				<u-input :focus="pd2" v-model="userRegisterInfo.checkpass" type="password" :password-icon="false" :height="100"
				 placeholder="" :maxlength="pdMaxLength" :custom-style="registerStyle" :clearable="false" @focus="pd2Focus" @blur="pd2Blur" />
			
				<view class="clear" @click="clearInput('checkpass')" v-show="userRegisterInfo.checkpass!=='' && ispd2Focus">
					<image :src="clearImg" class="img"></image>
				</view>
			</view>
			
			<view class="input-text" :style="isemailFocus?input_boder_style.focus:input_boder_style.blur">
				<view :style="isemailFocus?label_style.focus:label_style.blur">
					邮 箱
				</view>
			
				<u-input :focus="email" v-model="userRegisterInfo.email" type="text"  :height="100"
				 placeholder=""  :custom-style="registerStyle" :clearable="false" @focus="emailFocus" @blur="emailBlur" />
			
				<view class="clear" @click="clearInput('email')" v-show="userRegisterInfo.email!=='' && isemailFocus">
					<image :src="clearImg" class="img"></image>
				</view>
			</view>
			
			
			
			<view class="input-text" :style="iscodeFocus?input_boder_style.focus:input_boder_style.blur">
				<view :style="iscodeFocus?label_style.focus:label_style.blur">
					验证码
				</view>
			
				<u-input :focus="code" v-model="userRegisterInfo.code" type="text"  :height="100"
				 placeholder="" :custom-style="registerStyle" :clearable="false" @focus="codeFocus" @blur="codeBlur" />
			
				<view class="clear" @click="clearInput('code')" v-show="userRegisterInfo.code!=='' && iscodeFocus">
					<image :src="clearImg" class="img"></image>
				</view>
			</view>
			
			<view class="btn">
				<u-button size="default" :disabled="timeStr!=''"  :ripple="true"  :custom-style="isCodeBtn?send_btn_style.disabled:send_btn_style.able"
				  @click="sendCode">{{timeStr||'发送验证码'}}</u-button>
			</view>
			
			<view class="btn">
				<u-button size="default"  @click="goLogin" :custom-style="Register_btn_style.able">返回登录界面</u-button>
			</view>
			

			<view class="btn">
				<u-button size="default" :loading="isRegistering" :ripple="true" :custom-style="isDisabledBtn?Register_btn_style.disabled:Register_btn_style.able"
				   @click="register">{{isRegistering?'':'注册并登录'}}</u-button>
			</view>
			
			
			<u-toast ref="uToast"></u-toast>
		</view>
		<view class="copy-right">代码写不队出品</view>
	</view>
</template>

<script>
	import {ReqRegister,ReqSendCode} from '@/utils/request/index.js'
	export default {
		name:'register',
		data() {
			return {
				passReg : /^(?=.*\d)(?=.*[a-zA-Z])(?=.*[^\da-zA-Z\s]).{6,}$/,
				// ------------- 其他 -------------
				userRegisterInfo: {
					userName: '',
					password: '',
					checkpass:'',
					email:'',
					code:''
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
				pdMaxLength: 20,
				// 输入框是否获取到焦点
				isuserFocus: false,
				ispdFocus: false,
				ispd2Focus:false,
				isemailFocus:false,
				iscodeFocus:false,

				// 输入框自定义样式
				registerStyle: {
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
				Register_btn_style: {
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
				isRegistering: false,

				un: false,
				pd: false,
				pd2:false,
				email:false,
				code:false,

				isDisabledBtn: true,
				isCodeBtn : true,
				timeStr: '',
				emailReg : /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/,
				codeCheck: undefined
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

			pdFocus() {
				this.ispdFocus = true
			},
			pdBlur() {
				// 失去焦点事件先于清除事件触发，因此让其延迟即可先触发 clearInput 事件
				if(!this.passReg.test(this.userRegisterInfo.password)){
					uni.showToast({
						title: '请输入正确格式的密码',
						icon: 'none'
					})
				}
				setTimeout(() => {
					this.ispdFocus = false
				}, 1)
			},
			
			pd2Focus() {
				this.ispd2Focus = true
			},
			pd2Blur() {
				// 失去焦点事件先于清除事件触发，因此让其延迟即可先触发 clearInput 事件
				setTimeout(() => {
					this.ispd2Focus = false
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
			
			codeFocus() {
				this.iscodeFocus = true
			},
			codeBlur() {
				// 失去焦点事件先于清除事件触发，因此让其延迟即可先触发 clearInput 事件
				setTimeout(() => {
					this.iscodeFocus = false
				}, 1)
			},

			// 清除 input 内容
			clearInput(value) {
				switch (value) {
					case 'userName':
							this.userRegisterInfo.userName = ''
							// 清空内容之后保持焦点
							this.un = false
							this.$nextTick(() => {
								this.un = true
							})
						break
					case 'password':
							this.userRegisterInfo.password = ''
							this.pd = false
							this.$nextTick(() => {
								this.pd = true
							})
						break
					case 'checkpass':
							this.userRegisterInfo.checkpass = ''
							this.pd2 = false
							this.$nextTick(() => {
								this.pd2 = true
							})
						break
					case 'email':
							this.userRegisterInfo.email = ''
							this.email = false
							this.$nextTick(() => {
								this.email = true
							})
						break
					case 'code':
							this.userRegisterInfo.code = ''
							this.code = false
							this.$nextTick(() => {
								this.code = true
							})
						break
				}
			},
			
			goLogin(){
				this.$emit('goLogin')
			},
			
			async sendCode() {
				let self = this
				let count = 60
				
				
				let timerid = window.setInterval(function() {
					self.timeStr = `验证码已发送,${count--}秒后重新发送`
					if (count <= 0) {
					console.log('clear' + timerid)
					window.clearInterval(timerid)
					self.timeStr = ''
					}
				}, 1000)
				let res = await ReqSendCode(this.userRegisterInfo.email)
				if(res.code == 0 ){
					self.codeCheck = res.data.captcha
				}
			},
			async register() {
				// 密码正确
				this.isRegistering = true
				
				let res = await ReqRegister(this.userRegisterInfo.userName,this.userRegisterInfo.password,this.userRegisterInfo.email)

				if(res.code == 0 ){
					this.isRegistering = false
					this.$refs.uToast.show({
						title: '注册成功',
						type: 'success'
					})
					uni.setStorageSync('token',res.data.token)
					uni.setStorageSync('userName',this.userRegisterInfo.userName)
					this.$emit('registerHandle',this.userRegisterInfo.userName)
				}
			}
		},
		watch: {
			userRegisterInfo: {
				handler(newVal, oldVal) {
					if ((newVal.userName !== '') && 
					(newVal.password !== '' && this.passReg.test(newVal.password))&&
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
			},
			'userRegisterInfo.email':{
				handler(newVal, oldVal) {
					if (
					(this.emailReg.test(newVal)) 
					) {
						this.isCodeBtn = false
					} else {
						this.isCodeBtn = true
					}
				},
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

				.Register-btn {
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
