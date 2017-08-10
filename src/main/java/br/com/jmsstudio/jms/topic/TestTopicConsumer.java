package br.com.jmsstudio.jms.topic;

import javax.jms.*;
import javax.naming.InitialContext;
import java.util.Scanner;

public class TestTopicConsumer {

    public static void main(String[] args) throws Exception {
        System.out.println("Running topic consumer");
        InitialContext context = new InitialContext();

        ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("ConnectionFactory");
        Connection connection = connectionFactory.createConnection();
        connection.setClientID("store-consumer");
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topicStore = (Topic) context.lookup("store");

        MessageConsumer consumer = session.createDurableSubscriber(topicStore, "Store Consumer");

        consumer.setMessageListener(message -> {
            TextMessage textMessage = (TextMessage) message;
            try {
                System.out.println("Recebida mensagem: " + textMessage.getText());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });


        new Scanner(System.in).nextLine();

        session.close();
        connection.close();
        context.close();
    }
}
