// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.domain;

import com.labsynch.cmpdreg.domain.ParentAliasKind;
import com.labsynch.cmpdreg.domain.ParentAliasType;

privileged aspect ParentAliasKind_Roo_JavaBean {
    
    public ParentAliasType ParentAliasKind.getLsType() {
        return this.lsType;
    }
    
    public void ParentAliasKind.setLsType(ParentAliasType lsType) {
        this.lsType = lsType;
    }
    
    public String ParentAliasKind.getKindName() {
        return this.kindName;
    }
    
    public void ParentAliasKind.setKindName(String kindName) {
        this.kindName = kindName;
    }
    
}
