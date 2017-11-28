LTL 是一个仿京东购物商城 完成的一个简易购物网站的整体搭建，此项目不追求 功能的完整性，只用作 javaWeb开发领域内 技术的验证，以及使用，为技术的积累提供一个相对完善的开发环境，后端整体使用 spring + springMVC + mybatis 进行搭建，前端套用京东网站页面，一下部分 对个别技术点进行说明。

一、ORM框架-mybatis

  ORM框架采用MyBatis，为了提高开发效率，有两种实现方式
  1.先根据数据库表单结构自动生成Model和MyBatis相关类，生成命令如下：

    java -jar mybatis-generator-core-1.3.1.jar -configfile config_privilege.xml -overwrite

    生成时需要把mybatis-generator-core-1.3.1.jar、mysql-connector-java-5.1.24-bin.jar、config_privilege.xml放到一个目录下，生成的相关类和XML会放置到CreateResult文件夹下面。

    参考网址： http://www.mybatis.org/generator/ http://pan.baidu.com/s/1qW98L0C http://qiuguo0205.iteye.com/blog/819100 http://jadethao.iteye.com/blog/1726115

  2.与Model 对应的Map 实现com.github.abel533提供的通用类，可以快速的实现对单表的增删改查操作，但多表操作依然要自己实现SQL语句的编写

二、前后端通讯方式
  
  1.ajax 异步调用，后端利用springMVC实现 符合restful风格的接口供客户端调用。
  
    跨域问题解决方案：1.利用 jsonp 配合后端对spring 消息转换器的特定实现 解决跨域和非跨域的 无感切换
                    2. 利用 NGINX 反向代理实现 后端无感的跨域解决
  
  2.客户端通过httpclient 对服务端提供的restful接口  实现同域名下接口封装，也是对跨域问题解决的一种方式
    缺点：增加开发环节，开发复杂化
   
  3.采用Dubbo 实现客户端、服务器端服务调度以及服务的监控和管理
    Dubbo 的本地搭建以及使用可参考 Dubbo官网：http://alibaba.github.io/dubbo-doc-static/Home-zh.htm

  4.采用 rabbitMQ 实现不同系统间的 消息同步，以及信息更新
    rabbitMQ 的使用和搭建 参考官网http://www.rabbitmq.com/ 以及 各种技术博客

三、sso 单点登录系统

  此处 对单点登录系统进行了简易的实现，待后续更新
  
  实现：利用Redis 实现跨服务 用户登录信息的保存
        利用rabbitMQ 实现跨服务 用户信息的gengxin
  用户信息在浏览器端 使用cookie保存

四、Dubbo

  使用Dubbo 对多平台间的服务调用 进行改进，以实现不同平台服务的 管理和监控

五、Redis
  
  redis 实现用户信息，页面物品信息的缓存提高性能
  redis的使用和搭建 参考redis 官网

六、NGINX

  NGINX进行 利用反向代理以及，多节点的配置，静态文件缓存等功能提高服务性能
 
七、solr

  使用solr 完成 海量数据的快速检索 并配合rabbitMQ 完成检索数据的 实时更新 同步
  solr的使用和搭建 参考solr 官网

八、数据库Mysql

  使用开源数据库 并实现以主从配置为基础的 数据读写分离 提高服务性能

九、仿京东购物车
  
  仿京东购物车 完成登录和 非登录状态下 物品信息入库的保存逻辑

九、分页逻辑

  使用com.github.pagehelper.PageInterceptor 提供的工具 完成分页逻辑
