// 对axios进行二次封装
import axios from "axios";
import nprogress from "nprogress";
import 'nprogress/nprogress.css'; 

const requests = axios.create({
    baseURL:'/api',
});

requests.interceptors.request.use((config)=>{
    const token = sessionStorage.getItem('token')
    nprogress.start()
    config.data = JSON.stringify(config.data)
    config.headers = {
        'Content-Type': 'application/json;charset=UTF-8',
    }
    if(token) {
        config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
})

// 响应拦截器
requests.interceptors.response.use((res)=>{
    console.log(res)
    nprogress.done()
    return res.data
},(error)=>{
    return Promise.reject(new Error(error))
})

// 对外暴露
export default requests