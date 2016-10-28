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
