/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repositories;

import entities.Entreprise;
import entities.InstanceFormation;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Maxime
 */
@Stateless
public class EntrepriseFacade extends AbstractFacade<Entreprise> implements EntrepriseFacadeLocal {

    @PersistenceContext(unitName = "ProjetJEE_Formation-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EntrepriseFacade() {
        super(Entreprise.class);
    }

    @Override
    public List<String> getListMails(int id) {
        List<Entreprise> liste = em.createNamedQuery("Entreprise.findEtsByIdInstance")
                .setParameter("idInst", id)
                .getResultList();
        List<String> listToReturn = new ArrayList<>();
        for (Entreprise e : liste){
        listToReturn.add(e.getMailContact());
        }
        return listToReturn;
    }
    
}
