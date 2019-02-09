# JD Smart Home Security
## 0x01 Introduction
“Alpha”、“京东微联”品牌统一升级为“小京鱼”
Index：https://smartdev.jd.com
Download： https://smartdev.jd.com/docCenterDownload/list/2
APP：小京鱼
### 0x02 平台简介
IoT 开放平台是京东在IoT时代全力打造的开放平台，其目标是帮助各大品牌厂家打造更好的产品。IoT平台主要服务于生活场景的智能设备，如智能家居设备，智能家电设备，智能车载，可穿戴设备等。截至目前，IoT平台接入了上百家品牌厂家的上千款产品，已经销售的产品数量达到了千万数量级。
接入IoT平台的设备可以获取平台的物联网能力，和其他智能设备互联互通，并且可以被叮咚音箱及接入小京鱼平台的其他主控设备控制。
![](https://raw.githubusercontent.com/ReAbout/IoT-Home/master/images/jd_iot_1.png?token=AI5pPZyWiEgLpwqFa61Ooj6o7xMi-y8Yks5cTvB8wA%3D%3D)
### 0x03 分类
基于JoyLink2.0协议的SDK适用于的设备，按照在网络中的角色可以分为以下三种：
- 普通设备：即一般的可连网设备，这样的设备可以通过基站、路由器直接连入因特网，本身具有IP地址。
- 网关设备：这类设备不仅自身有IP地址，可以连入因特网；同时代理如ZigBee、BlueTooth、433等不具有独立IP地址的设备接入网络。
- 子设备：即不具有独立IP，不能直接连入因特网，需要依赖网关设备与其它设备或组件通讯。

在以上几种设备之外，还有以下三种系统角色：
- 蓝牙设备：一些可穿戴设备如蓝牙耳机、手环，通信主要走蓝牙协议，并非网络协议，为此我们提供了蓝牙设备SDK。
- 手机APP：与用户产生交互的控制端，指令的发起方，同时也是信息的查询窗口。
- 云平台：提供后台服务、提供广域网连接的具有公网IP的服务器端。
### 0x04 Sniffing method
#### Wifi
http://router.asus.com
192.168.50.1
tcpdump静态编译版本，适用于merlin等arm固件
如果希望抓包能在windows上用wireshark分析，可以适用以下命令：
tcpdump -i br0 -s 0 -w test.pcap
#### Bluetooth
怎么构造包？
### 0x05 Traffic Analysis
JoyLink通信协议、JoyLink-Bluetooth协议、设备入网（softap、一键配置）、云端API、Joylink2.0 指令转换及lua 脚本
分析方法：通过文档、SDK+抓包
#### 指令转换及lua 脚本
京东标准指令：在京东Joylink 协议里所有的事物（包括设备device, 设备快照snapshot, 操作属性stream_id, 操作指令cmd 等）均使用json 语言描述。
实现指令转化是靠Lua脚本实现的。
#### JoyLink-Bluetooth
> [APP]<—BLE—>[Device] 

JoyLink Profile
协议层：一帧大小20B
应用层：
&emsp;&emsp;通信层面：通信层面是为SDK服务
&emsp;&emsp;业务层面：业务层面是为App服务。
功能：设备发现、配置上网、设备->控制端通信、控制端->设备通信
通信安全级别
#### 设备入网 一键配置
一键配置通过802.11数据包的特定区域传输完成。而数据包的特定区域传输完成。
组播和广播两种方式。
#### Tips
1. 局域网中控制
2. 认证
3. 投屏
## Reference
- https://github.com/espressif/esp-joylink
- https://www.defcon.org/html/defcon-china/dc-cn-speakers.html
- ACM Conference on Security & Privacy,[Passwords in the Air: Harvesting Wi-Fi Credentials from SmartCfg Provisioning](https://loccs.sjtu.edu.cn/~romangol/publications/wisec18.pdf)
- [飘散在空中的Wi-Fi密码：SmartCfg无线配网方案的安全分析](https://zhuanlan.zhihu.com/p/35664962)
- IoT S&P,[Smart Solution, Poor Protection: An Empirical Study of Security and Privacy Issues in Developing and Deploying Smart Home Devices](https://loccs.sjtu.edu.cn/~romangol/publications/iotsp17.pdf)
