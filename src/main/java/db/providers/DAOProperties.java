package db.providers;

import misc.exception.DAOConfigurationException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DAOProperties {

    private static final String PROPERTIES_FILE = "@instance@/config/dao.properties";
    private static final Properties PROPERTIES = new Properties();

    static {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try {
            PROPERTIES.load( new FileInputStream( PROPERTIES_FILE ) );
        } catch (IOException e) {
            throw new DAOConfigurationException( "Cannot load properties file '" + PROPERTIES_FILE + "'.", e );
        }
    }

    public String getProperty(String key, boolean mandatory) throws DAOConfigurationException {
        String property = PROPERTIES.getProperty( key );

        if (property == null || property.trim().length() == 0) {
            if ( mandatory ) {
                throw new DAOConfigurationException("Required property '" + key + "'" + " is missing in properties file '" + PROPERTIES_FILE + "'.");
            } else {
                property = null;
            }
        }
        return property;
    }

}
