// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.domain;

import com.labsynch.cmpdreg.domain.IsoSalt;
import com.labsynch.cmpdreg.domain.Isotope;
import com.labsynch.cmpdreg.domain.Salt;
import com.labsynch.cmpdreg.domain.SaltForm;

privileged aspect IsoSalt_Roo_JavaBean {
    
    public Isotope IsoSalt.getIsotope() {
        return this.isotope;
    }
    
    public void IsoSalt.setIsotope(Isotope isotope) {
        this.isotope = isotope;
    }
    
    public Salt IsoSalt.getSalt() {
        return this.salt;
    }
    
    public void IsoSalt.setSalt(Salt salt) {
        this.salt = salt;
    }
    
    public String IsoSalt.getType() {
        return this.type;
    }
    
    public void IsoSalt.setType(String type) {
        this.type = type;
    }
    
    public Double IsoSalt.getEquivalents() {
        return this.equivalents;
    }
    
    public void IsoSalt.setEquivalents(Double equivalents) {
        this.equivalents = equivalents;
    }
    
    public Boolean IsoSalt.getIgnore() {
        return this.ignore;
    }
    
    public void IsoSalt.setIgnore(Boolean ignore) {
        this.ignore = ignore;
    }
    
    public SaltForm IsoSalt.getSaltForm() {
        return this.saltForm;
    }
    
    public void IsoSalt.setSaltForm(SaltForm saltForm) {
        this.saltForm = saltForm;
    }
    
}
