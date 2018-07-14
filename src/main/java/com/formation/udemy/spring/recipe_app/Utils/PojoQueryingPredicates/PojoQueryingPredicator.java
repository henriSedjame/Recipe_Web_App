package com.formation.udemy.spring.recipe_app.Utils.PojoQueryingPredicates;

import com.formation.udemy.spring.recipe_app.Utils.QPojoUtils.PojoUtils;

class PojoQueryingPredicator<T> {
    private Class className;

    public PojoQueryingPredicator(Class className) {
        this.className = className;
    }

    public T getQClasse() {
        return (T) PojoUtils.getQPojo(className);
    }
}
