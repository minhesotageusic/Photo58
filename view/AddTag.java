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
public class AddTag {
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
	private PhotoObj photo;
	/** Pane containing tag to select from
	 */
	@FXML
	private Pane availableTagPane;
	/** list of tag to select from
	 */
	@FXML
	private ListView<String> tagListView;
	/** list of tag to select from
	 */
	private ObservableList<String> obsList1;
	/** Pane containing tag to for given photo
	 */
	@FXML
	private Pane availableTagPane1;
	/**list of tag contain by this photo
	 */
	@FXML
	private ListView<String> hasTagListView;
	/**list of tag contain by this photo
	 */
	private ObservableList<String> obsList2;
	/**tag text field
	 */
	@FXML
	private TextField tagTextField;
	/**value text field
	 */
	@FXML
	private TextField valueTextField;
	/**caption for selected photo
	 */
	@FXML
	private Label photoLabel;
	/**Exit the application
	 * @param e mouse event
	 */
	@FXML
	void Exit(MouseEvent e) {
		Photo.SaveData(app);
		System.exit(0);
	}
	/**Cancel Adding tag and return to photo system
	 * @param e mouse event
	 * @exception throws IOException if resource could not be loaded
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
	/** Finish adding tag and return to photo system
	 * @param e mouse event
	 * @exception throws IOException if resource could not be loaded
	 */
	@FXML
	void Finish(MouseEvent e) throws IOException {
		//save data
		Photo.SaveData(app);
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
	/**Open create tag panel
	 * @param e mouse event
	 */
	@FXML
	void CreateTag(MouseEvent e) {		
		tagTextField.clear();
		valueTextField.clear();
		
		availableTagPane.setVisible(false);
		availableTagPane1.setVisible(true);
	}
	/** Create tag
	 * @param e mouse event
	 */
	@FXML
	void ConfirmTagCreation(MouseEvent e) {
		String tag = tagTextField.getText();
		String value = valueTextField.getText();
		
		if(tag == null) tag = "";
		if(value == null) value = "";
		
		tag = tag.trim();
		value = value.trim();
		
		Tag t = new Tag(tag);
		t.tagValue.add(value);
		
		for(int j = 0; j < obsList1.size(); j++) {
			if(obsList1.get(j).equals(tag + ", " + value)) {
				Photo.DisplayErrorMsg(4, stage);
				return;
			}
		}
		for(int j = 0; j < obsList2.size(); j++) {
			if(obsList2.get(j).equals(tag + ", " + value)) {
				Photo.DisplayErrorMsg(4, stage);
				return;
			}
		}
		
		obsList1.add(tag + ", " + value);
		obsList2.add(tag + ", " + value);
		
		boolean foundPreviousTag = false;
		
		for(int j = 0; j < user.previousTag.size(); j++) {
			if(user.previousTag.get(j).tagName.equals(tag)) {
				user.previousTag.get(j).tagValue.add(value);
				foundPreviousTag = true;
				break;
			}
		}
		if(!foundPreviousTag) {
			user.previousTag.add(t);
		}
		
		for(int j = 0; j < photo.tags.size(); j++) {
			if(photo.tags.get(j).tagName.equals(tag)) {
				photo.tags.get(j).tagValue.add(value);
				foundPreviousTag = true;
				break;
			}
		}
		if(!foundPreviousTag) {
			photo.addTag(t);
		}
		
		tagTextField.clear();
		valueTextField.clear();
		availableTagPane.setVisible(true);
		availableTagPane1.setVisible(false);
	}
	/**Cancel tag creation
	 * @param e mouse event
	 */
	@FXML
	void CancelTagCreation(MouseEvent e) {
		tagTextField.clear();
		valueTextField.clear();
		availableTagPane.setVisible(true);
		availableTagPane1.setVisible(false);
	}
	/**confirm tag selection
	 * @param e mouse event
	 */
	@FXML
	void ConfirmTagSelect(MouseEvent e) {
		if(obsList1.isEmpty()) {
			Photo.DisplayInfoMsg("Noting to select", stage);
			return;
		}
		int i = tagListView.getSelectionModel().getSelectedIndex();
		if(i < 0) {
			Photo.DisplayInfoMsg("Noting to select", stage);
			return;
		}
		String s = obsList1.get(i);
		String[] arr = s.split(",");
		String tag = arr[0].trim();
		String value = arr[1].trim();
		
		Tag t = new Tag(tag);
		t.tagValue.add(value);
		
		//check duplicate
		for(int j = 0; j < obsList2.size(); j++) {
			if(obsList2.get(j).equals(s)) {
				Photo.DisplayErrorMsg(4, stage);
				return;
			}
		}
		obsList2.add(tag + ", " + value);
		boolean foundPreviousTag = false;
		for(int j = 0; j < photo.tags.size(); j++) {
			if(photo.tags.get(j).tagName.equals(tag)) {
				photo.tags.get(j).tagValue.add(value);
				foundPreviousTag = true;
				break;
			}
		}
		if(!foundPreviousTag) {
			photo.addTag(t);
		}
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
		this.photo = currPhoto;

		photoLabel.setText(photo.caption);

		availableTagPane.setVisible(true);
		availableTagPane1.setVisible(false);

		// load in premade tag
		ArrayList<String> arr1 = new ArrayList<String>();
		for (int i = 0; i < user.previousTag.size(); i++) {
			Tag t = user.previousTag.get(i);
			for (int j = 0; j < t.tagValue.size(); j++) {
				arr1.add(t.tagName + ", " + t.tagValue.get(j));
			}
		}
		System.out.println(arr1.toString());
		obsList1 = FXCollections.observableArrayList(arr1);
		tagListView.setItems(obsList1);
		tagListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		if (!obsList1.isEmpty()) {
			tagListView.getSelectionModel().select(0);
		}
		// load in tag already on the photo
		ArrayList<String> arr = new ArrayList<String>();
		for (int i = 0; i < photo.tags.size(); i++) {
			Tag t = (Tag) photo.tags.get(i);
			for (int j = 0; j < t.tagValue.size(); j++) {
				arr.add(t.tagName + ", " + t.tagValue.get(j));
			}
		}
		obsList2 = FXCollections.observableArrayList(arr);
		hasTagListView.setItems(obsList2);
		hasTagListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		if (!obsList2.isEmpty()) {
			hasTagListView.getSelectionModel().select(0);
		}

	}

}
