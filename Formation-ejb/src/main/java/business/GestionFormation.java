/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import MessagesTypes.DemandeFormationMessage;
import MessagesTypes.EvenementFormationProjet1;
import MessagesTypes.EvenementFormationProjet2;
import MessagesTypes.EvenementFormationValidation;
import MessagesTypes.FormateurComp;
import MessagesTypes.ListeFormateursCompatibles;
import MessagesTypes.ListeSallesCompatibles;
import MessagesTypes.SalleComp;
import entities.InstanceFormation;
import entities.Participer;
import entities.ParticiperPK;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import repositories.EntrepriseFacadeLocal;
import repositories.InstanceFormationFacadeLocal;
import repositories.ParticiperFacadeLocal;
import senders.TopicFormation;

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
    @EJB
    EntrepriseFacadeLocal efl;
    
    TopicFormation topicFormation;
    List<InstanceFormation> formationsAValider;
    HashMap<Integer, ListeSallesCompatibles> sallesEnAttente;
    HashMap<Integer, ListeFormateursCompatibles> formateursEnAttente;
    
    
    public GestionFormation(){
    topicFormation = new TopicFormation();
    
    formationsAValider = new ArrayList<>();
    sallesEnAttente = new HashMap<Integer, ListeSallesCompatibles>();
    formateursEnAttente = new HashMap<Integer, ListeFormateursCompatibles>();
    }
    @Override
    public void traiterDemandeFormation(DemandeFormationMessage demande) {
        System.out.println("[FORMATION] : traitement dommande formation");
        int seuilValid = demande.getNbMin();
        int seuilProjet = demande.getNbMin()/2;
        int nbPart = demande.getNbParticipants();
        int nbAffecte = 0;
        int idF = demande.getIdFormation();
        int duree = demande.getDuree();
        List<InstanceFormation> listIsntF = iffl.findByIdFNb(idF, demande.getNbMax());
        InstanceFormation instF = null;
        int nb = 0;
        if (listIsntF.size() == 0) {
            instF = generateNewInstance(idF,duree);
        } else {
            instF = listIsntF.get(0);
            if (nb >= demande.getNbMax()) {
                instF = generateNewInstance(idF,duree);
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
                newInstF.setDuree(duree);
                newInstF.setDateCreation(new Date());
                newInstF.setIdFormation(idF);
                newInstF.setEtat("EN_ATTENTE");
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
        System.out.println("[TEST A SUPPRIMER] : nbAvant : "+nbAvant+" nbApres : "+nbApres+" seuil : "+seuilProjet);
        if((nbAvant<seuilProjet)&&(nbApres>=seuilProjet)){
            System.out.println("[FORMATION] : passage en projet");
            EvenementFormationProjet1 efp1 = new EvenementFormationProjet1();
            efp1.setIdInstance(i.getIdInstance());
            efp1.setIdFormation(i.getIdFormation());
            topicFormation.sendEvent(efp1, "projet1");
        }
        if((nbAvant<seuilValid)&&(nbApres>=seuilValid)){
            System.out.println("[FORMATION] : passage valide");
            if("EN_PROJET".equals(i.getEtat())){
                sendValidation(i);
            }else{
                formationsAValider.add(i);
            }
        }
    }

    private InstanceFormation generateNewInstance(int idF,int duree) {
        System.out.println("[FORMATION] : creation nouvelle instance");
        InstanceFormation newInstF = new InstanceFormation();
        newInstF.setDateCreation(new Date());
        newInstF.setIdFormation(idF);
        newInstF.setEtat("EN_ATTENTE");
        newInstF.setNbParticipants(0);
        newInstF.setDuree(duree);
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

    private int calcDuree(Date dateDebut, Date dateFin) {
        int duree = 0;
        Date day = new Date(dateDebut.getTime());
        while(day.before(dateFin)){
            DateFormat df = new SimpleDateFormat("EEEE");
            String nomJour = df.format(day);
            if( (!"samedi".equals(nomJour)) && (!"dimanche".equals(nomJour))){
                duree++;
            }
            day.setTime(day.getTime()+(25*3600*1000));
        }
        return duree;
    }

    private void sendValidation(InstanceFormation i) {
        EvenementFormationValidation efv = new EvenementFormationValidation();
            
            efv.setDateDebut(i.getDateDebut());
            int duree = calcDuree(i.getDateDebut(),i.getDateFin());
            efv.setDuree(duree);
            efv.setIdFormateur(i.getIdFormateur());
            efv.setIdInstance(i.getIdInstance());
            efv.setIdSalle(i.getNumeroSalle());
            efv.setMails(efl.getListMails(i.getIdInstance()));
            System.out.println("[TEST A SUPPR] : "+efl.getListMails(i.getIdInstance()).get(0));
            topicFormation.sendEvent(efv, "validation");
    }

    @Override
    public void traiterListeFormateurs(ListeFormateursCompatibles dfm) {
        if(dfm.getFormateursCompatibles().isEmpty()){
            System.out.println("[Formation] Pas de formateur dispo");
        }else{
        if(sallesEnAttente.containsKey(dfm.getIdInstance())){
            System.out.println("[Formation] recuperation des salles");
            affecterRessources(dfm,sallesEnAttente.get(dfm.getIdInstance()));
        }else{
            System.out.println("[Formation] mise en attente Formateur");
            formateursEnAttente.put(dfm.getIdInstance(), dfm);
        }}
    }

    @Override
    public void traiterListeSalles(ListeSallesCompatibles dfm) {
        if(dfm.getListeSallesComp().isEmpty()){
            System.out.println("[Formation] Pas de salles dispo");
        }else{
        if(formateursEnAttente.containsKey(dfm.getIdInstance())){
            System.out.println("[Formation] recuperation des formateurs");
            affecterRessources(formateursEnAttente.get(dfm.getIdInstance()),dfm);
        }else{
            System.out.println("[Formation] mise en attente salle");
            sallesEnAttente.put(dfm.getIdInstance(), dfm);
        }}
    }

    
      private void affecterRessources(ListeFormateursCompatibles forms, ListeSallesCompatibles salles) {
        InstanceFormation inF = iffl.find(salles.getIdInstance());
        System.out.println("[FORMATION] Recherche de la période de dispo");
        int duree = inF.getDuree();
        int nbJours = 0;
        Date day = addJour(new Date(), 5);
        Date dayDebut = addJour(new Date(), 5);
        List<SalleComp> sallesRestantes = fillSalles(salles);
        List<FormateurComp> formateursRestants = fillFormateurs(forms);
        boolean ok = false;
          while (!ok) {
              while ((!sallesRestantes.isEmpty()) && (!formateursRestants.isEmpty()) && (nbJours < duree)) {
                  System.out.println("[TEEEEEEESSSSST] date traitee "+ day + "avec date debut "+dayDebut);
                  removeForms(formateursRestants,day);
                  removeSalles(sallesRestantes,day);
                  day = addJour(day, 1);
                  nbJours ++;
                  System.out.println("[TEEEEEEESSSSST] Nb Jours : "+nbJours);
              }
              if((sallesRestantes.isEmpty()) || (formateursRestants.isEmpty()) || (nbJours < duree)){
              
              nbJours = 0;
              day = addJour(day, 1);
              dayDebut = addJour(day, 0);
              System.out.println("[TEEEEEEESSSSST] sorti de boucle on reprend au "+day);
              formateursRestants = fillFormateurs(forms);
              sallesRestantes = fillSalles(salles);
              }else{
              ok = true;
              }
          }
        System.out.println("[FORMATION] date de début : " + dayDebut);
          System.out.println(formateursRestants.get(0).getIdFormateur());
          System.out.println(sallesRestantes.get(0).getNumeroSalle());
        int idFormateur = formateursRestants.get(0).getIdFormateur();
        String numSalle = sallesRestantes.get(0).getNumeroSalle();
        inF.setIdFormateur(idFormateur);
        inF.setNumeroSalle(numSalle);
        inF.setDateDebut(dayDebut);
        inF.setDateFin(addJour(dayDebut, duree));
        inF.setEtat("EN_PROJET");
        EvenementFormationProjet2 ev = new EvenementFormationProjet2();
        ev.setDateDebut(dayDebut);
        ev.setDuree(duree);
        ev.setIdFormateur(idFormateur);
        ev.setIdInstance(salles.getIdInstance());
        ev.setIdSalle(numSalle);
        topicFormation.sendEvent(ev, "projet2");
        if(formationsAValider.contains(inF)){
            sendValidation(inF);
        }
    }
    
      
      private Date addJour(Date dat, int n){
          Date d = new Date(dat.getTime());
      for(int i = 0; i<n ; i++){
          DateFormat df = new SimpleDateFormat("EEEE");
            String nomJour = df.format(d);
            if( "vendredi".equals(nomJour)){
                d.setTime(d.getTime()+(25*3600*3000));
            }else if( "samedi".equals(d)){
                d.setTime(d.getTime()+3600*24*2000);
            }else{
                d.setTime(d.getTime()+3600*24*1000);
            }
      }
      return d;
      }

    private List<SalleComp> fillSalles(ListeSallesCompatibles salles) {
        List<SalleComp> liste = new ArrayList<>();
        for(SalleComp s : salles.getListeSallesComp()){
        liste.add(s);
    }
        return liste;
    }

    private List<FormateurComp> fillFormateurs(ListeFormateursCompatibles forms) {
        List<FormateurComp> liste = new ArrayList<FormateurComp>();
        for(FormateurComp f : forms.getFormateursCompatibles()){
        liste.add(f);
        }
        return liste;
    }

    private boolean containsDate(SalleComp s, Date day) {
        boolean hasDate = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(Date d : s.getDatesOccupees()){
            if(sdf.format(day).equals(sdf.format(d))){
                hasDate = true;
            }
        }
        return hasDate;
    }

    private boolean containsDate(FormateurComp f, Date day) {
       boolean hasDate = false;
       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(Date d : f.getDatesOccupees()){
            if(sdf.format(day).equals(sdf.format(d))){
                hasDate = true;
            }
        }
        return hasDate;
    }

    private void removeForms(List<FormateurComp> formateursRestants, Date day) {
        List<FormateurComp> toDelete = new ArrayList<>();
        for (FormateurComp f : formateursRestants) {
            if (containsDate(f, day)) {
                toDelete.add(f);
            }
        }
        for(FormateurComp f : toDelete){
        formateursRestants.remove(f);
        }
    }
    
    private void removeSalles(List<SalleComp> sallesRestants, Date day) {
        List<SalleComp> toDelete = new ArrayList<>();
        for (SalleComp s : sallesRestants) {
            if (containsDate(s, day)) {
                toDelete.add(s);
            }
        }
        for(SalleComp s : toDelete){
        sallesRestants.remove(s);
        }
    }

    

    

}
