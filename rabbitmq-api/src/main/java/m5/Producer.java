package m5;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {
        // 连接服务器
        ConnectionFactory f = new ConnectionFactory();
        f.setHost("192.168.64.140"); // wht6.cn
        f.setPort(5672); // 5672用来收发消息， 15672是管理控制台端口
        f.setUsername("admin");
        f.setPassword("admin");
        Connection con = f.newConnection();
        Channel c = con.createChannel(); // 通信通道

        // 创建 Direct 交换机： direct_logs
        c.exchangeDeclare("topic_logs", BuiltinExchangeType.TOPIC);

        // 向交换机发送消息，并携带路由键关键词
        while (true) {
            System.out.print("(+) 输入消息：");
            String s = new Scanner(System.in).nextLine();
            System.out.print("(+) 输入路由键：");
            String k = new Scanner(System.in).nextLine();
            // 对默认交换机""，会自动使用队列名作为 路由键
            //向topic_logs发送
            c.basicPublish("topic_logs", k, null, s.getBytes());
        }
    }
}
