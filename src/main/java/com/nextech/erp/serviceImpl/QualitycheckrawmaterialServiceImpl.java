package com.nextech.erp.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nextech.erp.dao.QualitycheckrawmaterialDao;
import com.nextech.erp.model.Qualitycheckrawmaterial;
import com.nextech.erp.service.QualitycheckrawmaterialService;
@Service
public class QualitycheckrawmaterialServiceImpl extends CRUDServiceImpl<Qualitycheckrawmaterial> implements QualitycheckrawmaterialService {
	
	@Autowired
	QualitycheckrawmaterialDao qualitycheckrawmaterialDao;

	@Override
	public Qualitycheckrawmaterial getQualitycheckrawmaterialByInvoiceIdAndRMId(long invoiceId, long rmID) throws Exception {
		
		return qualitycheckrawmaterialDao.getQualitycheckrawmaterialByInvoiceIdAndRMId(invoiceId, rmID);
	}
}
