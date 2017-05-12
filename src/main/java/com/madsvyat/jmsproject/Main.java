package com.madsvyat.jmsproject;

/**
 *
 */
public class Main {

    public static void main(String[] args) {

        Thread producerThread = new Thread(() -> {
            MyMessageProducer producer = new MyMessageProducer();

            producer.sendMessage("Hello");
        });

        Thread consumerThread = new Thread(() -> {
            MyMessageConsumer consumer = new MyMessageConsumer();

            consumer.receiveMessage();
        });

        producerThread.start();
        consumerThread.start();


        try {
            producerThread.join();
            consumerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
