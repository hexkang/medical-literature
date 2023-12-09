// 先引入mockjs实现
import Mock from "mockjs";

Mock.setup({
    timeout:500
})

import login from './login.json'
import fileList from './fileList.json'
import checkCode from './checkCode.json'
import Register from './Register.json'
import download from './download.json'
import like from './like.json'
import test from './test.json'
import likeFileList from './likeFileList.json'
import fileDetail from './fileDetail.json'
import getUrl from './getUrl.json'
import getRank from './getRank.json'

Mock.mock((RegExp('/api/login'+'*')),{"code": 0,data:login}) 
Mock.mock(RegExp('/api/getFile'+'*'),{"code": 0,data:fileList})
Mock.mock(RegExp('/api/getLikeFile'+'*'),{"code": 0,data:likeFileList})
Mock.mock(RegExp('/api/emailCheck'+'*'),{"code": 0,data:checkCode})
Mock.mock('/api/signup',(req)=>{
    return{
        "code": 0,
        data:Register
    }
})
Mock.mock(RegExp('/api/download'+'*'),{"code": 0,data:download})
Mock.mock('/api/like',{"code": 0,data:like})
Mock.mock('/api/uploadFile',{"code": 0})
Mock.mock('/api/changePass',{"code": 0})
Mock.mock('/api/findPass',{"code": 0})
Mock.mock('/api/getRank',{"code": 0,data:getRank})
Mock.mock(RegExp('/api/test'+'*'),{"code": 0,data:test})
Mock.mock(RegExp('/api/getDetail'+'*'),{"code": 0,data:fileDetail})
Mock.mock(RegExp('/api/getUrl'+'*'),{"code": 0,data:getUrl})
