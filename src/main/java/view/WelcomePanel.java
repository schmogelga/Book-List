package view;

import core.data.User;
import org.kordamp.ikonli.materialdesign2.MaterialDesignB;
import org.kordamp.ikonli.swing.FontIcon;
import view.cards.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class WelcomePanel {

    private MainFrame mainFrame;
    private JPanel rootPanel;
    private JLabel labelLogo;
    private JLabel labelName;
    private JLabel labelVersion;
    private JPanel leftPane;
    private JPanel rightPane;
    private JButton buttonDiscover;
    private JButton buttonMyLists;
    private JButton buttonMyAccount;
    private JPanel panelDiscover;
    private JPanel panelMyLists;
    private JPanel panelMyAccount;
    private JPanel panelMyBooks;
    private JButton buttonMyBooks;
    private JButton buttonReports;
    private JPanel panelReports;
    private ArrayList<JButton> buttons;
    private ArrayList<JPanel> panels;
    private User loggedUser;
    private MyAccountPanel myAccountCard;
    private DiscoverPanel discoverCard;
    private MyListsPanel myListsCard;
    private MyBooksPanel myBooksCard;
    private ReportPanel reportsCard;

    public WelcomePanel(JFrame frame) {

        this.mainFrame = (MainFrame) frame;
        myAccountCard = new MyAccountPanel( mainFrame );
        discoverCard = new DiscoverPanel( mainFrame );
        myListsCard = new MyListsPanel( mainFrame );
        myBooksCard = new MyBooksPanel( mainFrame );
        reportsCard = new ReportPanel( mainFrame );

        panelMyAccount.add( myAccountCard.getRootPanel() );
        panelDiscover.add( discoverCard.getRootPanel() );
        panelMyLists.add( myListsCard.getRootPanel() );
        panelMyBooks.add( myBooksCard.getRootPanel() );
        panelReports.add( reportsCard.getRootPanel() );

        buildLeftPane();
        buildInternalLists();

        buttonDiscover.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                setActiveButton( buttonDiscover );
                setActivePanel( panelDiscover );
            }
        });

        buttonMyLists.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                setActiveButton( buttonMyLists );
                setActivePanel( panelMyLists );
            }
        });

        buttonMyAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                setActiveButton( buttonMyAccount );
                setActivePanel( panelMyAccount );
            }
        });

        buttonMyBooks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                setActiveButton( buttonMyBooks );
                setActivePanel( panelMyBooks );
            }
        });
        buttonReports.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                setActiveButton( buttonReports );
                setActivePanel( panelReports );
            }
        });
    }

    public void initCards() {

        discoverCard.initComp();
        myListsCard.initComp();
        myAccountCard.initComp();
        myBooksCard.initComp();
        reportsCard.initComp();
    }

    private void setActivePanel( JPanel panel ) {

        hidePanes();
        panel.setVisible( true );
    }

    private void hidePanes() {

        for( JPanel panel : this.panels ) {
            panel.setVisible( false );
        }
    }

    private void setActiveButton( JButton button ) {

        resetButtons();

        button.setBackground( new Color (35, 171, 0 ) );
        button.setForeground( new Color ( 255, 255, 255 ) );
    }

    private void resetButtons() {
        for( JButton button : this.buttons ) {

            button.setBackground( new Color (235, 235, 235 ) );
            button.setForeground( new Color ( 0 ) );
        }
    }

    private void buildInternalLists() {

        buttons = new ArrayList<JButton>();
        buttons.add( buttonDiscover );
        buttons.add( buttonMyLists );
        buttons.add( buttonMyAccount );
        buttons.add( buttonMyBooks );
        buttons.add( buttonReports );

        panels = new ArrayList<JPanel>();
        panels.add( panelMyAccount );
        panels.add( panelMyLists );
        panels.add( panelDiscover );
        panels.add( panelMyBooks );
        panels.add( panelReports );
    }

    private void buildLeftPane() {

        leftPane.setSize( 500, 720 );

        FontIcon logoIcon = FontIcon.of( MaterialDesignB.BOOK );
        logoIcon.setIconSize( 50 );
        logoIcon.setIconColor( new Color( 35, 171, 0) );
        labelLogo.setIcon( logoIcon );
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}