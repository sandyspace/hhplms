package com.haihua.hhplms.common.service;

import com.haihua.hhplms.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

@Service("imageService")
public class ImageServiceImpl implements ImageService {
    @Value("${image.upload.path}")
    private String imgUploadPath;

    public String uploadImg(MultipartFile file, String subDir) {
        if (file == null) {
            return null;
        }
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        fileName = Calendar.getInstance().getTimeInMillis() + suffixName;
        String filePath = imgUploadPath + subDir + File.pathSeparator + fileName;
        File dest = new File(filePath);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        }catch (IllegalStateException e) {
            throw new ServiceException(e);
        } catch (IOException e) {
            throw new ServiceException(e);
        }
        return  subDir + File.pathSeparator + fileName;
    }
}
