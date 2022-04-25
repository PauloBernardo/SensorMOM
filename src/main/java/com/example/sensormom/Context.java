package com.example.sensormom;

import java.io.IOException;
import java.util.ArrayList;
import javax.jms.*;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Context {
    private static Context instance = null;
    private Double maxLimit;
    private Double minLimit;
    private Double actualLimit;
    private String sensorParameter;
    private final ArrayList<ResizableView> listeners;
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private final Session session;
    private final Connection connection;
    private MessageProducer publisher;

    private Context() throws JMSException {
        super();
        listeners = new ArrayList<>();
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        connection = connectionFactory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    public static Context getInstance() throws IOException, JMSException {
        if (instance == null) {
            try {
                instance = new Context();
            } catch (Exception e) {
                e.printStackTrace();
                instance = null;
                throw  e;
            }
        }
        return instance;
    }

    public void addListening(ResizableView contextListening) {
        listeners.add(contextListening);
    }

    public void removeListening(ResizableView contextListening) {
        listeners.remove(contextListening);
    }

    public Double getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(Double maxLimit) {
        this.maxLimit = maxLimit;
    }

    public Double getMinLimit() {
        return minLimit;
    }

    public void setMinLimit(Double minLimit) {
        this.minLimit = minLimit;
    }

    public Double getActualLimit() {
        return actualLimit;
    }

    public void setActualLimit(Double actualLimit) {
        this.actualLimit = actualLimit;
    }

    public String getSensorParameter() {
        return sensorParameter;
    }

    public void setSensorParameter(String sensorParameter) throws JMSException {
        if (publisher != null) {
            publisher.close();
        }
        /*
         * Criando Topic
         */
        Destination dest = session.createTopic(sensorParameter);

        /*
         * Criando Produtor
         */
        publisher = session.createProducer(dest);
        this.sensorParameter = sensorParameter;
    }

    public void sendMensage(String message) throws JMSException {
        TextMessage textMessage = session.createTextMessage();
        textMessage.setText(message);
        /*
         * Publicando Mensagem
         */
        publisher.send(textMessage);
    }

    public void closeConnection() throws JMSException {
        publisher.close();
        session.close();
        connection.close();
    }
}
