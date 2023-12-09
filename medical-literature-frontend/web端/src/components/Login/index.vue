<template>
   <div>
        <el-dialog title=" 账号登录" :visible.sync="outerVisible" modal :append-to-body="modalFlag" width="30%" top="10%">
            <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px">
                <el-form-item label="用户名" prop="userName" class="loginName" required>
                    <el-input v-model.number="ruleForm.userName"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="pass" required>
                    <el-input type="password" v-model="ruleForm.pass" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm('ruleForm')" >登录</el-button>
                <el-button  @click="goFindPass" >找回密码</el-button>
                <el-button @click="goRegister">注册</el-button>
            </div>
        </el-dialog>
   </div>
</template>
  
<script>
//单独引入：
import {Message} from 'element-ui'
import { mapState } from 'vuex';

export default {
    name: 'Login',
    computed:{
        ...mapState({
            token: state=> state.User.token
        })
    },
    data() {
        var checkUserName = (rule, value, callback) => {
            if (!value) {
                return callback(new Error('用户名不能为空'));
            }else{
                callback()
            }
        };
        var validatePass = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请输入密码'));
            }else {
                if (this.ruleForm.checkPass !== '') {
                    this.$refs.ruleForm.validateField('checkPass');
                }
                callback();
            }
        };
        return {
            modalFlag:true,
            outerVisible: false,
            ruleForm: {
                pass: '',
                userName: ''
            },
            rules: {
                pass: [
                    { validator: validatePass, trigger: 'blur' }
                ],
                userName: [
                    { validator: checkUserName, trigger: 'blur' }
                ],
            }
        };
    },
    mounted(){
        this.$bus.$on('login',()=>{
                this.outerVisible = true
        })
    },
    methods:{
        submitForm(formName) {
            this.$refs[formName].validate( async(valid) => {
                if (valid) {
                    await this.$store.dispatch('userLogin',this.ruleForm)
                    if(this.token){
                        sessionStorage.setItem('token',this.token)
                        this.$bus.$emit('userName',this.ruleForm.userName)
                        sessionStorage.setItem('userName',this.ruleForm.userName)
                        this.$refs[formName].resetFields();
                        this.outerVisible = false
                    }else{
                        this.$refs[formName].resetFields();
                        this.LoginError()
                    }
                } else {
                    return false;
                }
            });
        },
        goRegister(){
            this.outerVisible = false
            this.$bus.$emit('register')
        },
        goFindPass(){
            console.log(1)
            this.outerVisible = false
            this.$bus.$emit('findPass')
        },
        LoginError() {
            Message('账号或密码输入错误')
        },
    }
}
</script>

<style>

</style>