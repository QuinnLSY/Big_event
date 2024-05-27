package com.cjx.controller;

import com.cjx.pojo.Result;
import com.cjx.utils.AliOssUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
/**
 * 文件上传控制器
 */
@RestController
public class FileUploadController {
    /**
     * 上传文件到OSS（阿里云对象存储服务）
     * @param file 用户上传的文件
     * @return 返回文件的URL地址
     * @throws Exception 抛出异常，处理文件上传过程中的错误
     */
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws Exception {
        //把文件的内容存储到本地磁盘上
        String originalFilename = file.getOriginalFilename();
        //保证文件的名字是唯一的,从而防止文件覆盖
        String filename = UUID.randomUUID().toString()+originalFilename.substring(originalFilename.lastIndexOf("."));
        //file.transferTo(new File("C:\\Users\\Administrator\\Desktop\\files\\"+filename));
        // 使用阿里云OSS工具上传文件，并获取文件的URL地址
        String url = AliOssUtil.uploadFile(filename,file.getInputStream());
        return Result.success(url);
    }
}
