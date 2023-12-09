import { ReqCheck,ReqDownload } from "@/api"
import { Message } from "element-ui"

export const tokenCheck= async()=>{
    let token = sessionStorage.getItem('token')
    if(token){
            return true
    }else{
        return false
    }
}

export const  fileDownload = async(title, id)=>{
    let self = this
    let flag = await tokenCheck()
    if(flag){
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
    } else {
        Message('请登录账号')
    }
}