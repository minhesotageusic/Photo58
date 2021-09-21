package view;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * @author Yunhao Xu
 * @author Minhesota Geusic
 * @version %I% %G%
 * @since 1.2
 */
public class Tag implements Serializable { // tag information
	private static final long serialVersionUID = 4739211414982124048L;
	/** name of this tag
	 */
	public String tagName;
	/**
	 * tag values
	 */
	public ArrayList<String> tagValue; // since user can put multiple values (e.g. people appear in the photo)
	/**
	 * constructor 
	 * @param tagName name of tag
	 */
	public Tag(String tagName) {
		this.tagName = tagName;
		tagValue = new ArrayList<String>();
	}
}
