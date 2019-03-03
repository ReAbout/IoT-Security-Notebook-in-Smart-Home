# ReDeBug: Finding Unpatched Code Clones in Entire OS Distributions

>2012 IEEE Symposium on Security and Privacy

## [论文概述]

&emsp;&emsp;程序员永远不应该两次修复同一个错误。不幸的是，当错误代码的补丁没有传播到所有代码克隆时，常常会发生这种情况。未打补丁的代码克隆表示潜在的错误，对于安全关键的问题，潜在的漏洞，因此快速检测非常重要。
&emsp;&emsp;本文介绍了一种用于在OS-distribution scale代码库中快速查找未补丁代码克隆的ReDeBug系统。虽然以前也有关于代码克隆检测的工作，但是ReDeBug代表一个独特的设计点，它使用一种快速的、基于语法的方法，可以扩展到OS分发大小的代码库，其中包括用许多不同语言编写的代码。与以前的方法相比，ReDeBug可能会发现更少的代码克隆，但是可以提高规模、速度、降低错误检测率，并且与语言无关。我们通过检查Debian Lenny/Squeeze、Ubuntu Maverick/Oneiric、所有SourceForge C和c++项目中的所有包中的所有代码，以及未打补丁的代码克隆的Linux内核来评估ReDeBug。ReDeBug以70万LoC/min的速度处理了超过21亿行代码，构建了一个源代码数据库，然后在一台普通台式机上8分钟内检查了376个Debian/Ubuntu安全相关补丁，在当前部署的代码中发现了15546个已知漏洞代码的未修补副本。我们通过在最新版本的Debian Squeeze包中确认145个真正的bug来展示重新调试的实际影响。

### 系统架构
![](https://raw.githubusercontent.com/ReAbout/IoT-Home/master/images/redebug_1.PNG?token=AI5pPfzPunTeppb9nsLE78szQPTWJ1gkks5chMHxwA%3D%3D)


Lingxiao Jiang, Ghassan Misherghi, and Zhendong Su.Deckard: Scalable and accurate tree-based detection of code clones. In Proceedings of the International Conference on Software Engineering, 2007
