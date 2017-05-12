package com.madsvyat.jmsproject;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 *
 */
public class MyMessageProducer {

    private static final String QUEUE_NAME = "MyQueue";

    public void sendMessage(String message) {
        try {
            Connection connection = createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            Destination destination = session.createQueue(QUEUE_NAME);

            MessageProducer messageProducer = session.createProducer(destination);
            messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            TextMessage textMessage = session.createTextMessage(message);

            messageProducer.send(textMessage);

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
