<template>
  <div class="bank-view-container">
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
          <template slot="title">授信 管理</template>
          <el-menu-item index="2-1">审核 授信申请</el-menu-item>
          <el-menu-item index="2-2">查询 授信</el-menu-item>
        </el-submenu>

        <el-submenu index="3">
          <template slot="title">Token兑付 管理</template>
          <el-menu-item index="3-1">兑付 Token</el-menu-item>
          <el-menu-item index="3-2">查询 Token</el-menu-item>
        </el-submenu>
        <el-menu-item index="4">个人主页</el-menu-item>
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

import CreditAppraise from '@/components/Bank/CreditAppraise';
import BankCreditSearch from '@/components/Bank/BankCreditSearch';

import TokenConvert from '@/components/Bank/TokenConvert';
import BankTokenSearch from '@/components/Bank/BankTokenSearch';

export default {
  components: {
    Header,
    Profile,

    CreditAppraise,
    BankCreditSearch,

    TokenConvert,
    BankTokenSearch
  },
  data() {
    return {
      currentTabComponent: 'Profile',
      activeIndex: '4',
      activeIndex2: '4',
      username: this.$route.query.username,
      type: 'C'
    };
  },
  methods: {
    handleSelect(key, keyPath) {
      switch (key) {
        case '4':
          this.currentTabComponent = 'Profile';
          break

        case '3-1':
          this.currentTabComponent = 'TokenConvert';
          break
        case '3-2':
          this.currentTabComponent = 'BankTokenSearch';
          break

        case '2-1':
          this.currentTabComponent = 'CreditAppraise';
          break
        case '2-2':
          this.currentTabComponent = 'BankCreditSearch';
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
</style>
