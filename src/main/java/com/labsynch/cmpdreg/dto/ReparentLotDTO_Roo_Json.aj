// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.dto;

import com.labsynch.cmpdreg.dto.ReparentLotDTO;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect ReparentLotDTO_Roo_Json {
    
    public String ReparentLotDTO.toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String ReparentLotDTO.toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static ReparentLotDTO ReparentLotDTO.fromJsonToReparentLotDTO(String json) {
        return new JSONDeserializer<ReparentLotDTO>()
        .use(null, ReparentLotDTO.class).deserialize(json);
    }
    
    public static String ReparentLotDTO.toJsonArray(Collection<ReparentLotDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String ReparentLotDTO.toJsonArray(Collection<ReparentLotDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<ReparentLotDTO> ReparentLotDTO.fromJsonArrayToReparentLoes(String json) {
        return new JSONDeserializer<List<ReparentLotDTO>>()
        .use("values", ReparentLotDTO.class).deserialize(json);
    }
    
}
