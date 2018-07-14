package com.formation.udemy.spring.recipe_app.Model;

import lombok.*;

import javax.persistence.*;
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
@Builder
@Entity
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Version
  private int version;
  @Column(unique = true)
  private String description;
  @ManyToMany(mappedBy = "categories")
  private Set<Recipe> recipes;

  {
    recipes = new HashSet<>();
  }

}
