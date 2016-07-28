package at.l2d2;

import at.l2d2.view.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mainview.fxml"));
		Parent root = loader.load();
		MainController controller = loader.getController();
		controller.setStage(primaryStage);

//		Parent root = FXMLLoader.load(getClass().getResource("/view/mainview.fxml"));
		primaryStage.setTitle("Solarwelten PriceChecker");
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
		primaryStage.setResizable(false);
	}
}
