package de.ludwig.jfxmodel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * 
 * @author Daniel
 *
 * @param <MODELBEAN> type of the backing-bean
 */
public class Model<MODELBEAN> {
	private MODELBEAN modelObject;
	
	private Object owner;
	
	private List<Pair<Property<?>, Property<?>>> bindedProperties = new ArrayList<>();
	
	/**
	 * @param owner
	 */
	public Model(Object owner) {
		super();
		if(owner == null) {
			throw new RuntimeException("please provide the owner of this Model-Instance!");
		}
		this.owner = owner;
	}
	
	public Model(Object owner, MODELBEAN backingBean) {
		this(owner);
		this.modelObject = backingBean;
	}

	public MODELBEAN getModelObject() {
		return modelObject;
	}

	@SuppressWarnings("unchecked")
	private void setModelObjectRaw(Object bean){
		setModelObject((MODELBEAN) bean);
	}
	
	public void setModelObject(MODELBEAN bean) {
		modelObject = (MODELBEAN) bean;
	}
	
	@SuppressWarnings("unchecked")
	public final void bind(){
		if(modelObject == null) {
			throw new RuntimeException("you have to set a backing-bean to the model in order to call bind()");
		}
		
		final Field[] declaredFields = owner.getClass().getDeclaredFields();
		for(Field f : declaredFields){
			final BindToBeanProperty btb = f.getAnnotation(BindToBeanProperty.class);
			if(btb==null)
				continue;
			
			// at this point we know we have properties that requires model support
			boolean supportsCombined = supportsCombined(f.getType());
			if(supportsCombined){
				// binding done by component.
				// now get the model-object and put it into this one
				try {
					final SupportCombined interestedInModel = (SupportCombined) f.get(owner);
					Object useThis = PropertyUtils.getProperty(modelObject, f.getName());
					interestedInModel.getModel().unbind();
					interestedInModel.getModel().setModelObjectRaw(useThis);
					interestedInModel.getModel().bind();
				} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
					throw new RuntimeException(e);
				}
			} else {
				// simple binding
				final String bindPropertyName = btb.bindPropertyName();
				final Object objectFromField = objectFromField(f, owner);
				final Property<Object> jfxComponentProp = (Property<Object>) jfxProperty(bindPropertyName, objectFromField);
				final Property<Object> modelObjectProp = (Property<Object>) jfxProperty(f.getName(), modelObject);
				bind(jfxComponentProp, modelObjectProp);
				bindedProperties.add(new Pair<Property<?>, Property<?>>(jfxComponentProp, modelObjectProp));
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void bind(Property<Object> p1, Property<Object> p2){
		if(p1 == null || p2 == null) {
			throw new RuntimeException("one or both properties to bind is / are null. normally that means that you do not have initialized the Property");
		}
		
		if(p1 instanceof ObservableList && p2 instanceof ObservableList){
			Bindings.bindContentBidirectional((ObservableList<Object>)p1, (ObservableList<Object>)p2);
		} else {
			Bindings.bindBidirectional(p1, p2);
		}
	}
	
	public final void unbind() {
		for(Pair<Property<?>, Property<?>> p : bindedProperties){
			Bindings.unbindBidirectional(p.getKey(), p.getValue());
		}
	}
	
	private Property<?> jfxProperty(final String propertyName, final Object fromThis){
		try {
			final Method propertyGetter = fromThis.getClass().getMethod(propertyName + "Property");
			final Property<?> prop = (Property<?>) propertyGetter.invoke(fromThis);
			return prop;
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(String.format("did not found a JavaFX-Property %s in object of type %s", propertyName, fromThis.getClass().getName()));
		}
	}
	
	static Object objectFromField(final Field f, final Object source){
		try {
			final Object object = f.get(source);
			return object;
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			// in case of private Members we try to invoke a getter-Method
			final String getterName = "get" + capitalize(f.getName());
			try {
				Method method = source.getClass().getMethod(getterName);
				return method.invoke(source);
			} catch (NoSuchMethodException | SecurityException e1) {
				throw new RuntimeException("getter " + getterName + " not found at class " + source.getClass());
			} catch (IllegalAccessException e1) {
				throw new RuntimeException(e1);
			} catch (IllegalArgumentException e1) {
				throw new RuntimeException(e1);
			} catch (InvocationTargetException e1) {
				throw new RuntimeException(e1);
			}
		}
	}
	
	static String capitalize(String firstUp){
		if(firstUp.length() == 1){
			return firstUp.toUpperCase();
		}
		String firstLetter = firstUp.substring(0, 1).toUpperCase();
		return firstLetter + firstUp.substring(1);
	}
	
	private boolean supportsCombined(final Class<?> c){
		final Class<?>[] interfaces = c.getInterfaces();
		for(Class<?> i : interfaces){
			if(i.equals(SupportCombined.class))
				return true;
		}
		return false;
	}
}
