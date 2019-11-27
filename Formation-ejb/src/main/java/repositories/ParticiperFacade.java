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
public class ParticiperFacade extends AbstractFacade<Participer> implements ParticiperFacadeLocal {

    @PersistenceContext(unitName = "ProjetJEE_Formation-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ParticiperFacade() {
        super(Participer.class);
    }

    
  
    
}
