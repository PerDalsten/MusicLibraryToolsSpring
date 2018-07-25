package dk.purplegreen.musiclibrary.tools;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.cache.Caching;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.thoughtworks.xstream.XStream;

import dk.purplegreen.musiclibrary.tools.action.Action;
import dk.purplegreen.musiclibrary.tools.model.Album;
import dk.purplegreen.musiclibrary.tools.model.AlbumCollection;
import dk.purplegreen.musiclibrary.tools.model.Song;

@Configuration
@ComponentScan(basePackageClasses = { Action.class })
@PropertySource("classpath:musiclibrarytoolsspring.properties")
@EnableTransactionManagement
@EnableCaching
public class Config {

	private static final Logger log = LogManager.getLogger(Config.class);

	@Autowired
	private Environment environment;

	@SuppressWarnings("rawtypes")
	private static final Class[] classes = { AlbumCollection.class, Album.class, Song.class };

	private XStreamMarshaller xstreamMarshaller = new XStreamMarshaller() {
		@Override
		protected void customizeXStream(XStream xstream) {
			XStream.setupDefaultSecurity(xstream);
			xstream.allowTypes(classes);
		}
	};

	private Jaxb2Marshaller jaxbMarshaller = new Jaxb2Marshaller();

	@PostConstruct
	private void initialize() {
		xstreamMarshaller.setAnnotatedClasses(classes);

		jaxbMarshaller.setClassesToBeBound(classes);
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		jaxbMarshaller.setMarshallerProperties(properties);
	}

	@Bean
	@Value("${xmlprovider}")
	public Marshaller marshaller(String xmlProvider) {

		log.debug("XML provider: {}", xmlProvider);

		if ("xstream".equals(xmlProvider)) {
			return xstreamMarshaller;
		} else {
			return jaxbMarshaller;
		}
	}

	@Bean
	@Value("${xmlprovider}")
	public Unmarshaller unmarshaller(String xmlProvider) {

		log.debug("XML provider: {}", xmlProvider);

		if ("xstream".equals(xmlProvider)) {
			return xstreamMarshaller;
		} else {
			return jaxbMarshaller;
		}
	}

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(environment.getRequiredProperty("derby.driver"));
		dataSource.setUrl(environment.getRequiredProperty("derby.url"));
		dataSource.setUsername(environment.getRequiredProperty("derby.username"));
		dataSource.setPassword(environment.getRequiredProperty("derby.password"));

		return dataSource;
	}

	@Bean
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource());
	}

	@Bean
	public DataSourceTransactionManager dataSourceTransactionManager(DataSource dataSource) {
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
		dataSourceTransactionManager.setDataSource(dataSource);

		return dataSourceTransactionManager;
	}
	
	@Bean
    public CacheManager cacheManager() throws URISyntaxException {
		
		
		//return new JCacheCacheManager(Caching.getCachingProvider().getCacheManager());
		
		
		return new JCacheCacheManager(Caching.getCachingProvider().getCacheManager(
	            getClass().getResource("/ehcache.xml").toURI(),
	            getClass().getClassLoader()
	        ));
	      
	}
}
