<template>
  <div class="enterprise-view-container">
    <div class="head_back">
      <Header />
      <el-menu
        :default-active="activeIndex2"
        class="menu_style"
        mode="horizontal"
        @select="handleSelect"
        active-text-color="rgb(82, 99, 189)"
        text-color="rgb(148,160,170)"
      >
        <el-menu-item index="1">登 出</el-menu-item>
        <el-submenu index="2">
          <template slot="title">保单管理</template>
          <el-menu-item index="2-1">申请 保险</el-menu-item>
          <el-menu-item index="2-2">查询 保险</el-menu-item>
        </el-submenu>
        <el-submenu index="3">
          <template slot="title">仓物管理</template>
          <el-submenu index="3-1">
            <template slot="title">仓库管理</template>
            <el-menu-item index="3-1-1">入库确认</el-menu-item>
            <el-menu-item index="3-1-2">查询库存</el-menu-item>
          </el-submenu>
          <el-submenu index="3-2">
            <template slot="title">物流管理</template>
            <el-menu-item index="3-2-1">申请 运单</el-menu-item>
            <el-menu-item index="3-2-2">查询 运单</el-menu-item>
          </el-submenu>
        </el-submenu>
        <el-submenu index="4">
          <template slot="title">合同管理</template>
          <el-menu-item index="4-1">发起 合同</el-menu-item>
          <el-menu-item index="4-2">确认 合同</el-menu-item>
          <el-menu-item index="4-3">查询 合同</el-menu-item>
        </el-submenu>
        <el-submenu index="5">
          <template slot="title">银行/Token管理</template>
          <el-submenu index="5-1">
            <template slot="title">Tokern管理</template>
            <el-menu-item index="5-1-1">支付 Token</el-menu-item>
            <el-menu-item index="5-1-2">承兑 Token</el-menu-item>
            <el-menu-item index="5-1-3">查询 Token</el-menu-item>
          </el-submenu>
          <el-submenu index="5-2">
            <template slot="title">授信管理</template>
            <el-menu-item index="5-2-1">申请 授信</el-menu-item>
            <el-menu-item index="5-2-2">查询 授信</el-menu-item>
          </el-submenu>
        </el-submenu>
        <el-menu-item index="6">个人主页</el-menu-item>
      </el-menu>
    </div>
    <div>
      <keep-alive>
        <component v-bind:is="currentTabComponent" :username="username" :type="type"></component>
      </keep-alive>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex';

import Header from '@/components/Header.vue';
import Profile from '@/components/Profile';

// Token/授信管理
import PayToken from '@/components/common/Token/PayToken'
import AcceptToken from '@/components/common/Token/AcceptToken'
import SearchToken from '@/components/common/Token/SearchToken'
import LaunchCredit from '@/components/common/Credit/LaunchCredit'
import SearchCredit from '@/components/common/Credit/SearchCredit'

// 保险
import LaunchInsurance from '@/components/common/Insurance/LaunchInsurance'
import SearchInsurance from '@/components/common/Insurance/SearchInsurance'

// 合同管理
import ConfirmContract from '@/components/common/Contract/ConfirmContract';
import LaunchContract from '@/components/common/Contract/LaunchContract';
import SearchContract from '@/components/common/Contract/SearchContract';

// 仓物管理
import SearchWaybill from '@/components/common/Waybill/SearchWaybill'
import LaunchWaybill from '@/components/Enterprise/LaunchWaybill'
import ConfirmDelivery from '@/components/Enterprise/ConfirmDelivery'
import StorageSearch from '@/components/Enterprise/StorageSearch'

export default {
  components: {
    Header,
    Profile,

    // Token/授信管理
    PayToken,
    AcceptToken,
    SearchToken,
    LaunchCredit,
    SearchCredit,

    // 保险
    LaunchInsurance,
    SearchInsurance,

    // 合同管理
    LaunchContract,
    ConfirmContract,
    SearchContract,

    // 仓物管理
    SearchWaybill,
    LaunchWaybill,
    ConfirmDelivery,
    StorageSearch
  },
  data() {
    return {
      currentTabComponent: 'Profile',
      activeIndex: '6',
      activeIndex2: '6',
      username: this.$route.query.username,
      type: 'C'
    };
  },
  methods: {
    handleSelect(key, keyPath) {
      switch (key) {
        case '6':
          this.currentTabComponent = 'Profile';
          break

        case '5-1-1':
          this.currentTabComponent = 'PayToken';
          break
        case '5-1-2':
          this.currentTabComponent = 'AcceptToken';
          break
        case '5-1-3':
          this.currentTabComponent = 'SearchToken';
          break

        case '5-2-1':
          this.currentTabComponent = 'LaunchCredit';
          break
        case '5-2-2':
          this.currentTabComponent = 'SearchCredit';
          break

        case '4-1':
          this.currentTabComponent = 'LaunchContract';
          break
        case '4-2':
          this.currentTabComponent = 'ConfirmContract';
          break;
        case '4-3':
          this.currentTabComponent = 'SearchContract';
          break

        case '3-1-1':
          this.currentTabComponent = 'ConfirmDelivery';
          break
        case '3-1-2':
          this.currentTabComponent = 'StorageSearch';
          break
        case '3-2-1':
          this.currentTabComponent = 'LaunchWaybill';
          break
        case '3-2-2':
          this.currentTabComponent = 'SearchWaybill';
          break

        case '2-1':
          this.currentTabComponent = 'LaunchInsurance';
          break
        case '2-2':
          this.currentTabComponent = 'SearchInsurance';
          break

        case '1':
          this.$router.push({ path: '/' })
          break
      }
    }
  },
  computed: {
    // listen on the state store on vuex
    ...mapState({
      // expect mapping state[category]
    })
  }
};
</script>

<style>
.menu_style {
  background-color: white;
}
.el-menu--horizontal > .el-submenu {
  float: right;
  font-weight: bold;
}
.el-menu--horizontal > .el-menu-item {
  float: right;
  font-weight: bold;
}
</style>
