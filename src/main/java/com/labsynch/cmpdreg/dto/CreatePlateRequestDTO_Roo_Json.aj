// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.dto;

import com.labsynch.cmpdreg.dto.CreatePlateRequestDTO;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect CreatePlateRequestDTO_Roo_Json {
    
    public static CreatePlateRequestDTO CreatePlateRequestDTO.fromJsonToCreatePlateRequestDTO(String json) {
        return new JSONDeserializer<CreatePlateRequestDTO>()
        .use(null, CreatePlateRequestDTO.class).deserialize(json);
    }
    
    public static String CreatePlateRequestDTO.toJsonArray(Collection<CreatePlateRequestDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String CreatePlateRequestDTO.toJsonArray(Collection<CreatePlateRequestDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<CreatePlateRequestDTO> CreatePlateRequestDTO.fromJsonArrayToCreatePlateRequestDTO(String json) {
        return new JSONDeserializer<List<CreatePlateRequestDTO>>()
        .use("values", CreatePlateRequestDTO.class).deserialize(json);
    }
    
}
