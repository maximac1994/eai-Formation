/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listeners;

import MessagesTypes.DemandeFormationMessage;
import MessagesTypes.ListeFormateursCompatibles;
import MessagesTypes.ListeSallesCompatibles;
import business.GestionFormationLocal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * File Formation qui recoit les demande de froamtion et les listes de ressources compatibles
 * @author Maxime
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "FILE_FORMATION")
    ,
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class FileFormation implements MessageListener {
    @EJB
    GestionFormationLocal gfl;

    /**
     *
     */
    public FileFormation() {
    }
    
    /**
     * reception d'un message : analyse de l'entete JMS pour determiner le traitement
     * @param message
     */
    @Override
    public void onMessage(Message message) {
        System.out.println("[Formation] : reception message]");
        ObjectMessage om = (ObjectMessage)message;
        Object o = null;
        try {
            o = om.getObject();
        } catch (JMSException ex) {
            Logger.getLogger(FileFormation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(o instanceof DemandeFormationMessage){
            DemandeFormationMessage dfm = (DemandeFormationMessage)o;
            gfl.traiterDemandeFormation(dfm);
        }
        if(o instanceof ListeFormateursCompatibles){
            ListeFormateursCompatibles dfm = (ListeFormateursCompatibles)o;
            gfl.traiterListeFormateurs(dfm);
        }
        if(o instanceof ListeSallesCompatibles){
            ListeSallesCompatibles dfm = (ListeSallesCompatibles)o;
            gfl.traiterListeSalles(dfm);
        }
    }
    
}
