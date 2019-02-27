# Phantom Device Attack: Uncovering the Security Implications of the Interactions among Devices, IoT Cloud, and Mobile Apps
## [论文概述]
&emsp;&emsp;智能家居将成百上千的家庭设备接入互联网，在云端运行智能算法，向设备发送远程命令。它在给用户带来前所未有的便利、可访问性和效率的同时，也给用户带来了安全隐患。之前的研究从多个方面对智能家居安全进行了研究。然而，我们发现参与实体(设备、物联网云和移动应用)之间交互的复杂性尚未得到系统的研究。在这项工作中，我们对四种广泛使用的智能家居解决方案进行了深入的分析。结合固件反向工程、网络流量拦截和黑盒测试，我们提取了表示这三个实体之间复杂交互的一般状态转换。基于状态机，我们揭示了导致意外状态转换的几个漏洞。虽然这些小的安全漏洞似乎无关紧要，但我们发现，将它们以一种令人惊讶的方式组合起来，会给智能家居用户带来严重的安全或隐私风险。为此，构造并举例说明了五种具体的攻击。我们还讨论了公开攻击在商业竞争环境中的影响。最后，我们提出一些总体设计建议，以建立一个更安全的智能家居解决方案。
>__paper结构:__  
I. INTRODUCTION   
II. INTERACTIONS AMONG DEVICES, IOT CLOUD AND MOBILE APPS   
&emsp;&emsp;A. Smart Home Solutions: Developer’s View   
&emsp;&emsp;B. Interaction Model   
&emsp;&emsp;C. State Transitions Involved in the Interactions: A First Glance   
&emsp;&emsp;D. Scope of Empirical Vulnerability Analysis   
III. PHANTOM DEVICE ATTACK: AN OVERVIEW   
&emsp;&emsp;A. Threat Model   
&emsp;&emsp;B. Analysis Methodology   
&emsp;&emsp;C. Identified Design Flaws   
IV. CASE STUDY: REMOTE DEVICE HIJACKING   

####Interaction Model：   
Setup   
- Device Discovery   
- WiFi Provisioning   
- Device Registration   
- Device Binding   
- Device Login   
  
Control   
- Remote Control   
- Local Control   
- Smart Control   
- Data Uploading   

#### 利用setup逻辑漏洞，攻击场景   
&emsp;&emsp;可以远程接管设备   
&emsp;&emsp;远程用控制下的不存在的幻影设备替换真实设备    
&emsp;&emsp;可以利用phantom设备自动向云端发送更新请求，大规模窃取各种专有固件。   
&emsp;&emsp;对手可以利用虚拟设备误导云，并在出售前占有真实设备的身份   
