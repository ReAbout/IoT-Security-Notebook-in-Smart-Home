# BadBluetooth: Breaking Android Security Mechanisms via Malicious Bluetooth Peripherals

>2019 NDSS

## [论文概述]

&emsp;&emsp;根据我们对蓝牙协议的研究以及在Android系统上的实现，我们发现蓝牙协议仍然存在一些设计缺陷，可能会导致严重的安全后果。例如，我们发现蓝牙配置文件上的身份验证过程是非常不一致的，并且是粗粒度的:如果成对的设备更改了它的配置文件，它将自动获得信任，并且不会通知用户。此外，蓝牙设备本身提供的信息没有经过严格的验证，因此恶意设备可以通过更改其名称、配置文件信息和显示在屏幕上的图标来欺骗用户。    
&emsp;&emsp;为了更好的理解这个问题，我们对蓝牙配置文件进行了系统的研究，并提出了三种攻击方式来论证这种蓝牙设计缺陷的可行性和潜在的危害。这些攻击是在Raspberry Pi 2设备上实现的，并使用不同的Android操作系统版本(从5.1到最新的8.1)进行评估。结果表明，对手可以绕过现有的Android保护(如权限、隔离等)，发起中间人攻击，控制受害者的应用程序和系统，窃取敏感信息等。为了减轻这种威胁，提出了一种新的蓝牙验证机制。我们基于AOSP项目实现了原型系统，并将其部署在谷歌Pixel 2手机上进行评估。实验表明，我们的解决方案可以有效地防止攻击。    
## [论文要点]
### 攻击方法
这些漏洞不是由编程错误引起的错误： 因为对配置修改权限粒度过大
- 恶意蓝牙设备可以从合法的配置文件秘密切换到人界面设备(Human Interface device, HID)配置文件。通过这种HID配置文件，恶意设备可以通过注入击键和鼠标移动以及单击事件来模拟蓝牙键盘和蓝牙鼠标的行为。    
- 可以改变手机配置，绕过安全保护，在不被发现的情况下安装恶意应用程序。
- 恶意蓝牙设备可以偷偷地将其配置文件更改为Personal Area Networking (PAN)，然后发起中间人攻击来嗅探网络流量或注入欺骗数据包(如DHCP/DNS应答将其指向恶意服务器)。
#### Bluetooth Profile
为了规范不同厂商生产的异构蓝牙设备之间的通信，提出了蓝牙配置文件的概念。每个概要文件都包含引导通信的设置，比如用户界面的格式和协议的依赖关系。到目前为止，蓝牙SIG标准化了30多个配置文件。最常用的配置文件是耳机配置文件(HSP)，它指定蓝牙耳机如何与移动电话一起使用

## Tips
- https://cve.mitre.org/cgi-bin/cvekey.cgi?keyword=bluetooth