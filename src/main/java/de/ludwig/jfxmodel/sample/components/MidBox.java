package de.ludwig.jfxmodel.sample.components;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.converter.IntegerStringConverter;
import de.ludwig.jfxmodel.BindInheritedToBeanProperty;
import de.ludwig.jfxmodel.BindToBeanProperty;
import de.ludwig.jfxmodel.Model;
import de.ludwig.jfxmodel.SupportCombined;
import de.ludwig.jfxmodel.sample.beans.MidBean;

@BindInheritedToBeanProperty(bindings = { @BindToBeanProperty(bindInheritedProperty = "minHeight") })
public class MidBox extends VBox implements Initializable, SupportCombined {
	@BindToBeanProperty(bindPropertyName = "text")
	@FXML
	private Text midText;
	
	@BindToBeanProperty(bindPropertyName = "text", converter = IntegerStringConverter.class)
	@FXML
	private Text aNumber;

	@BindToBeanProperty
	@FXML
	private BottomBox bottom;

	private Model<MidBean> model = new Model<>(this, new MidBean());

	public MidBox() {
		FXMLLoader loader = new FXMLLoader(
				MidBox.class
						.getResource("/de/ludwig/jfxmodel/sample/components/MidBox.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		model.bind();
	}

	@Override
	public Model<MidBean> getModel() {
		return model;
	}
}
