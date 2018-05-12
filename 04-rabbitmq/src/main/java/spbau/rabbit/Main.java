package spbau.rabbit;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class Main {
  private static final ConnectionFactory factory = new ConnectionFactory();
  private static Channel channel;
  private static String userID = UUID.randomUUID().toString();

  static {
    factory.setHost("192.168.66.199");
    factory.setUsername("spbau");
    factory.setPassword("spbau");
  }

  private static void subscribeTo(String exchangeName) throws IOException {
    Consumer consumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope,
          AMQP.BasicProperties properties, byte[] body)
          throws IOException {
        Message message = Message.parseFrom(body);
        if (!message.getAuthor().equals(userID)) {
          System.out.println(" [x] Received from " + exchangeName + ": " + message.getContent() + "'");
        }
      }
    };

    channel.exchangeDeclare(exchangeName, "fanout");
    String queueName = channel.queueDeclare().getQueue();
    channel.queueBind(queueName, exchangeName, "");
    channel.basicConsume(queueName, true, consumer);
  }

  private static void sendTo(String exchangeName, String message) throws IOException {
    Message protoMessage = Message.newBuilder().setContent(message).setAuthor(userID).build();
    channel.basicPublish(exchangeName, "", null, protoMessage.toByteArray());
  }

  public static void main(String[] args) throws IOException, TimeoutException {
    Connection connection = factory.newConnection();
    channel = connection.createChannel();
    Scanner scanner = new Scanner(System.in);
    while (true) {
      try {
        String line = scanner.nextLine();
        StringTokenizer tokenizer = new StringTokenizer(line);
        String firstToken = tokenizer.nextToken();
        if ("subscribe".equals(firstToken)) {
          String exchangeName = tokenizer.nextToken();
          subscribeTo(exchangeName);
        } else if ("post".equals(firstToken)) {
          String exchangeName = tokenizer.nextToken();
          StringBuilder message = new StringBuilder();
          while (tokenizer.hasMoreTokens()) {
            message.append(tokenizer.nextToken());
            if (tokenizer.hasMoreTokens()) {
              message.append(' ');
            }
          }
          sendTo(exchangeName, message.toString());
        } else if ("quit".equals(firstToken)) {
          break;
        } else {
          System.out.println("Unknown command. Try again.");
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    channel.close();
    connection.close();
  }
}