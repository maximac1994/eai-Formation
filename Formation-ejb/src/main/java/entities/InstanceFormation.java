/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Maxime
 */
@Entity
@Table(name = "instanceFormation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InstanceFormation.findAll", query = "SELECT i FROM InstanceFormation i")
    , @NamedQuery(name = "InstanceFormation.findByIdInstance", query = "SELECT i FROM InstanceFormation i WHERE i.idInstance = :idInstance")
    , @NamedQuery(name = "InstanceFormation.findByIdFormation", query = "SELECT i FROM InstanceFormation i WHERE i.idFormation = :idFormation")
    , @NamedQuery(name = "InstanceFormation.findByNumeroSalle", query = "SELECT i FROM InstanceFormation i WHERE i.numeroSalle = :numeroSalle")
    , @NamedQuery(name = "InstanceFormation.findByDateDebut", query = "SELECT i FROM InstanceFormation i WHERE i.dateDebut = :dateDebut")
    , @NamedQuery(name = "InstanceFormation.findByEtat", query = "SELECT i FROM InstanceFormation i WHERE i.etat = :etat")
    , @NamedQuery(name = "InstanceFormation.findByDateFin", query = "SELECT i FROM InstanceFormation i WHERE i.dateFin = :dateFin")
    , @NamedQuery(name = "InstanceFormation.findByDateCreation", query = "SELECT i FROM InstanceFormation i WHERE i.dateCreation = :dateCreation")})
public class InstanceFormation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idInstance")
    private Integer idInstance;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idFormation")
    private int idFormation;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "numeroSalle")
    private String numeroSalle;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dateDebut")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "etat")
    private String etat;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dateFin")
    @Temporal(TemporalType.DATE)
    private Date dateFin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dateCreation")
    @Temporal(TemporalType.DATE)
    private Date dateCreation;

    public InstanceFormation() {
    }

    public InstanceFormation(Integer idInstance) {
        this.idInstance = idInstance;
    }

    public InstanceFormation(Integer idInstance, int idFormation, String numeroSalle, Date dateDebut, String etat, Date dateFin, Date dateCreation) {
        this.idInstance = idInstance;
        this.idFormation = idFormation;
        this.numeroSalle = numeroSalle;
        this.dateDebut = dateDebut;
        this.etat = etat;
        this.dateFin = dateFin;
        this.dateCreation = dateCreation;
    }

    public Integer getIdInstance() {
        return idInstance;
    }

    public void setIdInstance(Integer idInstance) {
        this.idInstance = idInstance;
    }

    public int getIdFormation() {
        return idFormation;
    }

    public void setIdFormation(int idFormation) {
        this.idFormation = idFormation;
    }

    public String getNumeroSalle() {
        return numeroSalle;
    }

    public void setNumeroSalle(String numeroSalle) {
        this.numeroSalle = numeroSalle;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idInstance != null ? idInstance.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InstanceFormation)) {
            return false;
        }
        InstanceFormation other = (InstanceFormation) object;
        if ((this.idInstance == null && other.idInstance != null) || (this.idInstance != null && !this.idInstance.equals(other.idInstance))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.InstanceFormation[ idInstance=" + idInstance + " ]";
    }
    
}
