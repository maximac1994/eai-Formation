/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Maxime
 */
@Entity
@Table(name = "entreprise")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Entreprise.findAll", query = "SELECT e FROM Entreprise e")
    , @NamedQuery(name = "Entreprise.findByCodeEntreprise", query = "SELECT e FROM Entreprise e WHERE e.codeEntreprise = :codeEntreprise")
    , @NamedQuery(name = "Entreprise.findByNom", query = "SELECT e FROM Entreprise e WHERE e.nom = :nom")
    , @NamedQuery(name = "Entreprise.findByMailContact", query = "SELECT e FROM Entreprise e WHERE e.mailContact = :mailContact")
, @NamedQuery(name = "Entreprise.findEtsByIdInstance", query = "SELECT e FROM Entreprise e, Participer p WHERE e.codeEntreprise = p.participerPK.codeEntreprise AND p.participerPK.idInstance = :idInst")})


public class Entreprise implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "codeEntreprise")
    private String codeEntreprise;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nom")
    private String nom;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "mailContact")
    private String mailContact;

    public Entreprise() {
    }

    public Entreprise(String codeEntreprise) {
        this.codeEntreprise = codeEntreprise;
    }

    public Entreprise(String codeEntreprise, String nom, String mailContact) {
        this.codeEntreprise = codeEntreprise;
        this.nom = nom;
        this.mailContact = mailContact;
    }

    public String getCodeEntreprise() {
        return codeEntreprise;
    }

    public void setCodeEntreprise(String codeEntreprise) {
        this.codeEntreprise = codeEntreprise;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getMailContact() {
        return mailContact;
    }

    public void setMailContact(String mailContact) {
        this.mailContact = mailContact;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codeEntreprise != null ? codeEntreprise.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Entreprise)) {
            return false;
        }
        Entreprise other = (Entreprise) object;
        if ((this.codeEntreprise == null && other.codeEntreprise != null) || (this.codeEntreprise != null && !this.codeEntreprise.equals(other.codeEntreprise))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Entreprise[ codeEntreprise=" + codeEntreprise + " ]";
    }
    
}
