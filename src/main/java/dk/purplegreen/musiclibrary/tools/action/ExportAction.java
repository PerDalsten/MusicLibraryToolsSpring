package dk.purplegreen.musiclibrary.tools.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.transform.stream.StreamSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.oxm.Unmarshaller;

import dk.purplegreen.musiclibrary.tools.model.Album;
import dk.purplegreen.musiclibrary.tools.model.AlbumCollection;

public abstract class ExportAction implements Action {

	@Autowired
	private Environment environment;

	private Unmarshaller unmarshaller;

	protected ExportAction(Unmarshaller unmarshaller) {
		this.unmarshaller = unmarshaller;
	}

	@Override
	public void execute() throws IOException {

		File albumDir = new File(environment.getRequiredProperty("albumdir"));

		File[] albumCollections = albumDir.listFiles(file -> !file.isDirectory() && file.getName().endsWith(".xml"));
		for (File ac : albumCollections) {

			AlbumCollection albums;
			try (InputStream is = new FileInputStream(ac)) {
				albums = (AlbumCollection) unmarshaller.unmarshal(new StreamSource(is));

				saveAlbums(albums.getAlbums());
			}
		}
	}

	protected abstract void saveAlbums(List<Album> albums);
}
