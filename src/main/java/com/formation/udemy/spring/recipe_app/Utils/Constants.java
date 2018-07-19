package com.formation.udemy.spring.recipe_app.Utils;

public class Constants {
    public static final String PATH_MODEL = "com.formation.udemy.spring.recipe_app.Model";
    public static final String PATH_COMMANDS_MODEL = "com.formation.udemy.spring.recipe_app.Model.Commands";
    public static final String PREFIX_QCLASS = "Q";
    private Constants(){
        throw new IllegalStateException("Constants.class");
    }
    public static final String GET_METHOD_PREFIX = "get";
    public static final String BEAN_COMMAND_SUFFIX = "Command";
    public static final String SET_METHOD_PREFIX = "set";

}
