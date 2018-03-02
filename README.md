# servlet-proxy
  - 使用servlet转发http请求
  - 通过配置文件实现多个servlet拦截转发，可配置通配符形式拦截url，目的url，host请求头配置
  
  ## 配置文件说明：
```xml
#tomcat监听端口
proxy.port=6445
#是否开启代理日志打印
proxy.logging_enabled=true

-

#代理名称配置,逗号分隔
proxy.servlets=servlet1,servlet2
-
proxy.servlet1.url_mapping=/*
proxy.servlet1.target_url=http://127.0.0.1:3000
proxy.servlet1.host=http://127.0.0.1:3000
-

proxy.servlet2.url_mapping=/pc/login.html
proxy.servlet2.target_url=http://127.0.0.1:3000/login.html?foo=bar
proxy.servlet2.host=http://127.0.0.1:3000

```