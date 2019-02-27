# Smart Solution, Poor Protection:An Empirical Study of Security and Privacy Issues in Developing and Deploying Smart Home Devices
>IoT S&P'17
## [论文概述]
智能家居的概念推动了家居设备从传统模式升级到联网模式。制造商通常利用大型IT公司(如Amazon、谷歌)发布的现有智能家居解决方案来帮助构建智能家居网络，而不是从零开始开发智能设备。智能家居解决方案提供软件开发工具包(SDK)等组件和相关管理系统，促进智能家居设备的开发和部署。然而，第三方sdk和管理系统的参与使得这些设备的工作流程变得复杂。如果没有仔细地评估，复杂的工作流常常会导致对消费者和制造商的隐私和安全性的侵犯。在这篇文章中，我们阐述了一个被广泛使用的智能家居解决方案JoyLink是如何影响智能家居设备的安全和隐私的。结合网络流量拦截、源代码审核、二进制代码反向工程等具体分析，说明智能家居解决方案的设计是容易出错的。我们认为，如果不考虑安全和隐私问题，使用该解决方案的设备不可避免地会受到攻击，从而严重威胁智能家居的隐私和安全。

通过分析JoyLink    
在协议中秘钥key依赖关系：   
sessionkey→accesskey→localkey→tmpkey.  
#### JoyLink包类型：
|Packet Type|Function|Key Parameters|
|------|-------|------|
|PT_SCAN|Device discovery|ECDH pubkey,MAC address|   
|PT_AUTH|Establishing remote communication|accesskey,sessionkey|   
|PT_HEARTBEAT|Maintain conncetion with cloud|firmware version|
|PT_SCRIPTCONTROL|Device local control|-|
|PT_SERVERCONTROL|Device remote control|feedid|   
|PT_WRITEACCESSKEY|Device initialization|feedid,localkey,accesskey|
### Vulnerability
Vuln1：秘钥管理脆弱性，秘钥依赖关系sessionkey→accesskey→localkey→tmpkey.左边的密钥交付给对方时，其安全性取决于右边的密钥。可进行中间人攻击，并可以通过秘钥构造包进行重放控制设备。   
&emsp;&emsp;流量解密获取敏感信息   
&emsp;&emsp;局域网内设备劫持   
&emsp;&emsp;伪造服务器进行劫持   
&emsp;&emsp;设备模拟添加到受害者账号中    
Vuln2：下载、更新篡改固件数据。  
