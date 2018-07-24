package dk.purplegreen.musiclibrary.tools.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("albums")
@XmlRootElement(name = "albums")
public class AlbumCollection {

	@XStreamImplicit
	@XmlElement(name = "album")
	private List<Album> albums = new ArrayList<>();

	public AlbumCollection() {
	}

	public AlbumCollection(Collection<Album> albums) {
		this.albums.addAll(albums);
	}

	public List<Album> getAlbums() {
		return albums;
	}

	public void addAlbum(Album album) {
		getAlbums().add(album);
	}
}
