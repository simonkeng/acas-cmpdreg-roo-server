// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.dto.configuration;

import com.labsynch.cmpdreg.dto.configuration.MainConfigDTO;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect MainConfigDTO_Roo_Json {
    
    public String MainConfigDTO.toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String MainConfigDTO.toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static MainConfigDTO MainConfigDTO.fromJsonToMainConfigDTO(String json) {
        return new JSONDeserializer<MainConfigDTO>()
        .use(null, MainConfigDTO.class).deserialize(json);
    }
    
    public static String MainConfigDTO.toJsonArray(Collection<MainConfigDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String MainConfigDTO.toJsonArray(Collection<MainConfigDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<MainConfigDTO> MainConfigDTO.fromJsonArrayToMainCoes(String json) {
        return new JSONDeserializer<List<MainConfigDTO>>()
        .use("values", MainConfigDTO.class).deserialize(json);
    }
    
}
