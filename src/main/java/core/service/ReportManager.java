
package core.service;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

import core.data.Report;
import db.providers.ConnectionProvider;
import db.providers.DAOFactory;
import misc.util.Handler;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;


public class ReportManager
{
    private final ConnectionProvider connectionProvider;
    private static ReportManager instance;

    private ReportManager() {
        connectionProvider = DAOFactory.getInstance().getConnectionProvider();
    }

    public static ReportManager getInstance() {

        if ( instance == null ) {

            instance = new ReportManager();
        }
        return instance;
    }

    public void createReport( Report report ) throws SQLException {

        try
        {
            Connection connection = connectionProvider.getConnection();

            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream input = loader.getResourceAsStream( report.getPath() );

            JasperDesign jasperDesign = JRXmlLoader.load(input);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint print = JasperFillManager.fillReport(jasperReport, report.getParameters(), connection );
            JasperViewer.viewReport(print,false);
        }
        catch (Exception ex)
        {
            Handler.handle( ex );
        }
        finally {
            connectionProvider.closeConnection();
        }
    }
}
