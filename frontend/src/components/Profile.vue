<template>
  <div class="profile">
    <!--修改公司信息-->

    <div class="whole">
      <div class="left">
        <div class="logo">{{all_profile_data.orgType}}</div>

      </div>

      <div class="right">
        <div class="formP">
          <div class="right_le">公司名称</div>
          <div class="right_ri">{{all_profile_data.orgName}}</div>
        </div>

        <div class="formP">
          <div class="right_le">公司ID</div>
          <div class="right_ri">{{all_profile_data.orgID}}</div>
        </div>

        <div class="formP">
          <div class="right_le">创建时间</div>
          <div class="right_ri">{{all_profile_data.createTimestamp}}</div>
        </div>

        <div class="formP">
          <div class="right_le">可用授信金额</div>
          <div class="right_ri">{{all_profile_data.creditLimit}}</div>
        </div>

        <div class="formP">
          <div class="right_le">更新时间</div>
          <div class="right_ri">{{all_profile_data.updateTimestamp}}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Profile',
  props: { // 接收父组件传来的数据，并指定类型
    username: {
      type: String,
      required: true
    } },
  created() {
    this.$axios({
      method: 'post',
      url: 'http://119.23.78.199:8080/profile',
      data: {
        orgName: this.username
      }
    }).then(res => {
      this.all_profile_data = res.data;
    })
  },

  data() {
    return {
      all_profile_data: {}

    }
  },
  methods: {

  },
}

</script>

  <style scoped>
.profile {
  color: rgb(82, 99, 189);
  font-family: "微软雅黑";
  padding-bottom: 10%;
}

.whole {
  display: flex;
  width: 70%;
  margin-left: 15%;
  margin-top: 5%;
}

.left {
  width: 30%;
}

.logo {
  width: 46%;
  border: 1px solid grey;
  border-radius: 40%;
  color: rgb(108, 133, 166);
  height: 46%;
  font-size: 50px;
  background-color: rgb(235, 243, 249);
  display: flex;
  justify-content: center;
  align-items: center;
  margin-left: 30%;
}

.modify {
  width: 46%;
  /*border:1px solid grey;*/
  font-size: 20px;
  color: rgb(235, 243, 249);
  /*background-color:rgb(109,147,198);*/
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 8%;
  height: 15%;
  margin-left: 30%;
  margin-top: 13%;
}

.right {
  width: 70%;
}

.formP {
  display: flex;
  width: 100%;
  margin-top: 5%;
  font-weight: bold;
}

.right_le {
  background-color: rgb(182, 206, 238);

  width: 20%;
  display: flex;
  justify-content: center;
  border: 1px solid grey;
  font-size: 18px;
  color: black;
}

.right_ri {
  width: 80%;
  display: flex;
  justify-content: center;
  border: 1px solid grey;
  background-color: rgb(235, 243, 249);
  color: rgb(108, 133, 166);
  font-size: 16px;
}
</style>
