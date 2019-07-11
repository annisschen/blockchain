<template>
  <div class="credit-launch-view-container">
    <div class="form_top">
      <strong>申请 授信</strong>
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
      <div style="display: inline" class="form_left">请输入申请金额</div>
      <div style="display: inline" class="form_right">
        <el-input v-model="amount" placeholder="请输入申请的授信金额"></el-input>
      </div>
    </div>

    <div class="form">
      <div style="display: inline" class="form_left">选择抵押品证明文件</div>
      <div style="display: inline">
        <el-upload
          class="form_right"
          action="/upload"
          name="contract"
          :before-upload="onBeforeUpload"
        >
          <el-button size="small" type="primary">点击上传</el-button>
          <div slot="tip" class="el-upload__tip">只能上传jpg/png/pdf文件，且不超过1000kb</div>
        </el-upload>
      </div>
    </div>

    <el-button type="primary" class="button_submit" :plain="true" @click="submit_reminder">
      <strong>申请 授信</strong>
    </el-button>
  </div>
</template>

<script>
export default {
  name: 'LaunchCredit',
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
      this.all_bank = res.data;
    })
  },

  data() {
    return {
      initiatorName: this.username,
      recipientName: '',
      amount: null,
      all_bank: [],
      contractName: '',

    }
  },

  methods: {
    // 检查文件格式和大小
    onBeforeUpload(file) {
      this.contractName = file.name
      var suffix = file.name.substring(file.name.lastIndexOf('.') + 1)

      if (file.size / 1024 / 1024 > 10) {
        this.$message({
          message: '上传文件大小不能超过 10MB!',
          type: 'warning'
        });
        return false
      }

      if (suffix === 'jpg' || suffix === 'pdf' || suffix === 'png') {
        return true
      } else {
        this.$message({
          message: '上传文件只能是 jpg / png / pdf 格式!',
          type: 'warning'
        })
      }
      return false
    },

    submit_reminder() {
      this.$axios({
        method: 'post',
        url: '/enterprise/ApplyCredit',
        data: {
          initiatorName: this.username,
          recipientName: this.recipientName,
          amount: this.amount,
          contractName: this.contractName,
          information: this.contractName,
        },

      }).then(res => {
        if (res.data.success === true) {
          this.$message({
            message: '恭喜你，合同发送成功',
            type: 'success'
          })
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
.el-input__inner {
  background-color: #ebf3f9;
  border: 0px;
  font-size: 20px;
  color: rgb(108, 133, 166);
  /*font-weight: bold;*/
}
</style>
