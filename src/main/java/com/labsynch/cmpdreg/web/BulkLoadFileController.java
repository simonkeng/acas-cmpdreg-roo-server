package com.labsynch.cmpdreg.web;
import com.labsynch.cmpdreg.domain.BulkLoadFile;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;

@RequestMapping("/bulkloadfiles")
@Controller
@RooWebScaffold(path = "bulkloadfiles", formBackingObject = BulkLoadFile.class)
@RooWebFinder
public class BulkLoadFileController {
}
