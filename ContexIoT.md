# ContexIoT: Towards Providing Contextual Integrity to Appified IoT Platforms

## [论文概述]
>21st Network and Distributed Security Symposium (NDSS 2017), Feb 2017

&emsp;&emsp;物联网已经迅速发展成为一个新的应用时代，第三方开发者可以使用编程框架为物联网平台编写应用。与其他应用平台如智能手机平台一样，权限系统在平台安全中起着重要的作用。然而，目前的物联网平台权限模型的设计缺陷最近已经被报道，这给用户带来了严重的危害，比如入侵和盗窃。为了解决这些问题，当前和未来的物联网平台都需要一个新的访问控制模型。在本文中，我们提出了<font color='red'>ContexIoT，一个基于上下文的应用物联网平台权限系统，它通过支持细粒度的上下文识别来提供上下文完整性，并通过丰富的上下文信息提供运行时提示来帮助用户执行有效的访问控制。</font>ContexIoT中的上下文定义是在过程间控制和数据流级别上的，我们证明它比以前基于上下文的智能手机平台权限系统更加全面。ContexIoT的设计理念是向后兼容的，因此可以被目前的物联网平台直接采用。   
&emsp;&emsp;我们在三星SmartThings平台上开发了ContexIoT原型机，并开发了一种自动应用程序补丁机制来支持未经修改的商品SmartThings应用程序。为了评估系统的有效性，我们对应用物联网平台上可能发生的攻击进行了首次广泛的研究，方法是复制已报告的物联网攻击，并基于智能手机恶意软件类构建新的物联网攻击。我们基于生命周期和对手技术对这些攻击进行分类，并构建第一个分类的物联网攻击应用数据集。通过对该数据集上的ContexIoT进行评估，我们发现它可以有效地区分所有测试应用程序的攻击上下文。对283个商品物联网应用的性能评估显示，应用程序补丁给事件触发延迟增加了几乎可以忽略不计的延迟，权限请求频率远低于被认为有用户习惯或烦恼风险的阈值。 
>__paper结构:__  
>I. INTRODUCTION  
>II. RELATED WORK AND BACKGROUND  
>&emsp;&emsp;|-A. Related Work   
>&emsp;&emsp;&emsp;&emsp;|-1) Permission-based Access Control   
>&emsp;&emsp;&emsp;&emsp;|-2) IoT Security  
>&emsp;&emsp;|-B. Background   
>III. THREAT MODEL AND PROBLEM SCOPE   
>IV. ATTACK TAXONOMY  
>&emsp;&emsp;|-A. Reported IoT Attacks   
>&emsp;&emsp;&emsp;&emsp;|-1) Vulnerable Authentication   
>&emsp;&emsp;&emsp;&emsp;|-2) Malicious App/Firmware   
>&emsp;&emsp;&emsp;&emsp;|-3) Problematic Usage Scenario   
>&emsp;&emsp;|-B. Migrated from The Smartphone Platforms   
>V. CONTEXIOT DESIGN   
>&emsp;&emsp;|-A. Context Definition  
>&emsp;&emsp;|-B. ContexIoT Approach   
>&emsp;&emsp;|-C. Comparison with Other Context-based Security Approaches  
>VI. IMPLEMENTATION  
>&emsp;&emsp;|-A. SmartApp Patching Implementation  
>&emsp;&emsp;&emsp;&emsp;|-1) Static Analysis  
>&emsp;&emsp;&emsp;&emsp;|-2) Runtime Logging  
>&emsp;&emsp;&emsp;&emsp;|-3) Secure logic patching  
>&emsp;&emsp;|-B. End-to-End Implementation  
>VII. EVALUATION   
>&emsp;&emsp;|-A. Effectiveness of Secure Logic Patching   
## [思考]
>paper思路：攻击分类->框架设计如何通过上下文检测攻击行为->该框架如何应对常规的逃逸行为，并与其它框架进行对比->框架实现

&emsp;&emsp;该paper是密歇根大学继《Security Analysis of Emerging Smart Home Applications》IoT——Smart Home研究方向的第2篇。依照之前对SmartThings框架发现的漏洞，提出ContenIoT框架，针对其APP进行修补，构建权限服务云，提供上下文权限（敏感操作）使用提示及控制。   
![](https://raw.githubusercontent.com/ReAbout/IoT-Home/master/images/contexiot_3.PNG?token=AI5pPaS1Pz2fy-4T164aTqmb5PSCiM3-ks5cD7gLwA%3D%3D)  
&emsp;&emsp;后端权限服务为每个用户维护一个授权的许可上下文映射表。每次ContexIoT补丁应用程序尝试执行安全敏感操作时，都会向后台发送包含上下文信息的权限请求。基于云的权限服务检查上下文之前是否被允许或拒绝。如果没有，它将使用一个对话框提示用户显示权限请求和相关上下文，并向映射表添加一个额外条目，以便将用户的决策存储为安全首选项。
### 修改后敏感操作的step:  
(1)Collect the context information before the action is executed.  
(2)Allow or Deny   
该paper的IoT Security分为：Devices, Protocols and Platforms(权限粗粒度,流控制)  
### IoT攻击分类：
认证漏洞、恶意的应用或固件、错误的使用场景   
https://sites.google.com/site/iotcontextualintegrity/attack   
![](https://raw.githubusercontent.com/ReAbout/IoT-Home/master/images/contexiot_1.PNG?token=AI5pPeB1iuNEO52-XmtVZXVBk1hxgKLTks5cD7fkwA%3D%3D)  
![](https://github.com/ReAbout/IoT-Home/blob/master/images/contexiot_2.PNG)  
### 应对逃避检测能力
（1）Asynchronous leakage   
（2）Control flow abuse  
（3）Dynamic code loading  
（4）Policy abuse.   
