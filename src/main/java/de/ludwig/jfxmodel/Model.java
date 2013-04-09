package de.ludwig.jfxmodel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import javafx.util.StringConverter;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * Description goes here...
 * 
 * @author Daniel
 * 
 * @param <MODELBEAN>
 *            type of the backing-bean
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
		if (owner == null) {
			throw new RuntimeException(
					"please provide the owner of this Model-Instance!");
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
	private void setModelObjectRaw(Object bean) {
		setModelObject((MODELBEAN) bean);
	}

	public void setModelObject(MODELBEAN bean) {
		modelObject = (MODELBEAN) bean;
	}

	public final void bind() {
		if (modelObject == null) {
			throw new RuntimeException(
					"you have to set a backing-bean to the model in order to call bind(), "
							+ ownerMsgPart());
		}

		final Field[] declaredFields = owner.getClass().getDeclaredFields();
		for(Field f : declaredFields) {
//		for (Pair<Field, BindToBeanProperty> fb : bindToBeanPropertyFields()) {

//			final Field f = fb.getKey();
//			final BindToBeanProperty btb = fb.getValue();
			final BindToBeanProperty btb = f
					.getAnnotation(BindToBeanProperty.class);
			if (btb == null)
				continue;

			// GIT-2
			f.setAccessible(true);

			// at this point we know we have properties that requires model
			// support
			boolean supportsCombined = supportsCombined(f.getType());
			if (supportsCombined) {
				// binding done by component.
				// now get the model-object and put it into this one
				try {
					final SupportCombined interestedInModel = (SupportCombined) f
							.get(owner);
					Object useThis = PropertyUtils.getProperty(modelObject,
							f.getName());
					if (useThis == null) {
						throw new RuntimeException(
								"can't set modelObject to support combined because it is null, supportCombined: "
										+ interestedInModel.getClass()
												.getName()
										+ " model "
										+ ownerMsgPart());
					}
					interestedInModel.getModel().unbind();
					interestedInModel.getModel().setModelObjectRaw(useThis);
					interestedInModel.getModel().bind();
					supportCombinedAware(interestedInModel);
				} catch (IllegalArgumentException | IllegalAccessException
						| InvocationTargetException | NoSuchMethodException e) {
					throw new RuntimeException(
							"exception while setting combined model "
									+ ownerMsgPart(), e);
				}
			} else {
				bind(btb, f);
			}
		}
	}

	public final void unbind() {
		for (Pair<Property<?>, Property<?>> p : bindedProperties) {
			Bindings.unbindBidirectional(p.getKey(), p.getValue());
		}
	}

	private void supportCombinedAware(Object o) {
		if (o instanceof SupportCombinedAware == false)
			return;

		final SupportCombinedAware sca = (SupportCombinedAware) o;
		sca.afterCombinedBinding();
	}

	/**
	 * 
	 * @return set of fields that are annotated with {@link BindToBeanProperty}.
	 *         Included are inherited Fields, if the owner-class has Annotations
	 *         of type {@link BindToBeanProperty}. In the last case
	 *         {@link BindToBeanProperty#bindPropertyName()} is required!
	 */
	private Set<Pair<Field, BindToBeanProperty>> bindToBeanPropertyFields() {
		final Set<Pair<Field, BindToBeanProperty>> annotated = new HashSet<>();
		final Field[] declaredFields = owner.getClass().getDeclaredFields();
		for(Field f : declaredFields) {
			final BindToBeanProperty btb = f
					.getAnnotation(BindToBeanProperty.class);
			if (btb == null)
				continue;
			
			annotated.add(new Pair<Field, BindToBeanProperty>(f, btb));
		}
		
		final BindInheritedToBeanProperty bindInherited = owner.getClass().getAnnotation(BindInheritedToBeanProperty.class);
		if(bindInherited == null){
			return annotated;
		}
		
		final BindToBeanProperty[] bindings = bindInherited.bindings();
		for(BindToBeanProperty btb : bindings) {
			final String supClassPropName = btb.bindSuperclassProperty();
			if(supClassPropName == null || supClassPropName.equals("")) {
				throw new RuntimeException("can't retrieve inherited field because bindSuperclassProperty of BindToBeanProperty is not set, " + ownerMsgPart());
			}
		}
		return annotated;
	}

	/**
	 * Decides to bind with a converter or not.
	 * 
	 * @param beanBinding
	 * @param jfxComponentField
	 */
	private void bind(final BindToBeanProperty beanBinding,
			final Field jfxComponentField) {
		final Class<? extends StringConverter<?>> converter = beanBinding
				.converter();
		final boolean dummyConverter = converter
				.isAssignableFrom(DummyConverter.class);
		final String bindPropertyName = beanBinding.bindPropertyName();
		final Object objectFromField = objectFromField(jfxComponentField, owner);
		if (objectFromField == null) {
			throw new RuntimeException(
					"property to bind is null, forgot to initiate field "
							+ jfxComponentField.getName() + "? "
							+ ownerMsgPart());
		}

		if (dummyConverter) {
			final Property<Object> jfxComponentProp = jfxProperty(
					bindPropertyName, objectFromField);
			final Property<Object> modelObjectProp = jfxProperty(
					jfxComponentField.getName(), modelObject);
			bind(jfxComponentProp, modelObjectProp);
			bindedProperties.add(new Pair<Property<?>, Property<?>>(
					jfxComponentProp, modelObjectProp));
		} else {
			final Property<String> jfxComponentProp = jfxProperty(
					bindPropertyName, objectFromField);
			final Property<Object> modelObjectProp = jfxProperty(
					jfxComponentField.getName(), modelObject);
			bind(jfxComponentProp, modelObjectProp, converter(beanBinding));
			bindedProperties.add(new Pair<Property<?>, Property<?>>(
					jfxComponentProp, modelObjectProp));
		}
	}

	@SuppressWarnings("unchecked")
	private void bind(Property<Object> p1, Property<Object> p2) {
		if (p1 == null || p2 == null) {
			throw new RuntimeException(
					"one or both properties to bind is / are null. normally that means that you do not have initialized the Property, "
							+ ownerMsgPart());
		}

		if (p1 instanceof ObservableList && p2 instanceof ObservableList) {
			Bindings.bindContentBidirectional((ObservableList<Object>) p1,
					(ObservableList<Object>) p2);
		} else {
			Bindings.bindBidirectional(p1, p2);
		}
	}

	private void bind(Property<String> p1, Property<Object> p2,
			StringConverter<Object> c) {
		if (p1 == null || p2 == null) {
			throw new RuntimeException(
					"one or both properties to bind with converter is / are null. normally that means that you do not have initialized the Property, "
							+ ownerMsgPart());
		}

		Bindings.bindBidirectional(p1, p2, c);
	}

	@SuppressWarnings("unchecked")
	private <C extends StringConverter<Object>> C converter(
			final BindToBeanProperty btb) {
		final Class<? extends StringConverter<?>> converter = btb.converter();
		StringConverter<?> con;
		try {
			con = converter.newInstance();
			return (C) con;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("unable to instantiate "
					+ converter.getName() + ", " + ownerMsgPart());
		}
	}

	@SuppressWarnings("unchecked")
	private <P extends Property<?>> P jfxProperty(final String propertyName,
			final Object fromThis) {
		// GIT-4 if no name is given we return fromThis. Throws
		// runtime-Exception if fromThis is not of type Property
		if (propertyName == null || propertyName.equals("")) {
			if (fromThis instanceof Property == false) {
				throw new RuntimeException(
						"wanted to return field as jfxProperty but is not of type Property: "
								+ fromThis.getClass().getName() + ", "
								+ ownerMsgPart());
			}
			return (P) fromThis;
		}

		try {
			final Method propertyGetter = fromThis.getClass().getMethod(
					propertyName + "Property");
			final P prop = (P) propertyGetter.invoke(fromThis);
			return prop;
		} catch (NoSuchMethodException | SecurityException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new RuntimeException(
					String.format(
							"did not found a JavaFX-Property %s in object of type %s, %s",
							propertyName, fromThis.getClass().getName(),
							ownerMsgPart()));
		}
	}

	private boolean supportsCombined(final Class<?> c) {
		final Class<?>[] interfaces = c.getInterfaces();
		for (Class<?> i : interfaces) {
			if (i.equals(SupportCombined.class)
					|| i.equals(SupportCombinedAware.class))
				return true;
		}
		return false;
	}

	/**
	 * little helper method for exception messages. Provides a string with
	 * information of the owning object of this model.
	 * 
	 * @return s. description
	 */
	private String ownerMsgPart() {
		return "owner: " + owner.getClass().getName();
	}

	static Object objectFromField(final Field f, final Object source) {
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
				throw new RuntimeException("getter " + getterName
						+ " not found at class " + source.getClass());
			} catch (IllegalAccessException e1) {
				throw new RuntimeException(e1);
			} catch (IllegalArgumentException e1) {
				throw new RuntimeException(e1);
			} catch (InvocationTargetException e1) {
				throw new RuntimeException(e1);
			}
		}
	}

	static String capitalize(String firstUp) {
		if (firstUp.length() == 1) {
			return firstUp.toUpperCase();
		}
		String firstLetter = firstUp.substring(0, 1).toUpperCase();
		return firstLetter + firstUp.substring(1);
	}
}
