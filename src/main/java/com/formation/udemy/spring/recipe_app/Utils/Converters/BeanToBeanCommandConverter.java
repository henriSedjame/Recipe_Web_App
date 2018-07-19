package com.formation.udemy.spring.recipe_app.Utils.Converters;

public interface BeanToBeanCommandConverter<BEAN, BEANCOMMAND> {
    BEANCOMMAND convert(BEAN bean);
}
