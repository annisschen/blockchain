<template>
  <div class="pay-token-container">
    <div class="form_top">
      <strong>支付 Token</strong>
    </div>
    <div class="form">
      <div style="display: inline" class="form_left">选择支付对象</div>
      <div style="display: inline">
        <select v-model="recipientName" class="form_right">
          <option v-for="orgNames in company_data" :key="orgNames.orgID">{{orgNames.orgName}}</option>
        </select>
      </div>
    </div>

    <div class="form">
      <div style="display: inline" class="form_left">输入支付Token数量</div>
      <div style="display: inline" class="form_right">
        <el-input v-model="amount" placeholder="请输入支付Token的数量"></el-input>
      </div>
    </div>

    <div class="form">
      <div style="display: inline" class="form_left">输入账户密码</div>
      <div style="display: inline" class="form_right">
        <el-input type=password v-model="password" placeholder="请输入密码"></el-input>
      </div>
    </div>

    <el-button type="primary" class="button_submit" :plain="true" @click="submit_reminder">
      <strong>支付 Token</strong>
    </el-button>
  </div>
</template>

<script>
export default {
  name: 'PayToken',
  props: { // 接收父组件传来的数据，并指定类型
    username: {
      type: String,
      required: true
    } },
  components: {

  },
  // 获得企业list
  created() {
    this.$axios({
      method: 'post',
      url: '/transaction/sponsor/init',
      data: {
        orgName: ' '
      }
    }).then(res => {
      this.company_data = res.data
    })
  },

  data() {
    return {
      company_data: [],
      fileList: [],
      recipientName: '',
      amount: null,
      password: '',

    }
  },
  methods: {
    // 返回合同数据给后端
    /*           initiatorName: "enter", */
    submit_reminder() {
      this.$axios({
        method: 'post',
        url: '/token/pay',
        data: {
          fromOrg: this.username,
          recOrg: this.recipientName,
          amount: this.amount,
          password: this.password,
        },

      }).then(res => {
        if (res.data.success === true) {
          this.$message({
            message: '恭喜你，交易成功',
            type: 'success'
          })
        } else {
          this.$message.error({
            message: res.data.message
          })
        }
      })
    },

  },
  computed: {

  }
}

</script>

    <style>
</style>
