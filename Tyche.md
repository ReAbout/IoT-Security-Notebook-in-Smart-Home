# Tyche:A Risk-Based Permission Model for Smart Homes
## [论文概述]
> IEEE Cybersecurity Development Conference (SecDev), September 30th-Oct. 2nd, 2018

&emsp;&emsp;新兴的智能家居平台，与各种物理设备接口，支持第三方应用开发，目前使用的权限模型灵感来源于智能手机操作系统，访问操作的权限由执行操作的设备分开，而不是由执行操作的设备分开。不幸的是，这会导致两个问题：
（1）不需要访问所有已授予设备操作的app对这些操作的访问权限过大。
（2）app可能会给用户带来比需要更高的风险，因为物理设备操作基本上是风险不对称的门。"door.unlock"可能让窃贼进门。“door.lock”可能会导致锁上门。
&emsp;&emsp;拥有混合风险操作的过度特权的应用程序只会增加损害的可能性。我们提出了Tyche，一种安全的开发方法，它利用物理设备操作中的风险不对称来限制应用程序对智能家居用户造成的风险，而不会增加用户的决策开销。Tyche介绍了物联网系统基于风险的权限的概念。在使用基于风险的权限时，设备操作被分组到具有相似风险的单元中，并且用户授予应用程序对基于风险粒度的设备的访问权。我们从一组从流行的三星SmartThings平台获得的权限开始，对领域专家和Mechanical Turk用户进行用户研究，计算与设备操作相关的风险的相对排名。我们发现用户对风险的评估与领域专家的评估非常接近。利用这一观点，我们定义了基于风险的设备操作分组，并将其应用于现有的SmartThings应用程序。我们表明，现有的应用程序可以在保持可操作性的同时，将高风险操作的访问减少60%。
>__paper结构:__   
>I. INTRODUCTION   
>II. BACKGROUND   
>&emsp;&emsp;A. SmartThings Framework  
>&emsp;&emsp;B. Threat Model   
>III. TYCHE: THE RISK-BASED PERMISSION MODEL   
>&emsp;&emsp;A. Developing a Risk-Based Permission Model   
>&emsp;&emsp;B. Enforcing a Risk-Based Permissions Model   
>IV. USABILITY ANALYSIS  
>&emsp;&emsp;A.User Study Setup  
>&emsp;&emsp;B. Enforcing a Risk-Based Permissions Model     
>IV. USABILITY ANALYSIS   
>&emsp;&emsp;A. User Study Setup  
>V. DISCUSSION AND LIMITATIONS    
>&emsp;&emsp;A. Tyche vs. “Permission on first use”   
>&emsp;&emsp;B. Perception of Risk      
>&emsp;&emsp;C. Overprivilege in Tyche   
>&emsp;&emsp;D. Surfacing Risk Levels to Users   
>&emsp;&emsp;VI. RELATED WORK  
>&emsp;&emsp;VII. CONCLUSION      
## [思考]  
矛盾点：过度权限的控制    
分High、Medium、Low Risk   
对人员使用习惯调查评测    
风险感知针对不同年龄段的人群   

