package com.fa.ims.service;

import com.fa.ims.dto.OfferRecordDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;


public interface ExcelService {
    void exportOffersToExcel(List<OfferRecordDto> offers, OutputStream outputStream) throws IOException;

    void importJobsFromExcel(MultipartFile file) throws IOException;
}
