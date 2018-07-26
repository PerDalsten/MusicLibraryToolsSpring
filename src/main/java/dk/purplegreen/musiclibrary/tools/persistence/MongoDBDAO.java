package dk.purplegreen.musiclibrary.tools.persistence;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import dk.purplegreen.musiclibrary.tools.model.Album;

@Repository
@Qualifier("mongoDBDAO")
public class MongoDBDAO implements DAO {

	private MongoTemplate mongoTemplate;

	public MongoDBDAO(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public void saveAlbum(Album album) {
		mongoTemplate.save(album);
	}

	@Override
	public List<Album> getAlbums() {
		return mongoTemplate.findAll(Album.class);
	}

	@Override
	public Integer getArtistID(String artist) {
		throw new UnsupportedOperationException();
	}

}
