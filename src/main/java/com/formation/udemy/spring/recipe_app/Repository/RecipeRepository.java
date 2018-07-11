package com.formation.udemy.spring.recipe_app.Repository;

import com.formation.udemy.spring.recipe_app.Model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

/**
 * @Project recipe_app
 * @Author Henri Joel SEDJAME
 * @Date 11/07/2018
 */
public interface RecipeRepository extends JpaRepository<Recipe, Long>, QuerydslPredicateExecutor<Recipe> {
}
