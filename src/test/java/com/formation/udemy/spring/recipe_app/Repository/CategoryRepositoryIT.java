package com.formation.udemy.spring.recipe_app.Repository;

import com.formation.udemy.spring.recipe_app.Model.Category;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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
@DataJpaTest
public class CategoryRepositoryIT {
  @Autowired
  CategoryRepository categoryRepository;

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void findByDescription() {

    Optional<Category> catOptional = categoryRepository.findByDescription("American");

    assertTrue(catOptional.isPresent());
    assertEquals("American", catOptional.get().getDescription());
  }
}
