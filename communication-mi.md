# 小米IoT通信研究
小米(xiaomi)、绿米(aqara)、米家智能家庭(mijia)
## [局域网通信]
### 1.绿米网关局域网通信：
#### （1）设备发现与查询
组播、未加密
#### （2）加密机制  
局域网通信采用key加密方式，需要在米家智能家庭APP上对网关设置KEY（使用AES-CBC 128 加密，app下发随机的16个字节长度的字符串KEY）。必须拥有该网关的KEY，才能与该网关进行局域网通信。 
#### （3）设备心跳上报
### 2.场景和自动化
### 3.抓包方法
采用ARP欺骗攻击，扮演中间人角色，让设备流量从我的主机经过。攻击的工具为著名的ettercap
### 安全问题点
a.token   
b.投屏  
c.特定场景控制链   
#### 相关工具：
[miio](https://github.com/aholstenson/miio):Control Mi Home devices, such as Mi Robot Vacuums, Mi Air Purifiers, Mi Smart Home Gateway (Aqara) and more
#### 相关资料：
[绿米网关局域网通信协议V1.1.1](/files/绿米网关局域网通信协议V1.1.1_2017.12.21.doc)   
[网关局域网通信协议V2.0](http://docs.opencloud.aqara.cn/development/gateway-LAN-communication/)    
[小米智能家居设备流量分析及脚本控制](https://www.freebuf.com/articles/terminal/181846.html)    
[小米接入教程](https://homekit.loli.ren/docs/show/12 )       
[智能家居设备的另一种打开方式——如何控制局域网中的小米设备](https://paper.seebug.org/616/)  
[MIDC • 2018 小米IoT安全峰会议题 PPT 公布](https://paper.seebug.org/761/)   
>list：
小米 IoT 安全思考与实践   
小米IoT隐私数据合规实践   
IoT + AI + 安全 =？   
IoT 安全战地笔记   
智能门锁，让居住更安全   
IoT Reverse Engineering   
大安全下的 IoT 安全威胁演变与应对   
Beyond logical attacks   
## [云端通信]

## [其它]
[物联网设备的固件模拟环境搭建](https://paper.seebug.org/639/)