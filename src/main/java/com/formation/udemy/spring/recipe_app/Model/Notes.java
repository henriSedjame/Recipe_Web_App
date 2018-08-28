package com.formation.udemy.spring.recipe_app.Model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Project recipe_app
 * @Author Henri Joel SEDJAME
 * @Date 14/07/2018
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "recipeNotes"})
@ToString(exclude = {"recipe"})
@Builder
@Document
public class Notes {
  @Id
  private String id;
  private String recipeNotes;
  @DBRef
  private Recipe recipe;
}
