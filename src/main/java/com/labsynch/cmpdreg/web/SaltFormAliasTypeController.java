package com.labsynch.cmpdreg.web;
import com.labsynch.cmpdreg.domain.SaltFormAliasType;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/saltformaliastypes")
@Controller
@RooWebScaffold(path = "saltformaliastypes", formBackingObject = SaltFormAliasType.class)
public class SaltFormAliasTypeController {
}
