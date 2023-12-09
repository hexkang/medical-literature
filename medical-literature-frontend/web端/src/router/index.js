import Vue from "vue";
import VueRouter from "vue-router";

Vue.use(VueRouter)


import routes from '@/router/routes'

// 先把VueRouter原型对象push先保存一份
let oringinPush = VueRouter.prototype.push;
let oringinReplace = VueRouter.prototype.replace;


VueRouter.prototype.push = function(location,resolve,reject){
    if(resolve && reject){
        oringinPush.call(this,location,resolve,reject)
    }else{
        oringinPush.call(this,location,()=>{},()=>{})
    }
}

VueRouter.prototype.replace = function(location,resolve,reject){
    if(resolve && reject){
        oringinReplace.call(this,location,resolve,reject)
    }else{
        oringinReplace.call(this,location,()=>{},()=>{})
    }
}


// 配置路由
export default new VueRouter({
    routes,
    scrollBehavior(to,from,savePosition){
        return {x:0,y:0}
    }
})