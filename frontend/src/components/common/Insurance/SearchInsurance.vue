<template>
  <div class="insurance-search-view-container">
    <div class="form_top">
      <strong>查询 保单</strong>
    </div>

    <template>
      <el-table :data="all_data" style="width: 80%; margin-left: 10%;" border>
        <el-table-column prop="createTimestamp" label="发起日期" sortable></el-table-column>

        <el-table-column prop="initiatorOrgName" label="发起公司"></el-table-column>

        <el-table-column prop="recipientOrgName" label="保险公司"></el-table-column>

        <el-table-column prop="transID" label="保单编码"></el-table-column>

        <el-table-column prop="amount" label="金额"></el-table-column>

        <el-table-column prop="transStatus" label="合同状态"></el-table-column>
      </el-table>
    </template>
  </div>
</template>

<script>
export default {
  props: { // 接收父组件传来的数据，并指定类型
    username: {
      type: String,
      required: true
    }  },

  data() {
    return {
      all_data: []
    }
  },

  created: function () {
    this.$axios({
      method: 'post',
      url: '/transaction/queryType',
      data: {
        orgName: this.username,
        transType: 'insurance',
        init_rec: 2
      }
    }).then(res => {
      this.all_data = res.data
    })
  },

}
</script>

<style>
</style>
