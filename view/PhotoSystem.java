package view;

import java.io.File;
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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import photo.Photo;
/**
 * @author Yunhao Xu
 * @author Minhesota Geusic
 * @version %I% %G%
 * @since 1.2
 */
public class PhotoSystem {
	/**non admin controller
	 */
	private NonAdminSystem nonAdmin;
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
	/** current photo's indexx
	 */
	private int currPhotoIndex;
	/** caption text field
	 */
	@FXML
	private TextField captionText;
	/** current photo caption text field
	 */
	@FXML
	private TextField currCaptionText;
	/** add photo pane
	 */
	@FXML
	private Pane addPhotoPane;
	/** caption pane
	 */
	@FXML
	private Pane captionPane;
	/** list pane
	 */
	@FXML
	private Pane listPane;
	/** delete tage pane
	 */
	@FXML
	private Pane deleteTagPane;
	/** list view of photos
	 */
	@FXML
	private ListView<String> listView;
	/** list view of photos
	 */
	@FXML
	private ListView<String> tagListView;
	
	private ObservableList<String> obsList;
	private ObservableList<String> obsList2;
	/** file chosen from local directory
	 */
	private File chosenFile;
	/** add photo from local directory 
	 * @param event mouse event
	 * @exception throw IOException
	 */
	@FXML
	void AddPhotoPage(MouseEvent event) throws IOException {
		chosenFile = null;
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Add Photo");

		// config file chooser's allowable file
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPEG", "*.jpeg"),
				new FileChooser.ExtensionFilter("PNG", "*.png"), new FileChooser.ExtensionFilter("BMP", "*.bmp"),
				new FileChooser.ExtensionFilter("GIF", "*.gif"));

		chosenFile = fileChooser.showOpenDialog(stage);
		if (chosenFile == null)
			return;

		// wait for user to enter photo caption
		listPane.setVisible(false);
		captionPane.setVisible(false);
		deleteTagPane.setVisible(false);
		addPhotoPane.setVisible(true);
	}
	/** add photo pane 
	 * @param event mouse event
	 */
	@FXML
	void AddPhotoPaneConfirm(MouseEvent event) {
		// currently checking caption not file name
		String text = captionText.getText();

		if (text == null) {
			text = "";
		}
		text = text.trim();

		// check duplicate photo
		if (!AdminSystem.nameValid(text, obsList)) {
			Photo.DisplayErrorMsg(3, stage);
			return;
		}

		PhotoObj photoObj = new PhotoObj();
		photoObj.photoPath = chosenFile.getAbsolutePath();
		photoObj.photoName = chosenFile.getName();
		photoObj.caption = text;
		album.addPhoto(photoObj);

		obsList.add(text);

		captionText.clear();

		addPhotoPane.setVisible(false);
		captionPane.setVisible(false);
		deleteTagPane.setVisible(false);
		listPane.setVisible(true);
		if (currPhoto == null) {
			currPhoto = findPhoto(obsList.get(0));
			currPhotoIndex = 0;
			listView.getSelectionModel().select(currPhotoIndex);
		}
	}
	/** save caption
	 * @param event mouse event
	 */
	@FXML
	void SaveCaption(MouseEvent event) {
		String text = currCaptionText.getText();
		if (text == null)
			text = "";
		text = text.trim();

		if (!AdminSystem.nameValid(text, obsList)) {
			Photo.DisplayErrorMsg(3, stage);
			return;
		}
		PhotoObj photoObj = currPhoto;
		int i = 0;
		for (i = 0; i < obsList.size(); i++) {
			if (obsList.get(i).equals(photoObj.caption))
				break;
		}
		if (i >= obsList.size())
			return;
		photoObj.caption = text;
		obsList.set(i, text);

		currCaptionText.clear();

		addPhotoPane.setVisible(false);
		captionPane.setVisible(false);
		deleteTagPane.setVisible(false);
		listPane.setVisible(true);
	}
	/** cancel 
	 * @param event mouse event
	 */
	@FXML
	void Cancel(MouseEvent event) {
		captionText.clear();
		currCaptionText.clear();
		addPhotoPane.setVisible(false);
		captionPane.setVisible(false);
		deleteTagPane.setVisible(false);
		listPane.setVisible(true);
		if (obsList.isEmpty())
			return;
		if (currPhoto == null) {
			currPhoto = findPhoto(obsList.get(0));
			currPhotoIndex = 0;
			listView.getSelectionModel().select(currPhotoIndex);
		}
	}
	/** add tag page
	 * @param event mouse event
	 * @exception throw IOException
	 */	
	@FXML
	void AddTagPage(MouseEvent event) throws IOException {
		//open tag page
		if(obsList.isEmpty()) {
			Photo.DisplayInfoMsg("No photo to tag to", stage);
			return;
		}
		if (currPhoto == null) {
			currPhoto = findPhoto(obsList.get(0));
			currPhotoIndex = 0;
			listView.getSelectionModel().select(currPhotoIndex);
		}
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/AddTag.fxml"));
		Pane root = (Pane) loader.load();

		AddTag at = loader.getController();
		at.start(this.stage, user, app, album, currPhoto);
		stage.setTitle("Add Tag");
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	/** go to non admin page
	 * @param event mouse event
	 * @exception throw exception
	 */
	@FXML
	void Back(MouseEvent event) throws Exception {
		// we dont have to save when backing since we are
		// passing along the app object
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/NonAdminPage.fxml"));
		Pane root = (Pane) loader.load();

		nonAdmin = loader.getController();
		nonAdmin.start(this.stage, user, app);
		stage.setTitle("User");
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	/** set caption page
	 * @param event mouse event
	 */
	@FXML
	void CaptionPage(MouseEvent event) {
		currCaptionText.clear();

		if (obsList.isEmpty()) {
			currPhoto = null;
			Photo.DisplayInfoMsg("Nothing to caption", stage);
			return;
		}

		if (currPhoto == null) {
			return;
		}
		currCaptionText.setText(currPhoto.caption);

		addPhotoPane.setVisible(false);
		listPane.setVisible(false);
		deleteTagPane.setVisible(false);
		captionPane.setVisible(true);
	}
	/**
	 * open copy page
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void Copy(MouseEvent event) throws IOException {
		if(obsList.isEmpty()) {
			Photo.DisplayInfoMsg("Noting to copy", stage);
			return;
		}
		if( currPhoto == null ) {
			currPhoto = findPhoto(obsList.get(0));
			currPhotoIndex = 0;
			listView.getSelectionModel().select(currPhotoIndex);
		}
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/CopyPhoto.fxml"));
		Pane root = (Pane) loader.load();

		CopyPhoto dps = loader.getController();
		dps.start(this.stage, user, app, album, currPhoto);
		stage.setTitle("Copy Photo");
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	/**
	 * open delete tag page
	 * @param event
	 */
	@FXML
	void DeleteTagPage(MouseEvent event) {
		if( currPhoto == null ) {
			currPhoto = findPhoto(obsList.get(0));
			currPhotoIndex = 0;
			listView.getSelectionModel().select(currPhotoIndex);
		}
		ArrayList<String> arr = new ArrayList<String>();
		for (int i = 0; i < currPhoto.tags.size(); i++) {
			Tag t = (Tag) currPhoto.tags.get(i);
			for (int j = 0; j < t.tagValue.size(); j++) {
				arr.add(t.tagName + ", " + t.tagValue.get(j));
			}
		}		
		
		obsList2 = FXCollections.observableArrayList(arr);
		tagListView.setItems(obsList2);
		tagListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);	
		
		if(!obsList2.isEmpty()) {
			tagListView.getSelectionModel().select(0);
		}
		
		addPhotoPane.setVisible(false);
		listPane.setVisible(false);
		captionPane.setVisible(false);
		deleteTagPane.setVisible(true);
	}
	/**
	 * delete selected tag
	 * @param event
	 */
	@FXML
	void DeleteSelectTag(MouseEvent event) {
		if(obsList2.isEmpty()) {
			Photo.DisplayInfoMsg("Nothing to delete", stage);
			return;
		}
		int i = tagListView.getSelectionModel().getSelectedIndex();
		if( i < 0) {
			Photo.DisplayInfoMsg("Nothing to delete", stage);
			return;
		}
		String s = obsList2.get(i);
		String[] arr = s.split(",");
		String tag = arr[0].trim();
		String value = arr[1].trim();
		
		Tag t = new Tag(tag);
		t.tagValue.add(value);
		for(int j = 0; j < currPhoto.tags.size(); j++) {
			Tag tempT = currPhoto.tags.get(j);
			for(int g = 0; g < tempT.tagValue.size(); g++) {
				if(tempT.tagName.equals(tag) && tempT.tagValue.get(g).equals(value)) {
					tempT.tagValue.remove(g);
					obsList2.remove(i);
					if(tempT.tagValue.isEmpty()) {
						currPhoto.tags.remove(j);
					}
					break;
				}
			}
		}
		
	}
	/**
	 * open display photo page
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void DisplayPhotoPage(MouseEvent event) throws IOException {
		if (obsList.isEmpty()) {
			Photo.DisplayInfoMsg("Nothing to display", stage);
			return;
		}
		if( currPhoto == null ) {
			currPhoto = findPhoto(obsList.get(0));
			currPhotoIndex = 0;
			listView.getSelectionModel().select(currPhotoIndex);
		}
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/DisplayPhoto.fxml"));
		Pane root = (Pane) loader.load();

		DisplayPhotoSystem dps = loader.getController();
		dps.start(this.stage, user, app, album, currPhoto);
		stage.setTitle("Photo");
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	/** Exit application
	 * @param event mouse event
	 */
	@FXML
	void Exit(MouseEvent event) {
		Photo.SaveData(app);
		System.exit(0);
	}
	/** goto move photo page
	 * @param event mouse event
	 * @exception throw IOException
	 */
	@FXML
	void Move(MouseEvent event) throws IOException {
		if(obsList.isEmpty()) {
			Photo.DisplayInfoMsg("Noting to move", stage);
			return;
		}
		if( currPhoto == null ) {
			currPhoto = findPhoto(obsList.get(0));
			currPhotoIndex = 0;
			listView.getSelectionModel().select(currPhotoIndex);
		}
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/MovePhoto.fxml"));
		Pane root = (Pane) loader.load();

		MovePhoto dps = loader.getController();
		dps.start(this.stage, user, app, album, currPhoto);
		stage.setTitle("Move Photo");
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	/** goto next photo 
	 * @param event mouse event
	 */
	@FXML
	void Next(MouseEvent event) {
		if (obsList.isEmpty())
			return;
		if (currPhoto == null) {
			currPhoto = findPhoto(obsList.get(0));
			currPhotoIndex = 0;
			listView.getSelectionModel().select(currPhotoIndex);
			return;
		}
		
		int index = listView.getSelectionModel().getSelectedIndex();
		if(index >= 0) {
			currPhotoIndex = index;
			listView.getSelectionModel().select(index);
			currPhoto = findPhoto(obsList.get(index));
		}
		
		currPhotoIndex++;
		if (currPhotoIndex >= obsList.size()) {
			currPhotoIndex = 0;
		}
		currPhoto = findPhoto(obsList.get(currPhotoIndex));
		listView.getSelectionModel().select(currPhotoIndex);
	}
	/** goto previous photo
	 * @param event mouse event
	 */
	@FXML
	void Previous(MouseEvent event) {
		if (obsList.isEmpty())
			return;
		if (currPhoto == null) {
			currPhoto = findPhoto(obsList.get(0));
			currPhotoIndex = 0;
			listView.getSelectionModel().select(currPhotoIndex);
			return;
		}
		int index = listView.getSelectionModel().getSelectedIndex();
		if(index >= 0) {
			currPhotoIndex = index;
			listView.getSelectionModel().select(index);
			currPhoto = findPhoto(obsList.get(index));
		}
		currPhotoIndex--;
		if (currPhotoIndex < 0) {
			currPhotoIndex = obsList.size() - 1;
		}
		currPhoto = findPhoto(obsList.get(currPhotoIndex));
		listView.getSelectionModel().select(currPhotoIndex);
	}
	/** remove photo
	 * @param event mouse event
	 */
	@FXML
	void RemovePhotoPage(MouseEvent event) {
		// nothing to remove
		if (obsList.isEmpty()) {
			Photo.DisplayInfoMsg("Nothing to remove", stage);
			return;
		}
		int i = listView.getSelectionModel().getSelectedIndex();
		if (i < 0) {
			return;
		}
		obsList.remove(i);
		album.removePhoto(album.photos.get(i));
		if(obsList.isEmpty()) {
			currPhoto = null;
			currPhotoIndex = -1;
			return;
		}
		currPhotoIndex = listView.getSelectionModel().getSelectedIndex();
		currPhoto = findPhoto(obsList.get(currPhotoIndex));
	}
	/** start method for this page
	 * @param stage parent stage
	 * @param user user parent user
	 * @param app data
	 * @param album user's album
	 */
	public void start(Stage stage, User user, dataController app, Album album) {
		this.user = user;
		this.stage = stage;
		this.app = app;
		this.album = album;

		listPane.setVisible(true);
		addPhotoPane.setVisible(false);
		captionPane.setVisible(false);
		deleteTagPane.setVisible(false);

		obsList = FXCollections.observableArrayList(loadPhotoNames());
		listView.setItems(obsList);
		listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		// allow list view to have picture for each entry
		listView.setCellFactory(param -> new ListCell<String>() {
			private ImageView imageView = new ImageView();

			public void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setText(null);
					setGraphic(null);
					return;
				}
				try {
					// retrieve photoobj associated with item
					PhotoObj photoObj = album.getPhotoByCaption(item);

					if (photoObj == null)
						return;

					// load in the photo
					InputStream stream = new FileInputStream(photoObj.photoPath);
					Image image = new Image(stream);
					imageView.setImage(image);
					imageView.setX(0);
					imageView.setY(0);
					imageView.setFitWidth(100);
					imageView.setFitHeight(80);
					imageView.setPreserveRatio(true);
					// set graphic
					if (item != null) {
						setText(photoObj.caption);
						setGraphic(imageView);
					}
				} catch (FileNotFoundException e) {

				}
			}
		});
		if (!obsList.isEmpty()) {
			listView.getSelectionModel().select(0);
			currPhoto = findPhoto(obsList.get(0));
		}
	}
	/** find photo with given caption
	 * @param caption target caption
	 * @return photo object associated with the caption
	 */
	private PhotoObj findPhoto(String caption) {
		if (album == null)
			return null;
		for (PhotoObj po : album.photos) {
			if (po == null)
				continue;
			if (po.caption.equals(caption))
				return po;
		}
		return null;
	}
	/** load photo names from album
	 * @return list of photo
	 */
	public ArrayList<String> loadPhotoNames() {
		ArrayList<String> photoNames = new ArrayList<String>();
		for (int i = 0; i < album.photos.size(); i++) {
			photoNames.add(album.photos.get(i).caption);
		}
		return photoNames;
	}

}
