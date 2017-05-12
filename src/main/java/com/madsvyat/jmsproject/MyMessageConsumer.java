package com.madsvyat.jmsproject;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 *
 */
public class MyMessageConsumer {

    private static final String QUEUE_NAME = "MyQueue";

    public void receiveMessage() {

        try {
            Connection connection = createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            Destination destination = session.createQueue(QUEUE_NAME);

            MessageConsumer messageConsumer = session.createConsumer(destination);
            Message message = messageConsumer.receive(10_000);

            System.out.println("Received: " + ((TextMessage) message).getText());

            messageConsumer.close();
            session.close();
            connection.close();

        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

    private Connection createConnection() throws JMSException {
        ActiveMQConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory("vm://localhost");

        return connectionFactory.createConnection();
    }

}
