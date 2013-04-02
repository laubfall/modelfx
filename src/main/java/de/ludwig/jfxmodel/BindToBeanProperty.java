package de.ludwig.jfxmodel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Fields annotated with this annotation retrieve there values from a
 * {@link Model}s Backing-Bean.
 * 
 * @author Daniel
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface BindToBeanProperty {
	/**
	 * if the annotated field cannot be binded directly to a Backing-Bean
	 * because the field owns a property that should be bind, you can define the
	 * name of that property.
	 * 
	 * @return s. description.
	 */
	public String bindPropertyName() default "";
}
