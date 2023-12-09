<template>
	<login v-if="showLogin" @loginHandle="loginHandle" @goRegister='goRegister' @goFindPass='goFindPass'></login>
	<register v-else-if="showRegister" @goLogin='goLogin' @registerHandle="registerHandle"></register>
	<findpass v-else-if="showFindPass"  @goLogin='goLogin'></findpass> 
	<individual v-else :username="username" @out="out" ></individual>
</template>

<script>
	import login from './login.vue'
	import register from './register.vue'
	import individual from './individual.vue'
	import findpass from './findPass.vue'
	export default {
		components:{login,register,individual,findpass},
		data(){
			return{
				showLogin:false,
				showRegister:false,
				showFindPass:false,
				username :uni.getStorageSync('userName')||''
			}
		},
		methods:{
			loginHandle(username){
				this.showLogin = false
				this.username = username
			},
			goRegister(){
				this.showLogin = false
				this.showRegister = true
			},
			goLogin(){
				this.showFindPass = false
				this.showLogin = true
				this.showRegister = false
			},
			registerHandle(username){
				this.showRegister = false
				this.username = username
			},
			out(){
				this.showLogin = true
				this.name = ''
			},
			goFindPass(){
				console.log(1)
				this.showLogin = false
				this.showRegister = false
				this.showFindPass = true
			}
		},
		mounted() {
			this.showLogin = (this.username == '')
			this.$on('loginHandle')
			this.$on('out')
			this.$on('goRegister')
			this.$on('goLogin')
			this.$on('registerHandle')
			this.$on('goFindPass')
		}
	}
</script>

<style lang="scss">
	
</style>
