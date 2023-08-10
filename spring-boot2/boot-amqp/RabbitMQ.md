# RabbitMQ
- 访问页面：http://172.21.34.104:15672/#/
- 查看版本：rabbitmqctl version
- 查看当前运行状态：ps aux|grep rabbitmq

## 核心概念
- Server: 又称Broker，接受客户端的连接，实现AMQP实体服务
- Connection：连接，应用程序域Broker的网络连接
- Channel： 网络通信，基于所有的操作都在Channel中进行，Channel是进行消息读写的通道，客户端可建立多个Chan呢了，每个Channel代表一个会话任务
- Message: 消息，服务器和应用程序之间传递数据，又Properties和Body组成，Properties可以对消息进行修饰，比如消息的优先级，延迟等高级特性，Body则就是消息内容；
- Virtual host:虚拟地址，用于进行逻辑隔离，最上层的消息路由：一个Virtual  host里面可以有若干个Exchange和Queue，同一个Virtual host里面不能有相同名称的Exchange和Queue
- Exchange：交换机，接收消息，根据路由键转发消息到绑定的队列中
- Binding: Exchange 和Queue之间的虚拟连接，binding中可以包含routing key
- Routing Key: 一个路由规则，虚拟机可用它来确定如何路由一个特定消息
- Queue： 消息队列，保存消息并将它转发给消费者；

## FQA
###  channel error; protocol method: #method<channel.close>(reply-code=404, reply-text=NOT_FOUND - no queue 'bleem_ribbit_route' in vhost '/', class-id=50, method-id=10)
问题：监听的队列名写错了
