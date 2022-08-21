package view;

import core.data.User;
import core.service.UserManager;
import misc.exception.DataEntryException;
import misc.util.ApplicationContext;
import org.kordamp.ikonli.materialdesign2.*;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPanel {

    private JPanel rootPanel;
    private JPanel leftPane;
    private JPanel rightPane;
    private JLabel labelInstagram;
    private JLabel labelLogo;
    private JLabel labelFacebook;
    private JLabel labelTitle;
    private JLabel labelYouTube;
    private JTextField textFieldUser;
    private JLabel labelUserIcon;
    private JLabel labelUser;
    private JButton enterButton;
    private JLabel labelPassword;
    private JLabel labelPasswordIcon;
    private JLabel labelFeedback;
    private JPasswordField passwordField;
    private JLabel labelTwitter;
    private JLabel labelLinkedin;
    private JButton registerButton;

    private MainFrame mainFrame;

    public LoginPanel(JFrame frame ) {

        mainFrame = (MainFrame) frame;

        buildRightPane();
        buildLeftPane();

        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if( e.getKeyCode() == KeyEvent.VK_ENTER ) {
                    performLogin();
                }
            }
        });

        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                registerButton.setForeground( Color.gray );
            }
            @Override
            public void mouseExited( MouseEvent e ){
                registerButton.setForeground( Color.BLACK );
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                registerButton.setForeground( Color.BLACK );
                mainFrame.setPanel( mainFrame.getRegisterPane().getRootPanel() );
            }
        });
    }

    private void performLogin(){
        try {
            User user = UserManager.getInstance().login( textFieldUser.getText(), passwordField.getText() );

            registerButton.setForeground( Color.BLACK );
            ApplicationContext.getInstance().setLoggedUser( user );
            mainFrame.getWelcomePane().initCards();
            mainFrame.setPanel( mainFrame.getWelcomePane().getRootPanel() );
            passwordField.setText("");
            textFieldUser.setText("");
        }
        catch ( DataEntryException ex ) {
            labelFeedback.setVisible( true );
            labelFeedback.setText( ex.getMessage());
        }
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    private void buildRightPane() {

        FontIcon logoIcon = FontIcon.of( MaterialDesignB.BOOK );
        logoIcon.setIconSize( 200 );
        logoIcon.setIconColor( new Color( 255, 255, 255) );
        labelLogo.setIcon( logoIcon );

        FontIcon facebookIcon = FontIcon.of(MaterialDesignF.FACEBOOK );
        facebookIcon.setIconSize( 50 );
        facebookIcon.setIconColor( new Color( 0,0,0 ));
        labelFacebook.setIcon( facebookIcon );

        FontIcon instagramIcon = FontIcon.of(MaterialDesignI.INSTAGRAM );
        instagramIcon.setIconSize( 50 );
        instagramIcon.setIconColor( new Color( 0,0,0 ));
        labelInstagram.setIcon( instagramIcon );

        FontIcon youTubeIcon = FontIcon.of(MaterialDesignY.YOUTUBE );
        youTubeIcon.setIconSize( 50 );
        youTubeIcon.setIconColor( new Color( 0,0,0 ));
        labelYouTube.setIcon( youTubeIcon );

        FontIcon twitterIcon = FontIcon.of(MaterialDesignT.TWITTER );
        twitterIcon.setIconSize( 50 );
        twitterIcon.setIconColor( new Color( 0,0,0 ));
        labelTwitter.setIcon( twitterIcon );

        FontIcon linkedinIcon = FontIcon.of(MaterialDesignL.LINKEDIN );
        linkedinIcon.setIconSize( 50 );
        linkedinIcon.setIconColor( new Color( 0,0,0 ));
        labelLinkedin.setIcon( linkedinIcon );

        registerButton.setBorder( BorderFactory.createEmptyBorder() );
    }

    private void buildLeftPane() {

        FontIcon userIcon = FontIcon.of( MaterialDesignA.ACCOUNT );
        userIcon.setIconSize( 40 );
        userIcon.setIconColor( new Color( 0, 0, 0 ) );
        labelUserIcon.setIcon( userIcon );

        FontIcon passwordIcon = FontIcon.of( MaterialDesignL.LOCK );
        passwordIcon.setIconSize( 40 );
        passwordIcon.setIconColor( new Color( 0, 0, 0 ) );
        labelPasswordIcon.setIcon( passwordIcon );

        labelFeedback.setVisible( false );

        textFieldUser.setBorder( BorderFactory.createMatteBorder( 0,0, 1, 0, Color.gray ) );
        passwordField.setBorder( BorderFactory.createMatteBorder( 0,0, 1, 0, Color.gray ) );
    }
}
