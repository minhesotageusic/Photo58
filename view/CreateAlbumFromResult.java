package view;

import java.io.IOException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import photo.Photo;
/**
 * @author Yunhao Xu
 * @author Minhesota Geusic
 * @version %I% %G%
 * @since 1.2
 */
public class CreateAlbumFromResult {
	/** parent stage that initiated this page
	 */
	private Stage stage;
	/** user that initiated this page
	 */
	private User user;
	/**
	 * instance of the app's data
	 */
	private dataController app;
	/** album associated with the initated user
	 */
	private Album album;
	/** list of album
	 */
	private ObservableList<String> obsList;
	/** new album name
	 */
	@FXML
	private TextField albumNameText;
	/** confirm creation
	 * @exception throws IOException
	 */
	@FXML
	void Confirm() throws IOException {
		
		String albumName = albumNameText.getText();
		if(albumName == null) {
			albumName = "";
		}
		
		if(!AdminSystem.nameValid(albumName, obsList)) {
			Photo.DisplayErrorMsg(1, stage);
			return;
		}
		album.name = albumName;
		user.albums.add(album);
		
		// go back to photo system
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/NonAdminPage.fxml"));
		Pane root = (Pane) loader.load();

		NonAdminSystem dps = loader.getController();
		dps.start(this.stage, user, app);
		stage.setTitle("Photo");
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	/** cancel creation
	 * @param e mouse event
	 * @exception throws IOException
	 */
	@FXML
	void Cancel(MouseEvent e) throws IOException {
		// go back to photo system
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/NonAdminPage.fxml"));
		Pane root = (Pane) loader.load();

		NonAdminSystem dps = loader.getController();
		dps.start(this.stage, user, app);
		stage.setTitle("Photo");
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	/** Exit application
	 * @param e mouse event
	 */
	@FXML
	void Exit(MouseEvent e) {
		// if this album has not been added to app
		// then it will not be saved
		Photo.SaveData(app);
		System.exit(0);
	}
	/** start method for this page
	 * @param stage parent stage
	 * @param user user parent user
	 * @param app data
	 * @param album user's album
	 */
	public void start(Stage stage, User user, dataController app, Album album) {
		this.stage = stage;
		this.user = user;
		this.app = app;
		this.album = album;

		obsList = FXCollections.observableArrayList(loadAlbumNames());
	}

	/**
	 * load a list of album name not including the given album
	 * 
	 * @return a list of album name
	 */
	public ArrayList<String> loadAlbumNames() {
		ArrayList<String> albumName = new ArrayList<String>();
		for (int i = 0; i < user.albums.size(); i++) {
			if (user.albums.get(i) != album) {
				albumName.add(user.albums.get(i).name);
			}
		}
		return albumName;
	}
}
