module.exports = {
  outputDir: 'dist/static',
    devServer: {
        open: process.platform === 'darwin',
        host: '0.0.0.0',
        port: 8080,
        https: false,
        hotOnly: false,
        proxy: 'http://localhost:8081'
    }
 };
