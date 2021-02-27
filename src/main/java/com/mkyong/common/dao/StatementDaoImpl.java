package com.mkyong.common.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import com.mkyong.common.service.Data;

public class StatementDaoImpl implements StatementDao{
	private static final Logger logger = Logger.getLogger(StatementDaoImpl.class);
	private static final String ROLE_ADMIN = "ROLE_ADMIN";

	@Override
	public List<AccountStatement> getStatementList(Data data) throws ParseException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		List<AccountStatement> statements = new ArrayList<AccountStatement>();

		Set<String> authorities = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
		
		String query = "SELECT statement.account_id,account.account_number,account.account_type, statement.datefield,statement.amount "
				+ "FROM statement INNER JOIN account "+
				"ON account.ID = statement.account_id ";
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		// Step 1: Loading driver
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
		} catch (ClassNotFoundException cnfex) {
			logger.info("Problem in loading or "
					+ "registering MS Access JDBC driver");
			cnfex.printStackTrace();
		}

		// Step 2: open db  connection
		try {

			String msAccDB = "/home/loe/Downloads/accountsdb.accdb";

			String dbURL = "jdbc:ucanaccess://" + msAccDB;

			connection = DriverManager.getConnection(dbURL);

			statement = connection.prepareStatement(query);

			resultSet = statement.executeQuery();

			while (resultSet.next()) {
				AccountStatement stat = new AccountStatement();
				stat.setAccountId(resultSet.getInt(1));
				stat.setAccNumber(resultSet.getString(2));
				stat.setAccountType(resultSet.getString(3));
				stat.setDate(resultSet.getString(4));
				stat.setAmount(resultSet.getString(5));
				statements.add(stat);

			}
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
		} finally {
			try {
				if (null != connection) {

					resultSet.close();
					statement.close();
					connection.close();
				}
			} catch (SQLException sqlex) {
				sqlex.printStackTrace();
			}
		}
//		
//	if (authorities.contains("ROLE_USER"))
//		{
//			if(data.getAccId().isEmpty() && data.getAmountFrom().isEmpty() && data.getAmountTo().isEmpty() && data.getDateFrom().isEmpty() && data.getAmountTo().isEmpty())
//			{
//				
//				DateTimeFormatter your_format =  DateTimeFormatter.ofPattern("dd-MMM-yyyy");
//
//			    String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//			    LocalDate today = LocalDate.parse(formattedDate);
//			    String endDate=today.minusMonths(4).toString();
//			    
//			    Date dataFromDate = (Date) your_format.parse(endDate);
//				Date dataToDate = (Date) your_format.parse(formattedDate);
//
//				data.setDateTo(dataToDate.toString().replace("-", "."));
//				data.setDateFrom(dataFromDate.toString().replace("-", "."));
//
//
//
//			}
	
//	}
		return getListBasedOnData(data,statements);
	
	}

	private List<AccountStatement> getListBasedOnData(Data data, List<AccountStatement> statements) throws ParseException {
		List<AccountStatement> newStmt = new ArrayList<AccountStatement>();
		for(AccountStatement stmt : statements) {
			if(isDataAdded(stmt,data))
				newStmt.add(stmt);

		}
		return newStmt;
	}

	private boolean isDataAdded(AccountStatement stmt, Data data) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		Date stmtFromDate = sdf.parse(stmt.getDate());
		Date dataFromDate = null;
		Date dataToDate = null;
		float dataFromAmount = 0 ;
		float dataToAmount = 0 ;
		float stmtAmount = Float.valueOf(stmt.getAmount());

		if(!data.getAmountFrom().isEmpty()) {
			dataFromAmount = Float.valueOf(data.getAmountFrom());
			dataToAmount = Float.valueOf(data.getAmountTo());
		}
		if(!data.getDateFrom().isEmpty()) {
			dataFromDate = sdf.parse(data.getDateFrom());
			dataToDate = sdf.parse(data.getDateTo());
		}
		if(data.getAccId().isEmpty() && Integer.valueOf(stmt.getAccountId()).equals(Integer.valueOf((data.getAccId())))){
			if(data.getAmountFrom().isEmpty() && data.getDateFrom().isEmpty())
				return true;
			else if(((!data.getDateFrom().isEmpty() && data.getAmountFrom().isEmpty())&& stmtFromDate.after(dataFromDate) && stmtFromDate.before(dataToDate)))
				return true;
			else if(((!data.getAmountFrom().isEmpty() && data.getDateFrom().isEmpty()) && dataFromAmount<stmtAmount && stmtAmount<dataToAmount))
				return true;
			else if((!data.getDateFrom().isEmpty() && stmtFromDate.after(dataFromDate) && stmtFromDate.before(dataToDate))
					&&(!data.getAmountFrom().isEmpty() && dataFromAmount<stmtAmount && stmtAmount<dataToAmount))
				return true;
		}
		return false;

	}
}
