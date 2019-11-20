/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Maxime
 */
@Embeddable
public class ParticiperPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "codeEntreprise")
    private String codeEntreprise;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idInstance")
    private int idInstance;

    public ParticiperPK() {
    }

    public ParticiperPK(String codeEntreprise, int idInstance) {
        this.codeEntreprise = codeEntreprise;
        this.idInstance = idInstance;
    }

    public String getCodeEntreprise() {
        return codeEntreprise;
    }

    public void setCodeEntreprise(String codeEntreprise) {
        this.codeEntreprise = codeEntreprise;
    }

    public int getIdInstance() {
        return idInstance;
    }

    public void setIdInstance(int idInstance) {
        this.idInstance = idInstance;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codeEntreprise != null ? codeEntreprise.hashCode() : 0);
        hash += (int) idInstance;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ParticiperPK)) {
            return false;
        }
        ParticiperPK other = (ParticiperPK) object;
        if ((this.codeEntreprise == null && other.codeEntreprise != null) || (this.codeEntreprise != null && !this.codeEntreprise.equals(other.codeEntreprise))) {
            return false;
        }
        if (this.idInstance != other.idInstance) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ParticiperPK[ codeEntreprise=" + codeEntreprise + ", idInstance=" + idInstance + " ]";
    }
    
}
