<template>
  <div class="search-waybill-view-container">
    <div class="form_top">
      <strong>查询 运单</strong>
    </div>

    <template>
      <el-table :data="all_data" style="width: 80%; margin-left: 10%;" border>
        <el-table-column prop="createTimestamp" label="发起日期" sortable></el-table-column>

        <el-table-column prop="recipientOrgName" label="运输公司"></el-table-column>

        <el-table-column prop="transID" label="运单编码"></el-table-column>

        <el-table-column prop="transStatus" label="运单状态"></el-table-column>
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
    }
  },

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
        transType: 'dist',
        init_rec: 2
      }
    }).then(res => {
      this.all_data = res.data;
    })
  },
}
</script>

<style>
</style>
