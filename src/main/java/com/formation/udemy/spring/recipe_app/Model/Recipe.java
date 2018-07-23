package com.formation.udemy.spring.recipe_app.Model;

import com.formation.udemy.spring.recipe_app.Model.Enumerations.Difficulty;
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
@ToString(exclude = {"notes", "ingredients", "categories"})
@Builder
@Entity
public class Recipe {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Version
  private int version;
  private Integer prepTime;
  private Integer cookTime;
  private Integer servings;
  private String source;
  private String url;
  @Column(unique = true)
  String description;
  @Lob
  private String directions;
  @Enumerated(value = EnumType.STRING)
  private Difficulty difficulty;
  @Lob
  private Byte[] image;
  @OneToOne(cascade = CascadeType.ALL)
  private Notes notes;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "recipe")
  private Set<Ingredient> ingredients = new HashSet<>();
  @ManyToMany
  @JoinTable(name = "recipe_category",
          joinColumns = @JoinColumn(name = "recipe_id"),
          inverseJoinColumns = @JoinColumn(name = "category_id"))
  private Set<Category> categories = new HashSet<>();
}
