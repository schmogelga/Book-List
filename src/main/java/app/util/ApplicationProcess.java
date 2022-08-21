package app.util;

import misc.exception.FatalSystemException;

public abstract class ApplicationProcess
{
    private String description;
    private boolean splash;

    public ApplicationProcess(String description)
    {
        this.description = description;
        this.splash = true;
    }

    public ApplicationProcess(String description, boolean splash)
    {
        this.description = description;
        this.splash = splash;
    }

    public abstract void run() throws FatalSystemException;

    public String getDescription()
    {
        return description;
    }

    public boolean isSplash()
    {
        return splash;
    }



}
