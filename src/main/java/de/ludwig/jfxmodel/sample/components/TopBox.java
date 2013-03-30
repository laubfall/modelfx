package de.ludwig.jfxmodel.sample.components;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import de.ludwig.jfxmodel.BindToBeanProperty;
import de.ludwig.jfxmodel.Model;
import de.ludwig.jfxmodel.sample.beans.BottomBean;
import de.ludwig.jfxmodel.sample.beans.MidBean;
import de.ludwig.jfxmodel.sample.beans.TopBean;

public class TopBox implements Initializable {
	@BindToBeanProperty(bindPropertyName = "text")
	@FXML
	public Text messageOfTheDay;

	@FXML
	private ListView<String> messages;
	
	@BindToBeanProperty
	@FXML
	public MidBox midBox;
	
	private final AnchorPane content;
	
	private Model<TopBean> model = new Model<>(this);
	
	public TopBox(){
		FXMLLoader loader = new FXMLLoader(TopBox.class.getResource("/de/ludwig/jfxmodel/sample/components/TopBox.fxml"));
		loader.setController(this);
		try {
			content = (AnchorPane) loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		final TopBean topBean = new TopBean();
		topBean.setMessageOfTheDay("thats the message of the day");
		MidBean midBean = new MidBean();
		midBean.setMidText("hello world says tob box to mid box");
		BottomBean bottomBean = new BottomBean();
		bottomBean.setMessage("bottom message from top");
		bottomBean.getBottomMessages().add("top1");
		bottomBean.getBottomMessages().add("top2");
		midBean.setBottom(bottomBean);
		topBean.setMidBox(midBean);
		model.setModelObject(topBean);
		model.bind();
	}

	/**
	 * @return the content
	 */
	public AnchorPane getContent() {
		return content;
	}
}
