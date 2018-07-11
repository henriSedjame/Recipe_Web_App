package com.formation.udemy.spring.recipe_app.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private int version;
  private String description;
  @ManyToMany(mappedBy = "categories")
  private Set<Recipe> recipes;

  {
    recipes = new HashSet<>();
  }
}
