// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.domain;

import com.labsynch.cmpdreg.domain.CmpdRegAppSetting;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

privileged aspect CmpdRegAppSetting_Roo_Jpa_Entity {
    
    declare @type: CmpdRegAppSetting: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long CmpdRegAppSetting.id;
    
    @Version
    @Column(name = "version")
    private Integer CmpdRegAppSetting.version;
    
    public Long CmpdRegAppSetting.getId() {
        return this.id;
    }
    
    public void CmpdRegAppSetting.setId(Long id) {
        this.id = id;
    }
    
    public Integer CmpdRegAppSetting.getVersion() {
        return this.version;
    }
    
    public void CmpdRegAppSetting.setVersion(Integer version) {
        this.version = version;
    }
    
}
