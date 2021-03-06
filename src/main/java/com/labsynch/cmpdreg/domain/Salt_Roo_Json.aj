// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.domain;

import com.labsynch.cmpdreg.domain.Salt;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect Salt_Roo_Json {
    
    public String Salt.toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String Salt.toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static Salt Salt.fromJsonToSalt(String json) {
        return new JSONDeserializer<Salt>()
        .use(null, Salt.class).deserialize(json);
    }
    
    public static String Salt.toJsonArray(Collection<Salt> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String Salt.toJsonArray(Collection<Salt> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<Salt> Salt.fromJsonArrayToSalts(String json) {
        return new JSONDeserializer<List<Salt>>()
        .use("values", Salt.class).deserialize(json);
    }
    
}
