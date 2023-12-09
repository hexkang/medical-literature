// #ifndef H5
let BASE_URL = ' http://43.139.73.93:8081/api' //这里是自己的服务器地址
// #endif

// #ifdef H5
let BASE_URL = '/api' //''
// #endif

var  request = options => {
	return new Promise((resolve, reject) => {
		uni.request({
			url: BASE_URL + options.url,
			method: options.method || 'GET',
			data: JSON.stringify(options.data) || {}
		}).then(res=>{
			resolve(res[1].data)
		}).catch(e=>reject(e))
	})
}


export default request