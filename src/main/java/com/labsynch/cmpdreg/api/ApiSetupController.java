package com.labsynch.cmpdreg.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.labsynch.cmpdreg.domain.FileType;
import com.labsynch.cmpdreg.domain.Isotope;
import com.labsynch.cmpdreg.domain.Operator;
import com.labsynch.cmpdreg.domain.PhysicalState;
import com.labsynch.cmpdreg.domain.Project;
import com.labsynch.cmpdreg.domain.Scientist;
import com.labsynch.cmpdreg.domain.StereoCategory;
import com.labsynch.cmpdreg.domain.Unit;
import com.labsynch.cmpdreg.dto.configuration.MainConfigDTO;
import com.labsynch.cmpdreg.service.ChemStructureService;
import com.labsynch.cmpdreg.service.LoadPreDefCorpNameService;
import com.labsynch.cmpdreg.utils.Configuration;

@RequestMapping(value = {"/api/v1"})
@Controller
public class ApiSetupController {
	
	Logger logger = LoggerFactory.getLogger(ApiSetupController.class);
	
	@Autowired
	private ChemStructureService chemStructServ;
	
	@Autowired
	private LoadPreDefCorpNameService loadPreDefCorpNameService;

	@Transactional
	@RequestMapping(value = "/createAllJChemTables", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> createAllJChemTables(@RequestBody String password){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		MainConfigDTO mainConfig = Configuration.getConfigInfo();
		if (!password.equals("compoundAdmin") || !mainConfig.getServerSettings().isInitalDBLoad()){
			return new ResponseEntity<String>("Wrong passcode or isInitialDBLoad=false",headers, HttpStatus.BAD_REQUEST);
		}
		if (mainConfig.getServerSettings().isInitalDBLoad()){
			chemStructServ.createJchemPropertyTable();
			chemStructServ.createJChemTable("Salt_Structure", false);
			chemStructServ.createJChemTable("SaltForm_Structure", true);
			chemStructServ.createJChemTable("Parent_Structure", true);			
		}
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}
	
	@Transactional
	@RequestMapping(value = "/createPreDefCorpNames", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> createPreDefCorpNames(@RequestBody String password){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		MainConfigDTO mainConfig = Configuration.getConfigInfo();
		if (!password.equals("compoundAdmin") || !mainConfig.getServerSettings().isInitalDBLoad()){
			return new ResponseEntity<String>("Wrong passcode or isInitialDBLoad=false",headers, HttpStatus.BAD_REQUEST);
		}
		if (mainConfig.getServerSettings().isInitalDBLoad()){
			boolean success = loadPreDefCorpNameService.loadDefaultPreDefCorpNames();
			if (success==false){
				return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
			}else{
				return new ResponseEntity<String>(headers, HttpStatus.OK);
			}
		}
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/physicalStates/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createPhysicalStatesFromJsonArray(@RequestBody String json) {
        for (PhysicalState physicalState: PhysicalState.fromJsonArrayToPhysicalStates(json)) {
            physicalState.persist();
        }
        HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
	
	@RequestMapping(value = "/stereoCategories/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createStereoCategoriesFromJsonArray(@RequestBody String json) {
        for (StereoCategory stereoCategory: StereoCategory.fromJsonArrayToStereoCategorys(json)) {
            stereoCategory.persist();
        }
        HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
	
	@RequestMapping(value = "/isotopes/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createIsotopesFromJsonArray(@RequestBody String json) {
        for (Isotope isotope: Isotope.fromJsonArrayToIsotopes(json)) {
            isotope.persist();
        }
        HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
        
    }
	
	@RequestMapping(value = "/operators/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createOperatorsFromJsonArray(@RequestBody String json) {
        for (Operator operator: Operator.fromJsonArrayToOperators(json)) {
            operator.persist();
        }
        HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type", "application/text");        
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
	
	@RequestMapping(value = "/projects/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createProjectsFromJsonArray(@RequestBody String json) {
        for (Project project: Project.fromJsonArrayToProjects(json)) {
            project.persist();
        }
        HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type", "application/text");        
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
	
	@RequestMapping(value = "/units/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createUnitsFromJsonArray(@RequestBody String json) {
        for (Unit unit: Unit.fromJsonArrayToUnits(json)) {
            unit.persist();
        }
        HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
	
	@RequestMapping(value = "/scientists/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createScientistsFromJsonArray(@RequestBody String json) {
        for (Scientist scientist: Scientist.fromJsonArrayToScientists(json)) {
            scientist.persist();
        }
        HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
	
	@RequestMapping(value = "/fileTypes/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFileTypesFromJsonArray(@RequestBody String json) {
        for (FileType fileType: FileType.fromJsonArrayToFileTypes(json)) {
            fileType.persist();
        }
        HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

}
