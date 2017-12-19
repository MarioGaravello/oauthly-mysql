package config;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = ValidateUniqueUsernameValidator.class)
public @interface ValidateUniqueUsername {
    String message() default "error.invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

