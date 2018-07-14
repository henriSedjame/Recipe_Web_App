package com.formation.udemy.spring.recipe_app.Model;

import lombok.*;

import javax.persistence.*;

/**
 * @Project recipe_app
 * @Author Henri Joel SEDJAME
 * @Date 14/07/2018
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "recipeNotes"})
@Builder
@Entity
public class Notes {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Version
  private int version;
  @Lob
  private String recipeNotes;
  @OneToOne
  private Recipe recipe;
}
