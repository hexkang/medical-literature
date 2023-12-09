import sendFile from "@/pages/sendFile"
import Home from "@/pages/Home"
import userDetail from '@/pages/Detail/userDetail'
import Person from '@/pages/Person'
import searchDetail from '@/pages/Detail/searchDetail'
import About from '@/pages/about'

export default [
    {
        path:'/sendFile',
        component:sendFile,
        meta:{isShow:true}
    },
    {
        path:'/home',
        component:Home,
        meta:{isShow:true}
    },
    {
        path:'/userDetail',
        name:'userDetail',
        component:userDetail,
        meta:{isShow:true}
    },
    {
        path:'/searchDetail',
        name:'searchDetail',
        component:searchDetail,
        meta:{isShow:true}
    },
    {
        path:'/person',
        component:Person,
        meta:{isShow:true}
    },
    {
        path:'/about',
        component:About,
        meta:{isShow:true}
    },
    // 重定向，项目跑起来的时候访问自动定向首页
    {
        path:'*',
        redirect:'/about'
    }
]