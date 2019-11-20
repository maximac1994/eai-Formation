/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listeners;

import MessagesTypes.DemandeFormationMessage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 *
 * @author Maxime
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "FILE_FORMATION")
    ,
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class FileFormation implements MessageListener {
    
    public FileFormation() {
    }
    
    @Override
    public void onMessage(Message message) {
        ObjectMessage om = (ObjectMessage)message;
        Object o = null;
        try {
            o = om.getObject();
        } catch (JMSException ex) {
            Logger.getLogger(FileFormation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(o instanceof DemandeFormationMessage){
            DemandeFormationMessage dfm = (DemandeFormationMessage)o;
            
        }
    }
    
}
