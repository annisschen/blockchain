<template>
  <div class="accept-token-container">
    <div class="form_top">
      <strong>承兑 Token</strong>
    </div>

    <div class="form">
      <div style="display: inline" class="form_left">选择申请银行</div>
      <div style="display: inline">
        <select v-model="recipientName" class="form_right">
          <option v-for="orgNames in all_bank" :key="orgNames.orgID">{{orgNames.orgName}}</option>
        </select>
      </div>
    </div>

    <div class="form">
      <div style="display: inline" class="form_left">请输入承兑Token数量</div>
      <div style="display: inline" class="form_right">
        <el-input v-model="amount" placeholder="请输入承兑Token数量"></el-input>
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
  created() {
    this.$axios({
      method: 'post',
      url: '/queryOrgType', // bank的接口数据API
      data: {
        orgType: 'B'
      }
    }).then(res => {
      this.all_bank = res.data
    })
  },
  // 获得企业list

  data() {
    return {
      amount: null,
      recipientName: '',
      all_bank: [],
    }
  },
  methods: {
    // 返回合同数据给后端
    /*           initiatorName: "enter", */
    submit_reminder() {
      this.$axios({
        method: 'post',
        url: '/enterprise/ApplyRest',
        data: {
          initiatorName: this.username,
          recipientName: this.recipientName,
          transType: 'token',
          amount: this.amount,
          contractName: '1.jpg',
          information: ''
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
