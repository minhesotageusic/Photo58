package view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
public class DisplayPhotoSystem {
	
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
	/** photo associated with the album
	 */
	private PhotoObj photoObj;
	/** image view of photo
	 */
	@FXML
	public ImageView displayPhotoImageView;
	/** list view of tag
	 */
	@FXML
	public ListView<String> displayPhotoTagListView;
	/** list view of tag
	 */
	private ObservableList<String> obsList;
	/** photo date
	 */
	@FXML
	public Label displayPhotoDate;	
	/** photo caption
	 */
	@FXML
	public Label displayPhotoText;
	/** Exit application
	 * @param event mouse event
	 */
	@FXML
	void Exit(MouseEvent event) {
		Photo.SaveData(app);
		System.exit(0);
	}
	/** Go to photo system
	 * @param event mouse event
	 * @exception throws IOException
	 */
	@FXML
	void Back(MouseEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/PhotoSystem.fxml"));
		Pane root = (Pane) loader.load();

		PhotoSystem ps = loader.getController();
		ps.start(this.stage, user, app, album);
		stage.setTitle("Photo");
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	/**start method for this page
	 * @param stage parent stage
	 * @param user user parent user
	 * @param app data
	 * @param album user's album
	 * @param photoObj user's photo
	 * @throws FileNotFoundException
	 */
	public void start(Stage stage,  User user, dataController app, Album album, PhotoObj photoObj) throws FileNotFoundException {
		this.photoObj = photoObj;
		this.stage = stage;
		this.user = user;
		this.app = app;
		this.album = album;
		displayPhotoText.setText(photoObj.caption);
		displayPhotoDate.setText(photoObj.date);
		InputStream stream = new FileInputStream(photoObj.photoPath);
		Image image = new Image(stream);
		displayPhotoImageView.setImage(image);
		displayPhotoImageView.setPreserveRatio(true);
		
		ArrayList<String> arr = new ArrayList<String>();
		for (int i = 0; i < photoObj.tags.size(); i++) {
			Tag t = (Tag) photoObj.tags.get(i);
			for (int j = 0; j < t.tagValue.size(); j++) {
				arr.add(t.tagName + ", " + t.tagValue.get(j));
			}
		}
		obsList = FXCollections.observableArrayList(arr);
		displayPhotoTagListView.setItems(obsList);
		displayPhotoTagListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		if (!obsList.isEmpty()) {
			displayPhotoTagListView.getSelectionModel().select(0);
		}
		
	}
}
