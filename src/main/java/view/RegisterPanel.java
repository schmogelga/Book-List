package view;

import core.data.User;
import core.service.UserManager;
import misc.exception.DataEntryException;
import misc.util.ApplicationContext;
import misc.util.Validator;
import org.kordamp.ikonli.materialdesign2.*;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.security.NoSuchAlgorithmException;

public class RegisterPanel {

    private JPanel rootPanel;
    private JPanel leftPane;
    private JPanel rightPane;
    private JButton loginButton;
    private JLabel labelLogo;
    private JTextField textFieldName;
    private JButton enterButton;
    private JPasswordField passwordField;
    private JLabel nameLabel;
    private JLabel emailLabel;
    private JLabel loginLabel;
    private JLabel passwordLabel;
    private JTextField textFieldEmail;
    private JTextField textFieldLogin;
    private JLabel labelNameIcon;
    private JLabel labelEmailIcon;
    private JLabel labelLoginIcon;
    private JLabel labelPasswordIcon;
    private JLabel labelFeedback;

    MainFrame mainFrame;

    public RegisterPanel(JFrame frame ) {

        mainFrame = (MainFrame) frame;

        buildRightPane();
        buildLeftPane();

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                loginButton.setForeground( Color.BLACK );
                passwordField.setText( "" );
                textFieldName.setText( "" );
                textFieldEmail.setText( "" );
                textFieldLogin.setText( "" );
                labelFeedback.setVisible( false );

                mainFrame.setPanel( mainFrame.getLoginPane().getRootPanel() );
            }
        });
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                loginButton.setForeground( Color.gray );
            }
            @Override
            public void mouseExited( MouseEvent e ){
                loginButton.setForeground( Color.BLACK );
            }
        });
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performRegister();
            }
        });
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if( e.getKeyCode() == KeyEvent.VK_ENTER ) {
                    performRegister();
                }
            }
        });
    }

    private void buildLeftPane() {

        FontIcon logoIcon = FontIcon.of( MaterialDesignB.BOOK );
        logoIcon.setIconSize( 150 );
        logoIcon.setIconColor( new Color( 255, 255, 255) );
        labelLogo.setIcon( logoIcon );
    }

    private void buildRightPane() {

        labelFeedback.setVisible( false );

        Border bottomBorder = BorderFactory.createMatteBorder( 0,0, 1, 0, Color.gray );
        textFieldEmail.setBorder( bottomBorder );
        textFieldLogin.setBorder( bottomBorder );
        textFieldName.setBorder( bottomBorder );
        passwordField.setBorder( bottomBorder );

        FontIcon emailIcon = FontIcon.of(MaterialDesignE.EMAIL );
        emailIcon.setIconSize( 40 );
        emailIcon.setIconColor( new Color( 0, 0, 0) );
        labelEmailIcon.setIcon( emailIcon );

        FontIcon passwordIcon = FontIcon.of(MaterialDesignL.LOCK );
        passwordIcon.setIconSize( 40 );
        passwordIcon.setIconColor( new Color( 0, 0, 0) );
        labelPasswordIcon.setIcon( passwordIcon );

        FontIcon nameIcon = FontIcon.of(MaterialDesignA.ACCOUNT );
        nameIcon.setIconSize( 40 );
        nameIcon.setIconColor( new Color( 0, 0, 0) );
        labelNameIcon.setIcon( nameIcon );

        FontIcon loginIcon = FontIcon.of( MaterialDesignL.LOGIN );
        loginIcon.setIconSize( 40 );
        loginIcon.setIconColor( new Color( 0, 0, 0 ) );
        labelLoginIcon.setIcon( loginIcon );
    }

    public JPanel getRootPanel() {
        return this.rootPanel;
    }

    private void performRegister() {

        try {

            Validator.validateName( textFieldName.getText() );
            Validator.validateEmail( textFieldEmail.getText() );
            Validator.validateLogin( textFieldLogin.getText() );
            Validator.validatePassword( passwordField.getText() );

            labelFeedback.setVisible( false );
            User user = UserManager.getInstance().createUser( textFieldName.getText(), textFieldEmail.getText(), textFieldLogin.getText(), passwordField.getText() );
            ApplicationContext.getInstance().setLoggedUser( user );
            mainFrame.getWelcomePane().initCards();
            mainFrame.setPanel( mainFrame.getWelcomePane().getRootPanel() );

        } catch ( DataEntryException ex ) {

            labelFeedback.setText( ex.getMessage() );
            labelFeedback.setVisible( true );
        }
    }
}
