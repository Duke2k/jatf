package jatf.graphdb;

import org.neo4j.ogm.session.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@SuppressWarnings({"WeakerAccess", "unused"})
@EnableTransactionManagement
@EnableScheduling
public class Connection extends Neo4jConfiguration {

    private final static String PROPERTIES_FILE_NAME = "neo4j.properties";

    private static Logger log = LoggerFactory.getLogger(Connection.class);

    @Autowired
    private ClassService service;

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private Properties properties;

    public Connection() {
        properties = new Properties();
        initProperties();
    }

    @Override
    public SessionFactory getSessionFactory() {
        return null;
    }

    public GraphRepository<Class<?>> getRepository() {
        return service.repository;
    }

    private void initProperties() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(PROPERTIES_FILE_NAME);
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            log.error("Could not load properties file {0} due to {1}", PROPERTIES_FILE_NAME, e);
        }
    }
}
