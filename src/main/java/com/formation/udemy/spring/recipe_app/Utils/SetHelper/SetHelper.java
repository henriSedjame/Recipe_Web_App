package com.formation.udemy.spring.recipe_app.Utils.SetHelper;

/**
 * @Project recipe_app
 * @Author Henri Joel SEDJAME
 * @Date 12/07/2018
 */
public interface SetHelper {

  void addToSet(Object owner, String propertyName, Object elementToAdd);

  void removeFromSet(Object owner, String propertyName, Object elementToRemove);
}
