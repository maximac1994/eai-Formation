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
        System.out.println("[FORMATION] : traitement dommande formation");
        int seuilValid = demande.getNbMin();
        int seuilProjet = demande.getNbMin()/2;
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
        System.out.println("[FORMATION] : nombre de participants :"+nbPart+"");
        
        // participation = formation and nb partiicpant < nbMax
        // partiicpation : ajout entreprise
        while (nbAffecte < nbPart) {
            System.out.println("[FORMATION] : instance traitee :"+instF.toString()+"");
            nb = instF.getNbParticipants();
            ParticiperPK pk = new ParticiperPK(demande.getCodeEntreprise(),instF.getIdInstance());
            Participer p = pfl.find(pk);
            if(p==null){
                p = createParticipation(demande.getCodeEntreprise(), instF.getIdInstance());
            }
            
            int nbLibres = demande.getNbMax() - nb;
            System.out.println("[FORMATION] : places libres : "+nbLibres);
            int nbReste = nbPart - nbAffecte;
            for (int i = 0; i < nbLibres && i < nbReste; i++) {
                nbAffecte++;
                ajoutParticipant(p, instF,seuilProjet,seuilValid);
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

    private void ajoutParticipant(Participer p, InstanceFormation i,int seuilProjet, int seuilValid) {
        System.out.println("[FORMATION] : ajout participant");
        int nbAvant = i.getNbParticipants();
        p.setNbParticipants(p.getNbParticipants() + 1);
        i.setNbParticipants(i.getNbParticipants() + 1);
        int nbApres = i.getNbParticipants();
        if((nbAvant<seuilProjet)&&(nbApres>=seuilProjet)){
            //en projet
        }
        if((nbAvant<seuilValid)&&(nbApres>=seuilValid)){
            // valid
        }
    }

    private InstanceFormation generateNewInstance(int idF) {
        System.out.println("[FORMATION] : creation nouvelle instance");
        InstanceFormation newInstF = new InstanceFormation();
        newInstF.setDateCreation(new Date());
        newInstF.setIdFormation(idF);
        newInstF.setEtat("cree");
        newInstF.setNbParticipants(0);
        iffl.create(newInstF);
        return newInstF;
    }

    private Participer createParticipation(String codeE, int idInst) {
        System.out.println("[FORMATION] : creation nouvelle participation");
        Participer p = new Participer();
        p.setNbParticipants(0);
        ParticiperPK ppk = new ParticiperPK();
        ppk.setCodeEntreprise(codeE);
        ppk.setIdInstance(idInst);
        p.setParticiperPK(ppk);
        pfl.create(p);
        return p;
    }

    

}
