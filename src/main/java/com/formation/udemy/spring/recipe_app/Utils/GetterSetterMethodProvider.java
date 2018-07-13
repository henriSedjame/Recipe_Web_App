package com.formation.udemy.spring.recipe_app.Utils;

import com.mysema.commons.lang.Assert;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;

/**
 * Classe permettant de récupérer les méthodes accesseurs (getter ou setter) d'une classe
 * à partir d'une instance de celle si et du nom de l'attribut concerné par les méthodes accesseurs en question
 */
public class GetterSetterMethodProvider {

    public static Method getProperty(@NotNull Object instance, @NotNull String propertyName, String prefix, Class<?>... parametersType) throws NoSuchMethodException {
        //Vérifier que le préfixe est bien "get" ou "set"
        boolean goodPrefix = prefix.equals("get") || prefix.equals("set");
        Assert.isTrue(goodPrefix, "Le préfixe est erroné");

        //Récupérer la classe de l'instance
        Class<?> ownerClass = instance.getClass();
        //Récupérer la 1ère lettre du nom de l'attribut et la mettre en majuscule
        String firstCharOfPropertyName = propertyName.substring(0, 1).toUpperCase();
        //Récupérer le reste du nom de l'attribut (sans la 1ère lettre)
        String restOfpropertyName = propertyName.substring(1);
        //Constituer le nom de la méthode à renvoyer
        String propertyMethodName = prefix.concat(firstCharOfPropertyName).concat(restOfpropertyName);

        return ownerClass.getMethod(propertyMethodName, parametersType);
    }
}
