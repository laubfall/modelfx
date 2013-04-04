package de.ludwig.jfxmodel;

/**
 * Classes that implements this interface wanted to be notified after binding of
 * a model of a parent class. This becomes useful if you need to work on the
 * Model-Object of a parent class that were injected into the implementing one.
 * For example if it is necessary to add a PropertyChangeListner. Be careful! If
 * you add the listener to a JavaFX Property before this was binded to
 * Model-Object of the parent class, the listener will not work!
 * 
 * @author Daniel
 * 
 */
public interface SupportCombinedAware extends SupportCombined {
	/**
	 * Called by framework after injecting the model-object of the parent class and after binding.
	 */
	public void afterCombinedBinding();
}
