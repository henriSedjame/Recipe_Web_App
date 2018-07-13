package com.formation.udemy.spring.recipe_app.Utils.BidirectionnalSetterHelper;

import com.formation.udemy.spring.recipe_app.Utils.GetterSetterMethodProvider;
import org.springframework.stereotype.Component;

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
@Component("bidirectionnalSetterHelper")
public class BidirectionnalSetterHelperImpl implements BidirectionnalSetterHelper {
    @Override
    public void bidirectionnalSet(Object beanParent, Object beanChild) {
        //Récupérer les classes des deux paramètres
        Class<?> parentClass = beanParent.getClass();
        Class<?> childClass = beanChild.getClass();

        // appeler beanParent.setChild(beanChild)
        setChildIntoParent(beanParent, beanChild, parentClass, childClass);

        // appeler beanChild.setParent(beanParent)
        setChildIntoParent(beanChild, beanParent, childClass, parentClass);


    }

    private void setChildIntoParent(Object beanParent, Object beanChild, Class<?> parentClass, Class<?> childClass) {
        // Rechercher l'attribut child dans la classe parent
        Field childFieldInParentClass = getField(parentClass, childClass);
        //Récupérer le nom de l'attribut
        String childFieldInParentClassName = childFieldInParentClass.getName();
        try {
            //Récupérer la méthode setter
            Method parentBeanSetMethod = GetterSetterMethodProvider.getProperty(beanParent, childFieldInParentClassName, "set", childClass);
            //Invoquer la méthode
            parentBeanSetMethod.invoke(beanParent, beanChild);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private Field getField(Class<?> parentClass, Class<?> childClass) {
        Field[] parentClassDeclaredFields = parentClass.getDeclaredFields();
        Optional<Field> childFieldInParentClassOpt = Arrays.asList(parentClassDeclaredFields).stream()
                .filter(field -> field.getType().equals(childClass))
                .findFirst();
        return childFieldInParentClassOpt.orElseThrow(() -> new RuntimeException("Attribut de type " + childClass.getSimpleName() + " introuvable dans la classe " + parentClass.getSimpleName()));
    }
}
