package view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
/**
 * @author Yunhao Xu
 * @author Minhesota Geusic
 * @version %I% %G%
 * @since 1.2
 */
public class dataController implements Serializable {
	
	private static final long serialVersionUID = -3348796440734960020L;
	/** directory of file
	 */
	public static final String directory = "photo/view";
	/** file name
	 */
	public static final String file = "data.ser";
	/** list of users
	 */
	public ArrayList<User> users;

	/** read the data from database
	 * @return a non-null instance of the dataController
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static dataController readData() throws ClassNotFoundException, IOException {
		File _file = new File(directory + File.separator + file);
		//test if file exists
		if(!_file.exists()) {
			//write initial data if file does not exist
			dataController app = new dataController();
			app.users = new ArrayList<User>();
			_file.createNewFile();
			writeData(app);
			return app;
		}
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(_file));
		dataController app = (dataController) ois.readObject();
		ois.close();
		return app;
	}

	/** write the data to the database
	 * @param app the instance of dataController to be save
	 * @throws IOException
	 */
	public static void writeData(dataController app) throws IOException {
		File _file = new File(directory + File.separator + file);
		if(!_file.exists()) _file.createNewFile();
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(_file));
		oos.writeObject(app);
		oos.close();
	}
}
