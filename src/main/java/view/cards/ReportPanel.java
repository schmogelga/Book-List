package view.cards;

import core.data.*;
import core.service.BookManager;
import core.service.ReportManager;
import core.service.UserManager;
import misc.util.Handler;
import org.kordamp.ikonli.materialdesign2.MaterialDesignR;
import org.kordamp.ikonli.swing.FontIcon;
import view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReportPanel {

    private JPanel rootPanel;
    private JComboBox comboBoxReports;
    private JButton generateButton;
    private JPanel reportAForm;
    private JPanel reportBForm;
    private JPanel reportCForm;
    private JPanel reportDForm;
    private JPanel reportsPanel;
    private JPanel cardsPanel;
    private JButton refreshButton;
    private JComboBox comboBoxAuthors;
    private JComboBox comboBoxUsers;
    private MainFrame mainFrame;
    private List<JPanel> cards = new ArrayList<>();
    private Report reportAllBooks;
    private Report reportAllUsers;
    private Report reportBooksByAuthor;
    private Report reportListsByUser;
    private List<Report> reports = new ArrayList<>();
    private final int ALL_INDEX = 0;

    public ReportPanel(MainFrame mainFrame ) {
        this.mainFrame = mainFrame;

        comboBoxReports.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setActiveCard( ((FormItem) comboBoxReports.getSelectedItem()).getPanel() );
            }
        });
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateReport( ((FormItem) comboBoxReports.getSelectedItem()).getIndex() );
            }
        });
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comboBoxAuthors.setSelectedIndex( ALL_INDEX );
                comboBoxUsers.setSelectedIndex( ALL_INDEX );

                comboBoxReports.setSelectedIndex( 0 );
                setActiveCard( ((FormItem) comboBoxReports.getSelectedItem()).getPanel() );
                refreshFilters();
            }
        });
    }

    private void generateReport( int index )  {

        try {

            ReportManager.getInstance().createReport( reports.get( index ) );
        } catch (SQLException e) {
            Handler.handle( e );
        }
    }

    private void initReports(){
        reportAllBooks = new Report() {
            @Override
            public HashMap getParameters() {
                return new HashMap();
            }

            @Override
            public String getPath() {
                return "reports/allBooks.jrxml";
            }
        };

        reportAllUsers = new Report() {
            @Override
            public HashMap getParameters() {
                return new HashMap();
            }

            @Override
            public String getPath() {
                return "reports/allUsers.jrxml";
            }
        };

        reportBooksByAuthor = new Report() {
            @Override
            public HashMap getParameters() {

                HashMap<String, Integer> parameters = new HashMap<>();

                Author selectedAuthor = (Author) comboBoxAuthors.getSelectedItem();
                parameters.put( "author_id", selectedAuthor.getId() );

                return parameters;
            }
            @Override
            public String getPath() {
                return "reports/booksByAuthor.jrxml";
            }
        };

        reportListsByUser = new Report() {
            @Override
            public HashMap getParameters() {
                HashMap<String, Integer> parameters = new HashMap<>();

                User selectedUser = (User) comboBoxUsers.getSelectedItem();
                parameters.put( "users_id", selectedUser.getId() );

                return parameters;
            }
            @Override
            public String getPath() {
                return "reports/listsByUser.jrxml";
            }
        };

        reports.add(reportAllBooks);
        reports.add(reportAllUsers);
        reports.add(reportBooksByAuthor);
        reports.add(reportListsByUser);

    }


    public void initComp() {

        cards.add( reportAForm );
        cards.add( reportBForm );
        cards.add( reportCForm );
        cards.add( reportDForm );

        comboBoxReports.addItem( new FormItem( reportAForm, 0, "All Books") );
        comboBoxReports.addItem( new FormItem( reportBForm, 1, "All Users") );
        comboBoxReports.addItem( new FormItem( reportCForm, 2, "Books by Author") );
        comboBoxReports.addItem( new FormItem( reportDForm, 3, "Lists by User") );

        refreshFilters();

        initReports();
        buildButtons();
    }

    private void refreshFilters() {
        comboBoxAuthors.addItem( new Author( "All") );
        for( Author author : BookManager.getInstance().getAuthors() ) {
            comboBoxAuthors.addItem( author );
        }

        comboBoxUsers.addItem( new User( "All" ) );
        for( User user : UserManager.getInstance().getUsers() ) {
            comboBoxUsers.addItem( user );
        }
    }

    private void buildButtons() {

        FontIcon refreshIcon = FontIcon.of( MaterialDesignR.REFRESH );
        refreshIcon.setIconSize( 25 );
        refreshIcon.setIconColor( new Color( 0x06AF00 ) );
        refreshButton.setIcon( refreshIcon );
    }

    private void setActiveCard( JPanel panel ){

        for( JPanel card : cards ) {
            card.setVisible( false );
        }

        panel.setVisible( true );
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }


}

class FormItem{

    private JPanel panel;
    private int index;
    private String label;

    public FormItem(JPanel panel, int index, String label) {
        this.panel = panel;
        this.index = index;
        this.label = label;
    }

    @Override
    public String toString(){
        return this.label;
    }

    public JPanel getPanel() {
        return panel;
    }

    public int getIndex() {
        return index;
    }
}
