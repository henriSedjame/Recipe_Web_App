package com.formation.udemy.spring.recipe_app.Model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * @Project recipe_app
 * @Author Henri Joel SEDJAME
 * @Date 14/07/2018
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"id", "description"})
@ToString(exclude = {"recipe"})
@Builder
@Document
public class Ingredient {
  @Id
  private String id;
  @NonNull
  private String description;
  @NonNull
  private BigDecimal amount;
  @NonNull
  @DBRef
  private UnitOfMeasure unitOfMeasure;
  @NonNull
  @DBRef
  private Recipe recipe;
}
