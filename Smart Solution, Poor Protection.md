# Smart Solution, Poor Protection:An Empirical Study of Security and Privacy Issues in Developing and Deploying Smart Home Devices
>IoT S&P'17
## [论文概述]
智能家居的概念推动了家居设备从传统模式升级到联网模式。制造商通常利用大型IT公司(如Amazon、谷歌)发布的现有智能家居解决方案来帮助构建智能家居网络，而不是从零开始开发智能设备。智能家居解决方案提供软件开发工具包(SDK)等组件和相关管理系统，促进智能家居设备的开发和部署。然而，第三方sdk和管理系统的参与使得这些设备的工作流程变得复杂。如果没有仔细地评估，复杂的工作流常常会导致对消费者和制造商的隐私和安全性的侵犯。在这篇文章中，我们阐述了一个被广泛使用的智能家居解决方案JoyLink是如何影响智能家居设备的安全和隐私的。结合网络流量拦截、源代码审核、二进制代码反向工程等具体分析，说明智能家居解决方案的设计是容易出错的。我们认为，如果不考虑安全和隐私问题，使用该解决方案的设备不可避免地会受到攻击，从而严重威胁智能家居的隐私和安全。

JoyLink
秘钥key依赖关系：
sessionkey→accesskey→localkey→tmpkey.