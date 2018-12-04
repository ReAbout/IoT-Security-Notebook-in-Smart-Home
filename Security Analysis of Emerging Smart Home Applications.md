# Security Analysis of Emerging Smart Home Applications
## [论文概述]
>2016 IEEE Symposium on Security and Privacy

&emsp;&emsp;本文首次对这样一类新兴的智能家居框架进行了深入的实证安全分析。我们对三星旗下的SmartThings进行了分析。在目前可用的智能家居平台中，SmartThings拥有最多的应用程序，并支持多种设备，包括动作传感器、火警和门锁。SmartThings将应用程序运行时托管在一个专有的、封闭源代码的云后端，这使得审查具有挑战性。我们克服了这个挑战，对499个SmartThings应用程序(称为SmartApps)和132个设备处理器进行了静态源代码分析，并精心设计了测试用例，揭示了该平台的许多未注册特性。我们的主要发现有两方面。
&emsp;&emsp;首先，尽管SmartThings实现了一种特权分离模型，但我们发现了两个内在的设计缺陷，它们导致了SmartApps中显著的特权过度。我们的分析显示，商店中超过55%的智能应用程序由于功能权限过度使用。此外，一旦安装完毕，智能应用程序就可以完全访问设备，即使它指定只需要有限的访问设备。其次，SmartThings事件子系统(设备通过事件与SmartApps进行异步通信)并不能充分保护携带敏感信息(如锁码)的事件。我们利用框架设计缺陷构建了四种概念验证攻击:(1)秘密植入门锁码;(2)盗走现有门锁密码;(3)家庭度假模式;(4)诱发假火警。本文的结论为智能家居编程框架的设计提供了安全方面的借鉴。
>__paper结构:__   
>1 Introduction  
>2 RELATED WORK   
>&emsp;&emsp;|-Smart Home Security.   
>&emsp;&emsp;|-Overprivilege and Least-Privilege.  
>&emsp;&emsp;|-Permission/Capability Model Design.    
>3 SMARTTHINGS BACKGROUND AND THREAT MODEL  
>&emsp;&emsp;|-A Background     
>&emsp;&emsp;&emsp;&emsp;|-1) SmartApps and SmartDevices:  
>&emsp;&emsp;&emsp;&emsp;|-3) Events and Subscriptions:  
>&emsp;&emsp;&emsp;&emsp;|-2) Capabilities & Authorization:   
>&emsp;&emsp;&emsp;&emsp;|-4) WebService SmartApps   
>&emsp;&emsp;&emsp;&emsp;|-5) Sandboxing  
>&emsp;&emsp;|-B Threat Model   
>4 SECURITY ANALYSIS OF SMARTTHINGS FRAMEWORK   
>&emsp;&emsp;|-A. Occurrence of Overprivilege in SmartApps   
>&emsp;&emsp;|-B. Insufficient Sensitive Event Data Protection   
>&emsp;&emsp;|-C. Insecurity of Third-Party Integration------OAuth   
>&emsp;&emsp;|-D. Unsafe Use of Groovy Dynamic Method Invocation   
>&emsp;&emsp;|-E. API Access Control: Unrestricted Communication Abilities   
>5 EMPIRICAL SECURITY ANALYSIS OF SMARTAPPS    
>&emsp;&emsp;|-A. Overall Statistics of Our Dataset  
>&emsp;&emsp;|-B. Overprivilege    
>6 PROOF-OF-CONCEPT ATTACKS   
>&emsp;&emsp;|-A. Backdoor Pin Code Injection Attack   
>7 CHALLENGES AND OPPORTUNITIES   

### SmartThings生态系统结构
由三个主要组件组成:
(1)hub (2)SmartThings cloud (3)手机应用
![](https://raw.githubusercontent.com/ReAbout/IoT-Home/master/images/iot_1.PNG?token=AI5pPfa10Z4CcWbmJooc9uJ3hVDaFEZkks5cD7dywA%3D%3D)

### 从5个层面去分析IoT框架安全
Least-privilege principle adherence（权限）  
Sensitive event data protection（敏感数据）  
External, third-party integration safety（第三方集成）  
External input sanitization（输入）  
Access control of external communication APIs:（api的访问控制）  
### 针对SmartThings四种攻击 
以智能门锁为例，攻击有两个步骤:
(1)为受害者的SmartThings部署获取OAuth令牌。
(2)确定WebService SmartApp是否使用不安全的Groovy动态方法调用，如果使用，则在OAuth上注入适当格式化的命令字符串。
![](https://raw.githubusercontent.com/ReAbout/IoT-Home/master/images/iot_2.PNG?token=AI5pPeehgTfQVDI934GWmLrH_oCS3zkFks5cD7eSwA%3D%3D)
攻击步骤：  
（1）hacker利用电池监控器SmartApp注册所有类型的codeReport事件。   
（2）受害者设置新的pin-code  
（3）处理程序依次向集线器发出一系列set和get ZWave命令  
（4）通过ZWave网关与门锁通信，并创建日志。  
（5）获取铭文的pin-code日志信息。    
![](https://raw.githubusercontent.com/ReAbout/IoT-Home/master/images/iot_3.PNG?token=AI5pPU-ThNjnQKfyfchjXHD4Kpfbhdoaks5cD7e7wA%3D%3D)
## [Tips]  
1.一旦获取128bitkey，就可以调用IoT设备所有事件  
2.无法二进制分析，大部分逻辑都是在云端执行    
3.分析了SmartThings编译系统，并确定它拥有关于所有功能的信息。我们发现了一种查询编译系统的方法，即使用未发布的REST端点获取设备处理程序ID并返回一个JSON字符串，该字符串列出了设备处理程序实现的一组功能以及所有组成命令和属性。   
4.[密歇根大学 IOT安全 Link](https://iotsecurity.engin.umich.edu/)   
5.SmartThings支持Web IDE，开发人员可以用Groovy编程语言构建应用程序。   
