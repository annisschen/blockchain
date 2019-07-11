module.exports = {
  devServer: {
    open: 'google chrome',
    clientLogLevel: 'info',
    port: 3000,
    // lintOnSave: false,
    proxy: {
      '/': {
        /* target: 'http://rest.apizza.net/mock/c13edf073da9f394b65bc1c3d9ac1390/', */
        target: 'http://119.23.78.199:8080/',
        ws: false,
        changeOrigin: true
      }
    }
  },
  configureWebpack: {
    devtool: 'source-map'
  }
}
