# 消息中心 (IMC: Instant Message Center)
imc将消息推送主体抽象为channel，channel成员可以是显示的、隐式的、动态的、固定的形式。
如在线用户是一个channel，其成员就隐式包含了所有在线用户；而一个地理围栏是一个channel，
其内的移动的成员是动态的。
imc消息的发送和消费者可以是会话、设备、用户、登录账户等。
> 1、支持用户离线消息处理  
> 2、支持按应用推送  
> 3、支持按地理围栏推送  
> 4、支持按地物化身推送和感知  
> 5、支持朋友圈推送  
> 6、支持消息状态回执  
> 7、可以扩展自己的推送类型  
# 架构
> IMC采用kafka广播机制  
>
> 通过扩展comet扩大客户端接入数量，和针对outbox的处理能力  

![如图](https://github.com/carocean/cj-geochat-imc/blob/main/doc/pics/architecture_diagram.png)

# 依赖
> geochat认证中心  
> geochat网关  
