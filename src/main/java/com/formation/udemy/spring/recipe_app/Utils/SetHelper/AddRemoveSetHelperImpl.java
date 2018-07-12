package com.formation.udemy.spring.recipe_app.Utils.SetHelper;

import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;

/**
 * @Project recipe_app
 * @Author Henri Joel SEDJAME
 * @Date 12/07/2018
 */

@Component(value = "addRemoveSetHelper")
public class AddRemoveSetHelperImpl implements SetHelper {

  @Override
  public void addToSet(Object owner, String propertyName, Object elementToAdd) {

    try {
      Set property = getProperty(owner, propertyName);
      if (elementToAdd instanceof Collection) {
        property.addAll((Collection) elementToAdd);
      } else {
        property.add(elementToAdd);
      }
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void removeFromSet(Object owner, String propertyName, Object elementToRemove) {
    try {
      Set property = getProperty(owner, propertyName);
      if (elementToRemove instanceof Collection) {
        property.removeAll((Collection) elementToRemove);
      } else {
        property.remove(elementToRemove);
      }

    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  private Set<?> getProperty(Object owner, String propertyName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    Class<?> ownerClass = owner.getClass();
    String firstCharOfPropertyName = propertyName.substring(0, 1).toUpperCase();
    String restOfpropertyName = propertyName.substring(1);
    String prefix = "get";

    String getPropertyMethodName = prefix.concat(firstCharOfPropertyName).concat(restOfpropertyName);

    Method getProperty = ownerClass.getMethod(getPropertyMethodName, null);

    return (Set) getProperty.invoke(owner, null);
  }

}
