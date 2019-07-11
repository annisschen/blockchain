<template>
  <div class="token-search-view-container">
    <div class="form_top">
      <strong>查询 Token</strong>
    </div>

    <template>
      <el-table :data="all_data" style="width: 80%; margin-left: 10%;" border>
        <el-table-column prop="transTimestamp" label="发起日期" sortable></el-table-column>

        <el-table-column prop="initOrg" label="token申请方"></el-table-column>

        <el-table-column prop="recOrg" label="token接受方"></el-table-column>

        <el-table-column prop="tokenStatus" label="申请状态"></el-table-column>

        <el-table-column prop="amount" label="金额"></el-table-column>
      </el-table>
    </template>
  </div>
</template>

<script>
export default {
  name: 'BankTokenSearch',
  props: { // 接收父组件传来的数据，并指定类型
    username: {
      type: String,
      required: true
    }
  },

  data() {
    return {
      all_data: [],
    }
  },

  created: function () {
    this.$axios({
      method: 'post',
      url: '/token/queryOrgTokenAll',
      data: {
        orgName: this.username,

      }
    }).then(res => {
      this.all_data = res.data;
    })
  }

}
</script>
<style>
</style>
