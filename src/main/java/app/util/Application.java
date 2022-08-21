package app.util;

import core.service.DataBaseManager;
import core.service.ReportManager;
import db.providers.DAOFactory;
import misc.exception.DAOConfigurationException;
import misc.exception.DataBaseException;
import misc.exception.FatalSystemException;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;

import app.util.splash.Splash;
import misc.util.Handler;

import javax.swing.*;

public abstract class Application {

    public final static int START_WITH_DEFAULT_PROCESSES = 1;
    private ArrayList<ApplicationProcess> firstExecutionProcess;
    private ArrayList<ApplicationProcess> initialProcess;
    private String splashPath;

    public Application() {
        this.firstExecutionProcess = new ArrayList();
        this.initialProcess = new ArrayList();
    }

    public void addFirstExecutionProcess(ApplicationProcess process) {
        firstExecutionProcess.add(process);
    }

    public void addInitialProcess(ApplicationProcess process) {
        initialProcess.add(process);
    }

    public boolean isFirstExecution() throws SQLException {

        boolean first = false;

        try {

            DataBaseManager.getInstance().getDataBaseConnection();

        } catch ( SQLSyntaxErrorException e ) {
            first = true;
        }

        return first;
    }

    public void start( int defaultProcesses ) {
        try {
            if ( this.isFirstExecution() ) {
                if ( defaultProcesses == Application.START_WITH_DEFAULT_PROCESSES ) {
                    defineDefaultFirstExecutionProcesses();
                }
                this.defineFirstExecutionProcesses();
                this.runFirstExecutionProcesses();
            }
        } catch (SQLException e) {
            Handler.handle( e );
        }

        if ( defaultProcesses == Application.START_WITH_DEFAULT_PROCESSES ) {
            defineDefaultInitialProcesses();
        }
        defineInitialProcesses();
        runInitialProcesses();
    }

    public void start() {
        start(Application.START_WITH_DEFAULT_PROCESSES);
    }

    protected void defineDefaultFirstExecutionProcesses() {

    }

    public abstract void defineFirstExecutionProcesses();

    public abstract void defineInitialProcesses();

    protected void defineDefaultInitialProcesses() {

        this.addInitialProcess(new ApplicationProcess("Loading system properties...") {
            @Override
            public void run() throws FatalSystemException {
                try {
                    DAOFactory.getInstance();

                } catch ( DAOConfigurationException ex ) {
                    throw  new FatalSystemException( "failed to read set up DAOFactory" );
                }
            }
        });

        this.addInitialProcess(new ApplicationProcess("Testing the database connection...") {
            @Override
            public void run() throws FatalSystemException {
                try {
                    if( DAOFactory.getInstance().getConnectionProvider().getConnection() == null ){
                        throw new FatalSystemException( "failed to get database connection" );
                    }
                } catch (SQLException e) {
                    throw new FatalSystemException( e.getMessage() );
                }
            }
        });

        this.addInitialProcess( new ApplicationProcess("Starting report manager...") {
            @Override
            public void run() throws FatalSystemException
            {
                try
                {
                    ReportManager.getInstance();
                }
                catch (Exception ex)
                {
                    throw new FatalSystemException("Failed to set up ReportManager");
                }
            }
        });
    }
    protected void createDataBaseSchemaGenerationProcess( String createSchemaPath, String createTablesPath ) throws FatalSystemException {
        try {

            DataBaseManager dataBaseManager = DataBaseManager.getInstance();

            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream createSchemaInput = loader.getResourceAsStream( createSchemaPath );
            InputStream createTablesInput = loader.getResourceAsStream( createTablesPath );

            dataBaseManager.getConnection();
            dataBaseManager.runScritpSQL( createSchemaInput );

            dataBaseManager.getDataBaseConnection();
            dataBaseManager.runScritpSQL( createTablesInput );

        } catch (DataBaseException | SQLException ex) {
            throw new FatalSystemException( ex.getMessage() );
        }
    }

    public String getSplashPath() {
        return splashPath;
    }

    public void setSplashPath(String splashPath) {
        this.splashPath = splashPath;
    }

    public void runInitialProcesses() {
        Splash splash = null;

        if (this.getSplashPath() != null && !this.getSplashPath().isBlank()) {
            splash = new Splash( getSplashPath() );
            splash.setSteps(initialProcess.size());
            splash.showSplash();
        }

        for (ApplicationProcess process : initialProcess) {
            System.out.println(process.getDescription());

            if (splash != null) {
                splash.next(process.getDescription());
                if (!process.isSplash()) {
                    splash.dispose();
                }
            }

            try {
                process.run();
            } catch (FatalSystemException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
                System.exit(0);
            }
        }

        if (splash != null) {
            splash.dispose();
        }
    }

    public void runFirstExecutionProcesses() {
        for (ApplicationProcess process : firstExecutionProcess) {
            System.out.println(process.getDescription());
            try {
                process.run();
            } catch (FatalSystemException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
                System.exit(0);
            }
        }
    }
}
