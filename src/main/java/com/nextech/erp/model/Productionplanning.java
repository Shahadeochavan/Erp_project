package com.nextech.erp.model;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import java.util.List;
import java.sql.Timestamp;


/**
 * The persistent class for the productionplanning database table.
 * 
 */
@Entity
@NamedQuery(name="Productionplanning.findAll", query="SELECT p FROM Productionplanning p")
public class Productionplanning implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	private long id;

	@Column(name="achived_quantity")
	private int achivedQuantity;

	@Column(name="created_by")
	private long createdBy;

	@Column(name="created_date")
	private Timestamp createdDate;

	@Temporal(TemporalType.DATE)
	private Date date;

	@Column(name="dispatch_quantity")
	private int dispatchQuantity;

	@Column(name="excess_quantity")
	private int excessQuantity;

	private boolean isactive;

	@Column(name="lag_quantity")
	private int lagQuantity;

	@Column(name="target_quantity")
	private int targetQuantity;

	@Column(name="updated_by")
	private int updatedBy;

	@Column(name="updated_date")
	private Timestamp updatedDate;

	//bi-directional many-to-one association to Product
	@ManyToOne
	@JoinColumn(name="productid")
	private Product product;
	
	private String remark;
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "productionplanning", cascade = CascadeType.ALL)
	private List<Productquality> productqualities;


	public Productionplanning() {
	}

	public Productionplanning(int id) {
		this.id=id;
	}
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getAchivedQuantity() {
		return this.achivedQuantity;
	}

	public void setAchivedQuantity(int achivedQuantity) {
		this.achivedQuantity = achivedQuantity;
	}

	public long getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(long createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getDispatchQuantity() {
		return this.dispatchQuantity;
	}

	public void setDispatchQuantity(int dispatchQuantity) {
		this.dispatchQuantity = dispatchQuantity;
	}

	public int getExcessQuantity() {
		return this.excessQuantity;
	}

	public void setExcessQuantity(int excessQuantity) {
		this.excessQuantity = excessQuantity;
	}

	public boolean getIsactive() {
		return this.isactive;
	}

	public void setIsactive(boolean isactive) {
		this.isactive = isactive;
	}

	public int getLagQuantity() {
		return this.lagQuantity;
	}

	public void setLagQuantity(int lagQuantity) {
		this.lagQuantity = lagQuantity;
	}

	public int getTargetQuantity() {
		return this.targetQuantity;
	}

	public void setTargetQuantity(int targetQuantity) {
		this.targetQuantity = targetQuantity;
	}

	public int getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	public List<Productquality> getProductqualities() {
		return this.productqualities;
	}

	public void setProductqualities(List<Productquality> productqualities) {
		this.productqualities = productqualities;
	}

	public Productquality addProductquality(Productquality productquality) {
		getProductqualities().add(productquality);
		productquality.setProductionplanning(this);

		return productquality;
	}

	public Productquality removeProductquality(Productquality productquality) {
		getProductqualities().remove(productquality);
		productquality.setProductionplanning(null);

		return productquality;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}