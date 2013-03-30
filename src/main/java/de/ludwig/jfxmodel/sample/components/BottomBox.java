package de.ludwig.jfxmodel.sample.components;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import de.ludwig.jfxmodel.BindToBeanProperty;
import de.ludwig.jfxmodel.Model;
import de.ludwig.jfxmodel.SupportCombined;
import de.ludwig.jfxmodel.sample.beans.BottomBean;

public class BottomBox extends AnchorPane implements Initializable, SupportCombined {

	private Model<BottomBean> model = new Model<>(this);
	
	
	@BindToBeanProperty(bindPropertyName="text")
	public Text message;

	@BindToBeanProperty(bindPropertyName="items")
	public ListView<String> bottomMessages;
	
	/**
	 * 
	 */
	public BottomBox() {
		FXMLLoader loader = new FXMLLoader(MidBox.class.getResource("/de/ludwig/jfxmodel/sample/components/BottomBox.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Model<?> getModel() {
		return model;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		model.setModelObject(new BottomBean());
	}

}
