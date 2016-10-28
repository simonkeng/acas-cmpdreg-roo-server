package com.labsynch.cmpdreg.service.initialsetup;

import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.labsynch.cmpdreg.domain.FileType;
import com.labsynch.cmpdreg.domain.Isotope;
import com.labsynch.cmpdreg.domain.Operator;
import com.labsynch.cmpdreg.domain.PhysicalState;
import com.labsynch.cmpdreg.domain.Project;
import com.labsynch.cmpdreg.domain.PurityMeasuredBy;
import com.labsynch.cmpdreg.domain.Scientist;
import com.labsynch.cmpdreg.domain.StereoCategory;
import com.labsynch.cmpdreg.domain.Unit;
import com.labsynch.cmpdreg.dto.configuration.MainConfigDTO;
import com.labsynch.cmpdreg.utils.Configuration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable

public class BasicSanityTest {
	
	private static final Logger logger = LoggerFactory.getLogger(BasicSanityTest.class);
	
	private static final MainConfigDTO mainConfig = Configuration.getConfigInfo();
	private static final boolean isUnitTestDB = mainConfig.getServerSettings().isUnitTestDB();
	private static final boolean isInitalDBLoad = mainConfig.getServerSettings().isInitalDBLoad();
	
	
	@Test
	public void loadPhysicalStates() {		
		if(isInitalDBLoad && PhysicalState.countPhysicalStates() == 0L){
			String jsonString = null;
			MainConfigDTO mainConfig = Configuration.getConfigInfo();
			if (mainConfig.getServerSettings().getCorpPrefix().equalsIgnoreCase("CMPD")){
				jsonString = "[{ \"code\": \"solid\", \"name\": \"solid\"}, { \"code\": \"liquid\", \"name\": \"liquid\"},{ \"code\": \"gel\", \"name\": \"gel\"}]";
			} else {
				jsonString = "[{ \"code\": \"solid\", \"name\": \"solid\"},{ \"code\": \"liquid\", \"name\": \"liquid\"},{ \"code\": \"glass\", \"name\": \"glass\"},{ \"code\": \"oil\", \"name\": \"oil\"},{ \"code\": \"resin\", \"name\": \"resin\"},{ \"code\": \"solution\", \"name\": \"solution\"}]";
			}
			Collection<PhysicalState> manyStates = PhysicalState.fromJsonArrayToPhysicalStates(jsonString);
			System.out.println(manyStates);	
			for (PhysicalState pState : manyStates) {
				pState.persist();
				System.out.println(pState);	
			}
		} else {
			logger.debug("Physical states already loaded -- nothing to load");
		}
		
	}

	@Test
	public void checkPhysicalStates() {
		if(PhysicalState.countPhysicalStates() > 0L){
			String jsonString = "[{\"id\":1, \"code\": \"solid\", \"name\": \"solid\"},{\"id\":2, \"code\": \"liquid\", \"name\": \"liquid\"}]";
			Collection<PhysicalState> manyStates = PhysicalState.fromJsonArrayToPhysicalStates(jsonString);
			System.out.println(manyStates);	
			for (PhysicalState pState : manyStates) {
				PhysicalState fromDbPState = PhysicalState.findPhysicalStatesByCodeEquals(pState.getCode()).getSingleResult();
				logger.debug(fromDbPState.toString());	
				Assert.assertEquals(fromDbPState.getName(), pState.getName());
			}    	
		}
	}

	@Test
	public void removePhysicalStates() {
		if (isUnitTestDB){
			String jsonString = "[{\"id\":1, \"code\": \"solid\", \"name\": \"solid\"},{\"id\":2, \"code\": \"liquid\", \"name\": \"liquid\"},{\"id\":3, \"code\": \"gel\", \"name\": \"gel\"}]";
			Collection<PhysicalState> manyStates = PhysicalState.fromJsonArrayToPhysicalStates(jsonString);
			logger.debug(manyStates.toString());	
			for (PhysicalState pState : manyStates) {
				PhysicalState fromDbPState = PhysicalState.findPhysicalStatesByCodeEquals(pState.getCode()).getSingleResult();
				fromDbPState.remove();
				logger.debug("removed state: " + pState.getCode());	
			} 
		}
	}

	@Test
	public void loadStereoCategories() {
		if(isInitalDBLoad && StereoCategory.countStereoCategorys() == 0L){
			String jsonString = "[{\"id\":1, \"code\": \"scalemic\", \"name\": \"Scalemic\"},{\"id\":2, \"code\": \"racemic\", \"name\": \"Racemic\"},{\"id\":3, \"code\": \"achiral\", \"name\": \"Achiral\"},{\"id\":4, \"code\": \"see_comments\", \"name\": \"See Comments\"}, {\"id\":5, \"code\": \"unknown\", \"name\": \"Unknown\"}]";

			Collection<StereoCategory> manyStates = StereoCategory.fromJsonArrayToStereoCategorys(jsonString);
			System.out.println(manyStates);	

			for (StereoCategory pState : manyStates) {
				pState.persist();
				System.out.println(pState);	
			}
		}

	}


	@Test
	public void isotopesRunOnceSetUp() {
		if(isInitalDBLoad && Isotope.countIsotopes() == 0L){
			String jsonString = "[{\"id\": 1, \"name\":\"Deuterium labeled\",\"abbrev\":\"2H\",\"massChange\":1},{\"id\": 2, \"name\":\"Tritium labeled\",\"abbrev\":\"3H\",\"massChange\":2},{\"id\": 3, \"name\":\"13C labeled\",\"abbrev\":\"13C\",\"massChange\":1},{\"id\": 4, \"name\":\"14C labeled\",\"abbrev\":\"14C\",\"massChange\":2}]"; 
			Collection<Isotope> manyIsotopes = Isotope.fromJsonArrayToIsotopes(jsonString);
			System.out.println(manyIsotopes);	
			for (Isotope isotope : manyIsotopes) {
				isotope.persist();
				System.out.println(isotope);	
			}
		}
	}


	@Test
	public void operatorRunOnceSetUp() {
		if(isInitalDBLoad && Operator.countOperators() == 0L){
			String jsonString = "[{\"id\":1, \"code\": \"=\", \"name\": \"=\"},{\"id\":2, \"code\": \"<\", \"name\": \"<\"},{\"id\":3, \"code\": \">\", \"name\": \">\"}]";


			Collection<Operator> manyOperators = Operator.fromJsonArrayToOperators(jsonString);
			System.out.println(manyOperators);	

			for (Operator operator : manyOperators) {
				operator.persist();
				System.out.println(operator);	
			}
		}
	}

	
	@Test
	public void projectRunOnceSetUp() {
		if(isInitalDBLoad && Project.countProjects() == 0L){
			String jsonString = "[{\"id\":1, \"code\": \"Project 1\", \"name\": \"Project 1\"},{\"id\":2, \"code\": \"Project 2\", \"name\": \"Project 2\"},{\"id\":3, \"code\": \"Project 3\", \"name\": \"Project 3\"}]";


			Collection<Project> manyProjects = Project.fromJsonArrayToProjects(jsonString);
			System.out.println(manyProjects);	

			for (Project project : manyProjects) {
				project.persist();
				System.out.println(project);	
			}
		}
	}
	
	
	@Test
	public void measuredByRunOnceSetUp() {
		if(isInitalDBLoad && PurityMeasuredBy.countPurityMeasuredBys() == 0L){

			String jsonString = "[{\"id\":1, \"code\": \"HPLC\", \"name\": \"HPLC\"},{\"id\":2, \"code\": \"NMR\", \"name\": \"NMR\"},{\"id\":3, \"code\": \"GC\", \"name\": \"GC\"}, {\"id\":4, \"code\": \"Not Done\", \"name\": \"Not Done\"}]";

			Collection<PurityMeasuredBy> manyMeasuredBy = PurityMeasuredBy.fromJsonArrayToPurityMeasuredBys(jsonString);
			System.out.println(manyMeasuredBy);	

			for (PurityMeasuredBy measureBy : manyMeasuredBy) {
				measureBy.persist();
				System.out.println(measureBy);	
			}
		}
	}

	@Test
	public void unitsRunOnceSetUp() {
		if(isInitalDBLoad && Unit.countUnits() == 0L){

			String jsonString = "[{\"id\":1, \"code\": \"mg\", \"name\": \"mg\"},{\"id\":2, \"code\": \"g\", \"name\": \"g\"},{\"id\":3, \"code\": \"kg\", \"name\": \"kg\"},{\"id\":4, \"code\": \"mL\", \"name\": \"mL\"}]";

			Collection<Unit> units = Unit.fromJsonArrayToUnits(jsonString);
			System.out.println(units);	

			for (Unit unit : units) {
				unit.persist();
				System.out.println(unit);	
			}
		}
	}    

	@Test
	public void scientistsRunOnceSetUp() {
		if(isInitalDBLoad && Scientist.countScientists() == 0L){

			String jsonString = "[{\"id\":1, \"code\": \"aadmin\", \"name\": \"Adam Admin\",\"isChemist\":false,\"isAdmin\":true},{\"id\":2, \"code\": \"cchemist\", \"name\": \"Corey Chemist\",\"isChemist\":true,\"isAdmin\":false},{\"id\":3, \"code\": \"bbiologist\", \"name\": \"Ben Biologist\",\"isChemist\":false,\"isAdmin\":false}]";

			Collection<Scientist> scientists = Scientist.fromJsonArrayToScientists(jsonString);
			System.out.println(scientists);	

			for (Scientist scientist : scientists) {
				scientist.persist();
				System.out.println(scientist);	
			}
		}
	}       

	@Test
	public void fileTypeRunOnceSetUp() {
		if(isInitalDBLoad && FileType.countFileTypes() == 0L){
			String jsonString = "[{\"code\":\"HPLC\",\"id\":1,\"name\":\"HPLC\",\"version\":0},{\"code\":\"LCMS\",\"id\":2,\"name\":\"LCMS\",\"version\":0},{\"code\":\"NMR\",\"id\":3,\"name\":\"NMR\",\"version\":0}]";

			Collection<FileType> manyFileTypes = FileType.fromJsonArrayToFileTypes(jsonString);
			System.out.println(manyFileTypes);	

			for (FileType fileType : manyFileTypes) {
				logger.debug(fileType.getCode());
				fileType.merge();
				System.out.println(fileType);	
			}
		}
	}

}
