# Mobile Application Web API Reconnaissance:Web-to-Mobile Inconsistencies & Vulnerabilities
>IEEE Symposium on Security and Privacy (SP) (2018)

## [论文概述]

&emsp;&emsp;在本文中，提出了一种自动分析移动应用程序到web API通信的新方法，用于检测应用程序及其各自的web API服务之间输入验证逻辑的不一致性。实现了一个工具WARDroid，基于静态分析的web API，用以发现API服务上的不一致性，这些不一致性可能导致攻击。作者系统的利用程序分析技术，自动的从Android应用程序中提取HTTP通信模块，这些模块对应用程序对web API服务发出的web请求施加的输入验证约束进行编码。WARDroid还通过服务器验证逻辑的黑盒测试来增强，以识别可能导致攻击的不一致性。  
&emsp;&emsp;效果：通过自谷歌Play Store的10,000个流行的免费应用程序上评估了系统。检测到超过4000个应用程序中使用的api存在逻辑问题，其中包括1743个使用未加密HTTP通信的应用程序。我们进一步测试了1000个应用程序，以验证web API劫持漏洞，这些漏洞可能会导致用户隐私和安全方面的潜在危害，并发现文中测试的示例应用程序集可能会影响数百万用户。   
## [论文要点]
### 漏洞问题
- 使用HTTP不安全
- 不一致性：data validation logic in a mobile app & data validation logic implemented at a remote web API server
eg.敏感数据客户端限制了，但是服务端未限制可以访问 
- web api甚至可能跳过输入验证，把这项工作交给应用程序->重放攻击
- Web API hijacking：授予攻击者未经授权的访问权
### 约束模型
Cs(S) = true => Ca(S) = true 在服务端的约束不会违反客户端的约束   
Ca(S) = false => Cs(S) = false 在客户端拒绝的，服务端也应该拒绝   
总结就是 服务端的约束更严格   
所以找不一致性的漏洞=客户端拒绝的反而客户端接受   
### 实现方法
1. 从APP提取API交互模块
2. 不一致评价->找客户端测试失败，服务端测试未失败的HTTP
- 基于Flowdroid（找HTTP代码路径和约束）拓展。
Flowdroid: Precise context,flow, field, object-sensitive and lifecycle-aware taint analysis for android apps     
![](https://raw.githubusercontent.com/ReAbout/IoT-Home/master/images/android_api_1.PNG?token=AI5pPUIu4fwgg2urvl5gTWGXs1bugSTzks5ciLfMwA%3D%3D)
- __程序切片__:缩小搜索范围    
找java.net.HttpURLConnection, org.apache。http、android.net.http android。volley、javax.net.ssl和java.net.URL等追踪
- __修改污染规则__:反向的污染传播规则
- __路径约束__:Flowdroid
- __UI分析__:XML关联到程序片段中的事件侦听器,找到UI加约束
![](https://raw.githubusercontent.com/ReAbout/IoT-Home/master/images/android_api_2.PNG?token=AI5pPSE3TT8njcUgLizyHMCk20beS_jrks5ciLfpwA%3D%3D)
- __常量__：Amazon AWS sdk的应用程序通常为每个请求发送API身份验证密钥。这个键通常硬编码在源代码中。
### 启发式
实质是加入简单的指纹识别然后进行些漏洞测试。   
启发式:例如，一些服务器将公开运行时框架、数据库和其他可用于对服务器进行指纹识别的细节。在我们的原型中，我们使用简单的启发式来标识web服务器运行时(PHP、asp.net等)和后端服务器(MySQL、mssql)。它们用于建议使用领域知识的进一步输入，例如生成简单的SQL注入类型输入值.
### 局限性
1. Obfuscated code   
2. State Changes
3. WebViews:不考虑通过webview加载JavaScript代码。
4. Authentication
## 问题
1. network-aware taint analysis approach？
2. program dependence graph (PDG)？
