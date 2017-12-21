package com.extrabux.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CSVUtil {
	private static final Log LOG = LogFactory.getLog(CSVUtil.class);

	public void wrieteIntoCsv(List<String[]> list,String fileName) throws FileNotFoundException{
		LOG.info("begin write to csv file");
		LOG.info(fileName);
		File csvFile = new File(fileName);
        PrintWriter pw = new PrintWriter(csvFile);
        StringBuilder sb = new StringBuilder();
        for(String[] row:list){
            for(int i=0;i<row.length;i++){
            	sb.append(row[i]);
                if(i == row.length - 1){
                    sb.append('\n');
                }else{
                    sb.append(',');
                }
            }
        }

        pw.write(sb.toString());
        pw.close();
		LOG.info("done!");
	}
	
}