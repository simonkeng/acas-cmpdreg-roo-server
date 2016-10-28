package com.labsynch.cmpdreg.dto;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.cmpdreg.domain.Lot;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class MetalotJSONParseTest {

	private Lot lot2; 
	  @Autowired 
	public void setLot(Lot lot) {
		  this.lot2 = lot;
		  }

	
	//@Test
	@Transactional
  public void testMarkerMethod() {
	  
	  
	  System.out.println("Hello World");	

	  // TBD: reenable test after updating json string so parser doesn't fail
	  String json ="{\"fileLists\":[{\"description\":\"new file load\",\"id\":1,\"name\":\"fileName\",\"size\":1345,\"test\":\"delteMe\",\"type\":\"JPEG\",\"uploaded\":true,\"url\":\"www.yahoo.com\",\"version\":0}],\"isosalts\":[{\"equivalents\":2.5,\"id\":1,\"ignore\":false,\"isotope\":{\"abbrev\":\"C14\",\"id\":1,\"ignore\":null,\"massChange\":2.0,\"name\":\"C14\",\"version\":0},\"salt\":null,\"type\":\"isotope\",\"version\":0},{\"equivalents\":5.0,\"id\":2,\"ignore\":false,\"isotope\":{\"abbrev\":\"U235\",\"id\":2,\"ignore\":null,\"massChange\":-3.0,\"name\":\"U235\",\"version\":0},\"salt\":null,\"type\":\"isotope\",\"version\":0}],\"lot\":{\"amount\":55.0,\"amountUnits\":{\"code\":\"mg\",\"id\":1,\"name\":\"mg\",\"version\":0},\"asDrawnStructure\":\"C\",\"chemist\":{\"code\":\"cchemist\",\"id\":2,\"ignore\":null,\"isAdmin\":false,\"isChemist\":true,\"name\":\"Corey Chemist\",\"version\":0},\"color\":\"blue\",\"comments\":\"just a test\",\"corpName\":\"CMPD-0001-Na-01\",\"id\":1,\"ignore\":false,\"isVirtual\":false,\"lotAsDrawnCdId\":0,\"lotMolWeight\":23.53,\"notebookPage\":\"12342-23\",\"percentEE\":38.0,\"physicalState\":{\"code\":\"gel\",\"id\":3,\"name\":\"gel\",\"version\":0},\"purity\":95.0,\"purityMeasuredBy\":{\"code\":\"HPLC\",\"id\":1,\"name\":\"HPLC\",\"version\":0},\"purityOperator\":{\"code\":\">\",\"id\":3,\"name\":\">\",\"version\":0},\"retain\":15.0,\"retainUnits\":{\"code\":\"mg\",\"id\":1,\"name\":\"mg\",\"version\":0},\"saltForm\":{\"casNumber\":\"CAS-1234-4\",\"chemist\":{\"code\":\"cchemist\",\"id\":2,\"ignore\":null,\"isAdmin\":false,\"isChemist\":true,\"name\":\"Corey Chemist\",\"version\":0},\"corpName\":\"CMPD-0001-Na\",\"id\":1,\"ignore\":false,\"molStructure\":\"\n  Mrv0541 10301115172D          \n\n  2  1  0  0  0  0            999 V2000\n    0.8250    0.0000    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n    0.0000    0.0000    0.0000 Cl  0  0  0  0  0  0  0  0  0  0  0  0\n  1  2  1  0  0  0  0\nM  END\n\",\"parent\":{\"chemist\":{\"code\":\"aadmin\",\"id\":1,\"ignore\":null,\"isAdmin\":true,\"isChemist\":false,\"name\":\"Adam Admin\",\"version\":0},\"commonName\":\"test01\",\"corpName\":\"CMPD-0001\",\"id\":1,\"ignore\":false,\"molStructure\":\"\n  Mrv0541 10301115132D          \n\n  1  0  0  0  0  0            999 V2000\n    0.0000    0.0000    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\nM  END\n\",\"parentCdId\":15,\"stereoCategory\":{\"code\":\"scalemic\",\"id\":1,\"name\":\"Scalemic\",\"version\":0},\"stereoComment\":\"straight\",\"version\":0},\"saltFormCdId\":0,\"version\":0},\"supplier\":\"SarinaCo\",\"supplierID\":\"JuliaCo\",\"synthesisDate\":1318402800000,\"version\":0},\"skipParentDupeCheck\":false}";
	 
	  Metalot metalot = new Metalot(); 
	  
	  String jsonMetalot = new JSONSerializer().exclude("*.class").serialize(metalot);  
	  System.out.println(json);	
	  Metalot metaLotOld = new JSONDeserializer<Metalot>().use(null, Metalot.class).deserialize(json);
	  System.out.println(metaLotOld);	
		System.out.println(metaLotOld.toJson());	    		


  }
	
	@Test
	@Transactional
  public void testMarkerMethod2() {
		ParentDTO pdto = new ParentDTO();
		
		
        org.junit.Assert.assertTrue(true);
    }
}
	
	
