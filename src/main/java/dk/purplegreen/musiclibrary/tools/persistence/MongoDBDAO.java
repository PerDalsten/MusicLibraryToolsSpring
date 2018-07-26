package dk.purplegreen.musiclibrary.tools.persistence;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import dk.purplegreen.musiclibrary.tools.model.Album;

@Repository
public class MongoDBDAO {

	private MongoTemplate mongoTemplate;

	public MongoDBDAO(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	public void saveAlbum(Album album) {
		mongoTemplate.save(album);
	}

	public List<Album> getAllAlbums() {
		return mongoTemplate.findAll(Album.class);
	}
}
