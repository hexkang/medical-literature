<template>
    <div>
        <el-container class="person">
            <el-aside class="userInfo" width="30%">
                <img src="@/assets/person-active.png" alt="">
                <span class="name">{{ username }}</span>
                <el-button type="primary" @click="changePass">修改密码</el-button>
                <el-button type="danger" @click="out">退出登录</el-button>
            </el-aside>
            <el-main>
                <thesis :fileList="fileList" :total="total" :likeFlag = 1></thesis>
            </el-main>
        </el-container>
        <el-dialog title=" 修改密码" :visible.sync="outerVisible" modal :append-to-body="modalFlag" width="30%" top="10%">
            <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px">
                <el-form-item label="原密码" prop="pass" required>
                    <el-input type="password" v-model="ruleForm.pass" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="新密码" prop="newPass" required>
                    <el-input type="password" v-model="ruleForm.newPass" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="确认密码" prop="checkPass">
                    <el-input type="password" v-model="ruleForm.checkPass" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm('ruleForm')" :plain = true>确认修改</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import { mapState } from 'vuex';
import {ReqChangePass} from '@/api'
import { Message } from 'element-ui';
export default {
    name: 'person',
    data(){
        let passReg = /^(?=.*\d)(?=.*[a-zA-Z])(?=.*[^\da-zA-Z\s]).{6,}$/
        var validatePass = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请输入密码'));
            }else if(!passReg.test(value)){
                callback(new Error('密码格式错误'));
            } else {
                if (this.ruleForm.checkPass !== '') {
                    this.$refs.ruleForm.validateField('checkPass');
                }
                callback();
            }
        };
        var validateNewPass = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请输入密码'));
            }else if(!passReg.test(value)){
                callback(new Error('新密码至少包含字母、数字、特殊字符，6位以上'));
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
        return {
            username:sessionStorage.getItem('userName'),
            outerVisible: false,
            ruleForm: {
                pass: '',
                newPass: '',
                checkPass:''
            },
            modalFlag:true,
            rules: {
                pass: [
                    { validator: validatePass, trigger: 'blur' }
                ],
                newPass: [
                    { validator: validateNewPass, trigger: 'blur' }
                ],
                checkPass: [
                    { validator: validatePass2, trigger: 'blur' }
                ],
            }
        };
    },
    computed: {
        ...mapState({
            fileList: state => state.User.likeFileList,
            total: state => state.User.total,
        })
    },
    methods: {
        changePass(){
            this.outerVisible = true
        },
        out(){
            this.$store.dispatch('out')
            sessionStorage.removeItem('userName')
            sessionStorage.removeItem('token')
            this.$bus.$emit('userName','')
            this.$router.push('/home')
        },
        submitForm(formName) {
            this.$refs[formName].validate( async(valid) => {
                if (valid) {
                    let res = await ReqChangePass(this.username,this.pass,this.newPass)
                    if(res.code == 0){
                        Message('修改成功')
                        this.$refs[formName].resetFields();
                        this.outerVisible = false
                    }else{
                        Message('原密码错误')
                        this.$refs[formName].resetFields();
                    }
                } else {
                    return false;
                }
            });
        },
    },
    mounted() {
        this.$store.dispatch('getLikeFileList', {username:sessionStorage.getItem('userName'),page:1})
    }
}
</script>

<style lang="less">
.person {
    margin-top: 30px;

    .userInfo {
        margin-top: 30px;
        height: 100vh;
        border-right: 2px solid #eee;
        display: flex;
        align-items: center;
        flex-direction: column;
        .el-button{
            margin:3px 0 ;
        }
        .name {
            color: darkcyan;
            font-size: 24px;
            margin-bottom: 20px;
        }

        img {
            width: 100px;
            height: 100px;
            margin-bottom: 20px;
        }

        span {
            display: block;
        }
    }

    .thesisItemContainer {
        margin-top: 10px;
        border: 1px solid #409eff;
        flex-direction: column;

        .thesisItem {
            color: #121212;
            box-sizing: border-box;
            border: 1px solid #eee;
            margin: 10px 10px 10px 10px;

            .el-header {
                min-height: 60px;
                line-height: 60px;
                flex-direction: row;

                .title {
                    font-size: 22px;
                }

                .el-button {
                    float: right;
                    margin: 10px 10px;
                }

                .active {
                    background-color: skyblue;
                }

                .info {
                    line-height: 40px;

                    .author {
                        margin-left: 15px;
                        font-size: 16px;
                        color: #161823;
                    }

                    .page {
                        margin-left: 30px;
                        font-size: 16px;
                        color: #161823;
                    }
                }
            }

            .el-main {
                .thesisItemContent {
                    line-height: 40px;
                    font-size: 16px;
                }
            }

            .el-footer {
                padding: 10px 20px;

                .score {
                    color: #808080;
                    line-height: 40px;
                }

                .el-button {
                    float: right;
                    margin: 0 10px;
                }
            }
        }

        .el-pagination {
            margin: 0 auto 10px;
        }
    }

}
</style>