package dk.purplegreen.musiclibrary.tools.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Marshaller;
import org.springframework.stereotype.Service;

import dk.purplegreen.musiclibrary.tools.model.Album;
import dk.purplegreen.musiclibrary.tools.persistence.JDBCDAO;

@Service
public class JDBCImportAction extends ImportAction {

	private JDBCDAO dao;

	@Autowired
	public JDBCImportAction(Marshaller marshaller, JDBCDAO dao) {
		super(marshaller);
		this.dao = dao;
	}

	@Override
	protected List<Album> getAlbums() {		
		return dao.getAlbums();
	}
}
