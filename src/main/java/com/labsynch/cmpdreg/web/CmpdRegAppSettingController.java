package com.labsynch.cmpdreg.web;
import org.gvnix.addon.datatables.GvNIXDatatables;
import org.gvnix.addon.web.mvc.addon.jquery.GvNIXWebJQuery;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.labsynch.cmpdreg.domain.CmpdRegAppSetting;

@RequestMapping("/cmpdregappsettings")
@Controller
@RooWebScaffold(path = "cmpdregappsettings", formBackingObject = CmpdRegAppSetting.class)
@RooWebFinder
@GvNIXWebJQuery
@GvNIXDatatables(ajax = true)
public class CmpdRegAppSettingController {
}
