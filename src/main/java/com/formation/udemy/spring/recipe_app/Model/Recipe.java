package com.formation.udemy.spring.recipe_app.Model;

import com.formation.udemy.spring.recipe_app.Model.Enumerations.Difficulty;
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
@ToString(exclude = {"notes", "ingredients", "categories"})
@Builder
@Document
public class Recipe {
  @Id
  private String id;
  private Integer prepTime;
  private Integer cookTime;
  private Integer servings;
  private String source;
  private String url;
  String description;
  private String directions;
  private Difficulty difficulty;
  private Byte[] image;
  @DBRef
  private Notes notes;
  @DBRef
  private Set<Ingredient> ingredients = new HashSet<>();
  @DBRef
  private Set<Category> categories = new HashSet<>();
}
