package br.com.jmsstudio.jms.queue;

import javax.jms.*;
import javax.naming.InitialContext;
import java.util.Scanner;

public class TestQueueProducer {

    public static void main(String[] args) throws Exception {
        System.out.println("Running message producer");
        InitialContext context = new InitialContext();

        ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("ConnectionFactory");
        Connection connection = connectionFactory.createConnection();

        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination filaFinanceiro = (Destination) context.lookup("financeiro");

        MessageProducer producer = session.createProducer(filaFinanceiro);

        for (int i = 0; i < 1000; i++) {
            producer.send(session.createTextMessage("Message " + (i+1)));
        }

        session.close();
        connection.close();
        context.close();
    }
}
