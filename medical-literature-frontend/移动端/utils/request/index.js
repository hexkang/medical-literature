import request from './request.js'

export const ReqLogin = (username,password)=>{
	return request({
		url:'/login',
		method:'post',
		data :{
			username,
			password
		}
	})
}

export const ReqRegister = (username,password,email)=>{
	return request({
		url:'/signup',
		method:'post',
		data :{
			username,
			password,
			email
		}
	})
}

export const ReqSendCode = (email)=>{
	return request({
		url:'/emailCheck',
		method:'post',
		data :{
			email
		}
	})
}

export const ReqFindPass = (username,email)=>{
	return request({
		url:'/findPass',
		method:'post',
		data :{
			username,
			email
		}
	})
}


export const ReqChangePass = (username, oldPass,newPass)=>{
	return request({
		url:'/changePass',
		method:'post',
		data :{
			username,
			oldPass,
			newPass
		}
	})
}

export const ReqGetFileList = (keyWord,page,username)=>{
	return request({
	  url:'/getFile',
	  method:'POST',
	  data:{
		keyWord,
		page,
		username
	  }
  })
}

export const ReqGetLikeFileList = (username,page)=>{
	return request({
	  url:'/getLikeFile',
	  method:'POST',
	  data:{
		username,
		page
	  }
  })
}

export const ReqLikeHandle = (id,username,like)=>{
	return request({
		  url:'/like',
		  method:'POST',
		  data:{
			id,
			username,
			like
		  }
	})
}

export const ReqCheck = (token,msg)=>{    
    return request({
        url:'/lock',
        method:'POST',
        data: {
        token,
        msg
      }
    })
}

export const ReqGetDetail = (id,keyWord)=>{    
    return request({
        url:'/getDetail',
        method:'POST',
        data: {
        id,
        keyWord
      }
    })
}

export const ReqGetUrl = (id)=>{    
    return request({
        url:'/getUrl',
        method:'POST',
        data: {
        id
      }
    })
}

export const ReqDownload = (id)=>{
   return request({
        url:'/download',
        method:'POST',
        data: {
			id
		  },
	  responseType:'blob'
    })
}

