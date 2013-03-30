package de.ludwig.jfxmodel.sample.beans;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;

public class BottomBean {
	private SimpleStringProperty message = new SimpleStringProperty();

	private SimpleListProperty<String> bottomMessages = new SimpleListProperty<>();
	
	/**
	 * 
	 */
	public BottomBean() {
		bottomMessages.set(FXCollections.observableArrayList(new ArrayList<String>()));
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message.get();
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message.set(message);
	}
	
	public SimpleStringProperty messageProperty(){
		return message;
	}

	/**
	 * @return the bottomMessages
	 */
	public List<String> getBottomMessages() {
		return bottomMessages.get();
	}

	/**
	 * @param bottomMessages the bottomMessages to set
	 */
	public void setBottomMessages(List<String> bottomMessages) {
		this.bottomMessages.addAll(bottomMessages);
	}
	
	public SimpleListProperty<String> bottomMessagesProperty(){
		return bottomMessages;
	}
}
