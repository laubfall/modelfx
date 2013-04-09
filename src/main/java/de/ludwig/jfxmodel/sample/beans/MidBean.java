package de.ludwig.jfxmodel.sample.beans;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MidBean {
	private BottomBean bottom = new BottomBean();

	private SimpleStringProperty midText = new SimpleStringProperty();

	private SimpleIntegerProperty aNumber = new SimpleIntegerProperty(4711);
	
	private SimpleDoubleProperty width = new SimpleDoubleProperty();
	
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

	/**
	 * @return the aNumber
	 */
	public Integer getaNumber() {
		return aNumber.get();
	}

	/**
	 * @param aNumber the aNumber to set
	 */
	public void setaNumber(Integer aNumber) {
		this.aNumber.set(aNumber);
	}
	
	public SimpleIntegerProperty aNumberProperty() {
		return aNumber;
	}

	/**
	 * @return the width
	 */
	public Double getWidth() {
		return width.getValue();
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(Double width) {
		this.width.set(width);
	}
	
	public SimpleDoubleProperty widthProperty() {
		return width;
	}
}
