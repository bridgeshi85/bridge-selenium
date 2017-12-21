package com.extrabux.tests.cn.bi;

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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import static org.testng.Assert.assertTrue;

import com.extrabux.tests.BaseTest;
import com.extrabux.util.CSVUtil;
import com.extrabux.util.DBUtil;


public class TransactionCheck extends BaseTest{
	private static final Log LOG = LogFactory.getLog(TransactionCheck.class);
	private DBUtil db;
	private CSVUtil csv = new CSVUtil();
	ITestContext testContext;
	private List<String[]> finalResult = new ArrayList<String[]>();
	
	@BeforeSuite
	public void onStart(ITestContext context) {
		testContext = context;
	}
	
	@BeforeMethod
	public void clearResult(){
		finalResult.clear();
	}
	
	@Test
	public void purchasesCountCompare() throws Exception{
		
		String[] header = {"Date","merchantId","normalCashback","vipCashback"};
		finalResult.add(header);
		
		db = getDBUtil();
		List<String[]> resultFromDB = db.getPurchasesCountCompare();

		for(String[] row : resultFromDB){
			 if(!row[0].equals(row[3]))
				 finalResult.add(row);
		}
		 
		 assertTrue(finalResult.size() == 1,"purchase count数量发现不一致");
	}

	@Test
	public void purchasesCommissionCompare() throws Exception{
		
		String[] header = {"orderId","purchaseId","oldAmount","oldCommission","newAmount","newCommission","newPurchaseId"};
		finalResult.add(header);
		
		db = getDBUtil();
		List<String[]> resultFromDB = db.getPurchaseCommissionCompare();
				
		for(String[] row : resultFromDB){
			
			if(row[6].equals("not existed"))
				finalResult.add(row);
			else if(compareAmount(row[2],row[4],10) || compareAmount(row[3],row[5],10))
				 finalResult.add(row);
			
		}
		
		assertTrue(finalResult.size() == 1,"purchase commission发现不一致");
	}
	
	@Test
	public void transactionCountCompare() throws Exception{
		
		String[] header = {"orderId","purchaseId","oldTransactions","newTransactions","oldDate","newDate"};
		finalResult.add(header);
		
		db = getDBUtil();
		List<String[]> resultFromDB = db.getTransactionCompare();

		for(String[] row : resultFromDB){
			 if(!row[2].equals(row[3]))
				 finalResult.add(row);
		}
		
		assertTrue(finalResult.size() == 1,"transaction 数量发现不一致");
	}
	
	@Test
	public void newTransactionPurchaseSalesCompare() throws Exception{
		
		String[] header = {"purchaseId","salesSum","commissionSum","saleFromPurchase","commissionFromPurchase"};
		finalResult.add(header);
		
		db = getDBUtil();
		List<String[]> resultFromDB = db.getNewSumCompare();

		for(String[] row : resultFromDB){
			if(compareAmount(row[1],row[3],2) || compareAmount(row[2],row[4],2))
				 finalResult.add(row);
		}
		
		assertTrue(finalResult.size() == 1,"transaction 数量发现不一致");
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
	
	public boolean compareAmount(String oldAmount,String newAmount,int cap){
		int different = Integer.parseInt(oldAmount) - Integer.parseInt(newAmount);
		Boolean result = false;

		if(Math.abs(different) > cap)
			result = true;
		
		return result;
	}
}
