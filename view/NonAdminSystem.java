package view;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import photo.Photo;
/**
 * @author Yunhao Xu
 * @author Minhesota Geusic
 * @version %I% %G%
 * @since 1.2
 */
public class NonAdminSystem {
	
	/** user of this page
	 */
	private User user;
	/** instance of app's data
	 */
	private dataController app;
	/** list of album
	 */
	@FXML
	private ListView<String> listOfAlbum; // set the type to string so that we can show the name
	/** list of album
	 */
	private ObservableList<String> obsList;
	/** parent stage that initiated this page
	 */
	private Stage stage;
	/** search photo controller
	 */
	private SearchPhoto search;
	/** photo system controlelr
	 */
	private PhotoSystem photoSystem;
	/** list name
	 */
	@FXML
	private Text listName;
	/** album photo quantity
	 */
	@FXML
	private Text quantity;
	/** edit name
	 */
	@FXML
	private Text editName;
	/** date range
	 */
	@FXML
	private Text DateRange;
	/** number of photo
	 */
	@FXML
	private Text NumOfPhoto;
	/** album name
	 */
	@FXML
	private Text AlbumName;
	/** edit text field
	 */
	@FXML
	private TextField editField;
	/** date range field
	 */
	@FXML
	private Text dateRangeField;
	/** add button
	 */
	@FXML
	private Button add;
	/** delete album button
	 */
	@FXML
	private Button deleteAlbumBtn;
	/** rename album
	 */
	@FXML
	private Button renameAlbumBtn;
	/** open album
	 */
	@FXML
	private Button openAlbumBtn;
	/** rename confirm button
	 */
	@FXML
	private Button rename;
	/** Exit application
	 * @param event mouse event
	 */
	@FXML
	void Exit(MouseEvent event) {
		SaveData();
		System.exit(0);
	}
	/** logout 
	 * @param event mouse event
	 * @exception throws exception
	 */
	@FXML
	void Logout(MouseEvent event) throws Exception {
		SaveData();
		Photo.OpenLoginPage();
	}
	/** open create album page
	 * @param event mouse event
	 */
	@FXML
	void CreateAlbumBtn(MouseEvent event) {
		editField.clear();
		listOfAlbumPage(0);
		createORrenamePage(1);
		rename.setVisible(false);
	}
	/** open delete album page
	 * @param event mouse event
	 */
	@FXML
	void DeleteAlbumBtn(MouseEvent event) {
		if (obsList.isEmpty()) {
			Photo.DisplayInfoMsg("Nothing to remove", stage);
			return;
		}
		int index = listOfAlbum.getSelectionModel().getSelectedIndex();
		obsList.remove(index);
		user.albums.remove(index);
	}
	/** open album
	 * @param event mouse event
	 * @exception throw Exception
	 */
	@FXML
	void OpenAlbumBtn(MouseEvent event) throws Exception {
		if (obsList.isEmpty())
			return;
		int index = listOfAlbum.getSelectionModel().getSelectedIndex();
		if (index < 0) return;
		// get album
		Album album = user.albums.get(index);

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/PhotoSystem.fxml"));
		Pane root = (Pane) loader.load();

		photoSystem = loader.getController();
		photoSystem.start(this.stage, user, app, album);
		stage.setTitle("Photo");
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	/** rename album
	 * @param event mouse event
	 */
	@FXML
	void RenameAlbumBtn(MouseEvent event) {
		if (obsList.isEmpty())
			return;
		editField.clear();
		listOfAlbumPage(0);
		createORrenamePage(1);
		add.setVisible(false);
	}
	/** search album 
	 * @param event mouse event
	 * @exception throw exception
	 */
	@FXML
	void SearchAlbumBtn(MouseEvent event) throws Exception {
		if (obsList.isEmpty())
			return;
		int index = listOfAlbum.getSelectionModel().getSelectedIndex();
		if (index < 0)
			return;
		// get album
		Album album = user.albums.get(index);
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/SearchPhotoSystem.fxml"));
		Pane root = (Pane) loader.load();

		search = loader.getController();
		search.start(this.stage, app, user, album);
		stage.setTitle("Search");
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	/** add album to user
	 * @param event mouse event
	 */
	@FXML
	void addAlbum(MouseEvent event) {
		String albumName = editField.getText().trim();
		boolean validName = AdminSystem.nameValid(albumName, this.obsList);
		if (!validName) {
			photo.Photo.DisplayErrorMsg(1, stage);
			return;
		}
		Album newAlbum = new Album(albumName); // creates an new instance of album and adds to user's album
													// collection
		user.albums.add(newAlbum);
		obsList.add(newAlbum.name); // surfaces the name
		listOfAlbum.getSelectionModel().select(0);
	}
	/** rename album
	 * @param event mouse event
	 */
	@FXML
	void renameAlbum(MouseEvent event) {
		String albumName = editField.getText().trim();
		boolean validName = AdminSystem.nameValid(albumName, this.obsList);
		if (!validName) {
			photo.Photo.DisplayErrorMsg(1, stage);
			return;
		}
		int index = listOfAlbum.getSelectionModel().getSelectedIndex();
		obsList.set(index, albumName);
		listOfAlbum.getSelectionModel().select(0);
	}
	/** select album
	 * @param event mouse event
	 */
	@FXML
	void Select(MouseEvent event) {
		int index = listOfAlbum.getSelectionModel().getSelectedIndex();
		if (index >= 0) {
			createORrenamePage(0);
			listOfAlbumPage(1);
			displayAlbumInfo(user.albums.get(index));
		}
	}
	/** display album information
	 * @param current album
	 */
	private void displayAlbumInfo(Album current) { // shows the detail of an album
		AlbumName.setText(current.name);
		NumOfPhoto.setText("" + current.size());
		dateRangeField.setText(current.earliestPhoto + "~" + current.latestPhoto);
	}
	/** start method for this page
	 * @param stage parent stage
	 * @param user user parent user
	 * @param app data
	 */
	public void start(Stage stage, User user, dataController app) {
		createORrenamePage(0);
		this.stage = stage;
		this.user = user;
		this.obsList = FXCollections.observableArrayList(loadAlbum(user));
		this.listOfAlbum.setItems(this.obsList);
		this.app = app;
		listOfAlbum.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		listOfAlbum.getSelectionModel().select(0);
		if (!obsList.isEmpty())
			displayAlbumInfo(user.albums.get(0));
	}
	/** load album name based on user
	 * @param user target user
	 * @return list of album name
	 */
	private ArrayList<String> loadAlbum(User user) { // surfaces the list of the name of each album that the user has
		ArrayList<String> albumNames = new ArrayList<String>();
		for (int j = 0; j < user.albums.size(); j++) {
			albumNames.add(user.albums.get(j).name);
		}
		return albumNames;
	}
	/** list of album page
	 * @param code instruction
	 */
	private void listOfAlbumPage(int code) { // display the list of album
		boolean instruction = onOroff(code);
		listName.setVisible(instruction);
		AlbumName.setVisible(instruction);
		NumOfPhoto.setVisible(instruction);
		quantity.setVisible(instruction);
		DateRange.setVisible(instruction);
		dateRangeField.setVisible(instruction);
	}
	/** create or rename page
	 * @param code instruction
	 */
	private void createORrenamePage(int code) { // shift pane to edit mode
		boolean instruction = onOroff(code);
		editName.setVisible(instruction);
		editField.setVisible(instruction);
		rename.setVisible(instruction);
		add.setVisible(instruction);
	}
	/** decide instruction
	 * @param code instruction
	 * @return true if code is 0, false otherwise
	 */
	private boolean onOroff(int code) { // decide the instruction - 0 = false, 1 = true
		boolean instruction = code == 0 ? false : true;
		return instruction;
	}
	/** save data
	 */
	public void SaveData() {
		if (app == null)
			return;
		Photo.SaveData(app);
	}

}
