package com.extrabux.tests.cn.bi;

import static org.testng.Assert.assertTrue;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.annotations.Test;

import com.extrabux.tests.BaseTest;
import com.extrabux.util.DBUtil;

public class CMBCTransactionCount extends BaseTest{

	private static final Log LOG = LogFactory.getLog(CMBCTransactionCount.class);
	private DBUtil db;
	public int cmbcLastCount = 0;
	
	@SuppressWarnings("deprecation")
	@Test
	public void transactionCountAlert() throws SQLException, ParseException, InterruptedException{
		
		db = getDBUtil();
		cmbcLastCount = db.getCMBCTransactionMatchCount();
		LOG.debug("cmbc transaction match count:"+cmbcLastCount);
		
		while(true){
			int cmbcMatchCount = db.getCMBCTransactionMatchCount();
			//cmbcLastCount+=1;
			LOG.debug("cmbc transaction match count:"+cmbcMatchCount);
			assertTrue(cmbcMatchCount >= cmbcLastCount,"transaction count decreased!!!");
			cmbcLastCount = cmbcMatchCount;
			Date date = new Date();			
			if(date.getHours()>=12)
				return;
			Thread.sleep(5*1000*60);
		}
	}
	
}
