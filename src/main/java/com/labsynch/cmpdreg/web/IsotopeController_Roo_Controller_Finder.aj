// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.web;

import com.labsynch.cmpdreg.domain.Isotope;
import com.labsynch.cmpdreg.web.IsotopeController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

privileged aspect IsotopeController_Roo_Controller_Finder {
    
    @RequestMapping(params = { "find=ByAbbrevEquals", "form" }, method = RequestMethod.GET)
    public String IsotopeController.findIsotopesByAbbrevEqualsForm(Model uiModel) {
        return "isotopes/findIsotopesByAbbrevEquals";
    }
    
    @RequestMapping(params = "find=ByAbbrevEquals", method = RequestMethod.GET)
    public String IsotopeController.findIsotopesByAbbrevEquals(@RequestParam("abbrev") String abbrev, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("isotopes", Isotope.findIsotopesByAbbrevEquals(abbrev, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) Isotope.countFindIsotopesByAbbrevEquals(abbrev) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("isotopes", Isotope.findIsotopesByAbbrevEquals(abbrev, sortFieldName, sortOrder).getResultList());
        }
        return "isotopes/list";
    }
    
    @RequestMapping(params = { "find=ByAbbrevEqualsAndNameEquals", "form" }, method = RequestMethod.GET)
    public String IsotopeController.findIsotopesByAbbrevEqualsAndNameEqualsForm(Model uiModel) {
        return "isotopes/findIsotopesByAbbrevEqualsAndNameEquals";
    }
    
    @RequestMapping(params = "find=ByAbbrevEqualsAndNameEquals", method = RequestMethod.GET)
    public String IsotopeController.findIsotopesByAbbrevEqualsAndNameEquals(@RequestParam("abbrev") String abbrev, @RequestParam("name") String name, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("isotopes", Isotope.findIsotopesByAbbrevEqualsAndNameEquals(abbrev, name, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) Isotope.countFindIsotopesByAbbrevEqualsAndNameEquals(abbrev, name) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("isotopes", Isotope.findIsotopesByAbbrevEqualsAndNameEquals(abbrev, name, sortFieldName, sortOrder).getResultList());
        }
        return "isotopes/list";
    }
    
    @RequestMapping(params = { "find=ByNameEquals", "form" }, method = RequestMethod.GET)
    public String IsotopeController.findIsotopesByNameEqualsForm(Model uiModel) {
        return "isotopes/findIsotopesByNameEquals";
    }
    
    @RequestMapping(params = "find=ByNameEquals", method = RequestMethod.GET)
    public String IsotopeController.findIsotopesByNameEquals(@RequestParam("name") String name, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("isotopes", Isotope.findIsotopesByNameEquals(name, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) Isotope.countFindIsotopesByNameEquals(name) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("isotopes", Isotope.findIsotopesByNameEquals(name, sortFieldName, sortOrder).getResultList());
        }
        return "isotopes/list";
    }
    
}
