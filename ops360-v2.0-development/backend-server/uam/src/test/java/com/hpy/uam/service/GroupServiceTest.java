//package com.hpy.uam.service;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.List;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.keycloak.admin.client.Keycloak;
//import org.keycloak.admin.client.resource.GroupResource;
//import org.keycloak.admin.client.resource.GroupsResource;
//import org.keycloak.representations.idm.GroupRepresentation;
//import org.mockito.InjectMocks;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import com.hpy.uam.dto.GroupRegistrationRequestDto;
//import com.hpy.uam.util.MethodUtil;
//
//import jakarta.ws.rs.core.Response;
//
//@ExtendWith(MockitoExtension.class)
//class GroupServiceTest {
//	
//    private Keycloak keycloak;
//	private MethodUtil methodUtil;
//	private GroupResource groupResource;
//	private GroupsResource groupsResource;
//	@InjectMocks
//	private GroupService groupService;
//	
//	
//	@BeforeEach
//	void setUp() {
//		keycloak=mock(Keycloak.class);
//		groupResource=mock(GroupResource.class);
//		groupsResource=mock(GroupsResource.class);
//		methodUtil=mock(MethodUtil.class);
//		groupService=new GroupService(keycloak, methodUtil);
//	}
//	
//	@Test //passed
//	void testCreateGroup() {
//		String groupName="weufh8-75hgrhe-rcuj9r";
//		GroupRegistrationRequestDto groupRegistration=new GroupRegistrationRequestDto();
//		groupRegistration.setName(groupName);
//		GroupRepresentation group=new GroupRepresentation();
//		group.setName(groupRegistration.getName());
//		when(methodUtil.getGroupsResource()).thenReturn(groupsResource);
//		Response mockResponse=mock(Response.class);
//		when(mockResponse.getStatus()).thenReturn(201);
//		when(groupsResource.add(any())).thenReturn(mockResponse);
//		Integer responseStatus=groupService.createGroup(groupRegistration);
//		assertEquals(Integer.valueOf(201),responseStatus);
//		verify(groupsResource).add(any(GroupRepresentation.class));
//	}
//	
//	@Test //passed
//	void testGetGroupById() {
//		String groupId="weufh8-75hgrhe-rcuj9r";
//		String name="my-group";
//		GroupRepresentation group=new GroupRepresentation();
//		group.setId(groupId);
//		group.setName(name);
//		when(methodUtil.getGroupsResource()).thenReturn(groupsResource);
//		when(groupsResource.group(groupId)).thenReturn(groupResource);
//		when(groupResource.toRepresentation()).thenReturn(group);
//		GroupRepresentation result=groupService.getGroupById(groupId);
//		assertEquals(group.getId(),result.getId());
//		assertEquals(group.getName(),result.getName());
//	}
//	
//	
//	@Test //passed
//	void testGetAllGroup() {
//		GroupRepresentation group1=new GroupRepresentation();
//		GroupRepresentation group2=new GroupRepresentation();
//		List<GroupRepresentation> groupList=List.of(group1, group2);
//		when(methodUtil.getGroupsResource()).thenReturn(groupsResource);
//		when(groupsResource.groups(0,100)).thenReturn(groupList);
//		assertEquals(2,groupService.getAllGroup().size());
//	}
//	
//	@Test
//	void testDeleteGroup(){
//		String groupId="weufh8-75hgrhe-rcuj9r";
//		when(methodUtil.getGroupsResource()).thenReturn(groupsResource);
//		when(groupsResource.group(groupId)).thenReturn(groupResource);
//		doNothing().when(groupResource).remove();
//
//        // Call the service method
//        groupService.deleteGroup(groupId);
// 
//        // Verify that the correct methods were called
//        verify((methodUtil).getGroupsResource().group(groupId),times(1)).remove();
//		
//	}
//}
