package com.formation.udemy.spring.recipe_app.Utils.SetHelper;

import com.formation.udemy.spring.recipe_app.Utils.GetterSetterMethodProvider;
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
      Method getMethod = GetterSetterMethodProvider.getProperty(owner, propertyName, "get", null);
      Set property = (Set) getMethod.invoke(owner, null);
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
      Method getMethod = GetterSetterMethodProvider.getProperty(owner, propertyName, "get", null);
      Set property = (Set) getMethod.invoke(owner, null);
      if (elementToRemove instanceof Collection) {
        property.removeAll((Collection) elementToRemove);
      } else {
        property.remove(elementToRemove);
      }

    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
  }
}
