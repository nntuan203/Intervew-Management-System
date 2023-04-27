package com.fa.ims.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserOfferDto {
    private Long id;
    private String userName;
}
