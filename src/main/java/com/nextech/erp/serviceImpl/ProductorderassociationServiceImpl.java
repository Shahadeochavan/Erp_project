package com.nextech.erp.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.nextech.erp.dao.ProductorderassociationDao;
import com.nextech.erp.model.Productorderassociation;
import com.nextech.erp.service.ProductorderassociationService;

public class ProductorderassociationServiceImpl extends CRUDServiceImpl<Productorderassociation> implements
		ProductorderassociationService {
@Autowired
ProductorderassociationDao productorderassociationDao;
	
	@Override
	public Productorderassociation getProductorderassociationByProdcutOrderIdandProdcutId(
			long pOrderId, long pId) throws Exception {
		return productorderassociationDao.getProductorderassociationByProdcutOrderIdandProdcutId(pOrderId, pId);
	}

	@Override
	public List<Productorderassociation> getProductorderassociationByProdcutId(
			long pId) throws Exception {
		return productorderassociationDao.getProductorderassociationByProdcutId( pId);
	}

	@Override
	public List<Productorderassociation> getProductorderassociationByOrderId(
			long orderId) throws Exception {
		return productorderassociationDao.getProductorderassociationByOrderId(orderId);
	}

}
