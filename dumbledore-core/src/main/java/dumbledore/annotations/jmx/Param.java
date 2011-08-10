package dumbledore.annotations.jmx;

public @interface Param {

    public String name();

    public String description() default "";
}
