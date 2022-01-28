package m1;

import com.rabbitmq.client.*;

import java.io.IOException;
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

        c.queueDeclare("helloworld", false, false, false, null);

        // 创建回调对象
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            byte[] a = message.getBody();
            String s = new String(a);
            System.out.println("收到： " + s);
        };

        CancelCallback cancelCallback = consumerTag -> {
        };

        // 开始接收消息，把消息传递给一个回调对象进行处理
        /*
        第二个参数：
            是否自动确认， autoAck
            Ack - Acknowledgement
            消费者确认处理消息内部的回执后告诉服务端删除消息
         */
        c.basicConsume(
                "helloworld",
                true,
                deliverCallback, //收到消息交给谁处理
                cancelCallback
        );
    }
}
