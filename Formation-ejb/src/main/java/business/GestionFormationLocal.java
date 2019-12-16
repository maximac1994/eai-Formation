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
 * Gestion des instances de formation
 * @author Maxime
 */
@Local
public interface GestionFormationLocal {

    /**
     * traiter une demande de formation après réception parl e service commercial
     * @param demande
     */
    public void traiterDemandeFormation(DemandeFormationMessage demande);

    /**
     * traiter la reception de la liste des formateurs compatibles
     * @param dfm
     */
    public void traiterListeFormateurs(ListeFormateursCompatibles dfm);

    /**
     * traiter la reception de la liste des salles compatibles
     * @param dfm
     */
    public void traiterListeSalles(ListeSallesCompatibles dfm);
}
