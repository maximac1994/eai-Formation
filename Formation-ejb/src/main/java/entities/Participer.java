/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Maxime
 */
@Entity
@Table(name = "participer")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Participer.findAll", query = "SELECT p FROM Participer p")
    , @NamedQuery(name = "Participer.findByCodeEntreprise", query = "SELECT p FROM Participer p WHERE p.participerPK.codeEntreprise = :codeEntreprise")
    , @NamedQuery(name = "Participer.findByIdInstance", query = "SELECT p FROM Participer p WHERE p.participerPK.idInstance = :idInstance")
    , @NamedQuery(name = "Participer.findByNbParticipants", query = "SELECT p FROM Participer p WHERE p.nbParticipants = :nbParticipants")})
public class Participer implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ParticiperPK participerPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nbParticipants")
    private int nbParticipants;

    public Participer() {
    }

    public Participer(ParticiperPK participerPK) {
        this.participerPK = participerPK;
    }

    public Participer(ParticiperPK participerPK, int nbParticipants) {
        this.participerPK = participerPK;
        this.nbParticipants = nbParticipants;
    }

    public Participer(String codeEntreprise, int idInstance) {
        this.participerPK = new ParticiperPK(codeEntreprise, idInstance);
    }

    public ParticiperPK getParticiperPK() {
        return participerPK;
    }

    public void setParticiperPK(ParticiperPK participerPK) {
        this.participerPK = participerPK;
    }

    public int getNbParticipants() {
        return nbParticipants;
    }

    public void setNbParticipants(int nbParticipants) {
        this.nbParticipants = nbParticipants;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (participerPK != null ? participerPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Participer)) {
            return false;
        }
        Participer other = (Participer) object;
        if ((this.participerPK == null && other.participerPK != null) || (this.participerPK != null && !this.participerPK.equals(other.participerPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Participer[ participerPK=" + participerPK + " ]";
    }
    
}
