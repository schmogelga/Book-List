package core.service;

import db.providers.DAOProperties;
import misc.exception.DataBaseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseManager {

    private Connection connection;
    private static final String PROPERTY_URL = "url";
    private static final String PROPERTY_DATABASE = "database";
    private static final String PROPERTY_USERNAME = "username";
    private static final String PROPERTY_PASSWORD = "password";

    private static DataBaseManager instance;
    private String url;
    private String database;
    private String username;
    private String password;

    public static DataBaseManager getInstance() {

        if( instance == null ) {
            instance = new DataBaseManager();
        }

        return instance;
    }

    private DataBaseManager() {

        DAOProperties properties = new DAOProperties();
        url = properties.getProperty( PROPERTY_URL, true );
        database = properties.getProperty( PROPERTY_DATABASE, true );
        password = properties.getProperty( PROPERTY_PASSWORD, false );
        username = properties.getProperty( PROPERTY_USERNAME, password != null );
    }

    public void getConnection() throws SQLException {
        connection = DriverManager.getConnection( url, username, password );
    }

    public void getDataBaseConnection() throws SQLException {
        connection = DriverManager.getConnection( url + "/" + database, username, password );
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

    public void executeSQL(String sql ) throws DataBaseException, SQLException {
        try
        {
            Statement stm = connection.createStatement();
            stm.executeUpdate(sql);
        }
        catch (SQLException ex)
        {
            throw new DataBaseException( ex.getMessage() );
        }
    }

    public void runScritpSQL( InputStream script ) throws DataBaseException, SQLException {
        try
        {
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( script ) );

            String sql = "";
            String linha = bufferedReader.readLine();
            while (linha != null)
            {
                // pular comentários
                if ((linha.length() > 0 && (linha.charAt(0) == '#' || linha.charAt(0) == '-')) || linha.length() == 0 )
                {
                    linha = bufferedReader.readLine();
                    continue;
                }
                sql += " " + linha.trim();

                //comando está completo (encontrou o ;)
                if(sql.charAt(sql.length() - 1) == ';')
                {
                    try
                    {
                        this.executeSQL( sql.trim() );
                        sql = "";
                    }
                    catch (DataBaseException ex)
                    {
                        bufferedReader.close();
                        throw new DataBaseException(ex.getMessage()+": "+sql.trim() );
                    }

                }
                linha = bufferedReader.readLine();
            }
            bufferedReader.close();
        }
        catch (IOException ex)
        {
            throw new DataBaseException("Falha na execução do script SQL");
        }
        finally {
            if( connection != null ) {
                connection.close();
            }
        }
    }
}
