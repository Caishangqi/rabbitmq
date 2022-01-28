package m2;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

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
        // 在服务器上创建一个队列：helloworld
        c.queueDeclare("task_queue",true,false,false,null);
        //向helloworld队列发送消息
        while (true) {
            System.out.print("输入消息：");
            String s = new Scanner(System.in).nextLine();
            c.basicPublish("", "task_queue",
                    MessageProperties.PERSISTENT_BASIC, //持久
                    s.getBytes());
        }
    }
}
