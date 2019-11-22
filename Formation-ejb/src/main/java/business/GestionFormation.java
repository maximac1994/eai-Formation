/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import MessagesTypes.DemandeFormationMessage;
import entities.InstanceFormation;
import entities.Participer;
import entities.ParticiperPK;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import repositories.InstanceFormationFacadeLocal;
import repositories.ParticiperFacadeLocal;

/**
 *
 * @author Maxime
 */
@Stateless
public class GestionFormation implements GestionFormationLocal {

    @EJB
    InstanceFormationFacadeLocal iffl;
    @EJB
    ParticiperFacadeLocal pfl;

    @Override
    public void traiterDemandeFormation(DemandeFormationMessage demande) {

        int nbPart = demande.getNbParticipants();
        int nbAffecte = 0;
        int idF = demande.getIdFormation();
        List<InstanceFormation> listIsntF = iffl.findByIdFNb(idF, demande.getNbMax());
        InstanceFormation instF = null;
        int nb = 0;
        if (listIsntF.size() == 0) {
            instF = generateNewInstance(idF);
        } else {
            instF = listIsntF.get(0);
            if (nb >= demande.getNbMax()) {
                instF = generateNewInstance(idF);
            }
        }

        // participation = formation and nb partiicpant < nbMax
        // partiicpation : ajout entreprise
        while (nbAffecte < nbPart) {
            nb = instF.getNbParticipants();
            Participer p = createParticipation(demande.getCodeEntreprise(), instF.getIdInstance());
            int nbLibres = demande.getNbMax() - nb;
            int nbReste = nbPart - nbAffecte;
            for (int i = 0; i < nbLibres && i < nbReste; i++) {
                nbAffecte++;
                ajoutParticipant(p, instF);
            }
            if (nbAffecte < nbPart) {
                InstanceFormation newInstF = new InstanceFormation();
                newInstF.setDateCreation(new Date());
                newInstF.setIdFormation(idF);
                newInstF.setEtat("cree");
                newInstF.setNbParticipants(0);
                iffl.create(newInstF);
                instF = newInstF;
            }
        }
    }

    private void ajoutParticipant(Participer p, InstanceFormation i) {
        p.setNbParticipants(p.getNbParticipants() + 1);
        i.setNbParticipants(i.getNbParticipants() + 1);
    }

    private InstanceFormation generateNewInstance(int idF) {
        InstanceFormation newInstF = new InstanceFormation();
        newInstF.setDateCreation(new Date());
        newInstF.setIdFormation(idF);
        newInstF.setEtat("cree");
        newInstF.setNbParticipants(0);
        iffl.create(newInstF);
        System.out.println(newInstF.getIdInstance());
        return newInstF;
    }

    private Participer createParticipation(String codeE, int idInst) {
        Participer p = new Participer();
        p.setNbParticipants(0);
        ParticiperPK ppk = new ParticiperPK();
        ppk.setCodeEntreprise(codeE);
        ppk.setIdInstance(idInst);
        System.out.println(ppk.getCodeEntreprise());
        p.setParticiperPK(ppk);
        pfl.create(p);
        return p;
    }

}
