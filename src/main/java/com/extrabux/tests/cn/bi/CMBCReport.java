package com.extrabux.tests.cn.bi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import java.util.*;
import java.util.Date;
import org.testng.annotations.Test;

import com.extrabux.tests.BaseTest;
import com.extrabux.util.DBUtil;



public class CMBCReport extends BaseTest{

	//private static final Log LOG = LogFactory.getLog(VipRateAlert.class);
	private DBUtil db;
	
	@Test
	public void CMBCReportTest() throws SQLException {
		 db = getDBUtil();

		String msg=db.getCmbcMessage();

		int SuccessCntindex=msg.lastIndexOf("SuccessCnt");
		//定位ApplyFailedCnt中‘A’的位置
		int ApplyFailedCntindex=msg.lastIndexOf("ApplyFailedCnt");
		//定位BindFailedCnt中‘B’的位置
		int BindFailedCntindex=msg.lastIndexOf("BindFailedCnt");
		//取出SuccessCnt的值
		String SuccessCnt=msg.substring(SuccessCntindex+15, ApplyFailedCntindex-5);
		//取出ApplyFailedCnt的值
		String ApplyFailedCnt=msg.substring(ApplyFailedCntindex+19,BindFailedCntindex-5);
		//判断SuccessCnt不等于0，执行以下判断；如果等于0，则提示上传空bind文件到服务器
		//将SuccessCnt值强制转换整型
		Map<String, Object[]> data = new HashMap<String, Object[]>();
		//data.put("1", new Object[] {"CardToken", "EbatesCNAccount", "Phone","Message"});

		if (Integer.valueOf(SuccessCnt)!=0){
			int i;
			System.out.println("total error: "+ ApplyFailedCnt);
			for(i=1;i<=Integer.valueOf(ApplyFailedCnt);i++)
			{
				System.out.println("error: "+i);
				int errorindexstart=msg.lastIndexOf((i-1)+" =>");
				int errorindexend=msg.lastIndexOf(i+" =>");
				//根据调试打印出最后一条errorindexend的定位是小于0的，故需要将此情况排除，最后一条直接根据errorindexstart打印出来
				if(errorindexend>0)
				{
					String error=msg.substring(errorindexstart, errorindexend-6);
					int messageindex=error.lastIndexOf("Message");
					int Typeindex=error.lastIndexOf("Type");
					int CardTokenindex=error.lastIndexOf("Card Token");
					int EBATESAccountindex=error.lastIndexOf("EBATES CN Account");
					int Channelindex=error.lastIndexOf("Channel");
					int Phoneindex=error.lastIndexOf("Phone");
					int Districtindex=error.lastIndexOf("District");
					
					String Message=error.substring(messageindex+13, Typeindex-10);
					String CardToken=error.substring(CardTokenindex+16, EBATESAccountindex-12);
					String EBATESAccount=error.substring(EBATESAccountindex+23, Channelindex-12);
					String Phone=error.substring(Phoneindex+11, Districtindex-12);

					System.out.println(CardToken+";"+EBATESAccount+";"+Phone+";"+Message);
					//读出数据的各个字段
					data.put(String.valueOf(i+1), new Object[] {CardToken, EBATESAccount, Phone,Message});

				}
				//最后一条error直接根据errorindexstart打印出来
				else{
					String lasterror=msg.substring(errorindexstart);
					int messageindex=lasterror.lastIndexOf("Message");
					int Typeindex=lasterror.lastIndexOf("Type");
					int CardTokenindex=lasterror.lastIndexOf("Card Token");
					int EBATESAccountindex=lasterror.lastIndexOf("EBATES CN Account");
					int Channelindex=lasterror.lastIndexOf("Channel");
					int Phoneindex=lasterror.lastIndexOf("Phone");
					int Districtindex=lasterror.lastIndexOf("District");
					
					String Message=lasterror.substring(messageindex+13, Typeindex-10);
					String CardToken=lasterror.substring(CardTokenindex+16, EBATESAccountindex-12);
					String EBATESAccount=lasterror.substring(EBATESAccountindex+23, Channelindex-12);
					String Phone=lasterror.substring(Phoneindex+11, Districtindex-12);

					System.out.println(CardToken+";"+EBATESAccount+";"+Phone+";"+Message);
					//读出数据的各个字段
					data.put(String.valueOf(i+1), new Object[] {CardToken, EBATESAccount, Phone,Message});
				}

			}
		}
		//判断SuccessCnt如果等于0，则提示上传空bind文件到服务器
		else
		{
			System.out.println("need to upload a blank bind file to server");
		}
    
		//iterate through each row from first sheet	
		rowIterator(data);
		//取出当日apply文件中的message字段
		
	}

	//写入Excel中
	public static void rowIterator(Map<String, Object[]> data){
		
		try{
			FileInputStream file = new FileInputStream(new File("src/test/resources/CMBCReport.xls"));
			
			HSSFWorkbook wb = new HSSFWorkbook(file);
			
			HSSFSheet sheet = wb.getSheetAt(0);
			
			int rownum = sheet.getLastRowNum()+1;
			
			//System.out.println(rownum);
			
			Set<String> keyset = data.keySet();
			//int rownum = 0;
			for (String key : keyset) {
				Row row = sheet.createRow(rownum++);
				Object [] objArr = data.get(key);
				int cellnum = 0;
				for (Object obj : objArr) {
					Cell cell = row.createCell(cellnum++);
					if(obj instanceof Date) 
						cell.setCellValue((Date)obj);
					else if(obj instanceof Boolean)
						cell.setCellValue((Boolean)obj);
					else if(obj instanceof String)
						cell.setCellValue((String)obj);
					else if(obj instanceof Double)
						cell.setCellValue((Double)obj);
				}
			}
			
			file.close();
			FileOutputStream out = new FileOutputStream (new File("src/test/resources/CMBCReport.xls"));
			wb.write(out);
			out.close();
		}
		catch(Exception ex){
		}
		
	}
		
	
}