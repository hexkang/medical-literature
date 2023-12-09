<template>
  <view>
    <web-view :src="allUrl"></web-view>
  </view>
</template>
 
<script>
  export default {
    data() {
	  return {
	    viewerUrl: '/hybrid/html/web/viewer.html', 
		allUrl: ''
	  }
	},
	onLoad(options) {
	  this.allUrl = this.viewerUrl + '?file=' + options.url 
	}
  }
</script>
 
 
<style>
 
</style>