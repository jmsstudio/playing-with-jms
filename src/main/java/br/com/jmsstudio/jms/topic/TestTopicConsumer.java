package br.com.jmsstudio.jms.topic;

import br.com.jmsstudio.jms.model.Pedido;

import javax.jms.*;
import javax.naming.InitialContext;
import java.util.Scanner;

public class TestTopicConsumer {

    public static void main(String[] args) throws Exception {
        System.out.println("Running topic consumer");
        System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES", "*");

        InitialContext context = new InitialContext();

        ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("ConnectionFactory");
        Connection connection = connectionFactory.createConnection();
        connection.setClientID("store-consumer");
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topicStore = (Topic) context.lookup("store");

        //consume apenas mensagens que atendam a restrição
        MessageConsumer consumer = session.createDurableSubscriber(topicStore, "Store Consumer", "ebook = false OR ebook is null", false);

        //consume todas as mensagens
//        MessageConsumer consumer = session.createDurableSubscriber(topicStore, "Store Consumer");

        consumer.setMessageListener(message -> {
            try {
                try {
                    TextMessage textMessage = (TextMessage) message;
                    System.out.println("Recebida mensagem: " + textMessage.getText());
                } catch (ClassCastException cce) {
                    ObjectMessage objectMessage = (ObjectMessage) message;
                    Pedido pedidoDeserialized = (Pedido) objectMessage.getObject();
                    System.out.println("Recebida mensagem: Pedido #" + pedidoDeserialized.getCodigo());
                }
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
