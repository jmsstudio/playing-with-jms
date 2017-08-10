package br.com.jmsstudio.jms.queue;

import javax.jms.*;
import javax.naming.InitialContext;
import java.util.Scanner;

public class TestQueueConsumer {

    public static void main(String[] args) throws Exception {
        System.out.println("Running message consumer");
        InitialContext context = new InitialContext();

        ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("ConnectionFactory");
        Connection connection = connectionFactory.createConnection();

        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination filaFinanceiro = (Destination) context.lookup("financeiro");

        MessageConsumer consumer = session.createConsumer(filaFinanceiro);

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
