# spring-boot-vue-cli-demo

这个工程演示了如何整合 gradle、spring-boot 和 vue-cli。编程语言是 Kotlin, 当然，你可以很容易的转为 Java。

关键点有几个：

根目录下的（其实你也可以用另一个子工程）build.gradle 添加对子工程（web）的引用，以保证编译 spring-boot 工程时，先编译 web 工程。

    // file: build.gradle
    dependencies {
        compile project(":web")
    }

web 子工程里加入 Vue.config.js 文件，并添加如下代码，让输出目录改为 dist/static 。

    // file: Vue.config.js
    module.exports = {
        outputDir: 'dist/static'
    };

web 子工程的 build.gradle 文件如下：

    plugins {
        id "com.moowork.node" version "1.2.0"
        id 'java'
    }
    //调用npm run build命令的Gradle任务
    task npmBuild(type: NpmTask, dependsOn: npmInstall) {
        group = 'node'
        args = ['run', 'build']
    }

    // 设置Gradle的java插件的 compileJava 任务依赖npmBuild, 即 web 子模块编译前必须运行 npm run build
    compileJava.dependsOn npmBuild

    //调用npm run dev
    task npmDev(type: NpmTask, dependsOn: npmInstall) {
        group = 'node'
        args = ['run', 'dev']
    }

    clean {
        delete 'dist'
    }

    sourceSets {
        main {
            resources {
                srcDir 'dist'
            }
        }
    }

另外，为了方便开发，在 Vue.config.js 中加入了代理配置：

      devServer: {
        open: process.platform === 'darwin',
        host: '0.0.0.0',
        port: 8080,
        https: false,
        hotOnly: false,
        proxy: 'http://localhost:8081'
      }

并把 spring-boot 的监听端口改为了 8081。这样，在 web 目录下，通过命令

    npm run serve
    
启动 node.js 内置的 web 服务器以后，就可以把 api 取数据相关的请求代理到 spring-boot 的后端服务。  
通过 http://localhost:8080/ 来访问页面，可以保证 vue 相关的前端改动立刻生效。  
用代理是因为 chrome 之类的新浏览器，会拦截跨域访问。

PS: 用 node 服务调试的时候，可以注释掉 compile project(":web") 以加快编译速度。

## 参考
感谢 [煲煲菜](https://segmentfault.com/u/caibaohong) 的文章：[使用Gradle整合SpringBoot+Vue.js-开发调试与打包](https://segmentfault.com/a/1190000008968295)

这个工程主要参考了它
