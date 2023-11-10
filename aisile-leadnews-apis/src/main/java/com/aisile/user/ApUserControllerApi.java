package com.aisile.user;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

public interface ApUserControllerApi {
    @PostMapping("upload")
    public String uploadFile(@RequestPart MultipartFile file);
}
