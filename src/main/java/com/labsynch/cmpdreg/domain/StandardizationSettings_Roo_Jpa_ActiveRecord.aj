// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.domain;

import com.labsynch.cmpdreg.domain.StandardizationSettings;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect StandardizationSettings_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager StandardizationSettings.entityManager;
    
    public static final List<String> StandardizationSettings.fieldNames4OrderClauseFilter = java.util.Arrays.asList("currentSettings", "modifiedDate", "needsStandardization", "currentSettingsHash");
    
    public static final EntityManager StandardizationSettings.entityManager() {
        EntityManager em = new StandardizationSettings().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long StandardizationSettings.countStandardizationSettingses() {
        return entityManager().createQuery("SELECT COUNT(o) FROM StandardizationSettings o", Long.class).getSingleResult();
    }
    
    public static List<StandardizationSettings> StandardizationSettings.findAllStandardizationSettingses() {
        return entityManager().createQuery("SELECT o FROM StandardizationSettings o", StandardizationSettings.class).getResultList();
    }
    
    public static List<StandardizationSettings> StandardizationSettings.findAllStandardizationSettingses(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM StandardizationSettings o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, StandardizationSettings.class).getResultList();
    }
    
    public static StandardizationSettings StandardizationSettings.findStandardizationSettings(Long id) {
        if (id == null) return null;
        return entityManager().find(StandardizationSettings.class, id);
    }
    
    public static List<StandardizationSettings> StandardizationSettings.findStandardizationSettingsEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM StandardizationSettings o", StandardizationSettings.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<StandardizationSettings> StandardizationSettings.findStandardizationSettingsEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM StandardizationSettings o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, StandardizationSettings.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void StandardizationSettings.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void StandardizationSettings.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            StandardizationSettings attached = StandardizationSettings.findStandardizationSettings(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void StandardizationSettings.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void StandardizationSettings.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public StandardizationSettings StandardizationSettings.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        StandardizationSettings merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}