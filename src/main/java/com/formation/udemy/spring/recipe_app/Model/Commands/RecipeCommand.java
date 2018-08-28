package com.formation.udemy.spring.recipe_app.Model.Commands;

import com.formation.udemy.spring.recipe_app.Model.Enumerations.Difficulty;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeCommand {
  private Long id;
  @NotBlank
  @Size(min = 3, max = 255)
  private String description;
  @Min(1)
  @Max(999)
  private Integer prepTime;
  @Min(1)
  @Max(999)
  private Integer cookTime;
  @Min(1)
  @Max(999)
  private Integer servings;
  private String source;
  @URL
  private String url;
  @NotBlank
  private String directions;
  private Byte[] image;
  private Set<IngredientCommand> ingredients = new HashSet<>();
  private Difficulty difficulty;
  private NotesCommand notes;
  private Set<CategoryCommand> categories = new HashSet<>();
}
