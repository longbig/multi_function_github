# multi_function_github
运行环境：Java8，maven

- ChatGPT接入企业微信成为聊天机器人
- 羊了个羊无限通关，通关教程在公众号**卷福同学** 最新文章里，所用工具包在公众号里发送`羊了个羊`获取
- 有脚本运行问题可公众号内询问

![公众号二维码](https://raw.githubusercontent.com/longbig/multi_function_github/main/%E5%BE%AE%E4%BF%A1%E5%85%AC%E4%BC%97%E5%8F%B7.png)

## 脚本使用
### ChatGPT 3.5 / 4接入企业微信
- 修改application.properties文件的chatgpt开头 和wechat开头的配置，配置内容看注释
- 默认访问超时时间是30s，如需修改，可自行修改`OkHttpUtils`中`DEFAULT_TIME_OUT`的值
- 使用教程：[ChatGPT3.5接入企业微信且支持连续对话](https://longbig.github.io/2023/03/05/ChatGPT3-5%E6%8E%A5%E5%85%A5%E4%BC%81%E4%B8%9A%E5%BE%AE%E4%BF%A1%E4%B8%94%E6%94%AF%E6%8C%81%E8%BF%9E%E7%BB%AD%E5%AF%B9%E8%AF%9D/)
- 发送`开始连续对话`进入连续对话模式，注意连续对话模式下，chatGPT账号额度消耗是累加（每次发消息，会累加这次和过去所有对话的额度消耗）
- 发送`结束连续对话`关闭连续对话模式
- 发送`开始GPT4`使用GPT4模型，发送`结束GPT4`关闭GPT4模型
  
- 可修改application.properties文件里的`chatgpt.flow.num`配置，自行修改最大对话次数（不建议太大，官方限制的消息最大长度是4096 token，大概20次对话之后就会超了）

### 其他脚本使用
- 羊了个羊：代码在YangService下，启动程序后，浏览器访问`http://localhost:8080/doc.html#/default/yang-service/getYangUsingGET`,
  在参数cookie里写入t=【替换为抓包工具抓到的t值】，直接请求即可，抓包教程见公众号最新文章
- 京东脚本：多个账号使用时,修改resources目录下的jd_cookie.txt文件,每行为pt_key,pt_pin的格式
- 掘金脚本：修改application.properties文件的juejin.Cookie为你的掘金cookie

- 运行MultiFunctionApplication类下的main函数启动项目
- 项目启动后，浏览器打开http://localhost:8080/doc.html#/default/jd-service/getJDUsingGET 可调试签到任务，入参和任务关系为：
  - 1 : 京东每日签到任务
  - 2 : 摇京豆签到
  - 3 : 抽京豆
  - 4 : plus会员签到
  - 5 : 掘金每日签到
  - 6 : 掘金每日抽奖


### Docker打包方式
- 在自己电脑或者服务器上建个jd_cookie.txt文件,里面放多个账号的cookie值，格式为pt_key,pt_pin
- 修改docker-compose.yml文件下的volumes变量值,替换为上一步txt文件的绝对路径,Mac电脑示例：/home/admin/opt:/opt
- 运行`maven package`将代码构建为jar文件
- 运行`docker build -t jdou:v1.1 ./`将jar包构建为docker镜像，名称为jdou:v1.1,放在当前路径下
- 运行`docker-compose up`运行上一步构建的镜像即可

## 更新
- 2023.05.06 更新，加上GPT4模型对话，修复ChatGPT消息长度过长，被微信截断的bug
- 2023.03.05 更新，使用GPT3.5接入企业微信，且新增连续对话功能
- 2023.02.27 修复ChatGPT接入企业微信未设置超时时间的bug
- 2023.02.22 修复ChatGPT接入企业微信agentId的bug
- 2023.02.18 增加ChatGPT接入企业微信，成为聊天机器人代码
- 2022.10.15 增加短信轰炸示例代码
- 2022.09.18 增加`羊了个羊`微信小程序通关调用
- 2022.09.10 修改任务脚本,支持多个账号的签到
- 2022.09.04 增加掘金自动签到任务和抽奖任务
- 2022.05.15 增加Dockerfile、docker-compose文件，用于docker构建镜像和运行镜像
- 2022.05.01 新增自动签到定时任务：摇京豆签到、抽京豆任务、plus会员签到。接口文档可视化界面
- 2022.04.27 新增服务探活接口和部署脚本deploy.sh，可用于阿里云云效自动化部署 具体部署步骤见博客：[2分钟教你部署2048小游戏到云服务器](https://blog.csdn.net/qq_36624086/article/details/123777993)