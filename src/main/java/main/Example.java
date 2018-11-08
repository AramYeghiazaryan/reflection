package main;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Example {
    private String name="Your Name";
    public int value=2;
    public A a=new A();
    @Transient
    public String example;

    public Example(String name, int value) {
        this.name = name;
        this.value = value;
    }
    public Example(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface Transient{

}
