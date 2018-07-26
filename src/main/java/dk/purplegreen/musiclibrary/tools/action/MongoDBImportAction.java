package dk.purplegreen.musiclibrary.tools.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.Marshaller;
import org.springframework.stereotype.Service;

import dk.purplegreen.musiclibrary.tools.model.Album;
import dk.purplegreen.musiclibrary.tools.persistence.MongoDBDAO;

@Service("mongoDBImportAction")
public class MongoDBImportAction extends ImportAction {

	private MongoDBDAO mongoDBDAO;

	public MongoDBImportAction(Marshaller marshaller, @Qualifier("mongoDBDAO") MongoDBDAO mongoDBDAO) {
		super(marshaller);
		this.mongoDBDAO = mongoDBDAO;
	}

	@Override
	protected List<Album> getAlbums() {
		return mongoDBDAO.getAlbums();
	}
}
