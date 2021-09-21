package view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
/**
 * @author Yunhao Xu
 * @author Minhesota Geusic
 * @version %I% %G%
 * @since 1.2
 */
public class PhotoObj implements Serializable {
	private static final long serialVersionUID = 6965456566028510327L;
	protected long _date;
	/** date of photo
	 */
	protected String date;
	/** caption of photo
	 */
	protected String caption;
	/** photo path
	 */
	protected String photoPath;
	/**photo name
	 */
	protected String photoName;
	/** list of tag for this photo
	 */
	protected ArrayList<Tag> tags;

	/**
	 * constructor
	 */
	public PhotoObj() { // constructor of Photo
		Calendar calendar = Calendar.getInstance(); // get the date when the photo is created
		calendar.set(Calendar.MILLISECOND, 0);
		this.date = calendar.getTime().toString();
		_date = calendar.getTimeInMillis();
		tags = new ArrayList<>();
	}
	/** get the date associated with this photo
	 * 
	 * @return the string value of the date
	 */
	public String getDate() {
		return this.date;
	}
	/** add a given tag to this photo
	 * @param tag tag to add
	 */
	public void addTag(Tag tag) {
		if (tag != null)
			tags.add(tag);
	}

	/**
	 * get the list of tag values associated with tag name
	 * 
	 * @param tagName the name of the tag
	 * @return a list of tag value if the tag name exists. otherwise, return null
	 */
	public ArrayList<String> getTagValue(String tagName) {
		for (int j = 0; j < tags.size(); j++) {
			if (tags.get(j).tagName.equals(tagName))
				return tags.get(j).tagValue;
		}
		return null;
	}

	/**
	 * to verify if in the list of tag values associated with tag name, the specific
	 * value exists
	 * 
	 * @param name  tag name
	 * @param value tag value
	 * @return true if value exists, false otherwise
	 */
	public boolean tag_name_value(String name, String value) {
		ArrayList<String> tags = getTagValue(name);
		if (tags == null)
			return false;
		for (int j = 0; j < tags.size(); j++) {
			if (value.equals(tags.get(j)))
				return true;
		}
		return false;

	}
}
