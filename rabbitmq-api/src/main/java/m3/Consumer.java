package m3;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        // 连接服务器
        ConnectionFactory f = new ConnectionFactory();
        f.setHost("192.168.64.140"); // wht6.cn
        f.setPort(5672); // 5672用来收发消息， 15672是管理控制台端口
        f.setUsername("admin");
        f.setPassword("admin");
        Connection con = f.newConnection();
        Channel c = con.createChannel(); // 通信通道

        // 1.创建队列 2.创建交换机 3.绑定
        String queue = UUID.randomUUID().toString();
        c.queueDeclare(queue, false, true, true, null);
        c.exchangeDeclare("logs", BuiltinExchangeType.FANOUT);
        // 对fanout交换机，第三个参数无效
        c.queueBind(queue, "logs", "");

        // 正常从队列接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String s = new String(message.getBody());
            System.out.println("(+) 收到： "+s);
        };
        CancelCallback cancelCallback = consumerTag -> {};
        c.basicConsume(queue, true, deliverCallback, cancelCallback);
    }
}
