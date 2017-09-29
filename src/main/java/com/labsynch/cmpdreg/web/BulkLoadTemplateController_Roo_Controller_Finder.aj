// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.web;

import com.labsynch.cmpdreg.domain.BulkLoadTemplate;
import com.labsynch.cmpdreg.web.BulkLoadTemplateController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

privileged aspect BulkLoadTemplateController_Roo_Controller_Finder {
    
    @RequestMapping(params = { "find=ByRecordedByEquals", "form" }, method = RequestMethod.GET)
    public String BulkLoadTemplateController.findBulkLoadTemplatesByRecordedByEqualsForm(Model uiModel) {
        return "bulkloadtemplates/findBulkLoadTemplatesByRecordedByEquals";
    }
    
    @RequestMapping(params = "find=ByRecordedByEquals", method = RequestMethod.GET)
    public String BulkLoadTemplateController.findBulkLoadTemplatesByRecordedByEquals(@RequestParam("recordedBy") String recordedBy, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("bulkloadtemplates", BulkLoadTemplate.findBulkLoadTemplatesByRecordedByEquals(recordedBy, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) BulkLoadTemplate.countFindBulkLoadTemplatesByRecordedByEquals(recordedBy) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("bulkloadtemplates", BulkLoadTemplate.findBulkLoadTemplatesByRecordedByEquals(recordedBy, sortFieldName, sortOrder).getResultList());
        }
        return "bulkloadtemplates/list";
    }
    
    @RequestMapping(params = { "find=ByTemplateNameEquals", "form" }, method = RequestMethod.GET)
    public String BulkLoadTemplateController.findBulkLoadTemplatesByTemplateNameEqualsForm(Model uiModel) {
        return "bulkloadtemplates/findBulkLoadTemplatesByTemplateNameEquals";
    }
    
    @RequestMapping(params = "find=ByTemplateNameEquals", method = RequestMethod.GET)
    public String BulkLoadTemplateController.findBulkLoadTemplatesByTemplateNameEquals(@RequestParam("templateName") String templateName, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("bulkloadtemplates", BulkLoadTemplate.findBulkLoadTemplatesByTemplateNameEquals(templateName, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) BulkLoadTemplate.countFindBulkLoadTemplatesByTemplateNameEquals(templateName) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("bulkloadtemplates", BulkLoadTemplate.findBulkLoadTemplatesByTemplateNameEquals(templateName, sortFieldName, sortOrder).getResultList());
        }
        return "bulkloadtemplates/list";
    }
    
    @RequestMapping(params = { "find=ByTemplateNameEqualsAndRecordedByEquals", "form" }, method = RequestMethod.GET)
    public String BulkLoadTemplateController.findBulkLoadTemplatesByTemplateNameEqualsAndRecordedByEqualsForm(Model uiModel) {
        return "bulkloadtemplates/findBulkLoadTemplatesByTemplateNameEqualsAndRecordedByEquals";
    }
    
    @RequestMapping(params = "find=ByTemplateNameEqualsAndRecordedByEquals", method = RequestMethod.GET)
    public String BulkLoadTemplateController.findBulkLoadTemplatesByTemplateNameEqualsAndRecordedByEquals(@RequestParam("templateName") String templateName, @RequestParam("recordedBy") String recordedBy, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("bulkloadtemplates", BulkLoadTemplate.findBulkLoadTemplatesByTemplateNameEqualsAndRecordedByEquals(templateName, recordedBy, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) BulkLoadTemplate.countFindBulkLoadTemplatesByTemplateNameEqualsAndRecordedByEquals(templateName, recordedBy) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("bulkloadtemplates", BulkLoadTemplate.findBulkLoadTemplatesByTemplateNameEqualsAndRecordedByEquals(templateName, recordedBy, sortFieldName, sortOrder).getResultList());
        }
        return "bulkloadtemplates/list";
    }
    
}
