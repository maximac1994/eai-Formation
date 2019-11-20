/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repositories;

import entities.InstanceFormation;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Maxime
 */
@Local
public interface InstanceFormationFacadeLocal {

    void create(InstanceFormation instanceFormation);

    void edit(InstanceFormation instanceFormation);

    void remove(InstanceFormation instanceFormation);

    InstanceFormation find(Object id);

    List<InstanceFormation> findAll();

    List<InstanceFormation> findRange(int[] range);

    int count();
    
}
