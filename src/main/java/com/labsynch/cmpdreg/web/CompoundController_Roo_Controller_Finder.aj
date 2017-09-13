// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.web;

import com.labsynch.cmpdreg.domain.Compound;
import com.labsynch.cmpdreg.web.CompoundController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

privileged aspect CompoundController_Roo_Controller_Finder {
    
    @RequestMapping(params = { "find=ByCdId", "form" }, method = RequestMethod.GET)
    public String CompoundController.findCompoundsByCdIdForm(Model uiModel) {
        return "compounds/findCompoundsByCdId";
    }
    
    @RequestMapping(params = "find=ByCdId", method = RequestMethod.GET)
    public String CompoundController.findCompoundsByCdId(@RequestParam("cdId") int CdId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("compounds", Compound.findCompoundsByCdId(CdId, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) Compound.countFindCompoundsByCdId(CdId) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("compounds", Compound.findCompoundsByCdId(CdId, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "compounds/list";
    }
    
    @RequestMapping(params = { "find=ByExternal_idEquals", "form" }, method = RequestMethod.GET)
    public String CompoundController.findCompoundsByExternal_idEqualsForm(Model uiModel) {
        return "compounds/findCompoundsByExternal_idEquals";
    }
    
    @RequestMapping(params = "find=ByExternal_idEquals", method = RequestMethod.GET)
    public String CompoundController.findCompoundsByExternal_idEquals(@RequestParam("external_id") String external_id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("compounds", Compound.findCompoundsByExternal_idEquals(external_id, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) Compound.countFindCompoundsByExternal_idEquals(external_id) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("compounds", Compound.findCompoundsByExternal_idEquals(external_id, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "compounds/list";
    }
    
}
