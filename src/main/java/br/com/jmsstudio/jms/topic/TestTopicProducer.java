package br.com.jmsstudio.jms.topic;

import br.com.jmsstudio.jms.model.Pedido;
import br.com.jmsstudio.jms.model.PedidoFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.xml.bind.JAXB;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

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

        List<Message> textMessages = createTextMessages(session);
        textMessages.stream().forEach((message1) -> {
            try {
                producer.send(message1);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });

        Message pedidoXmlMessage = createPedidoXmlMessage(session);
        producer.send(pedidoXmlMessage);

        Message pedidoObjectMessage = createPedidoObjectMessage(session);
        producer.send(pedidoObjectMessage);

        session.close();
        connection.close();
        context.close();
    }

    private static List<Message> createTextMessages(Session session) throws JMSException {
        List<Message> messages = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TextMessage message = session.createTextMessage("Message " + (i + 1));
            message.setBooleanProperty("ebook", i % 2 == 0);
            messages.add(message);
        }

        return messages;
    }

    private static Message createPedidoXmlMessage(Session session) throws JMSException {
        Pedido pedido = new PedidoFactory().geraPedidoComValores();
        StringWriter stringWriter = new StringWriter();

        JAXB.marshal(pedido, stringWriter);

        String xml = stringWriter.toString();

        return session.createTextMessage(xml);
    }

    private static Message createPedidoObjectMessage(Session session) throws JMSException {
        Pedido pedido = new PedidoFactory().geraPedidoComValores();
        return session.createObjectMessage(pedido);
    }


}
