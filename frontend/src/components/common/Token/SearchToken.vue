<template>
  <div class="token-search-view-container">
    <div class="form_top">
      <strong>查询 Token</strong>
    </div>

    <template>
      <el-table
        :data="(all_data1.concat(all_data2)).concat(all_data3)"
        style="width: 80%; margin-left: 10%;"
        border
      >
        <el-table-column prop="transTimestamp" label="发起日期" sortable></el-table-column>

        <el-table-column prop="fromOrg" label="Token发送方"></el-table-column>

        <el-table-column prop="recOrg" label="Token接受方"></el-table-column>

        <el-table-column prop="amount" label="金额"></el-table-column>
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
      all_data1: [],
      all_data2: [],
      all_data3: []
    }
  },

  created: function () {
    this.$axios({
      method: 'post',
      url: '/token/queryOrgToken',
      data: {
        orgName: this.username,
        init_rec: 2,
        tokenStatus: 'wait'
      }
    }).then(res => {
      this.all_data1 = res.data;
    })
    this.$axios({
      method: 'post',
      url: '/token/queryOrgToken',
      data: {
        orgName: this.username,
        init_rec: 2,
        tokenStatus: 'OK'
      }
    }).then(res => {
      this.all_data2 = res.data;
    })
    this.$axios({
      method: 'post',
      url: '/token/queryOrgToken',
      data: {
        orgName: this.username,
        init_rec: 2,
        tokenStatus: 'reject'
      }
    }).then(res => {
      this.all_data3 = res.data;
    })
  },

}
</script>

    <style>
</style>
