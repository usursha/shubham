package com.hpy.mappingservice.automapping.requestDto;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ATMAutoAssignRequestDTO {
    private Long leaveId;
    private String mappedType;
    private List<ATMAutoAssignEntryDTO> mappings;
}