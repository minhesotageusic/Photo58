package view;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Yunhao Xu
 * @author Minhesota Geusic
 * @version %I% %G%
 * @since 1.2
 */
public class Album implements Serializable {

	private static final long serialVersionUID = 106778270155554818L;
	/**
	 * name of this album
	 */
	protected String name;
	/**
	 * list of photos
	 */
	protected ArrayList<PhotoObj> photos;
	protected long earliesPhotoTime = 0;
	protected long latestPhotoTime = 0;
	/**
	 * earliest photo
	 */
	protected String earliestPhoto;
	/**
	 * lastest photo
	 */
	protected String latestPhoto;

	/**
	 * constructor
	 * 
	 * @param name   album name
	 * @param photos photos
	 */
	public Album(String name, ArrayList<PhotoObj> photos) {
		this.name = name;
		this.photos = photos;
		if (photos == null)
			this.photos = new ArrayList<PhotoObj>();
		for(int i = 0 ; i < photos.size(); i++) {
			PhotoObj _photo = photos.get(i);
			if(_photo._date <= earliesPhotoTime) {
				earliesPhotoTime = _photo._date;
				earliestPhoto = _photo.date;
			}
			if(_photo._date >= latestPhotoTime) {
				latestPhotoTime = _photo._date;
				latestPhoto = _photo.date;
			}
			if (_photo._date == earliesPhotoTime && 
					_photo._date == latestPhotoTime) {
				earliesPhotoTime = _photo._date;
				earliestPhoto = _photo.date;
				latestPhotoTime = _photo._date;
				latestPhoto = _photo.date;
			}
		}
	}

	/**
	 * constructor
	 * 
	 * @param name album name
	 */
	public Album(String name) {
		this.name = name;
		photos = new ArrayList<PhotoObj>();
	}

	/**
	 * return size of list of photos
	 * 
	 * @return size of album
	 */
	public int size() {
		if (photos == null)
			return 0;
		return photos.size();
	}

	/**
	 * Add a photo to album
	 * 
	 * @param photo the photo to be added
	 * 
	 * @return true if photo can be added. false otherwise
	 * 
	 */
	public boolean addPhoto(PhotoObj photo) {
		for (PhotoObj iphoto : photos) {
			if (iphoto == null)
				continue;
			if (iphoto.caption.equals(photo.caption)) {
				return false;
			}
		}
		if(earliesPhotoTime == 0 && latestPhotoTime == 0) {
			earliesPhotoTime = photo._date;
			latestPhotoTime = photo._date;
			earliestPhoto = photo.date;
			latestPhoto = photo.date;
		}
		if (photo._date <= earliesPhotoTime) {
			earliesPhotoTime = photo._date;
			earliestPhoto = photo.date;
		} 
		if (photo._date >= latestPhotoTime) {
			latestPhotoTime = photo._date;
			latestPhoto = photo.date;
		}
		if (photo._date == earliesPhotoTime && photo._date == latestPhotoTime) {
			earliesPhotoTime = photo._date;
			earliestPhoto = photo.date;
			latestPhotoTime = photo._date;
			latestPhoto = photo.date;
		}
		photos.add(photo);
		return true;
	}
	/**
	 * remove photo from album
	 * @param photo
	 * @return
	 */
	public boolean removePhoto(PhotoObj photo) {
		if(photos.remove(photo)) {
			if(photos.size() != 0) {
				earliesPhotoTime = photos.get(0)._date;
				latestPhotoTime = photos.get(0)._date;
			}
			for(int i = 0 ; i < photos.size(); i++) {
				PhotoObj _photo = photos.get(i);
				if(_photo._date <= earliesPhotoTime) {
					earliesPhotoTime = _photo._date;
					earliestPhoto = _photo.date;
				}
				if(_photo._date >= latestPhotoTime) {
					latestPhotoTime = _photo._date;
					latestPhoto = _photo.date;
				}
				if (_photo._date == earliesPhotoTime && 
						_photo._date == latestPhotoTime) {
					earliesPhotoTime = _photo._date;
					earliestPhoto = _photo.date;
					latestPhotoTime = _photo._date;
					latestPhoto = _photo.date;
				}
			}
			
			return true;
		}
		return false;
	}

	/**
	 * get the photo associated with the given name
	 * 
	 * @param name the name of the target photo
	 * 
	 * @return the photo object associated with that name. Null if no such name
	 * 
	 */
	public PhotoObj getPhoto(String name) {
		for (PhotoObj iphoto : photos) {
			if (iphoto == null)
				continue;
			if (iphoto.photoName.equals(name)) {
				return iphoto;
			}
		}
		return null;
	}

	/**
	 * get photo by caption
	 * 
	 * @param caption caption
	 * @return return photo object associated with caption
	 */
	public PhotoObj getPhotoByCaption(String caption) {
		for (PhotoObj iphoto : photos) {
			if (iphoto == null)
				continue;
			if (iphoto.caption.equals(caption)) {
				return iphoto;
			}
		}
		return null;
	}
}