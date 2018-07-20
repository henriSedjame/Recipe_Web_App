package com.formation.udemy.spring.recipe_app.Utils.Converters;

import com.formation.udemy.spring.recipe_app.Utils.Constants;
import com.formation.udemy.spring.recipe_app.Utils.GetterSetterMethodProvider;
import com.formation.udemy.spring.recipe_app.Utils.SetHelper.SetHelper;
import javafx.util.Pair;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class BeanToBeanCommandConverterImpl implements BeanToBeanCommandConverter {

    @Qualifier("addRemoveSetHelper")
    @Autowired
    private SetHelper setHelper;

    @Synchronized
    @Nullable
    @Override
    public Object convert(Object bean, Object parent) {
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
        Map<Object, Object> map = new HashMap<>();
        if (beanCommand != null){
            //Récuperer la liste des attributs du bean
            // excepté la version
            map.putAll(tranferParameters(bean, beanCommand, parent));
        }

        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            if ((key instanceof Method) && (value instanceof Method)) {
                try {
                    Object eltcommand = ((Method) key).invoke(beanCommand, null);
                    ((Method) value).invoke(eltcommand, beanCommand);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else if ((key instanceof Method) && (value instanceof Field)) {
                try {
                    Set collection = (Set) ((Method) key).invoke(beanCommand, null);

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        return beanCommand;
    }

    private Map<Object, Object> tranferParameters(Object bean, Object beanCommand, Object parent) {
        Map<Object, Object> map = new HashMap<>();
        for (Field field : bean.getClass().getDeclaredFields()) {
            if (!field.getName().equals("version")) {
                //Pour chaque attribut
                try {
                    //Récuperer la methode get correspondante du bean
                    Method get = GetterSetterMethodProvider.getProperty(bean, field.getName(), Constants.GET_METHOD_PREFIX, null);
                    //Récuperer la classe de l'attribut
                    Class<?> fieldClass = get.getReturnType();
                    //Recupérer la valeur
                    Object fieldValue = get.invoke(bean, null);
                    //Si la valeur est une liste
                    if (fieldValue instanceof Collection){
                        Set<?> fieldSet = (Set) fieldValue;
                        //Convertir chaque elément de la liste
                        //puis l'ajouter à la liste du beanCommand
                        for (Object elt : fieldSet) {
                            if (parent != null && parent.equals(elt)) {

                                map.put(get, field);
                            } else {
                                Object eltCommand = convert(elt, bean);
                                this.setHelper.addToSet(beanCommand, field.getName(), eltCommand);
                            }
                        }
                    }else{  //Sinon
                        Pair<Method, Method> pair = this.performSetForNonListField(parent, beanCommand, field, fieldClass, fieldValue);
                        if (pair != null) {
                            map.put(pair.getKey(), pair.getValue());
                        }

                    }
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    log.error(e.getMessage(), e.getCause());
                }
            }
        }
        return map;
    }

    private Pair<Method, Method> performSetForNonListField(Object parent, Object beanCommand, Field field, Class<?> fieldClass, Object fieldValue) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        //Récuperer la methode set correspondante du beancommand
        Method set ;
        try {

            // Vérifier que l'attribut n'est pas d'un type possédant une classe commande équivalente
            Class<?> fieldCommandClass = this.getCommandClass(fieldClass.getSimpleName());
            set = GetterSetterMethodProvider.getProperty(beanCommand, field.getName(), Constants.SET_METHOD_PREFIX, fieldCommandClass);
            if (parent != null && parent.equals(fieldValue)) {
                Method get = GetterSetterMethodProvider.getProperty(beanCommand, field.getName(), Constants.GET_METHOD_PREFIX, null);
                return new Pair(get, set);
            } else {
                // si oui convertir la valeur de l'attribut
                Object fieldCommandValue = convert(fieldValue, parent);
                set.invoke(beanCommand, fieldCommandValue);
            }

        }catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e.getCause());
            set = GetterSetterMethodProvider.getProperty(beanCommand, field.getName(), Constants.SET_METHOD_PREFIX, fieldClass);
            set.invoke(beanCommand, fieldValue);
        }

        return null;
    }

    private Class<?> getCommandClass(String beanClassName) throws ClassNotFoundException {
        //Construire le nom de la classe du bean commande résultant
        String beanCommandClassName = beanClassName.concat(Constants.BEAN_COMMAND_SUFFIX);
        return Class.forName(Constants.PATH_COMMANDS_MODEL.concat(".").concat(beanCommandClassName));
    }
}
