package view;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * @author Yunhao Xu
 * @author Minhesota Geusic
 * @version %I% %G%
 * @since 1.2
 */
public class User implements Serializable{
	private static final long serialVersionUID = -7387415633792918442L;
	/** user's name
	 */
	protected String username;
	/** album
	 */
	protected ArrayList<Album> albums;
	/**
	 * list of previously made tag for this user
	 */
	protected ArrayList<Tag> previousTag;
	/**
	 * size of the album
	 */
	protected int sizeOfAlbum;
	/**
	 * constructor
	 * @param name name of user
	 */
	public User (String name) {  // constructor of user class
		this.username = name;
		this.albums = new ArrayList<Album>();
		previousTag = new ArrayList<Tag>();
	}
	
	
    
}

