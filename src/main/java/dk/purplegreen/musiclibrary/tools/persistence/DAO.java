package dk.purplegreen.musiclibrary.tools.persistence;

import java.util.List;

import dk.purplegreen.musiclibrary.tools.model.Album;

public interface DAO {
	void saveAlbum(Album album);
	List<Album> getAlbums();
	default Integer getArtistID(String artist) {
		throw new UnsupportedOperationException("getArtistID not supported in this DAO");		
	};
}
