<template>
	<view>
		<view class="container" ref="container">
				<view class="img">
					<image src="../../static/upload-img.png" mode=""></image>
				</view>
			<lsj-upload
			    ref="lsjUpload"
			    childId="upload1"
			    :width="width"
			    :height="height"
			    :option="option"
			    :size="size"
			    :formats="formats"
			    :debug="debug"
				
			    :instantly="instantly"
				@uploadEnd = "onuploadEnd"
			    @progress="onprogress"
			    @change="onChange">
			        <button type="primary" :style="{width: width,height: height}" class="chooseBtn">选择附件</button> >
			</lsj-upload>
			
			<view class="fileList">
			    <view class="fileTitle">已选择文件列表</view>
			    <view v-for="(item,index) in files.values()" :key="index" >
			       <view class="fileItem">
						<text class="name">{{item.name}}</text>
						<view class=" code">
							<text style="float: right;">状态：{{item.type}}</text>
							<text >进度：{{item.progress}}</text>
						</view>
			       </view>
			    </view>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		ReqCheck
	} from '../../utils/request/index.js'
	export default {
	    data() {
	        return {
	            // 上传接口参数
	            option: {
	                // 上传服务器地址，此地址需要替换为你的接口地址
	                url: 'http://43.139.73.93:8081/api/uploadFile',
	                // 上传附件的key
	                name: 'file',
	                // 根据你接口需求自定义body参数
	                formData: {
	                }
	            },
	            // 选择文件后是否立即自动上传，true=选择后立即上传
	            instantly: true,
	            // 必传宽高且宽高应与slot宽高保持一致
	            width: '200rpx',
	            height: '80rpx',
	            // 限制允许选择的格式，空串=不限制，默认为空
	            formats: 'pdf',
	            // 文件上传大小限制
	            size: 10,
	            // 文件回显列表
	            files: new Map(),
	            // 微信小程序Map对象for循环不显示，所以转成普通数组，不要问为什么，我也不知道
	            wxFiles: [],
	            // 是否打印日志
	            debug: true,
	
	            // 演示用
	            tabIndex: 0,
	            list:[], 
	        }
	    },
	    methods: {
	        // 某文件上传结束回调(成功失败都回调)
	       async onuploadEnd(item) {
			   let token = uni.getStorageSync('token')
				let res  = await ReqCheck(token,'end')
	            console.log(`${item.name}已上传结束，上传状态=${item.type}`);
	
	            // 更新当前状态变化的文件
	            this.files.set(item.name,item);
	
	            // 演示上传完成后取服务端数据
	            if (item['responseText']) {
	                console.log('演示服务器返回的字符串JSON转对象');
	                this.files.get(item.name).responseText = JSON.parse(item.responseText);
	            }
	
	            // 微信小程序Map对象for循环不显示，所以转成普通数组，不要问为什么，我也不知道
	            // #ifdef MP-WEIXIN
	            this.wxFiles = [...this.files.values()];
	            // #endif
	
	            // 强制更新视图
	            this.$forceUpdate();
	
	            // ---可删除--演示判断是否所有文件均已上传成功
	            let isAll = [...this.files.values()].find(item=>item.type!=='success');
	            if (!isAll) {
	                console.log('已全部上传完毕');
	            }
	            else {
	                console.log(isAll.name+'待上传');
	            }
	
	        },
	        // 上传进度回调
	        onprogress(item) {
	            // 更新当前状态变化的文件
	            this.files.set(item.name,item);
	
	            console.log('打印对象',JSON.stringify(this.files.get(item.name)));
	            // 微信小程序Map对象for循环不显示，所以转成普通数组，不要问为什么，我也不知道
	            // #ifdef MP-WEIXIN
	            this.wxFiles = [...this.files.values()];
	            // #endif
	
	            // 强制更新视图
	            this.$forceUpdate();
	
	        },
	        // 文件选择回调
	        async onChange(files) {
				let token = uni.getStorageSync('token')
				if(!token){
					return false
				}else{
					let res  = await ReqCheck(token,'start')
					console.log(res)
					if(res.code == 0 ){
						// 更新选择的文件
						this.files = files;
						// 强制更新视图
						this.$forceUpdate();
							
						// 微信小程序Map对象for循环不显示，所以转成普通数组，不要问为什么，我也不知道
						// #ifdef MP-WEIXIN
						this.wxFiles = [...this.files.values()];
						// #endif
					}else{
						uni.showToast({
							title: '服务器繁忙，请稍后',
							icon: 'none'
						})
					}
				}
	        }
	
	    }
	}
</script>

<style lang="scss">
.container{
	border:2px solid #eee;
	margin:  2px ;
	display: flex;
	align-items: center;
	flex-direction: column;
	.img{
		width: 500rpx;
		image{
				width: 100%;
		}
	}
	.lsj-file{
		margin-top: -50rpx;
		margin-bottom: 20rpx;
	}
	.chooseBtn{
		font-size: 36rpx;
		line-height: 70rpx;
		height: 40rpx;
	}
	.fileList{
		margin-bottom: 10rpx;
		.fileTitle{
			text-align: center;
			font-size: 35rpx;
			line-height: 50rpx;
			height: 50rpx;
			color:#121212;
		}
		.fileItem{
			margin: 2rpx;
			border: 1px solid #eee;
			padding: 10rpx;
			.name{
				display: block;
				font-size: 35rpx;
				line-height: 50rpx;
			}
			.code{
				display: block;
				font-size: 30rpx;
				line-height: 40rpx;
				color: #808080;
			}
			.delete{
				margin-top: 10rpx;
				color: #808080;
			}
		}
	}
}
</style>
