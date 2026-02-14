package com.hpy.ops360.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class ATMIndexDetailsDTO extends GenericDto {

	private static final long serialVersionUID = 1L;

//	@JsonIgnore
//	private Long id;
//
//	private String atmId;
//	private String psid;
//	private String serialNo;
//	private String bankAccount;
//	private String siteCategory;
//	private String techLiveDate;
//	private String closureDate;
//	private String address;
//	private String city;
//	private String pincode;
//	private String district;
//	private String state;
//	private String siteLatitude;
//	private String siteLongitude;
//	private String circle;
//
//}


	@JsonIgnore 
	private Long id; 
	
	private String atmId;
	private String psid;
	private String serialNo;
	private String bankAccount;
	private String siteCategory;
	private String techLiveDate;
	private String closureDate;
	private String address;
	private String city;
	private String pincode;
	private String district;
	private String state;
	private String siteLatitude;
	private String siteLongitude;
	private String circle;
	
	private String atmVpiId;
	private String paid;
	private String sourcingStrategy;
	private String siteType;
	private String siteClassification;
	private String projectType;
	private String atmCrmUpi;
	private String atmOem;
	private String cashLiveDate;
	private String atmHandoverDate;
 // private Long srNo; 
	
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
