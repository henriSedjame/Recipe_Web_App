package com.formation.udemy.spring.recipe_app.Utils.QPojoUtils;

import java.lang.reflect.Constructor;

public abstract class PojoUtils {

    private static final String PATH_MODEL = "com.formation.spring.udemy.Model";
    private static final String PREFIX_QCLASS = "Q";

    public static Object getQPojo(Class classe) {

        /*Construire le nom de la QClasse à partir de la classe d'origine*/
        String className = classe.getSimpleName();
        String qClassName = PATH_MODEL.concat(".").concat(PREFIX_QCLASS).concat(className);

        /*Instancier dynamiquement la QClasse
         *  1- initailiser la classe à null
         *  2- initialiser le constructeur à null
         *  3- initailiser l'instance à null
         *  4- lister les types des différents paramètres du constructeur
         *  5- Lister les paramètres du constructeur */
        Class<?> qClasse ;
        Constructor<?> constructor;
        Object instance = null;
        Class<?>[] parametertypes = {String.class};
        Object[] initArgs = {className.toLowerCase()};

        try {
            /* 1- Récuperer la classe grace au nom de la classe
             *  2- Récupérer le constructeur avec la liste des paramètres
             *  3- instance la QClasse grace au constructeur */

            qClasse = Class.forName(qClassName);
            constructor = qClasse.getConstructor(parametertypes);
            instance = constructor.newInstance(initArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return instance;
    }
}
