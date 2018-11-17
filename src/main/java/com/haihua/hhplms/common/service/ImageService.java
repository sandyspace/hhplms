package com.haihua.hhplms.common.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String uploadImg(MultipartFile file, String subDir);
}
