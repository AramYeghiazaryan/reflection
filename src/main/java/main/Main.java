package main;

import java.lang.reflect.*;

public class Main {
    public static String name;
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException {

        Class<Example> clss = Example.class;
        System.out.println("Class Name : " + clss.getName());

        Constructor<Example>[] constructor = (Constructor<Example>[]) clss.getDeclaredConstructors();
        System.out.println("Constructors");
        for (Constructor<Example> con: constructor) {
            System.out.println(serialize(con));
        }
        System.out.println("Methods");
        Method[] methods = clss.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println(serialize(method));
        }
        System.out.println("Fields");
        Field[] fields=clss.getDeclaredFields();
        for(Field field : fields){
            System.out.println(serialize(field));
        }

    }

    public static String serialize(Object o) throws IllegalAccessException {
        StringBuilder value=new StringBuilder().append("{\n\t");
        if(o instanceof Constructor){
            value.append("\"Constructor "+((Constructor) o).getName()+"\" : \n\t{\n");
            Parameter[] parameters=((Constructor) o).getParameters();
            value.append("\t\t\"Parameters\" : \n\t\t{"+"\n");
            for(Parameter parameter: parameters){
               value.append("\t\t\t\""+parameter.getName()+"\" : \""+parameter.getType()+"\",\n");
            }
            value.deleteCharAt(value.length()-2);
            value.append("\t\t}\n");
            value.append("\t}\n");
            value.append("}");
        }
        if(o instanceof  Method){
            value.append("\"Method "+((Method) o).getName()+"\" : \n\t{\n");
            value.append("\t\t"+"\"Modifiers\" : \n\t\t{ \n\t\t\t\"modifier\" : \"" +
                    Modifier.toString(((Method) o).getModifiers())+"\"\n\t\t},\n");
            Parameter[] parameters=((Method) o).getParameters();
            value.append("\t\t\"Parameters\" : \n\t\t{"+"\n");
            for(Parameter parameter : parameters){
                value.append("\t\t\t\""+parameter.getName()+"\" : \""+parameter.getType()+"\"\n");

            }
            value.append("\t\t}\n");
            value.append("\t}\n}");
        }
        if(o instanceof Field){
            Example example = new Example();
            A a=new A();

            if(!((Field) o).getType().isPrimitive() && !((Field) o).getType().getName().contains("java.lang")){
                 name=((Field) o).getType().getName();

                Object classobject=((Field) o).get(example);
                Class tempclass=classobject.getClass();
                System.out.println("Constructors of "+ name);
                Constructor[] constructor = tempclass.getDeclaredConstructors();
                for (Constructor<Example> con: constructor) {
                    System.out.println(serialize(con));
                }
                System.out.println("Methods of "+ name);
                Method[] methods = tempclass.getDeclaredMethods();
                for (Method method : methods) {
                    System.out.println(serialize(method));
                }
                System.out.println("Fields of "+ name);
                Field[] fields=tempclass.getDeclaredFields();
                for(Field field : fields){
                    System.out.println(serialize(field));
                }
            } else {
                if (!((Field) o).isAnnotationPresent(Transient.class)) {

                    boolean private_ = false;
                    if (Modifier.toString(((Field) o).getModifiers()).equals("private")) {
                        ((Field) o).setAccessible(true);
                        private_ = true;
                    }
                    Object object;

                    if(name!=null && name.contains("A") ) {
                         object = ((Field) o).get(a);
                    }
                    else {
                        object=((Field) o).get(example);
                    }
                    value.append("\"Field " + ((Field) o).getName() + "\" : \n\t{\n");
                    value.append("\t\t" + "\"Modifiers\" : \n\t\t{ \n\t\t\t\"modifier\" : \"" +
                            Modifier.toString(((Field) o).getModifiers()) + "\"\n\t\t},\n");
                    value.append("\t\t" + "\"Type\" : \n\t\t{ \n\t\t\t\"type\" : \"" +
                            ((Field) o).getType() + "\"\n\t\t},\n");
                    value.append("\t\t" + "\"Value\" : \n\t\t{ \n\t\t\t\"value\" : \"" +
                            object + "\"\n\t\t}\n");

                    value.append("\t}\n}");
                    if (private_) {
                        ((Field) o).setAccessible(false);
                    }
                }
            }
        }

        return value.toString();
    }

}
