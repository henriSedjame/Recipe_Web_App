package com.formation.udemy.spring.recipe_app.Utils.BidirectionnalSetterHelper;

import com.formation.udemy.spring.recipe_app.Utils.GetterSetterMethodProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

/**
 * Classe permettant de faire un set didirectionnel
 * Exemple : objetA.setObjetB(objetB);
 * objetB.setObjetA(objetA);
 */
@Slf4j
@Component("bidirectionnalSetterHelper")
public class BidirectionnalSetterHelperImpl implements BidirectionnalSetterHelper {
  @Override
  public void bidirectionnalSet(Object beanParent, Object beanChild) {
    Assert.notNull(beanParent, "Le 1er paramètre ne peut être null");
    Assert.notNull(beanChild, "Le 2nd paramètre ne peut être null");
    //Récupérer les classes des deux paramètres
    Class<?> parentClass = beanParent.getClass();
    Class<?> childClass = beanChild.getClass();

    // appeler beanParent.setChild(beanChild)
    setChildIntoParent(beanParent, beanChild, parentClass, childClass);

    // appeler beanChild.setParent(beanParent)
    setChildIntoParent(beanChild, beanParent, childClass, parentClass);
  }

  private void setChildIntoParent(Object parent, Object child, Class<?> classParent, Class<?> classChild) {
    // Rechercher l'attribut child dans la classe parent
    Field childFieldInParentClass = getField(classParent, classChild);
    //Récupérer le nom de l'attribut
    String childFieldInParentClassName = childFieldInParentClass.getName();
    try {
      //Récupérer la méthode setter
      Method parentBeanSetMethod = GetterSetterMethodProvider.getProperty(parent, childFieldInParentClassName, "set", classChild);
      //Invoquer la méthode
      parentBeanSetMethod.invoke(parent, child);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      log.error(e.getMessage());
    }
  }

  private Field getField(Class<?> parentClass, Class<?> childClass) {
    Field[] parentClassDeclaredFields = parentClass.getDeclaredFields();
    Optional<Field> childFieldInParentClassOpt = Arrays.stream(parentClassDeclaredFields)
            .filter(field -> field.getType().equals(childClass))
            .findFirst();
    return childFieldInParentClassOpt.orElseThrow(() -> new RuntimeException("Attribut de type " + childClass.getSimpleName() + " introuvable dans la classe " + parentClass.getSimpleName()));
  }
}
