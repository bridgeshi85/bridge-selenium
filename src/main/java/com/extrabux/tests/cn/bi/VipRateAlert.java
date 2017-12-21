package com.extrabux.tests.cn.bi;

import java.io.File;
import java.io.IOException;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import static org.testng.Assert.assertTrue;

import com.extrabux.tests.BaseTest;
import com.extrabux.util.CSVUtil;
import com.extrabux.util.DBUtil;


public class VipRateAlert extends BaseTest{
	private static final Log LOG = LogFactory.getLog(VipRateAlert.class);
	private DBUtil db;
	private CSVUtil csv = new CSVUtil();
	ITestContext testContext;
	private List<String[]> finalResult = new ArrayList<String[]>();
	
	@BeforeSuite
	public void onStart(ITestContext context) {
		testContext = context;
	}
	
	@Test
	public void cashbackCompare() throws Exception{
		
		String[] header = {"Date","merchantId","normalCashback","vipCashback","commissionRate"};
		finalResult.add(header);
		
		 db = getDBUtil();
		 List<String[]> resultFromDB = db.getVipCashBackBiggerThanCommission();
		 
		 String lastMerchantId = "";
		 int count = 0;
		 List<String[]> temp = new ArrayList<String[]>();
		 

		 for(String[] row : resultFromDB){

			 if(!(lastMerchantId.equals(row[1])) && count > 0){
				//LOG.debug("clear");
				count = 0;
				temp.clear();
			 }
			 
			 lastMerchantId = row[1];
			 count += 1;
			 temp.add(row);
			 
			 if(count == 3){
				 for(String[] tempRow:temp){
					 finalResult.add(tempRow);
				 }
				 temp.clear();
				 count = 0;
			 }
		 }
		 
		 assertTrue(finalResult.size() == 1,"vip或普通返利比例连续3天大于commission rate");
	}

	@AfterMethod
	public void generateReportFile(ITestResult result) throws IOException{
        // create new file
		if(finalResult.size() == 1){
			LOG.debug("no result!");
			return;
		}
		String currentTest = result.getMethod().getMethodName();

//		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//		LocalDate localDate = LocalDate.now();
        String fileName = "src/test/resources/"+currentTest + ".csv";

        csv.wrieteIntoCsv(finalResult, fileName);
	}
}
