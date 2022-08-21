package view;

import org.kordamp.ikonli.materialdesign2.MaterialDesignB;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private final LoginPanel loginPanel;
    private final RegisterPanel registerPanel;
    private final WelcomePanel welcomePanel;

    public MainFrame() {

        loginPanel = new LoginPanel( this );
        registerPanel = new RegisterPanel( this );
        welcomePanel = new WelcomePanel( this );

        FontIcon logoIcon = FontIcon.of( MaterialDesignB.BOOK );
        logoIcon.setIconSize( 200 );
        logoIcon.setIconColor( new Color( 0, 0, 0 ) );

        this.setIconImage( logoIcon.toImageIcon().getImage() );
        this.setTitle( "Book List" );
        this.setDefaultCloseOperation( EXIT_ON_CLOSE );

        add( loginPanel.getRootPanel() );
        setSize(1280, 720 );


        setLocationRelativeTo( null );
    }

    public void resetPanel(){
        setPanel( loginPanel.getRootPanel() );
    }

    public void setPanel( JPanel panel ) {
        getContentPane().removeAll();
        getContentPane().add( panel );
        revalidate();
        repaint();
    }

    public LoginPanel getLoginPane() {
        return loginPanel;
    }

    public RegisterPanel getRegisterPane() {
        return registerPanel;
    }

    public WelcomePanel getWelcomePane() {
        return welcomePanel;
    }

}
