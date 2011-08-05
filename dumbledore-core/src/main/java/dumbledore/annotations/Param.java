package dumbledore.annotations;

public @interface Param {

    public String name();

    public String description() default "";
}
