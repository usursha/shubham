package com.hpy.ops360.dashboard;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hpy.ops360.dashboard.repository.PortalPrivacyPolicyRepository;
import com.hpy.ops360.dashboard.service.PortalPrivacyPolicyService;


@ExtendWith(MockitoExtension.class)
public class PortalPrivacyPolicyTest {

@Mock
private PortalPrivacyPolicyRepository Repo;

@InjectMocks
private PortalPrivacyPolicyService policyService;


@Test

public void testGetPolicyValues(){
	
}

	
}