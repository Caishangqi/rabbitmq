package m3;

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

        //生产者不创建队列在fanout这种模式下

        // 创建 fanout 交换机： logs
        // c.exchangeDeclare("logs", "fanout");
        c.exchangeDeclare("logs", BuiltinExchangeType.FANOUT); //默认非持久

        // 向 logs 交换机发送消息
        while (true) {
            System.out.print("输入消息： ");
            String s = new Scanner(System.in).nextLine();
            // 对 fanout 交换机，第二个参数无效
            c.basicPublish("logs", "", null, s.getBytes());
        }
    }
}
