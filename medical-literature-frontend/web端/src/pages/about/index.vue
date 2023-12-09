<template>
  <div class="pageContainer">
    <div class="page1">
        <img src="@/assets/about1.png" alt="" class="about1">
        <router-link to="/home" class="enter1">进入体验</router-link>
    </div>
    <div class="page2">
        <div class="content">
            <img src="@/assets/about3.png" alt="" class="about3">
            <Transition name="about3" >
                <div class="text1" v-show="about3Flag">
                    MEON是MEDICAL EXAMINATION的缩写，是由学生团队开发的医学文献智慧检索平台。其在医学文献检索方面具有不俗的能力，以下是它的特征：
                    <br>
                    1.全面性2.精准性3.实时性4.多语言支持5.数据可视化6.数据库集成7.个性化8.阅读建议
                </div>
            </Transition>
        </div>
        <Transition name="about2">
            <img src="@/assets/about2.png" alt="" class="about2" v-show="about3Flag">
        </Transition>
    </div>
    <div class="page3">
        <img src="@/assets/about4.png" alt="" class="about4">
        <img src="@/assets/about5.png" alt="" class="about5">
        <Transition name="about6">
            <img src="@/assets/about6.png" alt="" class="about6"  v-show="about6Flag">
        </Transition>
        <img src="@/assets/about6.png" alt="" class="about6"  v-show="!about6Flag">
    </div>
    <div class="page4">
        <Transition name="about7">
            <img src="@/assets/about7.png" alt="" class="about7" v-show="about7Flag">
        </Transition>
        <Transition name="about8">
            <img src="@/assets/about8.png" alt="" class="about8" v-show="about7Flag">
        </Transition>
        <Transition name="about9">
            <img src="@/assets/about9.png" alt="" class="about9" v-show="about7Flag">
        </Transition>
    </div>
  </div>
</template>

<script>
export default {
    data(){
        return{
            about6Flag:false,
            about3Flag:false,
            about7Flag:false
        }
    },
    methods:{
        scrollHandle(){
            if(window.scrollY > 600){
                this.about3Flag = true
            }
            if(window.scrollY > 1200){
                this.about6Flag=true
            }
            if(window.scrollY > 1800){
                this.about7Flag=true
            }
        }
    },
    mounted(){
        window.addEventListener('scroll',this.scrollHandle)
        window.scrollTo(0,0)
    },
    beforeDestroy(){
        window.removeEventListener('scroll',this.scrollHandle)
    }
}
</script>

<style lang="less">
.pageContainer{
    width: 100%;
    height: 850px;
    .page1{
        width: 100%;
        height: 850px;
        position: relative;
        img{
            width: 100%;
            height: 100%;
        }
        .enter1{
            position: absolute;
            text-decoration: none;
            top: 57%;
            font-size: 32px;
            color:#eee ;
            left: 44.5%;
        }
    }
    .page2{
        width: 100%;
        height: 850px;
        position: relative;
        .about2{
            position: absolute;
            width:30%;
            right: 0;
            top: 50%;
        }
        .about3{
            width: 35%;
            position: absolute;
            top: 50px;
            left: 0;
            height: 10%;
        }
        .text1{
            width: 70%;
            position: absolute;
            height: 50%;
            top: 18%;
            font-size:30px ;
            line-height: 40px;
            color:#121212;
            opacity: 1;
        }
        .about2-enter-active{
        transition:  1.5s ease 0.5s;
        }
        .about3-enter-active{
        transition:  1s ease;
        }

        .about2-enter{
            opacity: 0;
            top: 70%;
        }
        .about3-enter {
            opacity: 0;
        }
    }
    .page3{
        width: 100%;
        height: 850px;
        position: relative;
        .about4{
            width: 35%;
            position: absolute;
            top: 50px;
            left: 0;
            height: 10%;
        }
        .about5{
            position: absolute;
            top: 30%;
            width: 70%;
            height: 50%;
        }
        .about6{
            position: absolute;
            top: 55%;
            width: 45%;
            height: 25%;
            z-index: 1;
            left: 60%;
        }
        .about6-enter{
            position: absolute;
            top: 30%;
            width: 70%;
            height: 50%;
            z-index: 1;
            left: 0;
        }
        .about6-enter-to{
            position: absolute;
            top: 55%;
            width: 45%;
            height: 25%;
            z-index: 1;
            left: 60%;
        }
        .about6-enter-active{
            transition: 2s ease-in-out 0.3s;
        }
    }
    .page4{
        width: 100%;
        height: 850px;
        position: relative;
        .about7{
            position: absolute;
            width: 45%;
            height: 90%;
            left: 0;
            top: 0;
        }
        .about8{
            position: absolute;
            width: 45%;
            height: 50%;
            left: 50%;
            top: 2%;
        }
        .about9{
            position: absolute;
            width: 45%;
            height: 10%;
            left: 50%;
            top: 55%;
        }
        .about8-enter-active,
        .about9-enter-active{
            transition:  1s ease;
        }
        .about7-enter-active{
            transition:  1.5s ease;
        }
        .about7-enter{
            opacity: 0;
            top: 30%;
        }
        .about9-enter{
            opacity: 0;
            top: 70%;
        }
        .about8-enter {
            top: 30%;
            opacity: 0;
        }
    }
}

</style>