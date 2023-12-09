// 当前的模块：API进行统一管理
import requests from "./myRequest";

export const ReqLogin = (username,password)=>{    
    return requests({
        url:'/login',
        method:'POST',
        data: {
        username,
        password
      }
    })
}

export const ReqEmailCheck = (email)=>{    
  return requests({
      url:'/emailCheck',
      method:'POST',
      data: {
      email
    }
  })
}

export const ReqSignUp = (username,password,email)=>{    
    return requests({
        url:'/signup',
        method:'POST', 
        data: {
        username,
        password,
        email
      }
    })
}

export const ReqFindPass = (username,email)=>{    
  return requests({
      url:'/findPass',
      method:'POST', 
      data: {
      username,
      email
    }
  })
}

export const ReqChangePass = (username,oldPass,newPass)=>{    
  return requests({
      url:'/changePass',
      method:'POST', 
      data: {
      username,
      oldPass,
      newPass
    }
  })
}

export const ReqGetRank = ()=>{    
  return requests({
      url:'/getRank',
      method:'GET'
  })
}


export const ReqCheck = (token)=>{    
    return requests({
        url:'/test',
        method:'POST',
        data: {
        token
      }
    })
}

export const ReqgetFileList = (keyWord,page,username)=>{    
  return requests({
      url:'/getFile',
      method:'POST',
      data:{
        keyWord,
        page,
        username
      }
  })
}

export const ReqgetLikeFileList = (username,page)=>{    
  return requests({
      url:'/getLikeFile',
      method:'POST',
      data:{
        username,
        page
      }
  })
}

export const ReqLikeHandle = (id,username,like)=>{    
    return requests({
        url:'/like',
        method:'POST',
        data: {
        id,
        username,
        like
      }
    })
}

export const ReqGetDetail = (id,keyWord)=>{    
    return requests({
        url:'/getDetail',
        method:'POST',
        data: {
        id,
        keyWord
      }
    })
}

export const ReqGetUrl = (id)=>{    
  return requests({
      url:'/getUrl',
      method:'POST',
      data: {
      id
    }
  })
}

export const ReqDownload = (id)=>{
   return requests({
        url:'/download',
        method:'POST',
        data: {
          id
        },
        responseType:'blob'
    })
}
