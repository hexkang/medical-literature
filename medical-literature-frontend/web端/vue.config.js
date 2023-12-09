const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave:false,
  devServer: {
    host: 'localhost',
    port: 8080,
    proxy: {
      // 本地使用代理  线上使用nginx
      '/api': {
        target: `http://43.139.73.93:8081/`,
        changeOrigin: true,
        pathRewrite: {
          '^/api': '/api'
        }
      }
    }
  }
})
