/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repositories;

import entities.InstanceFormation;
import entities.Participer;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Maxime
 */
@Stateless
public class InstanceFormationFacade extends AbstractFacade<InstanceFormation> implements InstanceFormationFacadeLocal {

    @PersistenceContext(unitName = "ProjetJEE_Formation-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public InstanceFormationFacade() {
        super(InstanceFormation.class);
    }

    @Override
    public List<InstanceFormation> findByIdFNb(int idF, int nbMax) {
        
                List<InstanceFormation> liste = em.createNamedQuery("InstanceFormation.findByIdPartNbParticipants")
                .setParameter("idF", idF)
                .setParameter("nbMax", nbMax)
                .getResultList();
                return liste;
    }

   
    
    
}
