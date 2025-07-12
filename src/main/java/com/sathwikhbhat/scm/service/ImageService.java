package com.sathwikhbhat.scm.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Slf4j
@Service
public class ImageService {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadImage(MultipartFile contactImage) {
        try {
            String filename = UUID.randomUUID().toString();
            byte[] data = new byte[contactImage.getInputStream().available()];
            contactImage.getInputStream().read(data);
            cloudinary.uploader().upload(data, ObjectUtils.asMap(
                    "public_id", filename,
                    "resource_type", "image",
                    "overwrite", true
            ));
            return getUrlFromPublicId(filename);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public String getUrlFromPublicId(String publicId) {
        return cloudinary.url().transformation(
                        new Transformation<>()
                                .width(500)
                                .height(500)
                                .crop("fill"))
                .generate(publicId);
    }

}