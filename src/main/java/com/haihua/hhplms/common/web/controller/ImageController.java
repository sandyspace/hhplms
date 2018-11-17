package com.haihua.hhplms.common.web.controller;

import com.haihua.hhplms.common.model.ResultBean;
import com.haihua.hhplms.common.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ImageController {
    @Autowired
    @Qualifier("imageService")
    private ImageService uploadService;

    @PostMapping(path = "/api/uploadImg", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<String> uploadImg(@RequestParam("file") MultipartFile file, String subDir) {
        String url = uploadService.uploadImg(file, subDir);
        return ResultBean.Success.of(url, "");
    }
}
