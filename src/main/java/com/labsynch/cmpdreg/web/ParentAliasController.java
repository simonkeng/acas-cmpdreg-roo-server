package com.labsynch.cmpdreg.web;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gvnix.addon.datatables.GvNIXDatatables;
import org.gvnix.addon.web.mvc.addon.jquery.GvNIXWebJQuery;
import org.gvnix.web.datatables.query.SearchResults;
import org.gvnix.web.datatables.util.DatatablesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;
import com.github.dandelion.datatables.extras.spring3.ajax.DatatablesParams;
import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.domain.ParentAlias;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.path.PathBuilder;

@RooWebScaffold(path = "parentaliases", formBackingObject = ParentAlias.class)
@RequestMapping("/parentaliases/**")
@Controller
@Transactional
@GvNIXWebJQuery
@GvNIXDatatables(ajax = true)
@RooWebFinder
public class ParentAliasController {
	
    @Autowired
    public ConversionService conversionService_dtt;
    
    @RequestMapping(method = RequestMethod.POST, value = "{id}")
    public void post(@PathVariable Long id, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
    }

    @RequestMapping
    public String index() {
        return "parentalias/index";
    }
    
    @RequestMapping(headers = "Accept=application/json", value = "/datatables/ajax", params = "ajax_find=ByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals", produces = "application/json")
    @ResponseBody
    public DatatablesResponse<Map<String, String>> findParentAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals(@DatatablesParams DatatablesCriterias criterias, @RequestParam("aliasName") String aliasName, @RequestParam("lsType") String lsType, @RequestParam("lsKind") String lsKind) {
        BooleanBuilder baseSearch = new BooleanBuilder();
        
        // Base Search. Using BooleanBuilder, a cascading builder for
        // Predicate expressions
        PathBuilder<ParentAlias> entity = new PathBuilder<ParentAlias>(ParentAlias.class, "entity");
        
        if(aliasName != null){
            baseSearch.and(entity.getString("aliasName").equalsIgnoreCase(aliasName));
        }else{
            baseSearch.and(entity.getString("aliasName").isNull());
        }
        if(lsType != null){
            baseSearch.and(entity.getString("lsType").equalsIgnoreCase(lsType));
        }else{
            baseSearch.and(entity.getString("lsType").isNull());
        }
        if(lsKind != null){
            baseSearch.and(entity.getString("lsKind").eq(lsKind));
        }else{
            baseSearch.and(entity.getString("lsKind").isNull());
        }
        
        SearchResults<ParentAlias> searchResult = DatatablesUtils.findByCriteria(entity, ParentAlias.entityManager(), criterias, baseSearch);
        
        // Get datatables required counts
        long totalRecords = searchResult.getTotalCount();
        long recordsFound = searchResult.getResultsCount();
        
        // Entity pk field name
        String pkFieldName = "id";
        
        DataSet<Map<String, String>> dataSet = DatatablesUtils.populateDataSet(searchResult.getResults(), pkFieldName, totalRecords, recordsFound, criterias.getColumnDefs(), null, conversionService_dtt); 
        return DatatablesResponse.build(dataSet,criterias);
    }

    
    @RequestMapping(headers = "Accept=application/json", value = "/datatables/ajax", params = "ajax_find=ByParentAndLsTypeEqualsAndLsKindEquals", produces = "application/json")
    @ResponseBody
    public DatatablesResponse<Map<String, String>> findParentAliasesByParentAndLsTypeEqualsAndLsKindEquals(@DatatablesParams DatatablesCriterias criterias, @RequestParam("parent") Parent parent, @RequestParam("lsType") String lsType, @RequestParam("lsKind") String lsKind) {
        BooleanBuilder baseSearch = new BooleanBuilder();
        
        // Base Search. Using BooleanBuilder, a cascading builder for
        // Predicate expressions
        PathBuilder<ParentAlias> entity = new PathBuilder<ParentAlias>(ParentAlias.class, "entity");
        
        if(parent != null){
            baseSearch.and(entity.get("parent").eq(parent));
        }else{
            baseSearch.and(entity.get("parent").isNull());
        }
        if(lsType != null){
            baseSearch.and(entity.getString("lsType").eq(lsType));
        }else{
            baseSearch.and(entity.getString("lsType").isNull());
        }
        if(lsKind != null){
            baseSearch.and(entity.getString("lsKind").eq(lsKind));
        }else{
            baseSearch.and(entity.getString("lsKind").isNull());
        }
        
        SearchResults<ParentAlias> searchResult = DatatablesUtils.findByCriteria(entity, ParentAlias.entityManager(), criterias, baseSearch);
        
        // Get datatables required counts
        long totalRecords = searchResult.getTotalCount();
        long recordsFound = searchResult.getResultsCount();
        
        // Entity pk field name
        String pkFieldName = "id";
        
        DataSet<Map<String, String>> dataSet = DatatablesUtils.populateDataSet(searchResult.getResults(), pkFieldName, totalRecords, recordsFound, criterias.getColumnDefs(), null, conversionService_dtt); 
        return DatatablesResponse.build(dataSet,criterias);
    }
    
    
    @RequestMapping(headers = "Accept=application/json", value = "/datatables/ajax", params = "ajax_find=ByParentAndLsTypeEqualsAndLsKindEqualsAndAliasNameEquals", produces = "application/json")
    @ResponseBody
    public DatatablesResponse<Map<String, String>> findParentAliasesByParentAndLsTypeEqualsAndLsKindEqualsAndAliasNameEquals(@DatatablesParams DatatablesCriterias criterias, @RequestParam("parent") Parent parent, @RequestParam("lsType") String lsType, @RequestParam("lsKind") String lsKind, @RequestParam("aliasName") String aliasName) {
        BooleanBuilder baseSearch = new BooleanBuilder();
        
        // Base Search. Using BooleanBuilder, a cascading builder for
        // Predicate expressions
        PathBuilder<ParentAlias> entity = new PathBuilder<ParentAlias>(ParentAlias.class, "entity");
        
        if(parent != null){
            baseSearch.and(entity.get("parent").eq(parent));
        }else{
            baseSearch.and(entity.get("parent").isNull());
        }
        if(lsType != null){
            baseSearch.and(entity.getString("lsType").eq(lsType));
        }else{
            baseSearch.and(entity.getString("lsType").isNull());
        }
        if(lsKind != null){
            baseSearch.and(entity.getString("lsKind").eq(lsKind));
        }else{
            baseSearch.and(entity.getString("lsKind").isNull());
        }
        if(aliasName != null){
            baseSearch.and(entity.getString("aliasName").eq(aliasName));
        }else{
            baseSearch.and(entity.getString("aliasName").isNull());
        }
        
        SearchResults<ParentAlias> searchResult = DatatablesUtils.findByCriteria(entity, ParentAlias.entityManager(), criterias, baseSearch);
        
        // Get datatables required counts
        long totalRecords = searchResult.getTotalCount();
        long recordsFound = searchResult.getResultsCount();
        
        // Entity pk field name
        String pkFieldName = "id";
        
        DataSet<Map<String, String>> dataSet = DatatablesUtils.populateDataSet(searchResult.getResults(), pkFieldName, totalRecords, recordsFound, criterias.getColumnDefs(), null, conversionService_dtt); 
        return DatatablesResponse.build(dataSet,criterias);
    }

   
}
