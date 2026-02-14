package com.hpy.ops360.dashboard.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ATMIndexDetails {

	@Id
	private Long srNo;
	private String atmId;

	private String atmVpiId;
	private String psid;
	private String paid;
	private String serialNo;
	private String bankAccount;
	private String sourcingStrategy;
	private String siteType;
	private String siteClassification;
	private String siteCategory;

	private String projectType;

	private String atmCrmUpi;

	private String atmOem;
	private String techLiveDate;

	private String cashLiveDate;

	private String atmHandoverDate;
	private String closureDate;
	private String address;
	private String city;
	private String pincode;
	private String district;
	private String state;
	private String siteLatitude;
	private String siteLongitude;
	private String circle;
	
    private String siteName;
    private String atmStatus;
    private String parentBranch;
    private String siteAccessHours;
    private String landlordName;
    private String contactNumber;
    private String landlordCity;
    private String landlordEmailId;
    
    private String parentCity;
    private String parentPinCode;
    private String parentDistrict;
    private String parentAddress;
    private String parentState;
    private String parentCircle;
	


}
