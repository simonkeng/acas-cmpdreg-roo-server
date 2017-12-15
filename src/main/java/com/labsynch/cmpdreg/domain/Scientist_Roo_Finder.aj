// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.domain;

import com.labsynch.cmpdreg.domain.Scientist;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect Scientist_Roo_Finder {
    
    public static Long Scientist.countFindScientistsByCodeEquals(String code) {
        if (code == null || code.length() == 0) throw new IllegalArgumentException("The code argument is required");
        EntityManager em = Scientist.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Scientist AS o WHERE o.code = :code", Long.class);
        q.setParameter("code", code);
        return ((Long) q.getSingleResult());
    }
    
    public static Long Scientist.countFindScientistsByCodeLike(String code) {
        if (code == null || code.length() == 0) throw new IllegalArgumentException("The code argument is required");
        code = code.replace('*', '%');
        if (code.charAt(0) != '%') {
            code = "%" + code;
        }
        if (code.charAt(code.length() - 1) != '%') {
            code = code + "%";
        }
        EntityManager em = Scientist.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Scientist AS o WHERE LOWER(o.code) LIKE LOWER(:code)", Long.class);
        q.setParameter("code", code);
        return ((Long) q.getSingleResult());
    }
    
    public static TypedQuery<Scientist> Scientist.findScientistsByCodeEquals(String code) {
        if (code == null || code.length() == 0) throw new IllegalArgumentException("The code argument is required");
        EntityManager em = Scientist.entityManager();
        TypedQuery<Scientist> q = em.createQuery("SELECT o FROM Scientist AS o WHERE o.code = :code", Scientist.class);
        q.setParameter("code", code);
        return q;
    }
    
    public static TypedQuery<Scientist> Scientist.findScientistsByCodeEquals(String code, String sortFieldName, String sortOrder) {
        if (code == null || code.length() == 0) throw new IllegalArgumentException("The code argument is required");
        EntityManager em = Scientist.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Scientist AS o WHERE o.code = :code");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Scientist> q = em.createQuery(queryBuilder.toString(), Scientist.class);
        q.setParameter("code", code);
        return q;
    }
    
    public static TypedQuery<Scientist> Scientist.findScientistsByCodeLike(String code) {
        if (code == null || code.length() == 0) throw new IllegalArgumentException("The code argument is required");
        code = code.replace('*', '%');
        if (code.charAt(0) != '%') {
            code = "%" + code;
        }
        if (code.charAt(code.length() - 1) != '%') {
            code = code + "%";
        }
        EntityManager em = Scientist.entityManager();
        TypedQuery<Scientist> q = em.createQuery("SELECT o FROM Scientist AS o WHERE LOWER(o.code) LIKE LOWER(:code)", Scientist.class);
        q.setParameter("code", code);
        return q;
    }
    
    public static TypedQuery<Scientist> Scientist.findScientistsByCodeLike(String code, String sortFieldName, String sortOrder) {
        if (code == null || code.length() == 0) throw new IllegalArgumentException("The code argument is required");
        code = code.replace('*', '%');
        if (code.charAt(0) != '%') {
            code = "%" + code;
        }
        if (code.charAt(code.length() - 1) != '%') {
            code = code + "%";
        }
        EntityManager em = Scientist.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Scientist AS o WHERE LOWER(o.code) LIKE LOWER(:code)");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Scientist> q = em.createQuery(queryBuilder.toString(), Scientist.class);
        q.setParameter("code", code);
        return q;
    }
    
}
