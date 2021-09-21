package view;

import java.io.IOException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
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
public class CopyPhoto {
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
	private PhotoObj currPhoto;
	
	/** from associate album label
	 */
	@FXML
	private Label fromAlbumLabel;
	/** list view of album
	 */
	@FXML
	private ListView<String> listView;
	/** list view of album
	 */
	private ObservableList<String> obsList;
	/** Confirm transfer
	 * @exception throws IOException
	 */
	@FXML
	void Confirm() throws IOException {
		if (obsList.isEmpty()) {
			Photo.DisplayInfoMsg("Nothing to copy photo to", stage);
			return;
		}
		int index = listView.getSelectionModel().getSelectedIndex();
		if (index < 0) {
			return;
		}
		String destAlbumName = obsList.get(index);
		for (Album a : user.albums) {
			if (a == null)
				continue;
			System.out.println(a.name + "\t" + destAlbumName);
			if (a.name.equals(destAlbumName)) {
				for (PhotoObj p : a.photos) {
					if (p == null)
						continue;
					if (p.caption.equals(currPhoto.caption)) {
						Photo.DisplayInfoMsg("Album contain the same photo", stage);
						return;
					}
				}
				a.addPhoto(currPhoto);
				break;
			}
		}
		// go back to photo system
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/PhotoSystem.fxml"));
		Pane root = (Pane) loader.load();

		PhotoSystem dps = loader.getController();
		dps.start(this.stage, user, app, album);
		stage.setTitle("Photo");
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	/** cancel transfer
	 * @param e mouse event
	 * @exception throws IOException
	 */
	@FXML
	void Cancel(MouseEvent e) throws IOException {
		// go back to photo system
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/PhotoSystem.fxml"));
		Pane root = (Pane) loader.load();
		
		PhotoSystem dps = loader.getController();
		dps.start(this.stage, user, app, album);
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
		Photo.SaveData(app);
		System.exit(0);
	}
	/** start method for this page
	 * @param stage parent stage
	 * @param user user parent user
	 * @param app data
	 * @param album user's album
	 * @param currPhoto user's photo
	 */
	public void start(Stage stage, User user, dataController app, Album album, PhotoObj currPhoto) {
		this.stage = stage;
		this.user = user;
		this.app = app;
		this.album = album;
		this.currPhoto = currPhoto;
		
		fromAlbumLabel.setText(album.name);
		
		obsList = FXCollections.observableArrayList(loadAlbumNames());
		listView.setItems(obsList);
		listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		if (!obsList.isEmpty()) {
			listView.getSelectionModel().select(0);
		}
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
