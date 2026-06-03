package com.cyberpath.smartlearn.servicio.implementacion.recurso;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.cyberpath.smartlearn.servicio.servicio.recurso.ImagenesServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class ImagenesImpl implements ImagenesServicio {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadFile(MultipartFile file) throws IOException {

        Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.emptyMap()
        );

        return uploadResult.get("secure_url").toString();
    }
}