package app;

import app.util.Application;
import app.util.ApplicationProcess;

import misc.exception.FatalSystemException;
import view.MainFrame;

public class SystemBookList extends Application {

    private static SystemBookList instance = new SystemBookList();

    public static SystemBookList getInstance() {
        return instance;
    }

    private SystemBookList() {

    }

    @Override
    public void defineFirstExecutionProcesses() {
        this.addFirstExecutionProcess(new ApplicationProcess("Creating the database schema...") {
            @Override
            public void run() throws FatalSystemException {
                createDataBaseSchemaGenerationProcess("scripts/createSchema.sql", "scripts/createTables.sql");
            }
        });
    }

    @Override
    public void defineInitialProcesses() {

        this.addInitialProcess(new ApplicationProcess("Opening the system screen...") {
            @Override
            public void run() {

                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible( true );
            }
        });
    }
}
