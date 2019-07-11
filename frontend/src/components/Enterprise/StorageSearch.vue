<template>
  <div class="storage-search-view-container">
    <div class="form_top">
      <strong>查询 库存</strong>
    </div>
    <div class="stoage-view-container">{{storage}}</div>
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
      storage: null
    }
  },

  created: function () {
    this.$axios({
      method: 'post',
      url: '/queryStore',
      data: {
        orgName: this.username,
      }
    }).then(res => {
      this.storage = res.data
    })
  },

  methods: {

  },

}
</script>

<style>
.stoage-view-container {
  text-align: center;
  color: blueviolet;
}
</style>
