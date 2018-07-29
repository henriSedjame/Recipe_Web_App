package com.formation.udemy.spring.recipe_app.Utils.FileUtils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Project recipe_app
 * @Author Henri Joel SEDJAME
 * @Date 29/07/2018
 * @Class purposes : .......
 */
@Component
public class FileUtils {

  public Byte[] getBytes(MultipartFile file) throws IOException {

    byte[] fileBytes = file.getBytes();

    Byte[] bytes = new Byte[fileBytes.length];

    int i = 0;
    for (byte b : fileBytes) bytes[i++] = b;

    return bytes;
  }


}
