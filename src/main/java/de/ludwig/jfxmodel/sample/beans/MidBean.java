package de.ludwig.jfxmodel.sample.beans;

import javafx.beans.property.SimpleStringProperty;

public class MidBean {
	private BottomBean bottom = new BottomBean();

	private SimpleStringProperty midText = new SimpleStringProperty();

	/**
	 * @return the midText
	 */
	public String getMidText() {
		return midText.get();
	}

	/**
	 * @param midText the midText to set
	 */
	public void setMidText(String midText) {
		this.midText.set(midText);
	}
	
	public SimpleStringProperty midTextProperty(){
		return this.midText;
	}

	/**
	 * @return the bottom
	 */
	public BottomBean getBottom() {
		return bottom;
	}

	/**
	 * @param bottom the bottom to set
	 */
	public void setBottom(BottomBean bottom) {
		this.bottom = bottom;
	}
}
