package com.extrabux.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DBUtil {
	private static final Log LOG = LogFactory.getLog(DBUtil.class);
	
	protected Configuration config;
	
	public DBUtil(Configuration config) {
		this.config = config;
	}

	// TODO make the db name configurable
	public int getMemberIdByEmail(String email) throws SQLException {
		String sql = "select id from extrabux.users where email = ?";
		int memberId = 0;
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, email);

			rs = ps.executeQuery();
			while (rs.next()) {
				memberId = rs.getInt(1);
			}
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		
		return memberId;
	}
	
	public List<Integer> getTicketStoreIdsForMember(int memberId) throws SQLException {
		String sql = "select merchant_id from extrabux.clicks where user_id = ?";
		List<Integer> ticketStoreIds = new ArrayList<Integer>();
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, memberId);

			rs = ps.executeQuery();
			while (rs.next()) {
				ticketStoreIds.add(rs.getInt(1));
			}
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		
		return ticketStoreIds;
	}	
	
	public List<String> getStoreNameById(List<Integer> storeIds) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		List<String> storeNames = new ArrayList<String>();
		
		StringBuilder inClause = new StringBuilder();
		boolean firstValue = true;
		for (int i = 0; i < storeIds.size(); i++) {
		  
		  if ( firstValue ) {
			  inClause.append("?");
		    firstValue = false;
		  } else {
		    inClause.append(",");
		    inClause.append("?");
		  }
		}
		
		String sql = "select name from extrabux.merchants where id in (" + inClause.toString() + ")";
		
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < storeIds.size(); i++) {
				ps.setInt(i + 1, storeIds.get(i));
			}

			rs = ps.executeQuery();
			while (rs.next()) {
				storeNames.add(rs.getString(1));
			}
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		
		return storeNames;
	}
	
	public List<String> getMemberTicketStoreNamesByEmail(String email) throws Exception {
		List<String> storeNames = new ArrayList<String>();
		
		int memberId = getMemberIdByEmail(email);
		LOG.info(email + " " + memberId);
		List<Integer> storeIds = getTicketStoreIdsForMember(memberId);
		LOG.info("storeIds: " + storeIds);		
		if (storeIds.isEmpty()) {
			throw new Exception("no stores were found for user " + email);
		}
		storeNames = getStoreNameById(storeIds);
		LOG.info(storeNames);
		
		return storeNames;
	}
	
	public void updatePurchaseToAvailabelByUserId(int memberId,int commssion) throws Exception {
		
		String sql = "update extrabux.purchases "
				+ "set available_date = '2014-12-12',status='Available',state='Confirmed',commission = ?"
				+ " where user_id = ?";

		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, commssion);
			ps.setInt(2, memberId);
			ps.executeUpdate();
			
			//conn.commit();
			LOG.debug("id: " + memberId + " update purchase success");
		}finally {
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}
	
	public void updateCommissionToAvailabelByUserId(int memberId) throws Exception {
		
		String sql = "update extrabux.commissions "
				+ "set available_date = '2014-12-12',status='Available',state='Confirmed'"
				+ " where user_id = ?"
				+ " and type <> 'PaymentMethodChoiceBonus'";

		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, memberId);
			ps.executeUpdate();
			LOG.debug("id: " + memberId + " update commission success");
		}finally {
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}
	
	public void deleteAllPaymentByUserId(int memberId) throws Exception {
		
		String sql = "delete from extrabux.payments "
				+ " where user_id = ?";

		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, memberId);
			ps.executeUpdate();
			LOG.debug("id: " + memberId + " delete all payments success");
		}finally {
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}
	
	public void deletePaymentMethodsByEmail(String email) throws Exception {
		
		String sql = "delete from extrabux.user_payment_methods "
				+ " where user_id = (select id from extrabux.users where email = ?)";

		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, email);
			ps.executeUpdate();
			LOG.debug("member: " + email + " delete all payment methods success");
		}finally {
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}
	
	public void deleteUserConnectsByEmail(String email) throws Exception {
		
		String sql = "delete from `extrabux`.user_connects "
				+ " where open_id = ?";

		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, email);
			ps.executeUpdate();
			LOG.debug("member: " + email + " delete user_connects success");
		}finally {
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}
	
	public void deleteUserByEmail(String email) throws Exception {
		
		String sql = "delete from `extrabux`.users "
				+ " where email = ?";

		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, email);
			ps.executeUpdate();
			LOG.debug("member: " + email + " delete user success");
		}finally {
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}
	
	public int getParentMemberIdByEmail(String email) throws SQLException {
		String sql = "select parent_id from extrabux.users where email = ?";
		int memberId = 0;
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, email);

			rs = ps.executeQuery();
			while (rs.next()) {
				memberId = rs.getInt(1);
			}
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		
		return memberId;
	}
	
	public int getCMBCTransactionMatchCount() throws SQLException {
		String sql = "select count(*) from extrabux.cmbc_transaction_matching_flow";
		int count = 0;
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);

			rs = ps.executeQuery();
			while (rs.next()) {
				count = rs.getInt(1);
			}
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		
		return count;
	}
	
	public String getCmbcMessage() throws SQLException {
		String sql = "SELECT message FROM `extrabux`.`credit_cards_process_file_log` WHERE DATE(`created`) = DATE(NOW()) ORDER BY id DESC";
		String msg = "";
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);

			rs = ps.executeQuery();
			while (rs.next()) {
				msg = rs.getString(1);
			}
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		
		return msg;
	}
	
	public List<String[]> getVipCashBackBiggerThanCommission() throws Exception {
		String sql = getSql("src/test/resources/sql.txt");
		
		List<String[]> resultList = new ArrayList<String[]>();
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);

			rs = ps.executeQuery();
			while (rs.next()) {
				String[] row = new String[5];
				row[0] = rs.getString(1);
				row[1] = rs.getString(2);
				row[2] = rs.getString(3);
				row[3] = rs.getString(4);
				row[4] = rs.getString(5);
				
				resultList.add(row);
				LOG.debug(row[0]+"  "+row[1]+"  "+row[2]+"  "+row[3]+"  "+row[4]);
			}
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		
		return resultList;
	}
	
	
	public List<String[]> getPurchasesCountCompare() throws Exception {
		String sql = getSql("src/test/resources/sqls/purchaseCountCompareSql.txt");
		
		List<String[]> resultList = new ArrayList<String[]>();
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);

			rs = ps.executeQuery();
			while (rs.next()) {
				String[] row = new String[4];
				row[0] = rs.getString(1);
				row[1] = rs.getString(2);
				row[2] = rs.getString(3);
				row[3] = rs.getString(4);
				
				resultList.add(row);
				LOG.debug(row[0]+"  "+row[1]+"  "+row[2]+"  "+row[3]);
			}
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		
		return resultList;
	}
	
	public List<String[]> getPurchaseCommissionCompare() throws Exception {
		String sql = getSql("src/test/resources/sqls/purchaseCommissionCompare.txt");
		
		List<String[]> resultList = new ArrayList<String[]>();
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);

			rs = ps.executeQuery();
			while (rs.next()) {
				String[] row = new String[7];
				row[0] = rs.getString(1);
				row[1] = rs.getString(2);
				row[2] = rs.getString(3);
				row[3] = rs.getString(4);
				row[4] = rs.getString(5);
				row[5] = rs.getString(6);
				row[6] = rs.getString(7);
				
				resultList.add(row);
				LOG.debug(row[0]+"  "+row[1]+"  "+row[2]+"  "+row[3]+" "+row[4]+" "+row[5]+" "+row[6]);
			}
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		
		return resultList;
	}
	
	public List<String[]> getTransactionCompare() throws Exception {
		String sql = getSql("src/test/resources/sqls/transactionCountCompare.txt");
		
		List<String[]> resultList = new ArrayList<String[]>();
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);

			rs = ps.executeQuery();
			while (rs.next()) {
				String[] row = new String[6];
				row[0] = rs.getString(1);
				row[1] = rs.getString(2);
				row[2] = rs.getString(3);
				row[3] = rs.getString(4);
				row[4] = rs.getString(5);
				row[5] = rs.getString(6);
				
				resultList.add(row);
				LOG.debug(row[0]+"  "+row[1]+"  "+row[2]+"  "+row[3]);
			}
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		
		return resultList;
	}
	
	public List<String[]> getNewSumCompare() throws Exception {
		String sql = getSql("src/test/resources/sqls/newSystemSumCompareSql.txt");
		
		List<String[]> resultList = new ArrayList<String[]>();
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);

			rs = ps.executeQuery();
			while (rs.next()) {
				String[] row = new String[6];
				row[0] = rs.getString(1);
				row[1] = rs.getString(2);
				row[2] = rs.getString(3);
				row[3] = rs.getString(4);
				row[4] = rs.getString(5);
				
				resultList.add(row);
				LOG.debug(row[0]+"  "+row[1]+"  "+row[2]+"  "+row[3]+" "+row[4]);
			}
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		
		return resultList;
	}
	
	//public 
	
	public String getSql(String fileName) throws IOException {
		 BufferedReader br = new BufferedReader(new FileReader(fileName));
		 String sql = "";
		 try {
		     StringBuilder sb = new StringBuilder();
		     String line = br.readLine();

		     while (line != null) {
		         sb.append(line);
		         sb.append(System.lineSeparator());
		         line = br.readLine();
		     }
		     sql = sb.toString();
		 } finally {
		     br.close();
		 }
		return sql;
	}
	
	private Connection getConnection() throws SQLException {
		Connection conn;
		//LOG.info(config.getString("db.url") + "?" + "user=" + config.getString("db.user") + "&password=" + config.getString("db.password"));
		conn = DriverManager.getConnection(config.getString("db.url") + "?" + "user=" + config.getString("db.user") + "&password=" + config.getString("db.password"));
		return conn;
	}
	
}
