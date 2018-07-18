package com.formation.udemy.spring.recipe_app.Boostrap;


import com.formation.udemy.spring.recipe_app.Model.*;
import com.formation.udemy.spring.recipe_app.Model.Enumerations.Difficulty;
import com.formation.udemy.spring.recipe_app.Repository.CategoryRepository;
import com.formation.udemy.spring.recipe_app.Repository.RecipeRepository;
import com.formation.udemy.spring.recipe_app.Repository.UnitOfMeasureRepository;
import com.formation.udemy.spring.recipe_app.Utils.BidirectionnalSetterHelper.BidirectionnalSetterHelper;
import com.formation.udemy.spring.recipe_app.Utils.SetHelper.SetHelper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Project recipe_app
 * @Author Henri Joel SEDJAME
 * @Date 14/07/2018
 */
@Component
@Transactional
public class RecipeBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final CategoryRepository categoryRepository;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    @Qualifier("bidirectionnalSetterHelper")
    private final BidirectionnalSetterHelper biSetterHelper;
    @Qualifier("addRemoveSetHelper")
    private final SetHelper setHelper;
    @Value("${recipe.guac.directions}")
    private String guacDirections;
    @Value("${recipe.guac.notes}")
    private String recipeGuacNotes;
    @Value("${recipe.tacos.directions}")
    private String tacosDirections;
    @Value("${recipe.tacos.notes}")
    private String recipeTacosNotes;

    public RecipeBootstrap(CategoryRepository categoryRepository, RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository, BidirectionnalSetterHelper biSetterHelper, SetHelper setHelper) {
        this.categoryRepository = categoryRepository;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.biSetterHelper = biSetterHelper;
        this.setHelper = setHelper;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        recipeRepository.saveAll(getRecipes());
    }

    private List<Recipe> getRecipes() {

        List<Recipe> recipes = new ArrayList<>(2);

        //get UOMs
        UnitOfMeasure eachUom = getUnitOfMeasure("Each");
        UnitOfMeasure tableSpoonUom = getUnitOfMeasure("Tablespoon");
        UnitOfMeasure teaSpoonUom = getUnitOfMeasure("Teaspoon");
        UnitOfMeasure dashUom = getUnitOfMeasure("Dash");
        UnitOfMeasure pintUom = getUnitOfMeasure("Pint");
        UnitOfMeasure cupsUom = getUnitOfMeasure("Cup");

        //get Categories
        Category americanCategory = getCategory("American");
        Category mexicanCategory = getCategory("Mexican");

        //Yummy Guac
        Notes guacNotes = new Notes();
        guacNotes.setRecipeNotes(recipeGuacNotes);

        Recipe guacRecipe = new Recipe();
        guacRecipe.setDescription("Perfect Guacamole");
        guacRecipe.setPrepTime(10);
        guacRecipe.setCookTime(0);
        guacRecipe.setDifficulty(Difficulty.EASY);
        guacRecipe.setDirections(guacDirections);
        this.biSetterHelper.bidirectionnalSet(guacRecipe, guacNotes);

      //get ingredients
        List<Ingredient> ingredients = getGuacIngredients(eachUom, tableSpoonUom, teaSpoonUom, dashUom, guacRecipe);

        this.setHelper.addToSet(guacRecipe, "ingredients", ingredients);
        this.setHelper.addToSet(guacRecipe, "categories", Arrays.asList(americanCategory, mexicanCategory));

        //add to return list
        recipes.add(guacRecipe);

        //Yummy Tacos
        Notes tacoNotes = new Notes();
        tacoNotes.setRecipeNotes(recipeTacosNotes);
        Recipe tacosRecipe = new Recipe();
        tacosRecipe.setDescription("Spicy Grilled Chicken Taco");
        tacosRecipe.setCookTime(9);
        tacosRecipe.setPrepTime(20);
        tacosRecipe.setDifficulty(Difficulty.MODERATE);
        tacosRecipe.setDirections(tacosDirections);
        this.biSetterHelper.bidirectionnalSet(tacosRecipe, tacoNotes);

        // get ingredients
        List<Ingredient> tacosIngredients = getTacosIngredients(eachUom, tableSpoonUom, teaSpoonUom, pintUom, cupsUom, tacosRecipe);

        this.setHelper.addToSet(tacosRecipe, "ingredients", tacosIngredients);
        this.setHelper.addToSet(tacosRecipe, "categories", Arrays.asList(americanCategory, mexicanCategory));

        recipes.add(tacosRecipe);
        return recipes;
    }

    private List<Ingredient> getTacosIngredients(UnitOfMeasure eachUom, UnitOfMeasure tableSpoonUom, UnitOfMeasure teaSpoonUom, UnitOfMeasure pintUom, UnitOfMeasure cupsUom, Recipe tacosRecipe) {
        Ingredient anchoChiliPowder = new Ingredient("Ancho Chili Powder", new BigDecimal(2), tableSpoonUom, tacosRecipe);
        Ingredient driedOregano = new Ingredient("Dried Oregano", new BigDecimal(1), teaSpoonUom, tacosRecipe);
        Ingredient driedCumin = new Ingredient("Dried Cumin", new BigDecimal(1), teaSpoonUom, tacosRecipe);
        Ingredient sugar = new Ingredient("Sugar", new BigDecimal(1), teaSpoonUom, tacosRecipe);
        Ingredient salt = new Ingredient("Salt", new BigDecimal(".5"), teaSpoonUom, tacosRecipe);
        Ingredient garlic = new Ingredient("Clove of Garlic, Choppedr", new BigDecimal(1), eachUom, tacosRecipe);
        Ingredient orangeZestr = new Ingredient("finely grated orange zestr", new BigDecimal(1), tableSpoonUom, tacosRecipe);
        Ingredient orangeJuice = new Ingredient("fresh-squeezed orange juice", new BigDecimal(3), tableSpoonUom, tacosRecipe);
        Ingredient oliveOil = new Ingredient("Olive Oil", new BigDecimal(2), tableSpoonUom, tacosRecipe);
        Ingredient bonelessChickenThighs = new Ingredient("boneless chicken thighs", new BigDecimal(4), tableSpoonUom, tacosRecipe);
        Ingredient smallCornTortillasr = new Ingredient("small corn tortillasr", new BigDecimal(8), eachUom, tacosRecipe);
        Ingredient packedBabyArugula = new Ingredient("packed baby arugula", new BigDecimal(3), cupsUom, tacosRecipe);
        Ingredient avocados = new Ingredient("medium ripe avocados, slic", new BigDecimal(2), eachUom, tacosRecipe);
        Ingredient radish = new Ingredient("radishes, thinly sliced", new BigDecimal(4), eachUom, tacosRecipe);
        Ingredient tomatoes = new Ingredient("cherry tomatoes, halved", new BigDecimal(".5"), pintUom, tacosRecipe);
        Ingredient redOnion = new Ingredient("red onion, thinly sliced", new BigDecimal(".25"), eachUom, tacosRecipe);
        Ingredient roughlyChoppedCilantro = new Ingredient("Roughly chopped cilantro", new BigDecimal(4), eachUom, tacosRecipe);
        Ingredient cream = new Ingredient("cup sour cream thinned with 1/4 cup milk", new BigDecimal(4), cupsUom, tacosRecipe);
        Ingredient lime = new Ingredient("lime, cut into wedges", new BigDecimal(4), eachUom, tacosRecipe);
        return Arrays.asList(anchoChiliPowder, driedOregano, driedCumin, sugar, salt, garlic, orangeZestr, orangeJuice, oliveOil, bonelessChickenThighs,
                smallCornTortillasr, packedBabyArugula, avocados, radish, tomatoes, redOnion, roughlyChoppedCilantro, cream, lime);
    }

    private List<Ingredient> getGuacIngredients(UnitOfMeasure eachUom, UnitOfMeasure tableSpoonUom, UnitOfMeasure teaSpoonUom, UnitOfMeasure dashUom, Recipe guacRecipe) {
        Ingredient ripeAvocados = new Ingredient("ripe avocados", new BigDecimal(2), eachUom, guacRecipe);
        Ingredient kosherSalt = new Ingredient("Kosher salt", new BigDecimal(".5"), teaSpoonUom, guacRecipe);
        Ingredient limeJuice = new Ingredient("fresh lime juice or lemon juice", new BigDecimal(2), tableSpoonUom, guacRecipe);
        Ingredient redOnion = new Ingredient("minced red onion or thinly sliced green onion", new BigDecimal(2), tableSpoonUom, guacRecipe);
        Ingredient serranoChiles = new Ingredient("serrano chiles, stems and seeds removed, minced", new BigDecimal(2), eachUom, guacRecipe);
        Ingredient cilantro = new Ingredient("Cilantro", new BigDecimal(2), tableSpoonUom, guacRecipe);
        Ingredient freshlyGratedBlackPepper = new Ingredient("freshly grated black pepper", new BigDecimal(2), dashUom, guacRecipe);
        Ingredient ripeTomato = new Ingredient("ripe tomato, seeds and pulp removed, chopped", new BigDecimal(".5"), eachUom, guacRecipe);

        return Arrays.asList(ripeAvocados, kosherSalt, limeJuice, redOnion, serranoChiles, cilantro, freshlyGratedBlackPepper, ripeTomato);
    }

    private Category getCategory(String description) {
        return categoryRepository.findByDescription(description)
                .orElseThrow(() -> new RuntimeException("Expected Category Not Found"));
    }

    private UnitOfMeasure getUnitOfMeasure(String description) {
        return unitOfMeasureRepository.findByDescription(description)
                .orElseThrow(() -> new RuntimeException("Expected UOM Not Found"));
    }
}
