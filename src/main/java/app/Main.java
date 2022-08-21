package app;

import view.MainFrame;

import javax.swing.*;

public class Main
{
    public static void main(String[] args) throws Exception {

        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            SystemBookList sys = SystemBookList.getInstance();

            sys.setSplashPath("images/book.gif");
            sys.start();

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            System.exit( 1 );
        }
    }

}
