/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import MessagesTypes.DemandeFormationMessage;
import MessagesTypes.ListeFormateursCompatibles;
import MessagesTypes.ListeSallesCompatibles;
import javax.ejb.Local;

/**
 *
 * @author Maxime
 */
@Local
public interface GestionFormationLocal {

    public void traiterDemandeFormation(DemandeFormationMessage demande);
    public void traiterListeFormateurs(ListeFormateursCompatibles dfm);
    public void traiterListeSalles(ListeSallesCompatibles dfm);
}
