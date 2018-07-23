package com.formation.udemy.spring.recipe_app.Model;

import lombok.*;

import javax.persistence.*;
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
@Entity
public class Ingredient {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Version
  private int version;
  @NonNull
  @Column(unique = true)
  private String description;
  @NonNull
  private BigDecimal amount;
  @NonNull
  @OneToOne(fetch = FetchType.EAGER)
  private UnitOfMeasure unitOfMeasure;
  @NonNull
  @ManyToOne
  @JoinColumn(name = "recipe_id")
  private Recipe recipe;
}
