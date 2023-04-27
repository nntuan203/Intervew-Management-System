package com.fa.ims.dto;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CandidateDetailDto {
        private String candidateFullname;

        private String candidateEmail;

        private String candidateBirth;

        private String candidateAddress;

        private String candidatePhone;

        private String candidateGender;

        private String uriPath;

        private String fileName;

        private String candidateStatus;

        private String positionCandidate;

        private Long candidateYoe;

        private String candidateSkills;

        private String highestLevel;

        private String userRecruiter;

        private String candidateNote;


}

