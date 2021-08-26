package org.example.rabbitTutorial;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

public class WorkerMulti extends Thread {

    private static final String TASK_QUEUE_NAME = "task_queue";

    public static final int TEST_COUNT = 3;
    private static final int TEST_TIME = 20000; //20000ms -> 20s
    private static final int TEST_QOS = 1;
    int name;

    public WorkerMulti(int name) {
        this.name = name;
    }

    @Override
    public void run() {
        try {
            ConnectionFactory factory = new ConnectionFactory();

            final Connection connection = factory.newConnection();
            final Channel channel = connection.createChannel();

            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");


            channel.basicQos(2);


            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");

                System.out.println("("+name+") [x] Received '" + message + "'");
                try {
                    doWork(message);
                } finally {
                    System.out.println("("+name+") [x] Done");
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false); //boolean -> autoAck
                }
            };
            channel.basicConsume(TASK_QUEUE_NAME, false, deliverCallback, consumerTag -> { }); //boolean -> autoAck
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private static void doWork(String task) {
        for (char ch : task.toCharArray()) {
            if (ch == '.') {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException _ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}


class Test {
    public static void main(String[] args) {

        for(int i=1; i<=WorkerMulti.TEST_COUNT; i++) {
            new WorkerMulti(i).start();
        }




    }
}