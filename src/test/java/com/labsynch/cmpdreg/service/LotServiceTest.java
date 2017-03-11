package com.labsynch.cmpdreg.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.cmpdreg.domain.Lot;
import com.labsynch.cmpdreg.domain.LotAlias;
import com.labsynch.cmpdreg.dto.LotDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class LotServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(LotServiceTest.class);
	
	@Autowired
	private LotService lotService;

	@Transactional
	@Rollback(false)
	@Test
	public void updateLotMeta1(){
		Lot lot = Lot.findLotsByCorpNameEquals("CMPD-0000001-001").getSingleResult();
		LotDTO lotDTO = new LotDTO();
		lotDTO.setLotCorpName(lot.getCorpName());
		lotDTO.setColor("blue 103");
		lotDTO.setBarcode("barcode TEST 102");
		LotAlias lotAlias = new LotAlias();
		lotAlias.setLsType("other name");
		lotAlias.setLsKind("Lot Common Name");
		lotAlias.setAliasName("test lot alias name 101");

		//		lotDTO.setBarcode("");

		Lot result = lotService.updateLotMeta(lotDTO, "cchemist");
		logger.info(result.toJson());
	}

	
	
	@Transactional
	@Rollback(false)
//	@Test
	public void updateSingleLotTest(){
		Double originalLotWeight = Lot.findLot(20712L).getLotMolWeight();
		Lot updatedLot = lotService.updateLotWeight(Lot.findLot(20712L));
		Double updateLotWeight = updatedLot.getLotMolWeight();
		Double lotDiff = updateLotWeight - originalLotWeight;
		if (lotDiff == 0.0D){
			logger.info("there is no molWeight diff");
		}
		logger.info("lot weight changed for : " + updatedLot.getCorpName() + "  " + lotDiff + " " + originalLotWeight + " " + updateLotWeight);

	}
	
	@Transactional
	@Rollback(false)
//	@Test
	public void updateAllLotsTest(){
		List<Lot> lots = Lot.findAllLots();
		Lot updatedLot;
		Double originalLotWeight;
		Double updateLotWeight;
		Double lotDiff;
		for (Lot lot : lots){
			originalLotWeight = lot.getLotMolWeight();
			updatedLot = lotService.updateLotWeight(lot);
			updateLotWeight = updatedLot.getLotMolWeight();
			lotDiff = updateLotWeight - originalLotWeight;

			if (lotDiff != 0.0D){
				logger.info("lot weight changed for : " + lot.getCorpName() + "  " + originalLotWeight + " " + updateLotWeight);
			}
		}
		
	}

}
