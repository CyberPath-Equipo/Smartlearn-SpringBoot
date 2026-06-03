package com.cyberpath.smartlearn.servicio.servicio.recurso;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImagenesServicio {
    String uploadFile(MultipartFile file) throws IOException;
}
