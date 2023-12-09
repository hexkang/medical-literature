<template>
    <el-dialog title=" 账号登录" :visible.sync="outerVisible" :append-to-body="modalFlag" width="30%" top="10%">
        <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px">
            <el-form-item label="用户名" prop="userName" class="loginName" required>
                <el-input v-model.number="ruleForm.userName"></el-input>
            </el-form-item>
            <el-form-item label="邮箱" prop="email" required ref="email">
                <el-input v-model="ruleForm.email"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="pass" required>
                <el-input type="password" v-model="ruleForm.pass" autocomplete="off" show-password ></el-input>
            </el-form-item>
            <el-form-item label="确认密码" prop="checkPass">
                <el-input type="password" v-model="ruleForm.checkPass" autocomplete="off" show-password></el-input>
            </el-form-item>
            <el-form-item label="验证码" prop="code" class="identifyingCode" required>
                <el-input v-model="ruleForm.code" ref="checkCode"></el-input>
                <el-button type="primary" @click="sendCode" :disabled="sendFlag">{{timeStr || '发送验证码' }}</el-button>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="submitForm('ruleForm')">注册并登录</el-button>
            <el-button @click="resetForm('ruleForm')">重置</el-button>
            <el-button @click="goLogin">返回登录</el-button>
        </div>
    </el-dialog>
</template>

<script>
import { Message } from 'element-ui';
import { mapState } from 'vuex';
export default {
    name: 'Register',
    data() {
        let passReg = /^(?=.*\d)(?=.*[a-zA-Z])(?=.*[^\da-zA-Z\s]).{6,}$/
        var checkUserName = (rule, value, callback) => {
            if (!value) {
                return callback(new Error('用户名不能为空'));
            } else {
                callback()
            }
        };
        var checkEmail = (rule, value, callback) => {
            let emailReg = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/
            if (!value) {
                return callback(new Error('邮箱不能为空'));
            }else if(!emailReg.test(value)){
                return callback(new Error('邮箱格式错误'));
            }else {
                this.checkFlag = 1
                callback()
            }
        };
        var validatePass = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请输入密码'));
            }else if(!passReg.test(value)){
                callback(new Error('至少包含字母、数字、特殊字符，6位以上'));
            } else {
                if (this.ruleForm.checkPass !== '') {
                    this.$refs.ruleForm.validateField('checkPass');
                }
                callback();
            }
        };
        var validatePass2 = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请再次输入密码'));
            } else if (value !== this.ruleForm.pass) {
                callback(new Error('两次输入密码不一致!'));
            } else {
                callback();
            }
        };
        var checkCode = (rule, value, callback) => {
            if (!value) {
                return callback(new Error('验证码不能为空'));
            } else {
                callback()
            }
        };
        return {
            checkFlag:0,
            modalFlag:true,
            sendFlag:false,
            outerVisible: false,
            timeStr : '',
            ruleForm: {
                pass: '',
                checkPass: '',
                userName: '',
                email: '',
                code: ''
            },
            rules: {
                pass: [
                    { validator: validatePass, trigger: 'blur' }
                ],
                checkPass: [
                    { validator: validatePass2, trigger: 'blur' }
                ],
                userName: [
                    { validator: checkUserName, trigger: 'blur' }
                ],
                email: [
                    { validator: checkEmail, trigger: 'blur' }
                ],
                code: [
                    { validator: checkCode, trigger: 'blur' }
                ],
            }
        };
    },
    computed:{
        ...mapState({
            token: state=> state.User.token,
            code:state=> state.User.code,
        })
    },
    methods: {
        submitForm(formName) {
            this.$refs[formName].validate(async(valid) => {
                if (valid) {
                    if(this.ruleForm.code === this.code){
                        await this.$store.dispatch('userRegister',{userName:this.ruleForm.userName,pass:this.ruleForm.pass,email:this.ruleForm.email})
                        if(this.token){
                            sessionStorage.setItem('token',this.token)
                            this.$bus.$emit('userName',this.ruleForm.userName)
                            sessionStorage.setItem('userName',this.ruleForm.userName)
                            this.$refs[formName].resetFields();
                            this.outerVisible = false
                        }else{
                            this.$refs[formName].resetFields();
                            Message('该用户已有人注册，请重新注册')
                        }
                    }else{
                        Message('验证码错误')
                    }
                } else {
                    return false;
                }
            });
        },
        resetForm(formName) {
            this.$refs[formName].resetFields();
        },
        sendCode(){
            var self = this
            if(this.checkFlag){
                this.sendFlag = true
                this.$store.dispatch('getCheckCode',this.ruleForm.email)
                Message('验证码已发送')
                let count = 60
                self.$refs['checkCode'].focus()
                let timerid = window.setInterval(function() {
                    self.timeStr = `验证码已发送,${count--}秒后重新发送`
                    if (count <= 0) {
                    console.log('clear' + timerid)
                    window.clearInterval(timerid)
                    self.timeStr = ''
                    }
                }, 1000)
            }else{
                Message('请输入正确的邮箱')
                self.$refs['email'].focus()
            }
        },
        goLogin(){
            this.outerVisible = false
            this.$bus.$emit('login')
        }
    },
    mounted(){
        this.$bus.$on('register',()=>{
            this.outerVisible = true
        })
    }
}
</script>

<style lang="less">
.identifyingCode {
    .el-input {
        width: 100%;
    }

    .el-button {
        margin-top: 3px;
    }
}
</style>