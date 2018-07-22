package com.formation.udemy.spring.recipe_app.Utils.Converters;


import java.lang.reflect.InvocationTargetException;

public interface BeanToBeanConverter {

  /**
   * @param bean
   * @return
   * @throws NoSuchMethodException
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   */
  Object convert(Object bean) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;
}

