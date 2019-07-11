<template>
  <div id="app">
    <div id="nav">
      <router-link to="/">Home</router-link> |
      <router-link to="/bank">Bank</router-link> |
      <router-link to="/enterprise">Enterprise</router-link> |
      <router-link to="/insurance">Insurance Company</router-link> |
      <router-link to="/transportation">Transportation Company</router-link> |
    </div>
    <b-container>
      <router-view />
    </b-container>

  </div>
</template>

<script>
export default {
}
</script>

<style lang="scss">
body,
html {
  padding: 0;
  margin: 0;
  width: 100%;
  min-height: 100vh;
}

body {
  background-color: rgb(231, 245, 252);
}

#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: left;
  color: #2c3e50;
}
#nav {
  padding: 30px;
  a {
    font-weight: bold;
    color: #2c3e50;
    &.router-link-exact-active {
      color: #42b983;
    }
  }
}
</style>
