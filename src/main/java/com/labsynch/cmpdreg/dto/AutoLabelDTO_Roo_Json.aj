// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.dto;

import com.labsynch.cmpdreg.dto.AutoLabelDTO;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect AutoLabelDTO_Roo_Json {
    
    public String AutoLabelDTO.toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String AutoLabelDTO.toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static AutoLabelDTO AutoLabelDTO.fromJsonToAutoLabelDTO(String json) {
        return new JSONDeserializer<AutoLabelDTO>()
        .use(null, AutoLabelDTO.class).deserialize(json);
    }
    
    public static String AutoLabelDTO.toJsonArray(Collection<AutoLabelDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String AutoLabelDTO.toJsonArray(Collection<AutoLabelDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<AutoLabelDTO> AutoLabelDTO.fromJsonArrayToAutoes(String json) {
        return new JSONDeserializer<List<AutoLabelDTO>>()
        .use("values", AutoLabelDTO.class).deserialize(json);
    }
    
}