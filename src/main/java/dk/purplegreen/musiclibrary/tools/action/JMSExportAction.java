package dk.purplegreen.musiclibrary.tools.action;

import java.util.List;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Repository;

import dk.purplegreen.musiclibrary.tools.model.Album;

@Repository("jmsExportAction")
public class JMSExportAction extends ExportAction {

	private JmsTemplate jmsTemplate;

	public JMSExportAction(Unmarshaller unmarshaller, JmsTemplate jmsTemplate) {
		super(unmarshaller);
		this.jmsTemplate = jmsTemplate;

	}

	@Override
	protected void saveAlbums(List<Album> albums) {

		albums.stream().flatMap(a -> a.getSongs().stream()).forEach(song -> {
			jmsTemplate.send(session -> session.createObjectMessage(song.getTitle()));
		});

	}

}
