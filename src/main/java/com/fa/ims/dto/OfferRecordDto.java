package com.fa.ims.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfferRecordDto {
    private Long offerId;
    private String candidateName;
    private String email;
    private String approve;
    private String department;
    private String note;
    private String status;
}
