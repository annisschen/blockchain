<template>
  <div class="lunch_insurance_view-container">
    <div class="form_top">
      <strong>发起 保单</strong>
    </div>
    <div class="form">
      <div style="display: inline" class="form_left">选择保险公司</div>
      <div style="display: inline">
        <select v-model="recipientName" class="form_right">
          <option v-for="orgNames in company_data" :key="orgNames.orgID">{{orgNames.orgName}}</option>
        </select>
      </div>
    </div>

    <div class="form">
      <div style="display: inline" class="form_left">输入保险金额</div>
      <div style="display: inline" class="form_right">
        <el-input v-model="amount" placeholder="请输入输入支付Token的数量"></el-input>
      </div>
    </div>

    <div class="form">
      <div style="display: inline" class="form_left">选择线下签署合同</div>
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
      <strong>发起 保单</strong>
    </el-button>
  </div>
</template>

<script>
import { mapState } from 'vuex';

export default {
  name: 'LaunchContract',
  props: { // 接收父组件传来的数据，并指定类型
    username: {
      type: String,
      required: true
    } },
  components: {

  },

  created() {
    this.$axios({
      method: 'post',
      url: '/queryOrgType',
      data: {
        orgType: 'I'
      }
    }).then(res => {
      this.company_data = res.data
    })
  },

  data() {
    return {
      company_data: [],
      recipientName: '',
      contractName: '',
      amount: null,
    }
  },
  methods: {
    // 返回合同数据给后端
    submit_reminder() {
      this.$axios({
        method: 'post',
        url: '/enterprise/ApplyRest',
        data: {
          initiatorName: this.username,
          recipientName: this.recipientName,
          transType: 'insurance',
          amount: this.amount,
          contractName: this.contractName,
          information: ''
        },

      }).then(res => {
        if (res.data.success === true) {
          this.$message({
            message: '恭喜你，保单发送成功',
            type: 'success'
          })
        } else {
          this.$message.error({
            message: res.data.message
          })
        }
      })
    },

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
  },
  computed: {
    // listen on the state store on vuex
    ...mapState([
      // expect mapping state[category]
    ])
  }
}

</script>

<style>
</style>
