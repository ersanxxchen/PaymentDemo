# 支付网关Demo
Payment Gateway
已集成Bank Of American

执行sql文件:start.sql

application.yml需调整数据库连接相关内容\n
spring.datasource.url 修改为数据库jdbc:mysql://{ip}:{port}/{数据库名}
spring.datasource.username 修改为数据库用户名
spring.datasource.password 修改为数据库用户密码
示例:
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/demo?useUnicode=true&characterEncoding=utf-8&useSSL=false&autoReconnect=true&failOverReadOnly=false&serverTimezone=GMT
    username: root
    password: root
