import { ReqGetDetail, ReqGetUrl} from "@/api"

const state = {
    searchUrl:'',
    likeUrl:'',
    keyPage:[]
}
const mutations = {
    GETLIKEDETAIL(state,data){
        state.likeUrl = data.url
    },
    GETSEARCHDETAIL(state,data){
        state.serachUrl = data.url
        state.keyPage = data.keyPage
    },
}
const actions = {
    async getSerachDetail({commit},{id,keyWord}){
        let result = await ReqGetDetail(id,keyWord)
        if(result.code=== 0){
            commit('GETSEARCHDETAIL',result.data)
        }
    },
    async getLikeDetail({commit},{id}){
        let result = await ReqGetUrl(id)
        if(result.code=== 0){
            commit('GETLIKEDETAIL',result.data)
        }
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