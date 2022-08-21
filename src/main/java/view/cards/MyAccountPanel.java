package view.cards;

import core.service.UserManager;
import misc.exception.DataEntryException;
import misc.util.ApplicationContext;
import misc.util.Validator;
import org.kordamp.ikonli.materialdesign2.MaterialDesignA;
import org.kordamp.ikonli.materialdesign2.MaterialDesignL;
import org.kordamp.ikonli.materialdesign2.MaterialDesignS;
import org.kordamp.ikonli.swing.FontIcon;
import view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MyAccountPanel {
    private JPanel rootPanel;
    private JPanel panelMyAccount;
    private JLabel labelUserIcon;
    private JTextField textFieldEmail;
    private JTextField textFieldLogin;
    private JButton editNameButton;
    private JTextField textFieldUserName;
    private JButton editEmailButton;
    private JButton editLoginButton;
    private JButton submitPasswordButton;
    private JLabel newPasswordLabel;
    private JLabel repeatPasswordLabel;
    private JLabel oldPasswordLabel;
    private JButton editPasswordButton;
    private JPasswordField newPasswordField;
    private JPasswordField repeatPasswordField;
    private JButton logoutButton;
    private JPasswordField oldPasswordField;
    private JLabel labelFeedback;
    private JLabel feedbackEmailLabel;
    private JLabel feedbackLoginLabel;
    private JLabel feedbackNameLabel;
    private JButton deleteAccountButton;
    private MainFrame mainFrame;

    public MyAccountPanel( MainFrame mainFrame ) {

        this.mainFrame = mainFrame;
        rootPanel.setVisible( true );

        editEmailButton.addMouseListener( new MouseAdapter() {
            @Override
            public void mouseEntered( MouseEvent e ) {
                super.mouseEntered( e );
                FontIcon icon = (FontIcon) editEmailButton.getIcon();
                icon.setIconColor( Color.gray );
            }
            @Override
            public void mouseExited( MouseEvent e ){
                FontIcon icon = (FontIcon) editEmailButton.getIcon();
                icon.setIconColor( Color.black );            }
        });
        editEmailButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                textFieldEmail.setSelectionStart( 0 );
                textFieldEmail.setSelectionEnd( textFieldEmail.getText().length() );
                textFieldEmail.setEditable( true );
                textFieldEmail.grabFocus();
                textFieldEmail.setBorder( BorderFactory.createMatteBorder( 0,0, 1, 0, Color.gray ) );
            }
        });

        textFieldEmail.addKeyListener( new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased( e );
                if( e.getKeyCode() == KeyEvent.VK_ENTER ) {

                    try {
                        Validator.validateEmail( textFieldEmail.getText());

                        ApplicationContext.getInstance().getLoggedUser().setEmail( textFieldEmail.getText() );
                        UserManager.getInstance().updateUser( ApplicationContext.getInstance().getLoggedUser() );

                        lockTextField( textFieldEmail );
                        feedbackEmailLabel.setText( "" );

                    } catch ( DataEntryException ex ) {

                        feedbackEmailLabel.setVisible( true );
                        feedbackEmailLabel.setText( ex.getMessage() );
                    }
                } else if( e.getKeyCode() == KeyEvent.VK_ESCAPE ) {

                    textFieldEmail.setText( ApplicationContext.getInstance().getLoggedUser().getEmail() );
                    lockTextField( textFieldEmail );
                }
            }
        });

        editLoginButton.addMouseListener( new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered( e );
                FontIcon icon = (FontIcon) editLoginButton.getIcon();
                icon.setIconColor( Color.gray );
            }
            @Override
            public void mouseExited( MouseEvent e ){
                FontIcon icon = (FontIcon) editLoginButton.getIcon();
                icon.setIconColor( Color.black );
            }
        });
        editLoginButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {

                editTextField( textFieldLogin );
            }
        });

        textFieldLogin.addKeyListener( new KeyAdapter() {
            @Override
            public void keyReleased( KeyEvent e ) {
                super.keyReleased( e );
                if( e.getKeyCode() == KeyEvent.VK_ENTER ) {

                    try {
                        Validator.validateLogin(textFieldLogin.getText());

                        ApplicationContext.getInstance().getLoggedUser().setLogin(textFieldLogin.getText());
                        UserManager.getInstance().updateUser(ApplicationContext.getInstance().getLoggedUser());

                        lockTextField(textFieldLogin);
                        feedbackLoginLabel.setText("");

                    } catch (DataEntryException ex) {

                        feedbackLoginLabel.setVisible(true);
                        feedbackLoginLabel.setText(ex.getMessage());
                    }
                } else if( e.getKeyCode() == KeyEvent.VK_ESCAPE ) {

                    textFieldLogin.setText( ApplicationContext.getInstance().getLoggedUser().getLogin() );
                    lockTextField( textFieldLogin );
                }
            }
        });

        editNameButton.addMouseListener( new MouseAdapter() {
            @Override
            public void mouseEntered( MouseEvent e ) {
                super.mouseEntered( e );
                FontIcon icon = (FontIcon) editNameButton.getIcon();
                icon.setIconColor( Color.gray );
            }
            @Override
            public void mouseExited( MouseEvent e ){
                FontIcon icon = (FontIcon) editNameButton.getIcon();
                icon.setIconColor( Color.black );            }
        });
        editNameButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {

                editTextField( textFieldUserName );
            }
        });

        textFieldUserName.addKeyListener( new KeyAdapter() {
            @Override
            public void keyReleased( KeyEvent e) {
                super.keyReleased( e );
                if( e.getKeyCode() == KeyEvent.VK_ENTER ) {

                    try {

                        Validator.validateName( textFieldUserName.getText() );

                        ApplicationContext.getInstance().getLoggedUser().setName(textFieldUserName.getText());
                        UserManager.getInstance().updateUser(ApplicationContext.getInstance().getLoggedUser());

                        lockTextField( textFieldUserName );
                        feedbackNameLabel.setText( "" );

                    } catch ( DataEntryException ex ) {

                        feedbackNameLabel.setVisible( true );
                        feedbackNameLabel.setText( ex.getMessage() );
                    }
                } else if( e.getKeyCode() == KeyEvent.VK_ESCAPE ) {

                    textFieldUserName.setText( ApplicationContext.getInstance().getLoggedUser().getName() );
                    lockTextField( textFieldUserName );
                }
            }
        });

        logoutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                logoutButton.setForeground( Color.gray );
                FontIcon icon = (FontIcon) logoutButton.getIcon();
                icon.setIconColor( Color.gray );
            }
            @Override
            public void mouseExited( MouseEvent e ){
                logoutButton.setForeground( Color.BLACK );
                FontIcon icon = (FontIcon) logoutButton.getIcon();
                icon.setIconColor( Color.black );            }
        });

        logoutButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                mainFrame.setPanel( mainFrame.getLoginPane().getRootPanel() );
                ApplicationContext.getInstance().setLoggedUser( null );
            }
        });

        editPasswordButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                editPasswordButton.setText( "Edit Password" );
                setPasswordFieldsVisible( true );
            }
        });

        submitPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {

                try {

                    Validator.validatePassword( newPasswordField.getText() );
                    UserManager.getInstance().login( ApplicationContext.getInstance().getLoggedUser().getLogin(), oldPasswordField.getText() );

                    if( !newPasswordField.getText().equals( repeatPasswordField.getText() ) ) {
                        throw new DataEntryException( "Passwords must be the same!" );
                    }

                    UserManager.getInstance().changePassword( ApplicationContext.getInstance().getLoggedUser().getLogin(), newPasswordField.getText() );
                    setPasswordFieldsVisible( false );
                    labelFeedback.setText( "" );
                    editPasswordButton.setText( "Password Changed!");

                } catch ( DataEntryException ex ) {

                    labelFeedback.setText( ex.getMessage() );
                    labelFeedback.setVisible( true );
                }
            }
        });

        editPasswordButton.addMouseListener( new MouseAdapter() {
            @Override
            public void mouseEntered( MouseEvent e ) {
                super.mouseEntered( e );
                editPasswordButton.setForeground( Color.gray );
            }
            @Override
            public void mouseExited( MouseEvent e ){
                editPasswordButton.setForeground( Color.BLACK );
            }
        });
        deleteAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                mainFrame.setPanel( mainFrame.getLoginPane().getRootPanel() );
                UserManager.getInstance().deleteUser( ApplicationContext.getInstance().getLoggedUser() );

                ApplicationContext.getInstance().setLoggedUser( null );


            }
        });
    }

    private void editTextField( JTextField field ) {

        field.setSelectionStart( 0 );
        field.setSelectionEnd( field.getText().length() );
        field.setEditable( true );
        field.grabFocus();
        field.setBorder( BorderFactory.createMatteBorder( 0,0, 1, 0, Color.gray ) );
    }

    private void lockTextField( JTextField field ) {

        field.setEditable( false );
        field.setBorder( BorderFactory.createEmptyBorder());
        field.transferFocus();
    }

    public void initComp() {
        rootPanel.setVisible( true );

        FontIcon userIcon = FontIcon.of( MaterialDesignA.ACCOUNT_CIRCLE );
        userIcon.setIconSize( 220 );
        userIcon.setIconColor( new Color( 80,80,80 ));
        labelUserIcon.setIcon( userIcon );

        textFieldEmail.setBorder( BorderFactory.createEmptyBorder() );
        textFieldUserName.setBorder( BorderFactory.createEmptyBorder() );
        textFieldLogin.setBorder( BorderFactory.createEmptyBorder() );

        textFieldEmail.setText( ApplicationContext.getInstance().getLoggedUser().getEmail() );
        textFieldUserName.setText( ApplicationContext.getInstance().getLoggedUser().getName() );
        textFieldLogin.setText( ApplicationContext.getInstance().getLoggedUser().getLogin() );

        FontIcon  editIcon = FontIcon.of(MaterialDesignS.SQUARE_EDIT_OUTLINE );
        editIcon.setIconSize( 25 );
        editIcon.setIconColor( new Color( 0 ));
        editEmailButton.setIcon( editIcon );
        editLoginButton.setIcon( editIcon );
        editNameButton.setIcon( editIcon );

        FontIcon logoutIcon = FontIcon.of( MaterialDesignL.LOGOUT );
        logoutIcon.setIconSize( 30 );
        logoutIcon.setIconColor( new Color(0 ) );
        logoutButton.setIcon( logoutIcon );

        oldPasswordField.setBorder( BorderFactory.createMatteBorder( 0,0, 1, 0, Color.gray ) );
        newPasswordField.setBorder( BorderFactory.createMatteBorder( 0,0, 1, 0, Color.gray ) );
        repeatPasswordField.setBorder( BorderFactory.createMatteBorder( 0,0, 1, 0, Color.gray ) );

        setPasswordFieldsVisible( false );
    }

    void setPasswordFieldsVisible( boolean visible ) {

        oldPasswordField.setVisible( visible );
        newPasswordField.setVisible( visible );
        repeatPasswordField.setVisible( visible );
        newPasswordLabel.setVisible( visible );
        oldPasswordLabel.setVisible( visible );
        repeatPasswordLabel.setVisible( visible );
        submitPasswordButton.setVisible( visible );

    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
