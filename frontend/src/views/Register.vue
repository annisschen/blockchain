<template>
  <div class="register-view-container">
    <!--header-->
    <div class="head_back">
      <Header />
      <b-tabs align="right">
        <b-tab class="guide" title="登 录" active @click="goto_home()"></b-tab>
        <b-tab class="guide" title="注 册" active @click="goto_register()"></b-tab>
      </b-tabs>
    </div>

    <!--form_body-->
    <el-form label-width="80px" :model="form" class="from_back" :inline="true">
      <h2 class="form_title">
        注册新账号
        <p class="form_sec_title">填表注册使用平台</p>
      </h2>

      <el-form-item label="注册类型" class="content_text_register">
        <el-select v-model="form.orgType" placeholder="请选择注册类型">
          <el-option value="企业"></el-option>
          <el-option value="银行"></el-option>
          <el-option value="保险公司"></el-option>
          <el-option value="运输公司"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="用户名" class="content_text_register">
        <el-input v-model="form.orgName" placeholder="请输入用户名"></el-input>
      </el-form-item>

      <el-form-item label="密 码" class="content_text_register">
        <el-input v-model="form.password" placeholder="请输入密码"></el-input>
      </el-form-item>

      <el-form-item label="确认密码" class="content_text_register">
        <el-input v-model="form.check_password" placeholder="请重新输入密码"></el-input>
      </el-form-item>

      <el-row :gutter="10" class="bottom_block">
        <el-col :span="8" :push="1">
          <h4 class="guide_title">
            已有金融平台账号？
            <router-link to="/" class="guide_sec_title">前往登陆</router-link>
          </h4>
        </el-col>
        <el-col :span="6" :offset="10" :push="2">
          <el-button type="primary" @click="handleRegister">Sign Up</el-button>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script>
import Header from '@/components/Header.vue'
export default {
  name: 'Register',
  components: {
    Header
  },
  data() {
    return {
      form: {
        orgName: '',
        password: '',
        check_password: '',
        orgType: ''
      },
      type: '',
    };
  },
  methods: {
    goto_register() {
      this.$router.push({ path: '/register' });
    },
    goto_home() {
      this.$router.push({ path: '/' });
    },
    handleRegister() {
      if (this.form.password !== this.form.check_password) {
        this.$message.error({
          message: '注册失败！两次所输入的密码不同！'
        })
      } else if (this.form.password === '' || this.form.orgName === '' || this.form.orgType === '') {
        this.$message.error({
          message: '注册失败！请输入相应信息'
        })
      } else {
        this.$axios({
          method: 'post',
          url: '/register',
          data: {
            orgName: this.form.orgName,
            password: this.form.password,
            orgType: this.type,
          }
        }).then(res => {
          if (res.data.success === true) {
            this.$message('注册成功，即将前往登录界面');
            this.$router.push({ path: '/' })
          } else {
            this.$message.error({
              message: res.data.message + '！用户名已存在，请使用其他用户名注册'
            })
          }
        })
      }
    },
    get_orgType() {
      switch (this.form.orgType) {
        case '企业':
          this.type = 'C'
          break
        case '银行':
          this.type = 'B'
          break
        case '保险公司':
          this.type = 'I'
          break
        default:
          this.type = 'L'
          break
      }
    }

  }
}
</script>

<style>
.register-view-container {
  color: rgb(82, 99, 189);
  font-family: "微软雅黑";
}

.content_text_register {
  width: 40%;
  margin-left: 6%;
  margin-right: 6%;
  size: large;
}
</style>
