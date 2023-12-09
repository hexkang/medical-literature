import Vue from "vue";
import Vuex from 'vuex'

// 需要使用插件一次
Vue.use(Vuex);

// 引入小仓库
import User from './User';
import Home from './Home';
import Detail from "./Detail";
import Rank from './Rank'


export default new Vuex.Store({
    modules:{
        User,
        Home,
        Detail,
        Rank
    }
})