package com.hpy.ops360.dashboard.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class PrivacyPolicyDto implements Serializable {

	private static final long serialVersionUID = -8895848828483086806L;
	private String privacyPolicy;
	private String infoCollectionAndUser;
	private String dataSecurity;
	private String sharingOFInformation;
}
