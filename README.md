1、本项目分两个工程进行部署，部署打包的模块为： supplier-deploy和portal-deploy<br/>
2、具体依赖关系查看pom文件的dependency<br/>
3、supplier-api对外提供供应商管理的服务的接口<br/>
4、portal-api对外提供用户访问平台的接口<br/>
5、platform-common提供整个项目功能的框架功能<br/>
6、platform-pub提供公共业务功能<br/>
7、platform-cloud提供云服务功能<br/>

技术选型：<br/>
采用sprintboot作为基础框架<br/>
阿里云的oss作为存储<br/>
短信的下发为阿里云的短信服务<br/>
快递查询是kuai100提供服务<br/>
idCard识别也是由阿里云提供<br/>
系统的缓存使用了redis<br/>


<div>
                  <img alt="系统截图" width="200" height="290" src="https://gitee.com/liuhangjun/mina_social_business/raw/master/resources/images/1.jpeg">
                  <img alt="系统截图" width="200" height="290" src="https://gitee.com/liuhangjun/mina_social_business/raw/master/resources/images/2.jpeg">
                  <img alt="系统截图" width="200" height="290" src="https://gitee.com/liuhangjun/mina_social_business/raw/master/resources/images/3.png">
                  <img alt="系统截图" width="200" height="290" src="https://gitee.com/liuhangjun/mina_social_business/raw/master/resources/images/4.jpeg">
                  </div>