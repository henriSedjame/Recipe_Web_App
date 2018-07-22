package com.formation.udemy.spring.recipe_app.Utils.Converters;

import com.formation.udemy.spring.recipe_app.Utils.Constants;
import com.formation.udemy.spring.recipe_app.Utils.GetterSetterMethodProvider;
import com.formation.udemy.spring.recipe_app.Utils.SetHelper.SetHelper;
import javafx.util.Pair;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
public class BeanToBeanConverterImpl implements BeanToBeanConverter {

  @Qualifier("addRemoveSetHelper")
  private SetHelper setHelper;

  public BeanToBeanConverterImpl(SetHelper setHelper) {
    this.setHelper = setHelper;
  }

  @Synchronized
  @Nullable
  @Override
  public Object convert(Object beanToConvert) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    return this.convert(beanToConvert, null, new HashMap<>(), new ArrayList<>());
  }

  /**
   * Methode permettant de convertir un objet bean en objet beanCommand et inversément
   *
   * @param beanToConvert                  : le bean à convertir
   * @param parent                         :
   * @param map                            : liste de couple clé-valeur contenant pour clé une paire(bean à  convertir enfant, bean à convertir parent) et pour valeur l'attribut qui lie les deux beans
   * @param listToCollectAllBeansConverted : liste permettant de collecter tous les objets convertis lors de la conversion (plusieurs apples récursifs pouvant se faire)
   * @return : l'objet converti
   */
  private Object convert(Object beanToConvert, Object parent, Map<Object, Object> map, List<Object> listToCollectAllBeansConverted) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

    //Si le bean à convertir est null on retourne null
    if (beanToConvert == null) return null;

    //Si le parent est null
    if (parent == null) {
      Assert.isTrue(map.isEmpty(), "La map doit être vide au début");
      Assert.isTrue(listToCollectAllBeansConverted.isEmpty(), "La liste doit être vide au début");
    }
    //Trouver  le nom de la classe du bean à convertir
    String beanToConvertClassName = beanToConvert.getClass().getSimpleName();

    //Initaliser le bean à retourner
    Object beanConverted = this.getInitializedBeanToReturn(beanToConvertClassName);

    //Assertion sur la non nullité du bean à retourner
    Assert.notNull(beanConverted, "Echec de l'initilaisation du bean à retourner : le bean à retourner est null");

    //Transférer la valeur de chaque attribut du bean à convertir dans l'attribut correspondant du bean converti
    this.tranferParameters(beanToConvert, beanConverted, parent, map, listToCollectAllBeansConverted);

    //Si le parent est null (cas du premier bean à convertir) ajouter le bean converti à la liste des beans convertis
    if (parent == null) listToCollectAllBeansConverted.add(beanConverted);

    //Et pour finir rétablir les relations bidirectionnelles entre les différents beans convertis
    this.restoreBeanConvertedsBidirectionnalRelationships(map, listToCollectAllBeansConverted);

    return beanConverted;
  }

  /**
   * Méthode permettant de restaurer les relations bidirectionnelles entre les différents beans convertis
   *
   * @param map                            : liste de couple clé-valeur contenant pour clé une paire(bean à  convertir enfant, bean à convertir parent) et pour valeur l'attribut qui lie les deux beans
   * @param listToCollectAllBeansConverted : liste des beans convertis
   * @throws NoSuchMethodException
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   */
  private void restoreBeanConvertedsBidirectionnalRelationships(Map<Object, Object> map, List<Object> listToCollectAllBeansConverted) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

    //Pour chaque entrée clé-valeur
    for (Map.Entry<Object, Object> entry : map.entrySet()) {

      //Récuperer la clé qui est une paire
      Pair pair = (Pair) entry.getKey();
      // Puis en récupérer les beans à convertir enfant et parent pour lesquels on souhaite rétablir la relation bidirectionnelle
      Object pairKey = pair.getKey();
      Object pairValue = pair.getValue();
      // Récuperer l'attribut qui les lie
      Field field = (Field) entry.getValue();
      //Récupérer le bean converti enfant correspondant au bean à convertir enfant dans la liste des beans convertis
      Object elementConvertedChild = this.getBeanEquivalentABeanFromAListOfBeans(listToCollectAllBeansConverted, pairKey);
      //Récupérer le bean converti parent correspondant au bean à convertir parent dans la liste des beans convertis
      Object elementConvertedParent = this.getBeanEquivalentABeanFromAListOfBeans(listToCollectAllBeansConverted, pairValue);
      // Si les deux denas convertis récupérés sont non null
      if (elementConvertedChild != null && elementConvertedParent != null) {
        //Récuperer la valeur de l'attribut qui les lie en appelant la méthode get sur le bean converti enfant
        Method get = GetterSetterMethodProvider.getProperty(elementConvertedChild, field.getName(), Constants.GET_METHOD_PREFIX);
        Object property = get.invoke(elementConvertedChild);
        //Si la propriété est une collection et que cette collection est vide
        if (property instanceof Collection && ((Collection) property).isEmpty()) {
          //Ajouter le bean converti parent à cette collection
          this.setHelper.addToSet(elementConvertedChild, field.getName(), elementConvertedParent);
        } else if (property == null) { //Sinon si la propriété est une propriété simple et qu'elle est nulle
          //Récupérer le type de cette propriété
          Class<?> fieldClass = get.getReturnType();
          //S'assurer que le type de la propriété correspond bien à celui du bean converti parent
          Assert.isInstanceOf(fieldClass, elementConvertedParent, "Erreur dans le type de retour attendu");
          //Si oui appeller la méthode set de la propriété sur le bean converti parent afin de la valoriser avec le bean converti parent
          Method set = GetterSetterMethodProvider.getProperty(elementConvertedChild, field.getName(), Constants.SET_METHOD_PREFIX, fieldClass);
          set.invoke(elementConvertedChild, elementConvertedParent);
        }
      }
    }
  }

  /**
   * Méthode permettant de trouver l'équivalent d'un bean dans une liste de beanCommands et inversément
   *
   * @param list                 : liste des beans parmi lesquels se trouve l'équivalent du bean fourni
   * @param beanToFindEquivalent : bean dont on veut trouver l'équivalent
   * @return
   */
  private Object getBeanEquivalentABeanFromAListOfBeans(List<Object> list, Object beanToFindEquivalent) {
    //Initialiser une nouvelle liste avec tous les élements de la liste fournie
    List<Object> listOfBeanCommandToLinkBeanCommand = new ArrayList<>(list);
    //Supprimer de la liste tous les élements qui n'équivalent pas au bean fourni
    listOfBeanCommandToLinkBeanCommand.removeIf(elt -> !this.areEquivalent(elt, beanToFindEquivalent));
    //Récupérer la taille de la liste
    int size = listOfBeanCommandToLinkBeanCommand.size();
    //Si la liste ne contient qu'un élément retourner ce dernier
    //Sinon retourner null
    return size == 1 ? listOfBeanCommandToLinkBeanCommand.get(0) : null;
  }

  /**
   * Méthode permettant d'initialiser le bean converti
   *
   * @param beanToConvertClassName : nom de la classe du bean à convertir
   * @return l'instance vide du bean converti
   */
  private Object getInitializedBeanToReturn(String beanToConvertClassName) {
    Object beanConverted = null;
    try {
      //Recupérer la classe du beanCommand à partir du nom de la classe du bean
      final Class<?> beanConvertedClass = this.getBeanConvertedClass(beanToConvertClassName);
      //Récuperer le constructeur par défaut du beanCommand
      final Constructor<?> constructor = beanConvertedClass.getConstructor();
      //Instancier le beanCommand
      beanConverted = constructor.newInstance();
      //Assertion sur la non nullité du beanCommand
      Assert.notNull(beanConverted, "Le beanCommand est null");
    } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
      log.error(e.getMessage(), e.getCause());
    }
    return beanConverted;
  }

  /**
   * Methode permettant de valoriser chaque atribut du bean converti avec celle de son équivalent dans le bean à convertir
   *
   * @param beanToConvert : bean à convertir
   * @param beanConverted : bean converti
   * @param parent        :
   * @param map           :
   * @param list          :
   */
  private void tranferParameters(Object beanToConvert, Object beanConverted, Object parent, Map<Object, Object> map, List<Object> list) {

    for (Field field : beanToConvert.getClass().getDeclaredFields()) {
      if (field.getName().equals("version")) continue;
      //Pour chaque attribut
      try {
        //Récuperer la methode get correspondante du bean
        Method get = GetterSetterMethodProvider.getProperty(beanToConvert, field.getName(), Constants.GET_METHOD_PREFIX);
        //Récuperer la classe de l'attribut
        Class<?> fieldClass = get.getReturnType();
        //Recupérer la valeur
        Object fieldValue = get.invoke(beanToConvert);
        //Si la valeur est une liste
        if (fieldValue instanceof Collection) {
          this.performAddToSetForListField(beanToConvert, beanConverted, parent, map, list, field, (Set) fieldValue);
        } else {  //Sinon
          this.performSetForNonListField(beanToConvert, beanConverted, field, fieldClass, fieldValue, parent, map, list);
        }
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
        log.error(e.getMessage(), e.getCause());
      }
    }
  }

  /**
   * Methode permettant de transferer les attributs de type collection
   *
   * @param beanToConvert :
   * @param beanConverted :
   * @param parent        :
   * @param map           :
   * @param list          :
   * @param field         : attribut à transférer
   * @param fieldValue    : valeur de l'attribut
   * @throws NoSuchMethodException
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   */
  private void performAddToSetForListField(Object beanToConvert, Object beanConverted, Object parent, Map<Object, Object> map, List<Object> list, Field field, Set fieldValue) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    //pour chaque élément de la conllection
    for (Object eltOfFieldValue : (Set<?>) fieldValue) {
      //Si le parent n'est pas null (signe que l'on est dans un appel récursif)
      //Et que le parent est égal à l'élément de la liste en cours
      //alimenter la map
      if (parent != null && parent.equals(eltOfFieldValue)) {
        map.put(new Pair<>(beanToConvert, parent), field);
      } else { //Sinon
        //Convertir l'élement de la collection
        Object eltOfFieldValueConverted = convert(eltOfFieldValue, beanToConvert, map, list);
        //Puis ajouter le résultat à la collection correspondante dans le bean converti
        this.setHelper.addToSet(beanConverted, field.getName(), eltOfFieldValueConverted);
        //Alimenter la liste des bean convertis
        list.add(eltOfFieldValueConverted);
      }
    }
  }

  /**
   * Methode permettant de transferer les attributs de type simple
   *
   * @param beanToConvert :
   * @param beanConverted :
   * @param field         :
   * @param fieldClass    : classe de l'attribut
   * @param fieldValue    : valeur de l'attrbut
   * @param parent        :
   * @param map           :
   * @param list          :
   * @throws NoSuchMethodException
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   */
  private void performSetForNonListField(Object beanToConvert, Object beanConverted, Field field, Class<?> fieldClass, Object fieldValue, Object parent, Map<Object, Object> map, List<Object> list) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

    //Récuperer la methode set correspondante du beancommand
    Method set;
    try {

      // Vérifier que l'attribut n'est pas d'un type provenant de java
      Class<?> fieldCommandClass = this.getBeanConvertedClass(fieldClass.getSimpleName());
      //Dans tous les cas récuperer la méthode set permettant de valoriser l'attribut
      set = GetterSetterMethodProvider.getProperty(beanConverted, field.getName(), Constants.SET_METHOD_PREFIX, fieldCommandClass);

      //Si l'attribut n'est pas d'un type provenant de java
      //Si le parent n'est pas null (signe que l'on est dans un appel récursif)
      //Et que le parent est égal la valeur de l'attribut
      //alimenter la map
      if (parent != null && parent.equals(fieldValue)) {
        map.put(new Pair<>(beanToConvert, parent), field);
      } else {
        //Sinon convertir la valeur de l'attribut
        Object fieldCommandValue = convert(fieldValue, beanToConvert, map, list);
        //Puis valoriser l'attribut correspondant dans le bean converti avec le résultat de la conversion de l'attribut
        set.invoke(beanConverted, fieldCommandValue);
        //Alimenter la liste des beans convertis
        list.add(fieldCommandValue);
      }

    } catch (ClassNotFoundException e) {
      //Si l'attribut est d'un type provenant de java
      log.error(e.getMessage(), e.getCause());
      //Valoriser l'attribut correspondant dans le bean converti avec la valeur de l'attribut
      set = GetterSetterMethodProvider.getProperty(beanConverted, field.getName(), Constants.SET_METHOD_PREFIX, fieldClass);
      set.invoke(beanConverted, fieldValue);
    }

  }

  /**
   * Methode permettant de retourner la classe du bean converti
   *
   * @param beanToCovertClassName :  nom de la classe du bean à convertir
   * @return : la classe du bean resultant de la converion
   * @throws ClassNotFoundException
   */
  private Class<?> getBeanConvertedClass(String beanToCovertClassName) throws ClassNotFoundException {

    String beanConvertedClassName;
    String dirPath;
    //Construire le nom de la classe du bean converti
    if (beanToCovertClassName.endsWith(Constants.BEAN_COMMAND_SUFFIX)) {
      beanConvertedClassName = StringUtils.removeEnd(beanToCovertClassName, Constants.BEAN_COMMAND_SUFFIX);
      dirPath = Constants.PATH_MODEL.concat(".");
    } else {
      beanConvertedClassName = beanToCovertClassName.concat(Constants.BEAN_COMMAND_SUFFIX);
      dirPath = Constants.PATH_COMMANDS_MODEL.concat(".");
    }
    return Class.forName(dirPath.concat(beanConvertedClassName));
  }

  /**
   * Methode permettant d'évaluer si deux beans, l'un du type du bean à convertir et l'autre du type du bean résultant de la conversion, sont équivalents
   *
   * @param obj1 : bean 1
   * @param obj2 : bean 2
   * @return : True si les beans sont équivalents , False sinon
   */
  private boolean areEquivalent(Object obj1, Object obj2) {
    //Récuperer le nom des classes des deux beans
    String name1 = obj1.getClass().getSimpleName();
    String name2 = obj2.getClass().getSimpleName();

    //Récuperer la différence entre ces deux noms
    String difference = StringUtils.difference(name1, name2);
    if (difference.isEmpty()) difference = StringUtils.difference(name2, name1);

    //Si cette différence n'est pas "Command" retourner False
    if (!difference.equals(Constants.BEAN_COMMAND_SUFFIX)) return false;

    //Récuperer la liste des attributs des deus beans
    Field[] fields1 = obj1.getClass().getDeclaredFields();
    Field[] fields2 = obj2.getClass().getDeclaredFields();

    //Calculer la valeur absolue de la différence de taille des deux listes
    int diffLength = Math.abs(fields1.length - fields2.length);
    //Si cette différence est différente de 1 retourner False
    //En effet un bean Command n'a pas d'attribut "version"
    if (diffLength != 1) return false;

    //Pour chaque élement d'une des liste
    for (Field field : fields1) {
      //Excepté l'attribut "version"
      if (field.getName().equals("version")) continue;
      try {
        //Récuperer les méthodes get correspondant à l'attribut pour chacun des beans
        Method get1 = GetterSetterMethodProvider.getProperty(obj1, field.getName(), Constants.GET_METHOD_PREFIX);
        Method get2 = GetterSetterMethodProvider.getProperty(obj2, field.getName(), Constants.GET_METHOD_PREFIX);
        //Récuperer les valeurs de l'attribut pour chacun des beans
        Object value1 = get1.invoke(obj1);
        Object value2 = get2.invoke(obj2);
        //Récuperer le nom du type de l'attribut
        String name = field.getType().getName();
        //Si le type est un type jave différent d'un type héritant de collection
        //Et que les valeurs de l'attribut pour les deux beans ne sont pas égaux
        //Retourner False
        if (!name.startsWith(Constants.APP_PATH) && !(value1 instanceof Collection) && !(value2 instanceof Collection)
          && value1 != null && value2 != null && !value1.toString().equals(value2.toString())) {
          return false;
        }
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
        log.error(e.getMessage(), e.getCause());
        return false;
      }
    }
    return true;
  }

}
