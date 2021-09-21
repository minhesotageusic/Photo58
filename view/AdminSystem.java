package view;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
public class AdminSystem {
	/**
	 * instance of the app's data
	 */
	private static dataController app;
	/** parent stage that initiated this page
	 */
	private Stage stage;
	/** admin pane
	 */
    @FXML
    private Pane AdminPane;
    /** list view of user
     */
    @FXML
    private ListView<String> listOfUser;
    /** list of user
     */
    private ObservableList<String> obsList;
    /** create user pane
     */
    @FXML
    private Pane createUser;
    /**user name text field
     */
    @FXML
    private TextField username;
    /** delete user button
     */
    @FXML
    private Button deleteUserBtn;
    
    /** Add user to list of user
     * @param event mouse event
     */
    @FXML
    void AddUser(MouseEvent event) {
    	// need to work on it...
    	String name = username.getText().trim();
    	boolean nameValid = nameValid(name, this.obsList);
    	
    	if (!nameValid) {  // throw an alert
    		photo.Photo.DisplayErrorMsg(1, stage);
    		username.clear();
    		return;
    	}
    	User newUser = new User(name);
    	obsList.add(newUser.username);
    	app.users.add(newUser);
    	
    	//display confirmation
    	photo.Photo.DisplayConfirmMsg("User has been created", stage);
    	username.clear();
    }
    /** check if name is valid for given observer list
     * @param name target name
     * @param obsList target list
     * 
     * @return true if target name is valid, false otherwise.
     */
    public static boolean nameValid(String name, ObservableList<String> obsList) {
    	if (name.isEmpty()) { // reject empty username
    		return false;
    	} else {
    		for (int j = 0; j < obsList.size(); j++) {
    			if (name.equals(obsList.get(j))) {   // reject duplicate
    				return false;
    			}
    		}
    	}
    	return true;
    }

    /**Exit the application
     * @param event mouse event
     */
    @FXML
    void Exit(MouseEvent event) {
    	//save info before logging out
    	Photo.SaveData(app);
    	System.exit(0);
    }
    /**Open Create user pane
     * @param event mouse event
     */
    @FXML
    void CreateUser(MouseEvent event) { 
    	defaltPage(0);
    	addUserPage(1);
    }
    /**Delete user from list
     * @param mouse event
     */
    @FXML
    void DeleteUser(MouseEvent event) {  // delete user from list
    	if(obsList.isEmpty()) {
    		Photo.DisplayInfoMsg("Nothing to remove", stage);
    	}
    	int selectedID = listOfUser.getSelectionModel().getSelectedIndex();
    	obsList.remove(selectedID);
    	app.users.remove(selectedID);
    	if(obsList.isEmpty()) deleteUserBtn.setVisible(false);
    }
    
    /**Show list of user
     * @param event mouse event
     */
    @FXML
    void ListUser(MouseEvent event) {  // show list of users
    	addUserPage(0);
    	defaltPage(1);
    }
    @FXML
    void ListView(MouseEvent event) {
    	//not needed
    }
    /** logout of admin
     * @param event mouse event 
     * @exception throw exception if login page could not be open
     */
    @FXML
    void Logout(MouseEvent event) throws Exception {
    	//save info before logging out
    	Photo.SaveData(app);
    	//go back to the login system
    	photo.Photo.OpenLoginPage();
    }
    /** start method for this page
     * @param stage parent stage
     * @param app data
     */
    public void start(Stage stage, dataController app) {   // initial setup
    	AdminSystem.app = app;
    	this.stage = stage;
    	obsList = FXCollections.observableArrayList(loadUsernames());
    	listOfUser.setItems(obsList);
    	listOfUser.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    	if(!obsList.isEmpty()) {
    		listOfUser.getSelectionModel().select(0);
    	}
    	defaltPage(1);
    	addUserPage(0);
    }
    /**load user name from app's list of users
     * @return a list of users
     */
    public ArrayList<String> loadUsernames () {
    	ArrayList<String> usernames = new ArrayList<String>();
    	for (int j = 0; j < app.users.size(); j++) {
    		usernames.add(app.users.get(j).username);
    	}
    	return usernames;
    }
    /** show list view based on code
     * @param code instruction
     */
    private void defaltPage(int code) {    // show the list view page
    	boolean instruction = code == 0? false:true;
    	listOfUser.setVisible(instruction);
    	listOfUser.getSelectionModel().select(0);
    	deleteUserBtn.setVisible(instruction);
    	if(obsList.isEmpty()) deleteUserBtn.setVisible(false);
    }
    /** show create user page
     * @param code instruction
     */
    private void addUserPage(int code) {           // show create user page
    	boolean instruction = code == 0? false:true;
    	createUser.setVisible(instruction);
    	username.setVisible(instruction);
    }
    
}

