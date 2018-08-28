package com.formation.udemy.spring.recipe_app.Repository;

import com.formation.udemy.spring.recipe_app.Model.UnitOfMeasure;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * @Project recipe_app
 * @Author Henri Joel SEDJAME
 * @Date 11/07/2018
 */
public interface UnitOfMeasureRepository extends CrudRepository<UnitOfMeasure, String>, QuerydslPredicateExecutor<UnitOfMeasure> {
    Optional<UnitOfMeasure> findByDescription(String description);
}
