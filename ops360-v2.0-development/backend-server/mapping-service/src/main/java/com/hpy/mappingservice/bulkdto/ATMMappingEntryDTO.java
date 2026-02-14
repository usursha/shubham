package com.hpy.mappingservice.bulkdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ATMMappingEntryDTO {
    private Long ceId;
    private Long tempMappedCeId;
    private String atmList; // comma-separated ATM codes
}
