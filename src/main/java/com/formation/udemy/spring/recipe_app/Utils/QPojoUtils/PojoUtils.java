package com.formation.udemy.spring.recipe_app.Utils.QPojoUtils;

import com.formation.udemy.spring.recipe_app.Utils.Constants;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;

@Slf4j
public abstract class PojoUtils {

  private PojoUtils() {
    throw new IllegalStateException("PojoUtils.class");
  }

  public static Object getQPojo(Class classe) {

    /*Construire le nom de la QClasse à partir de la classe d'origine*/
    String className = classe.getSimpleName();
    String qClassName = Constants.PATH_MODEL.concat(".").concat(Constants.PREFIX_QCLASS).concat(className);

    /*Instancier dynamiquement la QClasse
     *  1- initailiser la classe à null
     *  2- initialiser le constructeur à null
     *  3- initailiser l'instance à null
     *  4- lister les types des différents paramètres du constructeur
     *  5- Lister les paramètres du constructeur */
    Class<?> qClasse;
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
      log.error(e.getMessage());
    }

    return instance;
  }
}
