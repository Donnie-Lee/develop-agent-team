package com.interviewai.resume.service;

import com.interviewai.common.BusinessException;
import com.interviewai.resume.config.OssConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Service
public class FileUploadService {

    @Autowired
    private OssConfig ossConfig;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final String[] ALLOWED_EXTENSIONS = {".pdf", ".doc", ".docx"};

    public String uploadFile(MultipartFile file) {
        // 验证文件大小
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BusinessException(1001, "文件大小不能超过10MB");
        }

        // 验证文件类型
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new BusinessException(1001, "文件名不能为空");
        }

        String extension = getFileExtension(originalFilename).toLowerCase();
        boolean isAllowed = false;
        for (String allowed : ALLOWED_EXTENSIONS) {
            if (allowed.equals(extension)) {
                isAllowed = true;
                break;
            }
        }
        if (!isAllowed) {
            throw new BusinessException(1001, "只支持PDF、Word格式的文件");
        }

        // 生成唯一文件名
        String uniqueFileName = UUID.randomUUID().toString() + extension;
        String objectName = "resumes/" + uniqueFileName;

        try {
            // TODO: 实际调用阿里云OSS上传
            // 这里简化处理，实际生产环境需要调用OSS SDK
            // InputStream inputStream = file.getInputStream();
            // ossClient.putObject(ossConfig.getBucketName(), objectName, inputStream);

            // 模拟返回URL
            String fileUrl = ossConfig.getDomain() + "/" + objectName;
            log.info("文件上传成功: originalFilename={}, fileUrl={}", originalFilename, fileUrl);

            return fileUrl;
        } catch (Exception e) {
            log.error("文件上传失败: {}", e.getMessage(), e);
            throw new BusinessException(4001, "文件上传失败: " + e.getMessage());
        }
    }

    private String getFileExtension(String filename) {
        int lastIndexOf = filename.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return filename.substring(lastIndexOf);
    }
}
