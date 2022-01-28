package m5;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Scanner;
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

        // 1.创建随机队列 2.创建交换机 3.使用绑定键关键词绑定
        String queue = UUID.randomUUID().toString();
        c.queueDeclare(queue, false, true, true, null);
        c.exchangeDeclare("topic_logs", BuiltinExchangeType.TOPIC);
        System.out.println("输入绑定键关键词，用空格隔开：");// aa bb  cc
        String s = new Scanner(System.in).nextLine();
        String[] a = s.split("\\s+"); //   \s空白字符  +一到多个
        for (String k : a) {
            c.queueBind(queue, "topic_logs", k);
        }
        // 正常接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String msg = new String(message.getBody());
            String key = message.getEnvelope().getRoutingKey();
            System.out.println(key+" --- "+msg);
        };
        CancelCallback cancelCallback = consumerTag -> {};
        c.basicConsume(queue, true, deliverCallback, cancelCallback);
    }
}
