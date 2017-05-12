package com.madsvyat.jmsproject.chat;

import javax.jms.*;
import javax.naming.InitialContext;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by root on 12.05.17.
 */
public class Chat implements MessageListener {

    private TopicSession pubSession;
    private TopicPublisher publisher;
    private TopicConnection connection;
    private String username;

    public Chat(String topicFactory, String topicName, String username)
            throws Exception {

        InitialContext ctx = new InitialContext();

        TopicConnectionFactory conFactory =
                (TopicConnectionFactory)ctx.lookup(topicFactory);

        TopicConnection connection = conFactory.createTopicConnection();

        TopicSession pubSession = connection.createTopicSession(
                false, Session.AUTO_ACKNOWLEDGE);
        TopicSession subSession = connection.createTopicSession(
                false, Session.AUTO_ACKNOWLEDGE);

        Topic chatTopic = (Topic)ctx.lookup(topicName);

        TopicPublisher publisher =
                pubSession.createPublisher(chatTopic);
        TopicSubscriber subscriber =
                subSession.createSubscriber(chatTopic, null, true);

        subscriber.setMessageListener(this);

        this.connection = connection;
        this.pubSession = pubSession;
        this.publisher = publisher;
        this.username = username;

        connection.start( );
    }

    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText( );
            System.out.println(text);
        } catch (JMSException jmse){ jmse.printStackTrace( ); }
    }

    protected void writeMessage(String text) throws JMSException {
        TextMessage message = pubSession.createTextMessage( );
        message.setText(username+": "+text);
        publisher.publish(message);
    }

    public void close( ) throws JMSException {
        connection.close( );
    }

    public static void main(String [] args){
        try{
            if (args.length!=3)
                System.out.println("Factory, Topic, or username missing");

            Chat chat = new Chat(args[0],args[1],args[2]);

            BufferedReader commandLine = new
                    java.io.BufferedReader(new InputStreamReader(System.in));

            while(true){
                String s = commandLine.readLine( );
                if (s.equalsIgnoreCase("exit")){
                    chat.close( ); // close down connection
                    System.exit(0);// exit program
                } else
                    chat.writeMessage(s);
            }
        } catch (Exception e){ e.printStackTrace( ); }
    }

}
