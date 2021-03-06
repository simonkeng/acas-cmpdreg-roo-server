// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.web;

import com.labsynch.cmpdreg.domain.Salt;
import com.labsynch.cmpdreg.web.SaltController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

privileged aspect SaltController_Roo_Controller {
    
    @RequestMapping(params = "form", produces = "text/html")
    public String SaltController.createForm(Model uiModel) {
        populateEditForm(uiModel, new Salt());
        return "salts/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String SaltController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("salt", Salt.findSalt(id));
        uiModel.addAttribute("itemId", id);
        return "salts/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String SaltController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("salts", Salt.findSaltEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) Salt.countSalts() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("salts", Salt.findAllSalts(sortFieldName, sortOrder));
        }
        return "salts/list";
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String SaltController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Salt.findSalt(id));
        return "salts/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String SaltController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Salt salt = Salt.findSalt(id);
        salt.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/salts";
    }
    
    void SaltController.populateEditForm(Model uiModel, Salt salt) {
        uiModel.addAttribute("salt", salt);
    }
    
}
