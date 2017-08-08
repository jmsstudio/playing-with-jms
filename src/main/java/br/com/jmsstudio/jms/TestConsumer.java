package br.com.jmsstudio.jms;

import javax.jms.*;
import javax.naming.InitialContext;
import java.util.Scanner;

public class TestConsumer {

    public static void main(String[] args) throws Exception {
        System.out.println("Running message consumer");
        InitialContext context = new InitialContext();

        ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("ConnectionFactory");
        Connection connection = connectionFactory.createConnection();

        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination filaFinanceiro = (Destination) context.lookup("financeiro");

        MessageConsumer consumer = session.createConsumer(filaFinanceiro);

        Message message = consumer.receive();

        System.out.println("Recebida mensagem: " + message);

        new Scanner(System.in).nextLine();

        session.close();
        connection.close();
        context.close();
    }
}
