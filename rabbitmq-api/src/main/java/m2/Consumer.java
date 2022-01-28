package m2;

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
        // 在服务器上创建一个队列：helloworld
        c.queueDeclare("task_queue",true,false,false,null);

        // 创建对调对象
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String s = new String(message.getBody());
            System.out.println("(+) 收到： "+s);
            //遍历消息所有字符，每遇到一个 '.' 暂停1秒，模拟耗时任务
            for (int i = 0; i <s.length(); i++) {
                if (s.charAt(i) == '.') {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            }
            // (2) 发送回执 消息处理完毕后发送回执
            // c.basicAck(回执, 是否同时确认之前收到过的多条消息);
            c.basicAck(message.getEnvelope().getDeliveryTag(), false);
            System.out.println("(!) 消息处理完成");
        };
        CancelCallback cancelCallback = consumerTag -> {};

        // qos=1，每次收一条，处理完之前不收下一条，手动ack模式下才有效
        c.basicQos(1);

        // 接收消息
        c.basicConsume("task_queue",
                false, // (1) 手动确认模式
                deliverCallback,
                cancelCallback
        );
    }
}
