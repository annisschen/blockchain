<template>
  <div class="home-view-container">
    <!--header-->
    <div class="head_back">
      <Header />
      <b-tabs align="right">
        <b-tab class="guide" title="登 录" active @click="goto_home()"></b-tab>
        <b-tab class="guide" title="注 册" @click="goto_register()"></b-tab>
      </b-tabs>
    </div>
    <Login />
  </div>
</template>

<script>
// @ is an alias to /src
import Header from '@/components/Header.vue'
import Login from '@/components/Login.vue'

export default {
  name: 'Home',
  components: {
    Header,
    Login
  },
  methods: {
    /**
     * TODO: 页面切换时的过渡动画
     * 怎么实现记录在每个路由组件的停留时间
     * 不考虑在每个路由里面记录时间，要统一记录\
     */
    goto_register() {
      this.$router.push({ path: '/register' })
    },
    goto_home() {
      this.$router.push({ path: '/' })
    }
  }

}

</script>

<style>
.home-view-container {
  font-family: "微软雅黑";
}

.head_back {
  width: 100%;
  height: 100%;
  background-color: rgb(255, 255, 255);
}

.head_title {
  font-size: 35px;
  padding-top: 0.7%;
  margin-left: 3%;
  text-align: left;
}

.container {
  width: 100%;
  padding-right: 0px;
  padding-left: 0px;
  margin-right: 0;
  margin-left: 0;
  max-width: 100%;
}

.nav-tabs .nav-item.show .nav-link,
.nav-tabs .nav-link.active {
  color: #5263bd;
  border-color: #dee2e6 #dee2e6 #fff;
  font-weight: bold;
  margin-left: 10px;
  margin-right: 10px;
}

a {
  color: rgb(148, 160, 170);
}
</style>
