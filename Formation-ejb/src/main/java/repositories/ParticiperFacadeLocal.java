/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repositories;

import entities.Participer;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Maxime
 */
@Local
public interface ParticiperFacadeLocal {

    void create(Participer participer);

    void edit(Participer participer);

    void remove(Participer participer);

    Participer find(Object id);

    List<Participer> findAll();

    List<Participer> findRange(int[] range);

    int count();
    
}
