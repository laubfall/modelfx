package de.ludwig.jfxmodel.sample;

import de.ludwig.jfxmodel.sample.components.TopBox;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ModelSampleApp extends Application {

	@Override
	public void start(Stage primaryStage) {
		final TopBox tb = new TopBox();
		final Scene s = new Scene(tb.getContent());
		primaryStage.setScene(s);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
