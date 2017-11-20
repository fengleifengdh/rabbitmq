package com.flf.fenbu2.two;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;

import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;

public class Send {
  private final static String QUEUE_NAME = "hello";

  public static void main(String[] argv) throws java.io.IOException, TimeoutException {
    /*连接可以抽象为socket连接，为我们维护协议版本信息和协议证书等。这里我们连接
    上了本机的消息服务器实体（localhost）。如果我们想连接其它主机上的RabbitMQ服务，只需要修改一下主机名或是IP就可以了*/
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    /*接下创建channel（信道），这是绝大多数API都能用到的。为了发送消息，你必须要声明一个消息消息队列，然后向该队列里推送消息*/
    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//    String message = "Hello World1!";
    String message = getMessage(argv);

    channel.basicPublish("", "hello", null, message.getBytes());
    System.out.println(" [x] Sent '" + message + "'");
    
    /*声明一个幂等的队列（只有在该队列不存在时，才会被创建）。消息的上下文是一个
      字节数组，你可以指定它的编码。*/
    channel.close();
    connection.close();
  }
  private static String getMessage(String[] strings){
      if (strings.length < 1)
          return "Hello World!";
      return joinStrings(strings, " ");
  }

  private static String joinStrings(String[] strings, String delimiter) {
      int length = strings.length;
      if (length == 0) return "";
      StringBuilder words = new StringBuilder(strings[0]);
      for (int i = 1; i < length; i++) {
          words.append(delimiter).append(strings[i]);
      }
      return words.toString();
  }
}
