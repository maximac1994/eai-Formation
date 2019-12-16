/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package senders;

import MessagesTypes.EvenementFormation;
import MessagesTypes.ReponseExistenceFormation;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Topic formation
 * @author Maxime
 */
public class TopicFormation {
        Context context = null;
    ConnectionFactory factory = null;
    Connection connection = null;
    String factoryName = "jms/__defaultConnectionFactory";
    String destName = "TOPIC_FORMATION";
    Destination dest = null;
    Session session = null;
    MessageProducer sender = null;

    /**
     * creation contexte
     */
    public void createContext() {
        try {
            // create the JNDI initial context.
            context = new InitialContext();
            // look up the ConnectionFactory
            factory = (ConnectionFactory) context.lookup(factoryName);
            // look up the Destination
            dest = (Destination) context.lookup(destName);
        } catch (NamingException exception) {
            exception.printStackTrace();
        } 
    }

    /**
     * connection JMS
     * @throws JMSException
     */
    public void connect() throws JMSException {
        // create the connection
        connection = factory.createConnection();
        // create the session
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // create the sender
        sender = session.createProducer(dest);
        // start the connection, to enable message sends
        connection.start();
    }

    /**
     * envoie un evenement vers le topic FORMATION. Il sera ensuite analysé par les autres applis
     * @param ev
     * @param type
     */
    public void sendEvent(EvenementFormation ev, String type) {
       
            try {
                if (context == null) {
                    this.createContext();
                }
                
                this.connect();
                ObjectMessage message = session.createObjectMessage(ev);
                message.setJMSType(type);
                //message.setJMSType();
                sender.send(message);
                this.close();
            } catch (JMSException ex) {
                Logger.getLogger(TopicFormation.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    /**
     * fermeture de la connection
     */
    public void close() {
        // close the context
        if (context != null) {
            try {
                context.close();
            } catch (NamingException exception) {
                exception.printStackTrace();
            }
        }

        // close the connection
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException exception) {
                exception.printStackTrace();
            }
        }
    }

}
