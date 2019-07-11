<template>
  <div class="bank-token-convert-view-container">
    <div class="form_top">
      <strong>Token 兑付</strong>
    </div>
    <template>
      <el-table :data="orderList" style="width: 80%; margin-left: 10%;" border>
        <el-table-column prop="transTimestamp" label="发起日期" sortable></el-table-column>

        <el-table-column prop="initOrg" label="承兑公司"></el-table-column>

        <el-table-column prop="amount" label="承兑金额"></el-table-column>

        <el-table-column label="拒绝">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" @click="click_confirm(scope.row.tokenID,re)">拒绝</el-button>
          </template>
        </el-table-column>

        <el-table-column label="同意">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" @click="click_confirm(scope.row.tokenID,ok)">确认</el-button>
          </template>
        </el-table-column>
      </el-table>
    </template>
  </div>
</template>

<script>
export default {
  name: 'TokenConvert',
  props: { // 接收父组件传来的数据，并指定类型
    username: {
      type: String,
      required: true
    },
    type: {
      type: String,
      required: true
    }
  },

  created() {
    this.$axios({
      method: 'post',
      url: '/token/queryOrgToken',
      data: {
        orgName: this.username,
        init_rec: 1,
        tokenStatus: 'wait'
      }
    }).then(res => {
      this.orderList = res.data;
    })
  },

  data() {
    return {
      orderList: [],
      success: '',
      message: '',
      ok: 'OK', // 同意
      re: 'reject', // 拒绝
    }
  },

  methods: {

    click_confirm(transID, acceptStatus) {
      this.$axios({
        method: 'post',
        url: '/bank/accept',
        data: {
          tokenID: transID,
          tokenStatus: acceptStatus

        }
      }).then(res => {
        if (res.success === false) {
          this.$message.error({
            message: res.message
          })
        } else {
          this.$message({
            message: 'token兑付成功,即将前往主页',
            type: 'success'
          })
          this.$router.go(0, { query: { username: this.username } })
        }
      })
    }

  }
}
</script>

<style>
</style>
