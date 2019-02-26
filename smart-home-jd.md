# JD Smart Home Security
## 0x01 Introduction
“Alpha”、“京东微联”品牌统一升级为“小京鱼”
Index：https://smartdev.jd.com
Download： https://smartdev.jd.com/docCenterDownload/list/2
APP：小京鱼
### 0x02 Platform
### 平台简介
IoT开放平台是京东在IoT时代全力打造的开放平台，其目标是帮助各大品牌厂家打造更好的产品。IoT平台主要服务于生活场景的智能设备，如智能家居设备，智能家电设备，智能车载，可穿戴设备等。截至目前，IoT平台接入了上百家品牌厂家的上千款产品，已经销售的产品数量达到了千万数量级。   
接入IoT平台的设备可以获取平台的物联网能力，和其他智能设备互联互通，并且可以被叮咚音箱及接入小京鱼平台的其他主控设备控制。   
![](https://raw.githubusercontent.com/ReAbout/IoT-Home/master/images/jd_iot_1.png?token=AI5pPcdcwF6L-p2BPeCUhKmvxV019nufks5cfixDwA%3D%3D)   
#### 分类
基于JoyLink2.0协议的SDK适用于的设备，按照在网络中的角色可以分为以下三种：   
- 普通设备：即一般的可连网设备，这样的设备可以通过基站、路由器直接连入因特网，本身具有IP地址。   
- 网关设备：这类设备不仅自身有IP地址，可以连入因特网；同时代理如ZigBee、BlueTooth、433等不具有独立IP地址的设备接入网络。   
- 子设备：即不具有独立IP，不能直接连入因特网，需要依赖网关设备与其它设备或组件通讯。   

在以上几种设备之外，还有以下三种系统角色：   
- 蓝牙设备：一些可穿戴设备如蓝牙耳机、手环，通信主要走蓝牙协议，并非网络协议，为此我们提供了蓝牙设备SDK。  
- 手机APP：与用户产生交互的控制端，指令的发起方，同时也是信息的查询窗口。   
- 云平台：提供后台服务、提供广域网连接的具有公网IP的服务器端。   
### 0x03 Sniffing method
#### Wifi
http://router.asus.com    
192.168.50.1   
tcpdump静态编译版本，适用于merlin等arm固件   
如果希望抓包能在windows上用wireshark分析，可以适用以下命令：      
tcpdump -i br0 -s 0 -w test.pcap   
#### Bluetooth
怎么构造包？  
官方文档定义安全等级   
### 0x04 Traffic Analysis
JoyLink通信协议、JoyLink-Bluetooth协议、设备入网（softap、一键配置）、云端API、Joylink2.0 指令转换及lua 脚本   
分析方法：通过文档、SDK+抓包   
#### (1)指令转换及lua 脚本
京东标准指令：在京东Joylink 协议里所有的事物（包括设备device, 设备快照snapshot, 操作属性stream_id, 操作指令cmd 等）均使用json语言描述。   
实现指令转化是靠Lua脚本实现的。   
#### (2)JoyLink通信协议   
##### 通信交互模型[Reference.5]：
Setup
&emsp;&emsp;Device Discovery(通常明文，可用于局域网内探测IoT设备）
&emsp;&emsp;WiFi Provisioning(入网配置)
&emsp;&emsp;Device Registration 
&emsp;&emsp;Device Binding
&emsp;&emsp;Device Login
Control
&emsp;&emsp;Remote Control
&emsp;&emsp;Local Control
&emsp;&emsp;Smart Control
&emsp;&emsp;Data Uploading
##### 包类型：
|Packet Type|Function|Key Parameters|
|------|-------|------|
|PT_SCAN|Device discovery|ECDH pubkey,MAC address|
|PT_AUTH|Establishing remote communication|accesskey,sessionkey|
|PT_HEARTBEAT|Maintain conncetion with cloud|firmware version|
|PT_SCRIPTCONTROL|Device local control|-|
|PT_SERVERCONTROL|Device remote control|feedid|
|PT_WRITEACCESSKEY|Device initialization|feedid,localkey,accesskey|
Vuln[Reference.5]：秘钥管理脆弱性，秘钥依赖关系sessionkey→accesskey→localkey→tmpkey.左边的密钥交付给对方时，其安全性取决于右边的密钥。可进行中间人攻击，并可以通过秘钥构造包进行重放控制设备。   
&emsp;&emsp;流量解密获取敏感信息  
&emsp;&emsp;局域网内设备劫持   
&emsp;&emsp;伪造服务器进行劫持   
&emsp;&emsp;设备模拟添加到受害者账号中   
Vuln[Reference.5]：下载、更新篡改固件数据。
Vuln[Reference.6]：利用setup逻辑漏洞，攻击场景
&emsp;&emsp;可以远程接管设备
&emsp;&emsp;远程用控制下的不存在的幻影设备替换真实设备
&emsp;&emsp;可以利用phantom设备自动向云端发送更新请求，大规模窃取各种专有固件。
&emsp;&emsp;对手可以利用虚拟设备误导云，并在出售前占有真实设备的身份
#### (3)JoyLink-Bluetooth
> [APP]<—BLE—>[Device] 

JoyLink Profile   
协议层：一帧大小20B   
应用层：     
&emsp;&emsp;通信层面：通信层面是为SDK服务   
&emsp;&emsp;业务层面：业务层面是为App服务。   
功能：设备发现、配置上网、设备->控制端通信、控制端->设备通信   
通信安全级别   
#### (4)设备入网 softap
一键配置通过802.11数据包的特定区域传输完成。而数据包的特定区域传输完成。   
组播和广播两种方式。   
Vuln[Reference.4]：设备入网WIFI用户密码，敏感信息泄露。

#### (5)云端API

#### Tips
局域网中控制   
认证绑定、解绑    
setup:app通过BLE组播发送，云端身份绑定、认证，设备初始化    
投屏（隐蔽安装App）
网关、蓝牙网关   
场景交互      
## Reference
- [1]https://github.com/espressif/esp-joylink
- [2]https://www.defcon.org/html/defcon-china/dc-cn-speakers.html
- [3][飘散在空中的Wi-Fi密码：SmartCfg无线配网方案的安全分析](https://zhuanlan.zhihu.com/p/35664962)
- [4]ACM Conference on Security & Privacy,[Passwords in the Air: Harvesting Wi-Fi Credentials from SmartCfg Provisioning](https://loccs.sjtu.edu.cn/~romangol/publications/wisec18.pdf) 对于一键配置WIFI功能的安全监测
- [5]IoT S&P,[Smart Solution, Poor Protection: An Empirical Study of Security and Privacy Issues in Developing and Deploying Smart Home Devices](https://loccs.sjtu.edu.cn/~romangol/publications/iotsp17.pdf) 对JoyLink协议分析，并发现隐私和安全问题
- [6]arxiv,[Phantom Device Attack: Uncovering the Security Implications of the Interactions among Devices, IoT Cloud, and Mobile Apps](https://arxiv.org/abs/1811.03241)

https://www.rsaconference.com/writable/presentations/file_upload/cmi1-r08_managing_security_in_internet_of_things_using_api_management_platforms_final2.pdf
