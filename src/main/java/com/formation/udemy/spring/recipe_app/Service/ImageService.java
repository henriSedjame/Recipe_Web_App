package com.formation.udemy.spring.recipe_app.Service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Project recipe_app
 * @Author Henri Joel SEDJAME
 * @Date 29/07/2018
 * @Class purposes : .......
 */
public interface ImageService {

  void saveImageFile(Long id, MultipartFile file);
}
