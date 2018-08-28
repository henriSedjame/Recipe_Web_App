package com.formation.udemy.spring.recipe_app.Repository;

import com.formation.udemy.spring.recipe_app.Model.Category;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @Project recipe_app
 * @Author Henri Joel SEDJAME
 * @Date 14/07/2018
 * @Class purposes : Classe de test d'intégration pour la classe CategoryRepository
 */
@RunWith(SpringRunner.class)
@DataMongoTest
@Ignore
public class CategoryRepositoryIT {
  @Autowired
  CategoryRepository categoryRepository;

  @Test
  public void findByDescription() {

    Optional<Category> catOptional = categoryRepository.findByDescription("American");

    assertTrue(catOptional.isPresent());
    if (catOptional.isPresent()){
      assertEquals("American", catOptional.get().getDescription());
    }
  }
}
