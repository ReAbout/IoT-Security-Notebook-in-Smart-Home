# Rethinking Access Control and Authentication for the Home Internet of Things (IoT)
## [论文概述]
>2018 27th USENIX Security Symposium

&emsp;&emsp;计算正在从单用户设备向物联网(IoT)过渡，在物联网中，具有复杂社会关系的多个用户与一个设备交互。当前部署的技术无法在此类设置中提供可用的访问控制规范或身份验证。在本文中，我们开始重新构想家庭物联网的访问控制和身份验证。我们建议将访问控制的重点放在物联网功能上，而不是在每个设备粒度上。在一项有425名参与者参与的在线用户研究中，我们发现，参与者对单一设备中不同功能的访问控制策略的期望存在显著差异，而且这种策略的使用取决于谁试图使用该功能。从这些期望的策略中，我们确定默认策略的可能候选者。我们还指出了指定更复杂但更理想的访问控制策略所必需的原语。这些原语的范围从每天的时间到用户的当前位置。最后，我们讨论不同的身份验证方法可能支持所需策略的程度。
accesscontrol-policy specification (expressing which particular
users, in which contexts, are permitted to access a resource)
authentication (verifying that users are who
they claim to be)
认证一次无需在认证