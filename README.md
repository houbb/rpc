# rpc

[rpc](https://github.com/houbb/rpc) 是基于 netty 实现的 java rpc 框架，类似于 dubbo。

主要用于个人学习，由渐入深，理解 rpc 的底层实现原理。

# 前言

工作至今，接触 rpc 框架已经有很长时间。

但是对于其原理一直只是知道个大概，从来没有深入学习过。

以前一直想写，但由于各种原因被耽搁。

## 技术准备

[Java 并发实战学习](https://houbb.github.io/2019/01/18/jcip-00-overview)

[TCP/IP 协议学习笔记](https://houbb.github.io/2019/04/05/protocol-tcp-ip-01-overview-01)

[Netty 权威指南学习](https://houbb.github.io/2019/05/10/netty-definitive-gudie-00-overview)

这些技术的准备阶段，花费了比较长的时间。

也建议想写 rpc 框架的有相关的知识储备。

其他 rpc 框架使用的经验此处不再赘述。

## 快速迭代

原来一直想写 rpc，却不行动的原因就是想的太多，做的太少。

想一下把全部写完，结果就是啥都没写。

所以本次的开发，每个代码分支做的事情实际很少，只做一个功能点。

陆陆续续经过近一个月的完善，对 rpc 框架有了自己的体会和进一步的认知。

代码实现功能，主要参考 [Apache Dubbo](http://dubbo.apache.org/en-us/)

# 文档

## 文档

文档将使用 markdown 文本的形式，补充 code 层面没有的东西。

## 代码注释

代码有详细的注释，便于阅读和后期维护。

## 测试

目前测试代码算不上完善。后续将陆续补全。

# rpc 模块

rpc-common 公共代码

rpc-server 服务端

rpc-client 客户端

rpc-register 注册中心

rpc-test 测试模块

# 代码分支

[release_0.0.1-server 服务端启动](https://github.com/houbb/rpc/tree/release_0.0.1)

[release_0.0.2-client 客戶端启动](https://github.com/houbb/rpc/tree/release_0.0.2)

[release_0.0.3-客户端调用服务端](https://github.com/houbb/rpc/tree/release_0.0.3)

[release_0.0.4-p2p 客户端主动调用服务端](https://github.com/houbb/rpc/tree/release_0.0.4)

[release_0.0.5-serial 序列化](https://github.com/houbb/rpc/tree/release_0.0.5)

[release_0.0.6-通用的反射调用](https://github.com/houbb/rpc/tree/release_0.0.6)

[release_0.0.7-timeout 超时处理](https://github.com/houbb/rpc/tree/release_0.0.7)

[release_0.0.8-register 注册中心](https://github.com/houbb/rpc/tree/release_0.0.8)

[release_0.0.9-load balance 负载均衡](https://github.com/houbb/rpc/tree/release_0.0.9)

[release_0.1.0-callType 调用方式](https://github.com/houbb/rpc/tree/release_0.1.0)

[release_0.1.1-fail 失败策略](https://github.com/houbb/rpc/tree/release_0.1.1)

[release_0.1.2-generic 泛化调用](https://github.com/houbb/rpc/tree/release_0.1.2)


## 测试代码

从 v0.0.6 及其之后，为了让代码保持纯净，将测试代码全部放在 rpc-example。

每个测试代码和实现版本一一对应。

[rpc-example](https://github.com/houbb/rpc-example)

# 文档说明

[0.0.1-server 服务端启动](https://github.com/houbb/rpc/blob/master/doc/dev/0.0.1-server%20服务端启动.md)

[0.0.2-client 客戶端启动](https://github.com/houbb/rpc/blob/master/doc/dev/0.0.2-client%20客户端启动.md)

[0.0.3-客户端调用服务端](https://github.com/houbb/rpc/blob/master/doc/dev/0.0.3-客户端调用服务端.md)

[0.0.4-p2p 客户端主动调用服务端](https://github.com/houbb/rpc/blob/master/doc/dev/0.0.4-p2p客户端主动调用服务端.md)

[0.0.5-serial 序列化](https://github.com/houbb/rpc/blob/master/doc/dev/0.0.5-serial序列化.md)

[0.0.6-通用反射调用](https://github.com/houbb/rpc/blob/master/doc/dev/0.0.6-通用反射调用.md)

[0.0.7-timeout 超时处理](https://github.com/houbb/rpc/blob/master/doc/dev/0.0.7-timeout超时处理.md)

[0.0.8-register 注册中心](https://github.com/houbb/rpc/blob/master/doc/dev/0.0.8-register注册中心.md)

[0.0.9-load balance 负载均衡](https://github.com/houbb/rpc/blob/master/doc/dev/0.0.9-load-balance-负载均衡.md)

[0.1.0-callType 调用方式](https://github.com/houbb/rpc/blob/master/doc/dev/0.1.0-callType-调用方式.md)

[0.1.1-fail 失败策略](https://github.com/houbb/rpc/blob/master/doc/dev/0.1.1-fail-失败策略)

[0.1.2-generic 泛化调用](https://github.com/houbb/rpc/blob/master/doc/dev/0.1.2-generic-泛化调用)
