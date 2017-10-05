// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.web;

import com.labsynch.cmpdreg.domain.IsoSalt;
import com.labsynch.cmpdreg.web.IsoSaltController;
import com.labsynch.cmpdreg.web.IsoSaltController_Roo_Controller;
import com.labsynch.cmpdreg.web.IsoSaltController_Roo_GvNIXDatatables;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.path.PathBuilder;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.commons.collections.CollectionUtils;
import org.gvnix.web.datatables.util.DatatablesUtils;
import org.gvnix.web.datatables.util.QuerydslUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.convert.ConversionService;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

privileged aspect IsoSaltController_Roo_GvNIXDatatables {
    
    declare precedence: IsoSaltController_Roo_GvNIXDatatables, IsoSaltController_Roo_Controller;
    
    @Autowired
    public ConversionService IsoSaltController.conversionService_dtt;
    
    @Autowired
    public MessageSource IsoSaltController.messageSource_dtt;
    
    public BeanWrapper IsoSaltController.beanWrapper;
    
    @RequestMapping(method = RequestMethod.GET, produces = "text/html")
    public String IsoSaltController.listDatatables(Model uiModel, HttpServletRequest request, @ModelAttribute IsoSalt IsoSalt) {
        // Get parentId parameter for details
        if (request.getParameterMap().containsKey("_dt_parentId")){
            uiModel.addAttribute("parentId",request.getParameter("_dt_parentId"));
        }
        // Get data (filtered by received parameters) and put it on pageContext
        @SuppressWarnings("unchecked") List<IsoSalt> isoSalts = findIsoSaltsByParameters(IsoSalt, request != null ? request.getParameterNames() : null);
        uiModel.addAttribute("isosalts",isoSalts);
        return "isosalts/list";
    }
    
    @ModelAttribute
    public void IsoSaltController.populateDatatablesConfig(Model uiModel) {
        uiModel.addAttribute("datatablesHasBatchSupport", false);
        uiModel.addAttribute("datatablesUseAjax",false);
        uiModel.addAttribute("datatablesInlineEditing",false);
        uiModel.addAttribute("datatablesInlineCreating",false);
        uiModel.addAttribute("datatablesSecurityApplied",true);
        uiModel.addAttribute("datatablesStandardMode",true);
    }
    
    @RequestMapping(produces = "text/html")
    public String IsoSaltController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        // overrides the standard Roo list method and
        // delegates on datatables list method
        return listDatatables(uiModel, null, null);
    }
    
    public Map<String, String> IsoSaltController.populateParametersMap(HttpServletRequest request) {
        Map<String, Object> params;
        if (request == null) {
            params = Collections.emptyMap();
        } else {
            params = new HashMap<String, Object>(request.getParameterMap());
        }
        
        Map<String, String> allParams = new HashMap<String, String>(params.size());
        
        String value;
        Object objValue;
        for (String key : params.keySet()) {
            objValue = params.get(key);
            if (objValue instanceof String[]) {
                value = ((String[]) objValue)[0];
            } else {
                value = (String) objValue;
            }
            allParams.put(key, value);
        }
        return allParams;
    }
    
    public Map<String, Object> IsoSaltController.getPropertyMap(IsoSalt IsoSalt, Enumeration<Map<String, String>> propertyNames) {
        Map<String, Object> propertyValuesMap = new HashMap<String, Object>();
        
        // If no entity or properties given, return empty Map
        if(IsoSalt == null || propertyNames == null) {
            return propertyValuesMap;
        }
        
        List<String> properties = new ArrayList<String>();
        CollectionUtils.addAll(properties, propertyNames);
        
        // There must be at least one property name, otherwise return empty Map
        if (properties.isEmpty()) {
            return propertyValuesMap;
        }
        
        // Iterate over given properties to get each property value
        BeanWrapper entityBean = new BeanWrapperImpl(IsoSalt);
        for (String propertyName : properties) {
            if (entityBean.isReadableProperty(propertyName)) {
                Object propertyValue = null;
                try {
                    propertyValue = entityBean.getPropertyValue(propertyName);
                } catch (Exception e){
                    // TODO log warning
                    continue;
                }
                propertyValuesMap.put(propertyName, propertyValue);
            }
        }
        return propertyValuesMap;
    }
    
    public Map<String, Object> IsoSaltController.getPropertyMap(IsoSalt IsoSalt, HttpServletRequest request) {
        // URL parameters are used as base search filters
        @SuppressWarnings("unchecked") Map<String, Object> propertyValuesMap = getPropertyMap(IsoSalt, request.getParameterNames());
        // Add to the property map the parameters used as query operators
        Map<String, Object> params = new HashMap<String, Object>(populateParametersMap(request));
        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            if (key.startsWith(QuerydslUtils.OPERATOR_PREFIX)) {
                propertyValuesMap.put(key, params.get(key));
            } else if (DatatablesUtils.ROWS_ON_TOP_IDS_PARAM.equals(key)) {
                propertyValuesMap.put(key, request.getParameterMap().get(key));
            } else if(DatatablesUtils.BOUNDING_BOX_PARAM.equals(key) || DatatablesUtils.BOUNDING_BOX_FIELDS_PARAM.equals(key)){
                propertyValuesMap.put(key, request.getParameterMap().get(key));
            }
        }
        return propertyValuesMap;
    }
    
    public void IsoSaltController.setDatatablesBaseFilter(Map<String, Object> propertyMap) {
        // Add here your baseFilters to propertyMap.
    }
    
    @ResponseBody
    @RequestMapping(headers = "Accept=application/json", params = "getColumnType")
    public String IsoSaltController.getColumnType(Model uiModel, HttpServletRequest request, @RequestParam(value = "_columnName_", required = false) String columnName) {
        // Getting all declared fields
        boolean fieldExists = false;
        Field attr = null;
        for(Field field : IsoSalt.class.getDeclaredFields()){
            if(field.getName().equals(columnName)){
                attr = field;
                fieldExists = true;
                break;
            }
        }
        // If current field not exists on entity, find on superclass
        if(!fieldExists){
            if(IsoSalt.class.getSuperclass() != null){
                for(Field field : IsoSalt.class.getSuperclass().getDeclaredFields()){
                    if(field.getName().equals(columnName)){
                        attr = field;
                        fieldExists = true;
                        break;
                    }
                }
            }
        }
        if(fieldExists){
            // Getting field type
            Object fieldType = null;
            if (attr != null) {
                fieldType = attr.getType();
                String type = fieldType.toString();
                // Returning value based on type
                if ("String".equals(type)){
                    return "{\"columnType\": \"string\"}";
                } else if ("float".equals(type) || type.contains("Float")){
                    return "{\"columnType\": \"number\"}";
                } else if ("short".equals(type) || type.contains("Short")){
                    return "{\"columnType\": \"number\"}";
                } else if ("long".equals(type) || type.contains("Long")){
                    return "{\"columnType\": \"number\"}";
                } else if ("double".equals(type) || type.contains("Double")){
                    return "{\"columnType\": \"number\"}";
                } else if ("int".equals(type) || type.contains("Integer")){
                    return "{\"columnType\": \"number\"}";
                } else if ("Date".equals(type)){
                    return "{\"columnType\": \"date\"}";
                } else if ("boolean".equals(type) || type.contains("Boolean")){
                    return "{\"columnType\": \"boolean\"}";
                } else {
                    // Returning by default
                    return "{\"columnType\": \"undefined\"}";
                }
            }
        }
        // Returning by default
        return "{\"columnType\": \"undefined\"}";
    }
    
    @ResponseBody
    @RequestMapping(headers = "Accept=application/json", params = "geti18nText")
    public String IsoSaltController.geti18nText(Model uiModel, HttpServletRequest request, @RequestParam(value = "_locale_", required = false) String locale) {
        // Getting current locale
        Locale defaultLocale = new Locale(locale);
        // Building JSON response
        StringBuilder json = new StringBuilder();
        json.append("\"all_isnull\": \"" + messageSource_dtt.getMessage("global.filters.operations.all.isnull", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"all_notnull\": \"" + messageSource_dtt.getMessage("global.filters.operations.all.notnull", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"boolean_false\": \"" + messageSource_dtt.getMessage("global.filters.operations.boolean.false", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"boolean_true\": \"" + messageSource_dtt.getMessage("global.filters.operations.boolean.true", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"date_between\": \"" + messageSource_dtt.getMessage("global.filters.operations.date.between", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"date_date\": \"" + messageSource_dtt.getMessage("global.filters.operations.date.date", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"date_day\": \"" + messageSource_dtt.getMessage("global.filters.operations.date.day", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"date_month\": \"" + messageSource_dtt.getMessage("global.filters.operations.date.month", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"date_year\": \"" + messageSource_dtt.getMessage("global.filters.operations.date.year", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"number_between\": \"" + messageSource_dtt.getMessage("global.filters.operations.number.between", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"string_contains\": \"" + messageSource_dtt.getMessage("global.filters.operations.string.contains", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"string_ends\": \"" + messageSource_dtt.getMessage("global.filters.operations.string.ends", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"string_isempty\": \"" + messageSource_dtt.getMessage("global.filters.operations.string.isempty", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"string_isnotempty\": \"" + messageSource_dtt.getMessage("global.filters.operations.string.isnotempty", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"string_starts\": \"" + messageSource_dtt.getMessage("global.filters.operations.string.starts", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"button_find\": \"" + messageSource_dtt.getMessage("button_find", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_all_isnull\": \"" + messageSource_dtt.getMessage("help.all.isnull", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_all_notnull\": \"" + messageSource_dtt.getMessage("help.all.notnull", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_boolean_false\": \"" + messageSource_dtt.getMessage("help.boolean.false", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_boolean_true\": \"" + messageSource_dtt.getMessage("help.boolean.true", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_date_between\": \"" + messageSource_dtt.getMessage("help.date.between", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_date_date\": \"" + messageSource_dtt.getMessage("help.date.date", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_date_day\": \"" + messageSource_dtt.getMessage("help.date.day", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_date_month\": \"" + messageSource_dtt.getMessage("help.date.month", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_date_year\": \"" + messageSource_dtt.getMessage("help.date.year", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_number_between\": \"" + messageSource_dtt.getMessage("help.number.between", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_number_eq\": \"" + messageSource_dtt.getMessage("help.number.eq", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_number_neq\": \"" + messageSource_dtt.getMessage("help.number.neq", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_number_gt\": \"" + messageSource_dtt.getMessage("help.number.gt", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_number_lt\": \"" + messageSource_dtt.getMessage("help.number.lt", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_number_goe\": \"" + messageSource_dtt.getMessage("help.number.goe", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_number_loe\": \"" + messageSource_dtt.getMessage("help.number.loe", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_string_contains\": \"" + messageSource_dtt.getMessage("help.string.contains", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_string_ends\": \"" + messageSource_dtt.getMessage("help.string.ends", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_string_isempty\": \"" + messageSource_dtt.getMessage("help.string.isempty", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_string_isnotempty\": \"" + messageSource_dtt.getMessage("help.string.isnotempty", null, defaultLocale) + "\"");
        json.append(",");
        json.append("\"help_string_starts\": \"" + messageSource_dtt.getMessage("help.string.starts", null, defaultLocale) + "\"");
        json.append("}");
        // return JSON with locale strings
        return json.toString();
    }
    
    @RequestMapping(produces = "text/html", method = RequestMethod.POST, params = "datatablesRedirect")
    public String IsoSaltController.createDatatablesDetail(@RequestParam(value = "datatablesRedirect", required = true) String redirect, @Valid IsoSalt isosalt, BindingResult bindingResult, Model uiModel, RedirectAttributes redirectModel, HttpServletRequest httpServletRequest) {
        // Do common create operations (check errors, populate, persist, ...)
        String view = create(isosalt, bindingResult, uiModel, httpServletRequest);
        // If binding errors or no redirect, return common create error view (remain in create form)
        if (bindingResult.hasErrors() || redirect == null || redirect.trim().isEmpty()) {
            return view;
        }
        String[] paramValues = httpServletRequest.getParameterValues("dtt_table_id_hash");
        if(paramValues != null && paramValues.length > 0) {
            redirectModel.addFlashAttribute("dtt_table_id_hash", paramValues[0]);
        }else{
            redirectModel.addFlashAttribute("dtt_table_id_hash", "");
        }
        redirectModel.addFlashAttribute(DatatablesUtils.ROWS_ON_TOP_IDS_PARAM, isosalt.getId());
        // If create success, redirect to given URL: master datatables
        return "redirect:".concat(redirect);
    }
    
    @RequestMapping(produces = "text/html", method = RequestMethod.PUT, params = "datatablesRedirect")
    public String IsoSaltController.updateDatatablesDetail(@RequestParam(value = "datatablesRedirect", required = true) String redirect, @Valid IsoSalt isosalt, BindingResult bindingResult, Model uiModel, RedirectAttributes redirectModel, HttpServletRequest httpServletRequest) {
        // Do common update operations (check errors, populate, merge, ...)
        String view = update(isosalt, bindingResult, uiModel, httpServletRequest);
        // If binding errors or no redirect, return common update error view (remain in update form)
        if (bindingResult.hasErrors() || redirect == null || redirect.trim().isEmpty()) {
            return view;
        }
        String[] paramValues = httpServletRequest.getParameterValues("dtt_table_id_hash");
        if(paramValues != null && paramValues.length > 0) {
            redirectModel.addFlashAttribute("dtt_table_id_hash", paramValues[0]);
        }else{
            redirectModel.addFlashAttribute("dtt_table_id_hash", "");
        }
        redirectModel.addFlashAttribute(DatatablesUtils.ROWS_ON_TOP_IDS_PARAM, isosalt.getId());
        // If update success, redirect to given URL: master datatables
        return "redirect:".concat(redirect);
    }
    
    @RequestMapping(produces = "text/html", method = RequestMethod.DELETE, params = "datatablesRedirect", value = "/{id}")
    public String IsoSaltController.deleteDatatablesDetail(@RequestParam(value = "datatablesRedirect", required = true) String redirect, @PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        // Do common delete operations (find, remove, add pagination attributes, ...)
        String view = delete(id, page, size, uiModel);
        // If no redirect, return common list view
        if (redirect == null || redirect.trim().isEmpty()) {
            return view;
        }
        // Redirect to given URL: master datatables
        return "redirect:".concat(redirect);
    }
    
    public List<IsoSalt> IsoSaltController.findIsoSaltsByParameters(IsoSalt IsoSalt, Enumeration<Map<String, String>> propertyNames) {
        // Gets propertyMap
        Map<String, Object> propertyMap = getPropertyMap(IsoSalt, propertyNames);
        
        // if there is a filter
        if (!propertyMap.isEmpty()) {
            // Prepare a predicate
            BooleanBuilder baseFilterPredicate = new BooleanBuilder();
            
            // Base filter. Using BooleanBuilder, a cascading builder for
            // Predicate expressions
            PathBuilder<IsoSalt> entity = new PathBuilder<IsoSalt>(IsoSalt.class, "entity");
            
            // Build base filter
            for (String key : propertyMap.keySet()) {
                baseFilterPredicate.and(entity.get(key).eq(propertyMap.get(key)));
            }
            
            // Create a query with filter
            JPAQuery query = new JPAQuery(IsoSalt.entityManager());
            query = query.from(entity);
            
            // execute query
            return query.where(baseFilterPredicate).list(entity);
        }
        
        // no filter: return all elements
        return IsoSalt.findAllIsoSalts();
    }
    
}