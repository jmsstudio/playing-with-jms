package br.com.jmsstudio.jms;

import javax.jms.*;
import javax.naming.InitialContext;
import java.util.Enumeration;
import java.util.Scanner;

public class TestQueueBrowser {

    public static void main(String[] args) throws Exception {
        System.out.println("Running queue browser");
        InitialContext context = new InitialContext();

        ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("ConnectionFactory");
        Connection connection = connectionFactory.createConnection();

        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination filaFinanceiro = (Destination) context.lookup("financeiro");

        QueueBrowser browser = session.createBrowser((Queue) filaFinanceiro);

        Enumeration enumeration = browser.getEnumeration();

        while (enumeration.hasMoreElements()) {
            TextMessage msg = (TextMessage) enumeration.nextElement();
            System.out.println("> Mensagem encontrada sem ser consumida: " + msg.getText());
        }

        new Scanner(System.in).nextLine();

        session.close();
        connection.close();
        context.close();
    }
}
