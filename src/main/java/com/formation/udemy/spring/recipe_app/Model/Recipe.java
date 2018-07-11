package com.formation.udemy.spring.recipe_app.Model;

import com.formation.udemy.spring.recipe_app.Model.Enumerations.Difficulty;
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
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private int version;
    private int prepTime;
    private int cookTime;
    private int servings;
    private String source;
    private String url;
    private String directions;
  @Enumerated(value = EnumType.STRING)
  private Difficulty difficulty;
  @Lob
  private Byte[] image;
  @OneToOne(cascade = CascadeType.ALL)
  private Notes note;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "recipe")
  private Set<Ingredient> ingredients;
  @ManyToMany
  @JoinTable(name = "recipe_category",
    joinColumns = @JoinColumn(name = "recipe_id"),
    inverseJoinColumns = @JoinColumn(name = "category_id"))
  private Set<Category> categories;

  {
    ingredients = new HashSet<>();
    categories = new HashSet<>();
  }
}
