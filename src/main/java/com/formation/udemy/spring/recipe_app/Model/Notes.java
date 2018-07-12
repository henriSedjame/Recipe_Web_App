package com.formation.udemy.spring.recipe_app.Model;

import lombok.*;

import javax.persistence.*;

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
