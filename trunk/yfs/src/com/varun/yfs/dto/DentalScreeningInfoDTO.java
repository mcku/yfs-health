package com.varun.yfs.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;


public class DentalScreeningInfoDTO extends BaseModelData
{
	private static final long serialVersionUID = -2090915894989047979L;
	private long id;
	private String findings;
	private String treatmentAdviced;
	private String referral;
	private String medicine;

	public String getFindings()
	{
		return findings;
	}

	public void setFindings(String findings)
	{
		set("DentalFindings",findings);
		this.findings = findings;
	}

	public String getTreatmentAdviced()
	{
		return treatmentAdviced;
	}

	public void setTreatmentAdviced(String treatmentAdviced)
	{
		set("DentalTreatment",treatmentAdviced);
		this.treatmentAdviced = treatmentAdviced;
	}


	public void setMedicine(String medicine)
	{
		set("DentalMedicine",medicine);
		this.medicine = medicine;
	}

	public String getMedicine()
	{
		return medicine;
	}

	public void setReferral(String referral)
	{
		set("DentalReferral",referral);
		this.referral = referral;
	}

	public String getReferral()
	{
		return referral;
	}

	public void setId(long id)
	{
		set("DentalScreeningInfoId",id);
		this.id = id;
	}

	public long getId()
	{
		return id;
	}

}
