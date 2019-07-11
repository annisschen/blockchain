<template>
  <div class="login">
    <!--form_body-->
    <el-form :label-position="labelPosition" label-width="80px" :model="form" class="from_back">
      <h2 class="form_title">
        {{Ftext}}
        <p class="form_sec_title">{{SecFtext}}</p>
      </h2>

      <el-form-item label="用户名" class="content_text">
        <el-input v-model="form.orgName"></el-input>
      </el-form-item>
      <el-form-item label="密 码" class="content_text">
        <el-input type=password v-model="form.password"></el-input>
      </el-form-item>

      <el-row :gutter="24" class="bottom_block">
        <el-col :span="8" :push="1">
          <h4 class="guide_title">
            {{Gtext}}
            <router-link to="/register" class="guide_sec_title">{{SecGtext}}</router-link>
          </h4>
        </el-col>
        <el-col :span="4" :offset="10" :push="2">
          <el-button type="primary" @click="login">Login</el-button>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script>
export default {
  name: 'Login',
  data() {
    return {
      Ftext: '欢迎回到金融平台 !',
      SecFtext: '登录金融平台',
      Gtext: '还没有账号？',
      SecGtext: '前往注册',
      labelPosition: 'top',
      form: {
        orgName: '',
        password: ''
      },

    }
  },
  methods: {
    login() {
      this.$axios({
        method: 'post',
        url: '/login',
        data: {
          orgName: this.form.orgName,
          password: this.form.password,
          orgType: 'B',
        }
      }).then(res => {
        if (res.data.success === true) {
          /* this.$router.push({ path: '/enterprise' }) // type要处理 */

          switch (res.data.message) {
            case 'B':
              this.$router.push({ path: '/bank', query: { username: this.form.orgName } })
              break
            case 'C':
              this.$router.push({ path: '/enterprise', query: { username: this.form.orgName } })
              break
            case 'I':
              this.$router.push({ path: '/insurance', query: { username: this.form.orgName } })
              break
            default:
              this.$router.push({ path: '/transportation', query: { username: this.form.orgName } })
              break
          }
        } else {
          this.$message.error({
            message: res.data.message
          })
        }
      })
    },

  }
}
</script>

<style>
.login {
  color: rgb(82, 99, 189);
  font-family: "微软雅黑";
}

.from_back {
  width: 50%;
  height: 30%;
  background-color: #fff;
  margin-left: 25%;
  margin-top: 6%;
  margin-bottom: 5%;
}
.form_title {
  margin-left: 6%;
  padding-top: 5%;
  padding-bottom: 6%;
  font-size: 32px;
  font-weight: bold;
}
.form_sec_title {
  font-size: 16px;
  display: inline;
  color: rgb(148, 160, 170);
  font-weight: initial;
}

.el-form-item {
  margin-bottom: 3%;
}

label {
  margin-bottom: 0;
}
.el-form-item__label {
  font-size: 16px;
}
.el-form--label-top .el-form-item__label {
  padding: 0 0 0px;
}

.content_text {
  margin-left: 6%;
  margin-right: 6%;
}

.guide_title {
  font-size: 16px;
  margin-left: 6%;
  margin-top: 6%;
  margin-bottom: 6%;
  color: rgb(148, 160, 170);
}

.guide_sec_title {
  color: #5263bd;
}

.bottom_block {
  margin-top: 8%;
  margin-bottom: 4%;
}
/*.el-input__inner {
     background-color:white;
     border: 1px solid #DCDFE6;
     font-size: inherit;
     color:black;

    }*/
</style>
