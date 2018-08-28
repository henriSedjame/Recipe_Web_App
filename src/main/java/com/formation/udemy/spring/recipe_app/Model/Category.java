package com.formation.udemy.spring.recipe_app.Model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

/**
 * @Project recipe_app
 * @Author Henri Joel SEDJAME
 * @Date 14/07/2018
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "description"})
@ToString(exclude = {"recipes"})
@Builder
@Document
public class Category {
  @Id
  private String id;
  private String description;
  @DBRef
  private Set<Recipe> recipes = new HashSet<>();
}
