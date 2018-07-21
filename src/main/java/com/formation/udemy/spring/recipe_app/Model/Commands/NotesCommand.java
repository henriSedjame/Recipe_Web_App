package com.formation.udemy.spring.recipe_app.Model.Commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NotesCommand {
    private Long id;
    private String recipeNotes;
  private RecipeCommand recipe;
}
