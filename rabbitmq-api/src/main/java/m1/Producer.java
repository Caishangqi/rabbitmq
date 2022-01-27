package m1;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException {
        // 连接服务器
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.64.140"); // wht6.cn
        connectionFactory.setPort(5672); // 5672用来收发消息， 15672是管理控制台端口
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");

        Connection con = connectionFactory.newConnection();
        Channel c = con.createChannel(); // 通信通道

        // 在服务器上创建一个队列：helloworld
        // 如果队列在服务器上已经存在，不会重复创建

        /*
        参数：
          第2个参数： 是否是持久队列
          第3个参数： 是否是排他队列、独占队列
          第4个参数： 是否自动删除
          第5个参数： 队列的其他属性
         */

        c.queueDeclare("helloword", false, false, false, null);


        // 向 helloworld 队列发送消息
        /*
        "" - 默认的交换机
        null - 消息的其他参数属性
         */
        c.basicPublish("", "helloworld", null, "Hello world!".getBytes());

        c.close(); //通道断开
        con.close(); //链接断开
    }
    
}
