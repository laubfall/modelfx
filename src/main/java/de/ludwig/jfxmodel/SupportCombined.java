package de.ludwig.jfxmodel;

/**
 * Classes that implements this interface usually create a {@link Model} for themselves and return this model by the only Method of this interface.
 * Classes that implements this interface allows injection of Backing-Beans by components that hold an instance of the implementing class.
 * 
 * @author Daniel
 *
 */
public interface SupportCombined {
	public Model<?> getModel();
}
