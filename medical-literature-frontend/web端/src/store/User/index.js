import { ReqLogin,ReqEmailCheck,ReqgetLikeFileList, ReqSignUp} from "@/api"

const state = {
    'token' : '',
    'code' : '',
    'username':'',
    'likeFileList':[],
    'total': 0
}
const mutations = {
    USERLOGIN(state,{token,username}){
        state.token = token
        state.username = username
    },
    USERREGISTER(state,{token,username}){
        state.token = token
        state.username = username
    },
    GETCHECKCODE(state,code){
        state.code = code
    },
    GETLIKEFILELIST(state,{thesisList,total}){
        state.likeFileList = thesisList
        state.total = total
    },
    OUT(state){
        state.token = undefined
        state.username = undefined
    }
}
const actions = {
    async userLogin({commit},{userName,pass}){
        let result = await ReqLogin(userName,pass)
        if(result.code=== 0){
            commit('USERLOGIN',{token:result.data.token,username:userName})
        }
    },
    async userRegister({commit},{userName,pass,email}){
        let result = await ReqSignUp(userName,pass,email)
        if(result.code=== 0){
            commit('USERREGISTER',{token:result.data.token,username:userName})
        }
    },
    async getCheckCode({commit},email){
        let result = await ReqEmailCheck(email)
        if(result.code===0){
            commit('GETCHECKCODE',result.data.captcha)
        }
    },
    async getLikeFileList({commit},{username,page}){
        let result = await ReqgetLikeFileList(username,page)
        if(result.code===0){
            commit('GETLIKEFILELIST',result.data)
        }
    },
    out({commit}){
        commit('OUT')
    }
}
const getters = {
    
}

export default{
    state,
    mutations,
    actions,
    getters
}