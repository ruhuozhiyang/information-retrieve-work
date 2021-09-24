module.exports = {
    css: {
      loaderOptions: {
        less: {
          lessOptions: {
            javascriptEnabled: true,
          },
        },
      },
    },
    devServer: {
      port: 8000,
      proxy: {
        '/api': {
          target: 'http://127.0.0.1:7788',
          ws: true,
          changeOrigin: true,
        },
      },
    },
  };