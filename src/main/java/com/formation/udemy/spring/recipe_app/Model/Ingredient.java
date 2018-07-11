package com.formation.udemy.spring.recipe_app.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Ingredient {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Version
  private int version;
  private String description;
  private BigDecimal amount;
  @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
  private UnitOfMeasure unitOfMeasure;
  @ManyToOne
  @JoinColumn(name = "recipe_id")
  private Recipe recipe;
}
