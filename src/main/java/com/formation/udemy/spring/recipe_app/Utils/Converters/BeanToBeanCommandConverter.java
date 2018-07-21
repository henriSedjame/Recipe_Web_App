package com.formation.udemy.spring.recipe_app.Utils.Converters;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public interface BeanToBeanCommandConverter {

  /**
   * Methode permettant de convertir un objet bean en objet beanCommand
   *
   * @param bean   : l'objet à convertir
   * @param parent :
   * @param map    :
   * @param list   :
   * @return : l'objet converti
   */
  Object convert(Object bean, Object parent, Map<Object, Object> map, List<Object> list) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;
}
