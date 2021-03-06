// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.web;

import com.labsynch.cmpdreg.domain.Scientist;
import com.labsynch.cmpdreg.web.ScientistController;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect ScientistController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String ScientistController.create(@Valid Scientist scientist, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, scientist);
            return "scientists/create";
        }
        uiModel.asMap().clear();
        scientist.persist();
        return "redirect:/scientists/" + encodeUrlPathSegment(scientist.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String ScientistController.createForm(Model uiModel) {
        populateEditForm(uiModel, new Scientist());
        return "scientists/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String ScientistController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("scientist", Scientist.findScientist(id));
        uiModel.addAttribute("itemId", id);
        return "scientists/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String ScientistController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("scientists", Scientist.findScientistEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) Scientist.countScientists() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("scientists", Scientist.findAllScientists(sortFieldName, sortOrder));
        }
        return "scientists/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String ScientistController.update(@Valid Scientist scientist, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, scientist);
            return "scientists/update";
        }
        uiModel.asMap().clear();
        scientist.merge();
        return "redirect:/scientists/" + encodeUrlPathSegment(scientist.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String ScientistController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Scientist.findScientist(id));
        return "scientists/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String ScientistController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Scientist scientist = Scientist.findScientist(id);
        scientist.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/scientists";
    }
    
    void ScientistController.populateEditForm(Model uiModel, Scientist scientist) {
        uiModel.addAttribute("scientist", scientist);
    }
    
    String ScientistController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
    
}
