package photo;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import view.LoginSystem;
import view.dataController;
/**
 * @author Yunhao Xu
 * @author Minhesota Geusic
 * @version %I% %G%
 * @since 1.2
 */
public class Photo extends Application {
	/** location for stocks photo
	 */
	public static String[] stockPhotoLocation;
	/**
	 * controller for login system
	 */
	private static LoginSystem controller;
	/**
	 * primary stage
	 */
	private static Stage primaryStage;
	/**
	 * display an error based on the option for the given stage
	 * 
	 * @param option error message
	 * @param owner	stage to attach error message to
	 */
	public static void DisplayErrorMsg(int option, Stage owner) {
		String errorMessage = "";
		switch (option) {
		case 0:
			errorMessage = "No Such User";
			break;
		case 1:
			errorMessage = "Name is invalid";
			break;
		case 2:
			errorMessage = "Duplicate name";
			break;
		case 3:
			errorMessage = "No items found";
			break;
		case 4:
			errorMessage = "Photo already contain this tag-value pair";
			break;
		}
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(owner);
		alert.setHeaderText(errorMessage);
		alert.showAndWait();
	}
	/**
	 * display an information based on the message for the given stage
	 * 
	 * @param msg information message
	 * @param owner	stage to attach error message to
	 */
	public static void DisplayInfoMsg(String msg, Stage owner) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(owner);
		alert.setHeaderText(msg);
		alert.showAndWait();
	}
	/**
	 * display a confirmation based on the message for the given stage
	 * 
	 * @param msg confirmation message
	 * @param owner	stage to attach error message to
	 */
	public static void DisplayConfirmMsg(String msg, Stage owner) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.initOwner(owner);
		alert.setHeaderText(msg);
		alert.showAndWait();
	}
	/**
	 * Save the given app
	 * 
	 * @param app data to be save
	 */
	public static void SaveData(dataController app) {
		try {    		
	    	//request write to app
    		dataController.writeData(app);
	    }catch(IOException e) {
	    	//debug
	    	System.out.println(e);
	    }
	}
	/**
	 * open login page
	 * @throws Exception
	 */
	public static void OpenLoginPage() throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Photo.class.getResource("/view/loginPage.fxml"));
		Pane root = (Pane) loader.load();

		controller = loader.getController();
		controller.start(primaryStage);
		primaryStage.setTitle("Login");
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	/**
	 * Start method
	 * @param primaryStage the primary stage for this page
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		Photo.primaryStage = primaryStage;
		stockPhotoLocation = new String[5];
		stockPhotoLocation[0] = "photo/res/book.png";
		stockPhotoLocation[1] = "photo/res/download.png";
		stockPhotoLocation[2] = "photo/res/Ruby_logo.png";
		stockPhotoLocation[3] = "photo/res/stonks.png";
		stockPhotoLocation[4] = "photo/res/unnamed.png";
		
		OpenLoginPage();
	}
	/** main method
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

}
