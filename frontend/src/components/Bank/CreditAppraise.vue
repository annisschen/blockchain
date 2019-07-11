<template>
  <div class="credit-appraise-view-container">
    <div class="form_top">
      <strong>审核 授信申请</strong>
    </div>

    <template>
      <el-table :data="apply_enterprise" style="width: 80%; margin-left: 10%;" border>
        <el-table-column prop="createTimestamp" label="申请日期" sortable></el-table-column>

        <el-table-column prop="initiatorOrgName" label="申请公司"></el-table-column>

        <el-table-column prop="amount" label="申请金额"></el-table-column>

        <el-table-column label="查看抵押品">
          <template slot-scope="scope">
            <el-button size="mini" @click="confirm(scope.row.transID)">下载</el-button>
          </template>
        </el-table-column>

        <el-table-column label="确认申请">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" @click="confirm(scope.row.transID)">确认</el-button>
          </template>
        </el-table-column>
      </el-table>
    </template>
  </div>
</template>

<script>
export default {
  name: 'CreditAppraise',
  props: {
    username: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      apply_enterprise: [],
      file: Object
    }
  },

  methods: {

    download(transID) {
      window.location.href = this.returnURL(transID)
    },

    confirm(ID) {
      this.$axios({
        method: 'post',
        url: '/enterprise/dealRest',
        data: {
          transID: ID,
          transStatus: 'signed'
        }
      }).then(res => {
        if (res.success === false) {
          this.$message.error({
            message: res.message
          })
        } else {
          this.$message({
            message: '授信审核成功,即将前往主页',
            type: 'success'
          })
          this.$router.go(0, { query: { username: this.username } })
        }
      })
    },

    returnURL(transID) {
      var a = 'http://119.23.78.199:8080/download/?transID=' + transID
      return a
    }
  },
  created() {
    this.$axios({
      method: 'post',
      url: '/bank/queryRecName',
      data: {
        orgName: this.username,
      }
    }).then(res => {
      this.apply_enterprise = res.data;
    })
  }

}

</script>

  <style >
</style>
