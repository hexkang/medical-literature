import { ReqgetFileList} from "@/api"

const state = {
    fileList : [],
    like:undefined,
    unlike:undefined,
    total:0 
}
const mutations = {
    GETFILELIST(state,{thesisList,total}){
        state.fileList = thesisList
        state.total = total
        state.like = []
        state.unlike = []
        for(let i = 0;i<thesisList.length;i++){
            if(thesisList[i].like==1){
                state.like.push(1)
                state.unlike.push(0)
            }else if(thesisList[i].like==-1){
                state.like.push(0)
                state.unlike.push(1)
            }else{
                state.like.push(0)
                state.unlike.push(0)
            }
             
        }
    },
    CLEARLIKE(state){
        state.like = undefined
        state.unlike = undefined
    }
}
const actions = {
    async getFileList({commit},{keyWord,page,username}){
        let result = await ReqgetFileList(keyWord,page,username)
        if(result.code=== 0){
            commit('GETFILELIST',result.data)
        }
    },
    clearLike({commit}){
        commit('CLEARLIKE')
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