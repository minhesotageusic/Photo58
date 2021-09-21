package view;

import java.io.IOException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
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
public class SearchPhoto {
	/** non-admin controlelr
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
	/** list view of photo
	 */
    @FXML
    private ListView<PhotoObj> listView;
    /** list view of photo
	 */
    private ObservableList<PhotoObj> obsList;
    /** date range
     */
    @FXML
    private Text dateRange;
    /** date range field
     */
    @FXML
    private TextField dateRangeField;
    /** search button
     */
    @FXML
    private Button SearchBtn;
    /** tag name
     */
    @FXML
    private Text tagName;
    /** tag value
     */
    @FXML
    private Text tagValue;
    /** tag name field
     */
    @FXML
    private TextField tagNameField;
    /** tag value field
     */
    @FXML
    private TextField tagValueField;
    /** tag name
     */
    @FXML
    private Text tagName1;
    /** tag name field
     */
    @FXML
    private TextField tagNameField1;
    /** tag value field
     */
    @FXML
    private Text tagValue1;
    /** tag value field
     */
    @FXML
    private TextField tagValueField1;
    /** search method
     */
    @FXML
    private Button switchMethod;
    /** search method name
     */
    @FXML
    private Text methodName;
    /** date range field
     */
    @FXML
    private TextField dateRangeField1;
    /**
     * search mode
     */
    private int mode;
	/**
	 * goto non-admin page
	 * @param event
	 * @throws Exception
	 */
    @FXML
    void Back(MouseEvent event) throws Exception {
    	FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/NonAdminPage.fxml"));
		Pane root = (Pane)loader.load();
		
		nonAdmin = loader.getController();
		nonAdmin.start(this.stage, user, app);
		stage.setTitle("User");
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
    }
    /**
     * Exit application
     * @param event
     */
    @FXML
    void Exit(MouseEvent event) {
    	//TODO: load memory to disk
    	SaveData();
    	System.exit(0);
    }
    /**
     * open date page
     * @param event
     */
    @FXML
    void dateBtn(MouseEvent event) {
    	obsList = null;
    	byTagPageS(0);
    	byTagPageD(0);
    	byDatePage(1);
    }
    /**
     * search by single pair
     * @param event
     */
    @FXML
    void tagBtn(MouseEvent event) {
    	obsList = null;
    	byDatePage(0);
    	this.mode = 0;
    	byTagPageD(0);
    	byTagPageS(1);
		methodName.setText("Single Pair");
    }
    /**
     * change method
     * @param event
     */
    @FXML
    void MethodBtn(MouseEvent event) {
    	this.mode++;
    	showDiffMode();
    }
    /**
     * goto create new album page
     * @param event
     * @throws IOException
     */
    @FXML
    void CreateNewAlbum(MouseEvent event) throws IOException {
    	if(obsList == null || obsList.isEmpty()) {
    		Photo.DisplayInfoMsg("Cannot create album from empty set of photo", stage);
    		return;
    	}    	
    	ArrayList<PhotoObj> arr = new ArrayList<PhotoObj>();
    	for(PhotoObj po : obsList) {
    		arr.add(po);
    	}
    	Album tempAlbum = new Album("tempAlbum", arr);
    	
    	FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/CreateAlbumFromResult.fxml"));
		Pane root = (Pane) loader.load();

		CreateAlbumFromResult c = loader.getController();
		c.start(this.stage, user, app, tempAlbum);
		stage.setTitle("Photo");
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
    }
    
    /**
     * handles the different methods of searching for photo
     */
    private void showDiffMode () {
    	int value = this.mode % 3; // we would only consider [0 2] because of the three methods
    	switch (value) {
    	case 0:
    		byTagPageD(0);
    		single();
    		break;
    	case 1:
    		byTagPageD(1);
    		methodName.setText("AND");
    		ANDOR(1);
    		break;
    	case 2:
    		methodName.setText("OR");
    		ANDOR(0);
    		break;
    	default:
    		break;
    	}
    }
    /**
     * initialize the observable list
     */
    private void initializeList () {
    	obsList = FXCollections.observableArrayList();
    	listView.setItems(obsList);
    }
    /**
     * method of searching based on single pair of name-value
     */
    private void single () {
    	initializeList();
    	String name = tagNameField.getText().trim(), value = tagValueField.getText().trim();
    	for (int j = 0; j < album.photos.size(); j++) {
    		PhotoObj current = album.photos.get(j);
    		if (current.tag_name_value(name, value)) obsList.add(current); // surface the photo....
    	}
    }
    
    /**
     * AND or OR method of searching for photos
     * 
     * @param code decide whether it is AND or OR
     */
    private void ANDOR (int code) {  // code = 1 = AND   code = 0 = OR
    	initializeList();
    	String name = tagNameField.getText().trim(), value = tagValueField.getText().trim(), 
    			name1 = tagNameField1.getText().trim(), value1 = tagValueField1.getText().trim();
    	for (int j = 0; j < album.photos.size(); j++) {
    		PhotoObj current = album.photos.get(j);
    		if (code == 0 && (current.tag_name_value(name, value) || current.tag_name_value(name1, value1))) obsList.add(current);
    		if (code == 1 && current.tag_name_value(name, value) && current.tag_name_value(name1, value1)) obsList.add(current);
    	}
    }

    /**
     * show the list view while making other views disappear
     */
    private void showListView () {
    	byDatePage(0);
		byTagPageS(0);
		byTagPageD(0);
    }
    /**
     * initial setup
     * 
     * @param stage primary stage
     * @param app controller
     * @param user the specific user
     * @param album user's album
     */
    public void start (Stage stage, dataController app, User user, Album album) {
    	this.app = app;
		this.user = user;
		this.album = album;
		this.stage = stage;
		showListView();
    }
    
    /**
     * turn on or off all the fields needed for search based on date
     * 
     * @param code 1 = on; 0 = off
     */
    private void byDatePage (int code) {    
    	boolean instruction = code == 0? false:true;
    	dateRange.setVisible(instruction);
    	dateRangeField1.setVisible(instruction);
    	dateRangeField.setVisible(instruction);
    	SearchBtn.setVisible(instruction);
    }
    
    /**
     * turn on or off all the fields needed for search based on tag
     * 
     * @param code 1 = on; 0 = off
     */
    private void byTagPageS(int code) {   // turn on or off for all the fields needed for tag search
    	boolean instruction = code == 0? false:true;
    	SearchBtn.setVisible(instruction);
    	tagName.setVisible(instruction);
    	tagNameField.setVisible(instruction);
    	tagValue.setVisible(instruction);
    	tagValueField.setVisible(instruction);
    	switchMethod.setVisible(instruction);
    	methodName.setVisible(instruction);
    }
    
    /**
     * turn on or off all the fields needed for search based on tag
     * 
     * @param code 1 = on; 0 = off
     */
    private void byTagPageD (int code) {
    	boolean instruction = code == 0? false:true;
    	tagName1.setVisible(instruction);
    	tagNameField1.setVisible(instruction);
    	tagValue1.setVisible(instruction);
    	tagValueField1.setVisible(instruction);
    }
    
    @FXML
    void Search(MouseEvent event) {
    	if (mode < 0) {
    		//TODO- dont know what to represent the time
    	}
    	else {
    		showDiffMode();
    	}
    	if (obsList.size() == 0) photo.Photo.DisplayErrorMsg(3, stage);
    	showListView();
    }
    
    /**
     * save data to the disk
     */
    public void SaveData() {
		if (app == null)
			return;
		Photo.SaveData(app);
	}

}


