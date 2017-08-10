package br.com.jmsstudio.jms.topic;

import javax.jms.*;
import javax.naming.InitialContext;

public class TestTopicProducer {

    public static void main(String[] args) throws Exception {
        System.out.println("Running topic message producer");
        InitialContext context = new InitialContext();

        ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("ConnectionFactory");
        Connection connection = connectionFactory.createConnection();

        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination topicStore = (Destination) context.lookup("store");

        MessageProducer producer = session.createProducer(topicStore);

        for (int i = 0; i < 10; i++) {
            producer.send(session.createTextMessage("Message " + (i+1)));
        }

        session.close();
        connection.close();
        context.close();
    }
}
