# rpc

[rpc](https://github.com/houbb/rpc) 是基于 netty 实现的 java rpc 框架，类似于 dubbo。

[![Build Status](https://travis-ci.com/houbb/rpc.svg?branch=master)](https://travis-ci.com/houbb/rpc)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.houbb/rpc/badge.svg)](http://mvnrepository.com/artifact/com.github.houbb/rpc)
[![](https://img.shields.io/badge/license-Apache2-FF0080.svg)](https://github.com/houbb/rpc/blob/master/LICENSE.txt)
[![Open Source Love](https://badges.frapsoft.com/os/v2/open-source.svg?v=103)](https://github.com/houbb/nlp-common)

> [变更日志](https://github.com/houbb/rpc/blob/master/CHANGELOG.md)

主要用于个人学习，由渐入深，理解 rpc 的底层实现原理。

## 特性

- 基于 netty4 的客户端调用服务端

- p2p 调用

- serial 序列化支持

- timeout 超时处理

- register center 注册中心

- load balance 负载均衡

- callType 支持 oneway sync 等调用方式

- fail 支持 failOver failFast 等失败处理策略

- generic 支持泛化调用

- gracefully 优雅关闭

- rpcInterceptor 拦截器

- filter 过滤器

- check 客户端启动检测服务是否可用

- heartbeat 服务端心跳

# 快速入门

## maven 引入

```xml
<dependency>
    <groupId>com.github.houbb</groupId>
    <artifactId>rpc-all</artifactId>
    <version>${rpc.version}</version>
</dependency>
```

ps: 如果本地 p2p 测试，register 注册中心可选。

## 测试

### 注册中心

```java
RegisterBs.newInstance().start();
```

### 服务端

```java
ServiceBs.getInstance()
         .register(ServiceIdConst.CALC, new CalculatorServiceImpl())
         .registerCenter(ServiceIdConst.REGISTER_CENTER)
         .expose();
```

### 客户端

```java
// 服务配置信息
ReferenceConfig<CalculatorService> config = ClientBs.newInstance();
config.serviceId(ServiceIdConst.CALC);
config.serviceInterface(CalculatorService.class);
// 自动发现服务
config.subscribe(true);
config.registerCenter(ServiceIdConst.REGISTER_CENTER);
// 拦截器测试
config.rpcInterceptor(new CostTimeInterceptor());

CalculatorService calculatorService = config.reference();
CalculateRequest request = new CalculateRequest();
request.setOne(10);
request.setTwo(20);

CalculateResponse response = calculatorService.sum(request);
System.out.println(response);
```

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

代码实现功能，主要参考 [Apache Dubbo](https://dubbo.apache.org/zh/docs/introduction/)

# 文档

## 文档

文档将使用 markdown 文本的形式，补充 code 层面没有的东西。

## 代码注释

代码有详细的注释，便于阅读和后期维护。

## 测试

目前测试代码算不上完善。后续将陆续补全。

# rpc 模块

| 模块 | 说明 |
|:---|:---|
| rpc-common | 公共代码 |
| rpc-register | 注册中心 |
| rpc-server | 服务端 |
| rpc-client | 客户端 |
| rpc-all | 全部引用模块（简化包引用） |

# 代码分支

[release_0.0.1-server 服务端启动](https://github.com/houbb/rpc/tree/release_0.0.1)

[release_0.0.2-client 客户端启动](https://github.com/houbb/rpc/tree/release_0.0.2)

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

[release_0.1.3-gracefully 优雅关闭](https://github.com/houbb/rpc/tree/release_0.1.3)

[release_0.1.4-rpcInterceptor 拦截器](https://github.com/houbb/rpc/tree/release_0.1.4)

# 文档说明

[0.0.1-server 服务端启动](https://github.com/houbb/rpc/blob/master/doc/dev/0.0.1-server%20服务端启动.md)

[0.0.2-client 客户端启动](https://github.com/houbb/rpc/blob/master/doc/dev/0.0.2-client%20客户端启动.md)

[0.0.3-客户端调用服务端](https://github.com/houbb/rpc/blob/master/doc/dev/0.0.3-客户端调用服务端.md)

[0.0.4-p2p 客户端主动调用服务端](https://github.com/houbb/rpc/blob/master/doc/dev/0.0.4-p2p客户端主动调用服务端.md)

[0.0.5-serial 序列化](https://github.com/houbb/rpc/blob/master/doc/dev/0.0.5-serial序列化.md)

[0.0.6-通用反射调用](https://github.com/houbb/rpc/blob/master/doc/dev/0.0.6-通用反射调用.md)

[0.0.7-timeout 超时处理](https://github.com/houbb/rpc/blob/master/doc/dev/0.0.7-timeout超时处理.md)

[0.0.8-register 注册中心](https://github.com/houbb/rpc/blob/master/doc/dev/0.0.8-register注册中心.md)

[0.0.9-load balance 负载均衡](https://github.com/houbb/rpc/blob/master/doc/dev/0.0.9-load-balance-负载均衡.md)

[0.1.0-callType 调用方式](https://github.com/houbb/rpc/blob/master/doc/dev/0.1.0-callType-调用方式.md)

[0.1.1-fail 失败策略](https://github.com/houbb/rpc/blob/master/doc/dev/0.1.1-fail-失败策略.md)

[0.1.2-generic 泛化调用](https://github.com/houbb/rpc/blob/master/doc/dev/0.1.2-generic-泛化调用.md)

[0.1.3-gracefully 优雅关闭](https://github.com/houbb/rpc/blob/master/doc/dev/0.1.3-gracefully-优雅关闭.md)

[0.1.4-rpcInterceptor 拦截器](https://github.com/houbb/rpc/blob/master/doc/dev/0.1.4-rpcInterceptor-拦截器.md)

# 测试代码

从 v0.0.6 及其之后，为了让代码保持纯净，将测试代码全部放在 rpc-example。

每个测试代码和实现版本一一对应。

ps: 这部分测试代码可以关注公众号【老马啸西风】，后台回复【rpc】领取。

![qrcode](qrcode.jpg)

# 后期 ROAD-MAP

- [x] all 模块

- [x] check 客户端启动检测
  
- [x] register 是否注册到注册中心
  
- [x] delay 延迟暴露

- [x] 关闭时通知 register center

- [x] 优雅关闭添加超时设置
  
- [x] heartbeat 心跳检测机制

- [x] 完善 load-balance 实现
  
- [x] 完善 filter 实现
  
- [x] 完善 rpcInterceptor 实现

- [ ] 失败重试的拓展
  
尝试其他服务端

指定重试策略（sisyphus）

- [ ] route 路由规则

可以和 echo 回声检测一起实现

- [ ] echo 回声服务

- [ ] spring 整合

- [ ] springboot 整合

- [ ] telnet 命令行治理
  
- [ ] rpc-admin 控台管理

服务治理

- [ ] async 异步执行
  
- [ ] ~~cache 结果缓存？~~

- [ ] ~~validator 参数校验~~

- [ ] ~~服务降级~~

- [ ] ~~version 多版本管理~~



select sum(renting_total_num) from xiaoqu_summary_info;


select count(*) from house_rent_info;


# 中间件等工具开源矩阵

[heaven: 收集开发中常用的工具类](https://github.com/houbb/heaven)

[rpc: 基于 netty4 实现的远程调用工具](https://github.com/houbb/rpc)

[mq: 简易版 mq 实现](https://github.com/houbb/mq)

[ioc: 模拟简易版 spring ioc](https://github.com/houbb/ioc)

[mybatis: 简易版 mybatis](https://github.com/houbb/mybatis)

[cache: 渐进式 redis 缓存](https://github.com/houbb/cache)

[jdbc-pool: 数据库连接池实现](https://github.com/houbb/jdbc-pool)

[sandglass: 任务调度时间工具框架](https://github.com/houbb/sandglass)

[sisyphus: 支持注解的重试框架](https://github.com/houbb/sisyphus)

[resubmit: 防止重复提交框架，支持注解](https://github.com/houbb/resubmit)

[auto-log: 日志自动输出](https://github.com/houbb/auto-log)

[async: 多线程异步并行框架](https://github.com/houbb/async)



