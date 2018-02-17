package com.abhi.report.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.abhi.report.model.FTCAvailableDateEnvForm;

public class FTCAvailableDateEnvFormMapper implements RowMapper<FTCAvailableDateEnvForm> {
	/**
	 * PROCESS_DATA_DATE varchar(20), ENVIRONMENT varchar(20),
	 * PROCESS_DATA_INDICATOR VARCHAR(1), PROCESSED_BY varchar(20) not null,
	 * PROCESSED_DATE_TIME DATE not null,
	 */
	@Override
	public FTCAvailableDateEnvForm mapRow(ResultSet rs, int rowNum) throws SQLException {
		FTCAvailableDateEnvForm availableDateEnvForm = new FTCAvailableDateEnvForm();
		availableDateEnvForm.setFtcdate(rs.getString("PROCESS_DATA_DATE"));
		availableDateEnvForm.setEnvironment(rs.getString("ENVIRONMENT"));
		availableDateEnvForm.setProcessById(rs.getString("PROCESSED_BY"));
		availableDateEnvForm.setProcesseddateTime(rs.getDate("PROCESSED_DATE_TIME"));
		return availableDateEnvForm;
	}

}
