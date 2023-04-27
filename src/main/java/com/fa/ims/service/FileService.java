package com.fa.ims.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    String saveFile(MultipartFile file, String email);

    Resource loadFile(String path);
}
