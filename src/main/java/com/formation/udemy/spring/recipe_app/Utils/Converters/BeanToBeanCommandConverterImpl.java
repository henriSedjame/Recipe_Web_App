package com.formation.udemy.spring.recipe_app.Utils.Converters;

import com.formation.udemy.spring.recipe_app.Utils.Constants;
import com.formation.udemy.spring.recipe_app.Utils.GetterSetterMethodProvider;
import com.formation.udemy.spring.recipe_app.Utils.SetHelper.SetHelper;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Component
@Slf4j
public class BeanToBeanCommandConverterImpl implements BeanToBeanCommandConverter {

  @Qualifier("addRemoveSetHelper")
  @Autowired
  private SetHelper setHelper;

  @Synchronized
  @Nullable
  @Override
  public Object convert(Object bean, Object parent, Map<Object, Object> map, List<Object> list) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    if (bean == null) return null;
    //Trouver  le nom de la classe du bean à convertir
    String beanClassName = bean.getClass().getSimpleName();
    Assert.isTrue(!beanClassName.endsWith(Constants.BEAN_COMMAND_SUFFIX), "Le bean à convertir en commande semble être déjà une commande");

    Object beanCommand = null;
    try {
      //Recupérer la classe du beanCommand à partir du nom de la classe du bean
      final Class<?> beanCommandClass = getCommandClass(beanClassName);
      //Récuperer le constructeur par défaut du beanCommand
      final Constructor<?> constructor = beanCommandClass.getConstructor(null);
      //Instancier le beanCommand
      beanCommand = constructor.newInstance();
      Assert.notNull(beanCommand, "Le beanCommand est null");
    } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
      log.error(e.getMessage(), e.getCause());
    }

    //Si le beanCommand est bien non null
    if (beanCommand != null) {

      // transférer la valeurs de chaque attribut du bean dans le beanCommand
      tranferParameters(bean, beanCommand, parent, map, list);
      if (parent == null) {
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
          Object key = entry.getKey();
          Field value = (Field) entry.getValue();

          List<Object> listOfBeanCommandToLinkBeanCommand = new ArrayList<>(list);
          listOfBeanCommandToLinkBeanCommand.removeIf(elt -> !this.areEquivalent(elt, key));

          Assert.isTrue(listOfBeanCommandToLinkBeanCommand.size() == 1, "Erreur dans la méthode areEquivalent()");

          Object eltCommand = listOfBeanCommandToLinkBeanCommand.get(0);

          Method get = GetterSetterMethodProvider.getProperty(eltCommand, value.getName(), Constants.GET_METHOD_PREFIX, null);
          Object property = get.invoke(eltCommand, null);

          if (property instanceof Collection) {
            this.setHelper.addToSet(eltCommand, value.getName(), beanCommand);
          } else {
            Class<?> fieldClass = get.getReturnType();
            Assert.isTrue(beanCommand.getClass().equals(fieldClass), "Erreur dans le type de retour attendu");
            Method set = GetterSetterMethodProvider.getProperty(eltCommand, value.getName(), Constants.SET_METHOD_PREFIX, fieldClass);
            set.invoke(eltCommand, beanCommand);
          }

        }
      }
    }

    return beanCommand;
  }

  private void tranferParameters(Object bean, Object beanCommand, Object parent, Map<Object, Object> map, List<Object> list) {

    for (Field field : bean.getClass().getDeclaredFields()) {
      if (field.getName().equals("version")) continue;
      //Pour chaque attribut
      try {
        //Récuperer la methode get correspondante du bean
        Method get = GetterSetterMethodProvider.getProperty(bean, field.getName(), Constants.GET_METHOD_PREFIX, null);
        //Récuperer la classe de l'attribut
        Class<?> fieldClass = get.getReturnType();
        //Recupérer la valeur
        Object fieldValue = get.invoke(bean, null);
        //Si la valeur est une liste
        if (fieldValue instanceof Collection) {
          performAddToSetForListField(bean, beanCommand, parent, map, list, field, (Set) fieldValue);
        } else {  //Sinon
          this.performSetForNonListField(bean, beanCommand, field, fieldClass, fieldValue, parent, map, list);
        }
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
        log.error(e.getMessage(), e.getCause());
      }
    }
  }

  private void performAddToSetForListField(Object bean, Object beanCommand, Object parent, Map<Object, Object> map, List<Object> list, Field field, Set fieldValue) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    Set<?> fieldSet = fieldValue;
            /*Convertir chaque elément de la liste
            puis l'ajouter à la liste du beanCommand*/
    for (Object elt : fieldSet) {
      if (parent != null && parent.equals(elt)) {
        map.put(bean, field);
      } else {
        Object eltCommand = convert(elt, bean, map, list);
        this.setHelper.addToSet(beanCommand, field.getName(), eltCommand);
        list.add(eltCommand);
      }
    }
  }

  private void performSetForNonListField(Object bean, Object beanCommand, Field field, Class<?> fieldClass, Object fieldValue, Object parent, Map<Object, Object> map, List<Object> list) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

    //Récuperer la methode set correspondante du beancommand
    Method set;
    try {

      // Vérifier que l'attribut n'est pas d'un type possédant une classe commande équivalente
      Class<?> fieldCommandClass = this.getCommandClass(fieldClass.getSimpleName());
      set = GetterSetterMethodProvider.getProperty(beanCommand, field.getName(), Constants.SET_METHOD_PREFIX, fieldCommandClass);
      if (parent != null && parent.equals(fieldValue)) {
        map.put(bean, field);
      } else {
        // si oui convertir la valeur de l'attribut
        Object fieldCommandValue = convert(fieldValue, bean, map, list);
        set.invoke(beanCommand, fieldCommandValue);
        list.add(fieldCommandValue);
      }

    } catch (ClassNotFoundException e) {
      log.error(e.getMessage(), e.getCause());
      set = GetterSetterMethodProvider.getProperty(beanCommand, field.getName(), Constants.SET_METHOD_PREFIX, fieldClass);
      set.invoke(beanCommand, fieldValue);
    }

  }

  private Class<?> getCommandClass(String beanClassName) throws ClassNotFoundException {
    //Construire le nom de la classe du bean commande résultant
    String beanCommandClassName = beanClassName.concat(Constants.BEAN_COMMAND_SUFFIX);
    return Class.forName(Constants.PATH_COMMANDS_MODEL.concat(".").concat(beanCommandClassName));
  }

  private boolean areEquivalent(Object obj1, Object obj2) {
    String name1 = obj1.getClass().getSimpleName();
    String name2 = obj2.getClass().getSimpleName();
    String difference = StringUtils.difference(name1, name2);
    if (difference.isEmpty()) difference = StringUtils.difference(name2, name1);

    if (!difference.equals(Constants.BEAN_COMMAND_SUFFIX)) return false;

    Field[] fields1 = obj1.getClass().getDeclaredFields();
    Field[] fields2 = obj2.getClass().getDeclaredFields();

    int diffLength = Math.abs(fields1.length - fields2.length);
    if (diffLength != 1) return false;

    for (Field field : fields1) {
      if (field.getName().equals("version")) continue;
      try {
        Method get1 = GetterSetterMethodProvider.getProperty(obj1, field.getName(), Constants.GET_METHOD_PREFIX, null);
        Method get2 = GetterSetterMethodProvider.getProperty(obj2, field.getName(), Constants.GET_METHOD_PREFIX, null);

        Object value1 = get1.invoke(obj1, null);
        Object value2 = get2.invoke(obj2, null);

        String name = field.getType().getName();

        if (!name.startsWith(Constants.APP_PATH) && !(value1 instanceof Collection) && !(value2 instanceof Collection)
          && value1 != null && value2 != null && !value1.toString().equals(value2.toString())) {
          return false;
        }

      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
        log.error(e.getMessage(), e.getCause());
      }

    }
    return true;
  }


}
