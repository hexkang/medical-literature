<template>
    <el-dialog title=" 账号登录" :visible.sync="outerVisible" :append-to-body="modalFlag" width="30%" top="10%">
        <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px">
            <el-form-item label="用户名" prop="userName" class="loginName" required>
                <el-input v-model.number="ruleForm.userName"></el-input>
            </el-form-item>
            <el-form-item label="邮箱" prop="email" required ref="email">
                <el-input v-model="ruleForm.email"></el-input>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="submitForm('ruleForm')">确认</el-button>
            <el-button @click="goLogin">返回登录</el-button>
        </div>
    </el-dialog>
</template>

<script>
import { ReqFindPass } from '@/api';
import { Message } from 'element-ui';
import { mapState } from 'vuex';
export default {
    name: 'findPass',
    data() {
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
        return {
            modalFlag:true,
            outerVisible: false,
            ruleForm: {
                userName: '',
                email: ''
            },
            rules: {
                userName: [
                    { validator: checkUserName, trigger: 'blur' }
                ],
                email: [
                    { validator: checkEmail, trigger: 'blur' }
                ]
            }
        };
    },
    methods: {
        submitForm(formName) {
            this.$refs[formName].validate(async(valid) => {
                if (valid) {
                        let res = await ReqFindPass(this.userName,this.email)
                        if( res.code == 0){
                            Message('密码已发送至邮箱')
                            this.$refs[formName].resetFields()
                            this.outerVisible = false
                            this.$bus.$emit('login')
                        }else{
                            this.$refs[formName].resetFields();
                            Message('用户名和邮箱对不上')
                        }
                } else {
                    return false;
                }
            });
        },
        goLogin(){
            this.outerVisible = false
            this.$bus.$emit('login')
        }
    },
    mounted(){
        this.$bus.$on('findPass',()=>{
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