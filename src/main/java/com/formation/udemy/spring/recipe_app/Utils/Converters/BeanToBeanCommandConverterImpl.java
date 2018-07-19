package com.formation.udemy.spring.recipe_app.Utils.Converters;

import com.formation.udemy.spring.recipe_app.Utils.Constants;
import com.formation.udemy.spring.recipe_app.Utils.GetterSetterMethodProvider;
import com.formation.udemy.spring.recipe_app.Utils.SetHelper.SetHelper;
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
    public Object convert(Object bean) {
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

        if (beanCommand != null){
            //Récuperer la liste des attributs du bean
            // excepté la version
            tranferParameters(bean, beanCommand);
        }

        return beanCommand;
    }

    private void tranferParameters(Object bean, Object beanCommand) {
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
                            Object eltCommand = convert(elt);
                            this.setHelper.addToSet(beanCommand, field.getName(), eltCommand);
                        }
                    }else{  //Sinon
                        this.performSetForNonListField(beanCommand, field, fieldClass, fieldValue);
                    }
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    log.error(e.getMessage(), e.getCause());
                }
            }
        }
    }

    private void performSetForNonListField(Object beanCommand, Field field, Class<?> fieldClass, Object fieldValue) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        //Récuperer la methode set correspondante du beancommand
        Method set ;
        try {
            // Vérifier que l'attribut n'est pas d'un type possédant une classe commande équivalente
            Class<?> fieldCommandClass = this.getCommandClass(fieldClass.getSimpleName());
            // si oui convertir la valeur de l'attribut
            Object fieldCommandValue = convert(fieldValue);
            set = GetterSetterMethodProvider.getProperty(beanCommand, field.getName(), Constants.SET_METHOD_PREFIX, fieldCommandClass);
            set.invoke(beanCommand,fieldCommandValue);
        }catch (ClassNotFoundException e) {
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
}
