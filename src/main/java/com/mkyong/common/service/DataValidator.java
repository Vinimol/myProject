package com.mkyong.common.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
public class DataValidator implements Validator {
	private static final Logger logger = Logger.getLogger(DataValidator.class);
	private static final String ROLE_ADMIN = "ROLE_ADMIN";
	public DataValidator() {
		super();
	}

	@Override
	public String validateData(Data data) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		Set<String> authorities = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
		if (authorities.contains(ROLE_ADMIN)){
			if (( data.getAccId() == null || data.getAccId().isEmpty()) ) 
				return Constansts.ACCOUNT_ID;
			else if ((!isInteger(data.getAccId())) )
				return Constansts.VALID_ACCOUNT;
			else if ((data.getDateFrom() != null && !data.getDateFrom().isEmpty()) && !isValidDate(data.getDateFrom()))
				return Constansts.DATE_FROM;
			else if ((data.getDateTo() != null && !data.getDateTo().isEmpty())&& !isValidDate(data.getDateTo())) 
				return Constansts.DATE_TO;
			else if ((data.getAmountFrom() != null && !data.getAmountFrom().isEmpty()) && !StringUtils.isNumeric(data.getAmountFrom())) 
				return Constansts.AMOUNT_FROM;
			else if(data.getAmountFrom().isEmpty() && !data.getAmountTo().isEmpty())
				return Constansts.AMOUNT_FROM_NEEDED;
			else if(!data.getAmountFrom().isEmpty() && data.getAmountTo().isEmpty())
				return Constansts.AMOUNT_TO_NEEDED;
			else if(!data.getDateFrom().isEmpty() && data.getDateTo().isEmpty())
				return Constansts.DATE_TO_NEEDED;
			else if(data.getDateFrom().isEmpty() && !data.getDateTo().isEmpty())
				return Constansts.DATE_FROM_NEEDED;
			else if ((data.getAmountTo() != null && !data.getAmountTo().isEmpty()) && !StringUtils.isNumeric(data.getAmountTo()) )
				return Constansts.AMOUNT_TO;
			else if((data.getAmountTo() != null && !data.getAmountTo().isEmpty()) && Integer.parseInt(data.getAmountFrom()) > Integer.parseInt(data.getAmountTo()))
				return Constansts.AMOUNT_FROM_AND_AMOUNT_TO;
		}
		if(authorities.contains("ROLE_USER") && !data.getAccId().isEmpty() || !data.getAmountFrom().isEmpty() || !data.getAmountTo().isEmpty() || !data.getDateFrom().isEmpty() || !data.getDateTo().isEmpty())
				return "403";
		return null;
	}

	private boolean isValidDate(String dateFrom) {
		Date date = null;
		if(dateFrom.isEmpty())
			return false;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			date = sdf.parse(dateFrom);
			if (!dateFrom.equals(sdf.format(date))) {
				date = null;
			}
		} catch (ParseException ex) {
			logger.error("Date parsing failed");
		}
		if (date == null) {
			return false;
		} else {
			return true;
		}
	}
	public static boolean isInteger(String s) {
		try { 
			Integer.parseInt(s); 
		} catch(NumberFormatException e) { 
			return false; 
		} catch(NullPointerException e) {
			return false;
		}
		// only got here if we didn't return false
		return true;
	}

}
