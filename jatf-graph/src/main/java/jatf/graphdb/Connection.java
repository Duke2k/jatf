/**
 * This file is part of JATF.
 * <p>
 * JATF is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * <p>
 * JATF is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with JATF.  If not, see <http://www.gnu.org/licenses/>.
 */

package jatf.graphdb;

import org.neo4j.ogm.MetaData;
import org.neo4j.ogm.session.Neo4jSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@SuppressWarnings({"WeakerAccess", "unused"})
@EnableTransactionManagement
@EnableScheduling
public class Connection extends Neo4jSession {

    private final static String PROPERTIES_FILE_NAME = "neo4j.properties";

    private static Logger log = LoggerFactory.getLogger(Connection.class);

    @Autowired
    private ClassService service;

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private Properties properties;

    public Connection(MetaData metaData) {
        super(metaData, null);
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
            log.error("Could not load properties file {} due to {}", PROPERTIES_FILE_NAME, e);
        }
    }
}
