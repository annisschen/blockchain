<template>
  <div class="contract-confirm-view-container">
    <div class="form_top">
      <strong>确认 合同</strong>
    </div>
    <template>
      <el-table :data="orderList" style="width: 80%; margin-left: 10%;" border>
        <el-table-column prop="createTimestamp" label="发起日期" sortable></el-table-column>

        <el-table-column prop="initiatorOrgName" label="公司"></el-table-column>

        <el-table-column prop="transID" label="合同编码"></el-table-column>

        <el-table-column label="上传合同">
          <template slot-scope="scope">
            <el-upload
              action="/verifyHash"
              name="contract"
              :data="{transID:scope.row.transID}"
              :on-success="handleUploadSuccess"
            >
              <el-button size="mini" type="primary">上传</el-button>
            </el-upload>
          </template>
        </el-table-column>

        <el-table-column label="确认合同">
          <template slot-scope="scope">
            <el-button
              type="primary"
              size="mini"
              :disabled="confirm"
              @click="handleConfirm(scope.row.transID)"
            >确认</el-button>
          </template>
        </el-table-column>
      </el-table>
    </template>
  </div>
</template>

<script>
export default {
  name: 'ConfirmContract',
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
      url: '/enterprise/queryRecName',
      data: {
        orgName: this.username,
        transType: 'order',
      }
    }).then(res => {
      this.orderList = res.data
      console.log(this.orderList)
    })
  },

  data() {
    return {
      orderList: [],
      confirm:true,
      success: '',
      message: '',
    }
  },

  methods: {
    handleUploadSuccess(res, file, fileList) {
      this.success = res.success
      this.message = res.message
      if (this.success === false) {
        this.$message.error({
          message: res.message
        })
      } else {
        this.$message({
          message: '合同匹配成功',
          type: 'success'
        })
      }
    },

    handleConfirm(transID) {
      this.$axios({
        method: 'post',
        url: '/enterprise/dealRest',
        data: {
          transID: transID,
          transStatus: 'signed'
        }
      }).then(res => {
        if (res.success === false) {
          this.$message.error({
            message: res.message
          })
        } else {
          this.$message({
            message: '合同确认成功,即将前往主页',
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
.el-table .cell {
  text-align: center;
}
</style>
