package de.ludwig.jfxmodel;

import javafx.util.StringConverter;

/**
 * Converter as Defaultvalue for the Annotation {@link BindToBeanProperty}
 * 
 * @author Daniel
 *
 */
abstract class DummyConverter extends StringConverter<Void> {

	@Override
	public Void fromString(String string) {
		return null;
	}

	@Override
	public String toString(Void object) {
		return null;
	}
}
