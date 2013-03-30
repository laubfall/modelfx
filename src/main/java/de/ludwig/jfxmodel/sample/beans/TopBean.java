package de.ludwig.jfxmodel.sample.beans;

import java.util.List;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;

public class TopBean {
	private MidBean midBox;

	private SimpleListProperty<String> messages = new SimpleListProperty<>();

	private SimpleStringProperty messageOfTheDay = new SimpleStringProperty();

	
	/**
	 * @return the messages
	 */
	public List<String> getMessages() {
		return messages.get();
	}

	/**
	 * @param messages
	 *            the messages to set
	 */
	public void setMessages(List<String> messages) {
		this.messages.addAll(messages);
	}

	/**
	 * @return the midBox
	 */
	public MidBean getMidBox() {
		return midBox;
	}

	/**
	 * @param midBox the midBox to set
	 */
	public void setMidBox(MidBean midBox) {
		this.midBox = midBox;
	}

	/**
	 * @return the messageOfTheDay
	 */
	public String getMessageOfTheDay() {
		return messageOfTheDay.get();
	}

	/**
	 * @param messageOfTheDay
	 *            the messageOfTheDay to set
	 */
	public void setMessageOfTheDay(String messageOfTheDay) {
		this.messageOfTheDay.set(messageOfTheDay);
	}
	
	public SimpleListProperty<String> messagesProperty(){
		return messages;
	}
	
	public SimpleStringProperty messageOfTheDayProperty(){
		return messageOfTheDay;
	}
}
