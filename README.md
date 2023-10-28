# 支付网关Demo
Payment Gateway
已集成Bank Of American

执行sql文件:start.sql</br>

application.yml需调整数据库连接相关内容</br>
spring.datasource.url 修改为数据库jdbc:mysql://{ip}:{port}/{数据库名}</br>
spring.datasource.username 修改为数据库用户名</br>
spring.datasource.password 修改为数据库用户密码</br>
示例:</br>
~~~xml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/demo?useUnicode=true&characterEncoding=utf-8&useSSL=false&autoReconnect=true&failOverReadOnly=false&serverTimezone=GMT
    username: root
    password: root
