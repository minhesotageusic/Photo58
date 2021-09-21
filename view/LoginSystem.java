package view;

import javafx.stage.Stage;
import photo.Photo;

import java.io.*;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
/**
 * @author Yunhao Xu
 * @author Minhesota Geusic
 * @version %I% %G%
 * @since 1.2
 */
public class LoginSystem {
	/** admin controller
	 */
	private AdminSystem admin;
	/** instance of the app's data
	 */
	private dataController app;
	/** parent stage that initiated this page
	 */
	private Stage stage;
	/** non-admin controller
	 */
	private NonAdminSystem nonAdmin;
	/** login pane
	 */
	@FXML
	private Pane loginPane;
	/**user name text field
	 */
	@FXML
	private TextField username;
	/** Exit Application
	 * @param event mouse event
	 */
	@FXML
	void Exit(MouseEvent event) { // exit the program without logging out
		System.exit(0);
	}
	/** login
	 * @param event mouse event
	 * @throws throws Exception
	 */
	@FXML
	void loginBtn(MouseEvent event) throws Exception {
		loginPage(event);
	}
	/**
	 * start method for this page
	 * @param primaryStage
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void start(Stage primaryStage) throws ClassNotFoundException, IOException {
		this.stage = primaryStage;
		//read in app data
		//2 scenario:
		//	1. app has not been created this is initial data
		//	2. app has been created, recalling login system 
		//		allow it to read any changes made in the app 
		//		by other systems
		app = dataController.readData();
		boolean foundStockUser = false;
		for(int i = 0; i < app.users.size(); i++) {
			if(app.users.get(i) == null) continue;
			if(app.users.get(i).username.equals("stock")) {
				foundStockUser = true;
			}
		}
		if(!foundStockUser) {
			ArrayList<PhotoObj> arr = new ArrayList<PhotoObj>();
			String[] loc = Photo.stockPhotoLocation;
			File file = null;
			for(int i = 0; i < loc.length ;i++) {
				file = new File(loc[i]);
				System.out.println("photo: " + loc[i]);
				if(!file.exists()) {
					System.out.println("not exist");
					continue;
				}
				PhotoObj photoObj = new PhotoObj();
				photoObj.photoPath = file.getAbsolutePath();
				photoObj.photoName = file.getName();
				photoObj.caption = file.getName();
				arr.add(photoObj);
			}			
			Album stockAlbum = new Album("stock", arr);
			User stockUser = new User("stock");
			stockUser.albums = new ArrayList<Album>();
			stockUser.albums.add(stockAlbum);
			app.users.add(stockUser);
		}
	}
	/**
	 * login page
	 * @param event
	 * @throws Exception
	 */
	public void loginPage(MouseEvent event) throws Exception {
		String username = this.username.getText();
		username.trim();
		if (username.equals("admin")) {
			//goto admin page
			loadAdminSystem();
		} else {
			//goto non-admin page
			loadNonAdmin(username);
		}
	}
	/**
	 * load non admin
	 * @param username
	 * @throws Exception
	 */
	public void loadNonAdmin(String username) throws Exception { // proceed to normal user system
		User user = null;
		int index = userExist(username);
		if (index >= 0)
			user = app.users.get(index);
		else {
			//user = new User(username);
			//no such user cannot login
			//open error and display no user
			photo.Photo.DisplayErrorMsg(0, stage);
			return;
		}
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
	/** check if user exist
	 * @param username user
	 * @return return index > 0 if user exist, -1 otherwise
	 */
	private int userExist(String username) { // if user exists, return its index. otherwise, return -1
		for (int i = 0; i < app.users.size(); i++) {
			User current = app.users.get(i);
			if (current.username.equals(username))
				return i;
		}
		return -1;
	}
	/**
	 * load admin
	 * @throws Exception
	 */
	public void loadAdminSystem() throws Exception { // proceed to admin system
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/AdminPage.fxml"));
		Pane root = (Pane) loader.load();

		admin = loader.getController();
		admin.start(stage, app);
		stage.setTitle("Admin");
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
