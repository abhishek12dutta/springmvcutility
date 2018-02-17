package com.abhi.report.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.abhi.report.model.FTCQueueDetailsForm;
import com.abhi.report.model.FTCTransactionDetailsForm;

public class FTCTxDetailMapper implements RowMapper<FTCTransactionDetailsForm> {

/*select f.FTC_TX_LOG_DATE,f.TX_ID,f.PNR,f.QUEUED,f.IS_NDC_BOOKING,f.TX_DATE_TIME,
f.BOOKING_TYPE,
f.SUCCESS_STATUS,
f.ADD_COLLECTION_AMOUNT,
f.REFUNDED_AMOUNT,
f.CURRENCY,
f.LOG_FILE_NAME,
f.ENVIRONMENT,
q.QUEUE_REASON,q.QUEUE_OFFICE_ID ,q.QUEUE_POS,q.QUEUE_NUMBER 
from APP.FTC_Transaction_Table f left outer join APP.FTC_Queue_Info_Table q on 
f.TX_ID = q.QUEUED_TX_ID and f.PNR = q.QUEUED_PNR
where FTC_TX_LOG_DATE='08-01-2018' and ENVIRONMENT='Live';*/
	@Override
	public FTCTransactionDetailsForm mapRow(ResultSet rs, int rownum)
			throws SQLException {
		boolean isQueued=rs.getString("QUEUED").equals("Y");
		FTCTransactionDetailsForm detailsForm=new FTCTransactionDetailsForm();	
		detailsForm.setFtcTxLogDate(rs.getString("FTC_TX_LOG_DATE"));
	    detailsForm.setTxId(rs.getString("TX_ID"));
	    detailsForm.setPnr(rs.getString("PNR"));
	    detailsForm.setQueued(isQueued);
        detailsForm.setNdcBooking(rs.getString("IS_NDC_BOOKING").equals("Y"));
        detailsForm.setTxDateTime(rs.getString("TX_DATE_TIME"));
        detailsForm.setTypeOfBooking(rs.getString("BOOKING_TYPE"));
        detailsForm.setSuccessStatus(rs.getString("SUCCESS_STATUS").equals("Y"));
        detailsForm.setAddtionalCollectionAmount(rs.getString("ADD_COLLECTION_AMOUNT"));
        detailsForm.setRefundAmount(rs.getString("REFUNDED_AMOUNT"));
        detailsForm.setCurrency(rs.getString("CURRENCY"));
        detailsForm.setLogFilename(rs.getString("LOG_FILE_NAME"));
        detailsForm.setEnv(rs.getString("ENVIRONMENT"));
        if(isQueued){
        	FTCQueueDetailsForm  ftcQueueDetailsForm=new FTCQueueDetailsForm();
        	ftcQueueDetailsForm.setQueueReason(rs.getString("QUEUE_REASON"));
        	ftcQueueDetailsForm.setQueueOfficeId(rs.getString("QUEUE_OFFICE_ID"));
        	ftcQueueDetailsForm.setQueuePOS(rs.getString("QUEUE_POS"));
        	ftcQueueDetailsForm.setQueueNumber(rs.getString("QUEUE_NUMBER"));
            detailsForm.setFtcQueueDetailsForm(ftcQueueDetailsForm);
        }	
		return detailsForm;
	}

	
	
	
}
