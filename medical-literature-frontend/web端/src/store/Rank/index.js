import { ReqGetRank} from "@/api"

const state = {
    wordRank : [],
    thesisRank:[]
}
const mutations = {
    GETRANK(state,data){
        state.wordRank = data.wordRank
        state.thesisRank = data.thesisRank
    }
}
const actions = {
    async getRank({commit}){
        let result = await ReqGetRank()
        console.log(result)
        if(result.code=== 0){
            commit('GETRANK',result.data)
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