package com.mkyong.common.dao;

import java.text.ParseException;
import java.util.List;

import com.mkyong.common.service.Data;

public interface StatementDao {
	public List<AccountStatement> getStatementList(Data data) throws ParseException;

}
