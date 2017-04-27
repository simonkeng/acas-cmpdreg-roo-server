// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.web;

import com.labsynch.cmpdreg.domain.Operator;
import com.labsynch.cmpdreg.web.OperatorController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

privileged aspect OperatorController_Roo_Controller_Finder {
    
    @RequestMapping(params = { "find=ByCodeEquals", "form" }, method = RequestMethod.GET)
    public String OperatorController.findOperatorsByCodeEqualsForm(Model uiModel) {
        return "operators/findOperatorsByCodeEquals";
    }
    
    @RequestMapping(params = "find=ByCodeEquals", method = RequestMethod.GET)
    public String OperatorController.findOperatorsByCodeEquals(@RequestParam("code") String code, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("operators", Operator.findOperatorsByCodeEquals(code, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) Operator.countFindOperatorsByCodeEquals(code) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("operators", Operator.findOperatorsByCodeEquals(code, sortFieldName, sortOrder).getResultList());
        }
        return "operators/list";
    }
    
    @RequestMapping(params = { "find=ByNameEquals", "form" }, method = RequestMethod.GET)
    public String OperatorController.findOperatorsByNameEqualsForm(Model uiModel) {
        return "operators/findOperatorsByNameEquals";
    }
    
    @RequestMapping(params = "find=ByNameEquals", method = RequestMethod.GET)
    public String OperatorController.findOperatorsByNameEquals(@RequestParam("name") String name, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("operators", Operator.findOperatorsByNameEquals(name, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) Operator.countFindOperatorsByNameEquals(name) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("operators", Operator.findOperatorsByNameEquals(name, sortFieldName, sortOrder).getResultList());
        }
        return "operators/list";
    }
    
}
