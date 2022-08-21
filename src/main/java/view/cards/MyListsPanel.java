package view.cards;

import core.data.Book;
import core.data.BookList;
import core.service.BookManager;
import misc.util.ApplicationContext;
import org.kordamp.ikonli.materialdesign2.MaterialDesignD;
import org.kordamp.ikonli.materialdesign2.MaterialDesignP;
import org.kordamp.ikonli.materialdesign2.MaterialDesignS;
import org.kordamp.ikonli.swing.FontIcon;
import view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

    public class MyListsPanel {

    private JPanel rootPanel;
    private JList jListLists;
    private JButton addListButton;
    private JList jListBooks;
    private JLabel bookListNameLabel;
    private JLabel bookListDescriptionLabel;
    private JButton editButton;
    private JButton deleteButton;
    private JLabel labelFeedback;
        private JScrollPane scrollPaneLists;
        private JScrollPane scrollPaneBooks;
        private JLabel bookLabel;
        private JLabel publisherLabel;
        private JLabel publishDateLabel;
        private JLabel authorsLabel;
        private JLabel categoriesLabel;
        private JLabel titleLabel;
        private JButton removeBookButton;
        private MainFrame mainFrame;
    private ArrayList<BookList> bookLists = new ArrayList<>();
    private BookListEditor bookListEditor;
        private BookList activeList;
        private Book activeBook;

    public MyListsPanel( MainFrame mainFrame ) {

        this.mainFrame = mainFrame;

        jListLists.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                activeList = (BookList) jListLists.getSelectedValue();
                setActiveBookList( null );
            }
        });

        jListBooks.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                activeBook = (Book) jListBooks.getSelectedValue();
                setActiveBook();
            }
        });

        addListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                bookListEditor.setVisible( true );
                bookListEditor.setMode( BookListEditor.MODE_CREATE );
                bookListEditor.resetInfo();
                labelFeedback.setText( "" );
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if( activeList == null ) {
                    labelFeedback.setText("Select a list!");
                    labelFeedback.setVisible(true);
                } else {
                    labelFeedback.setText( "" );
                    bookListEditor.setBookList( activeList );
                    bookListEditor.setMode( BookListEditor.MODE_UPDATE );
                    bookListEditor.setVisible( true );
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if( activeList == null ) {
                    labelFeedback.setText( "Select a list!" );
                    labelFeedback.setVisible(true);
                } else {
                    BookManager.getInstance().deleteBookList( activeList );
                    bookListNameLabel.setText( "Select a list" );
                    bookListDescriptionLabel.setText( "Description" );
                    labelFeedback.setText( "" );
                    refreshLists();
                    activeList = null;
                }
            }
        });
        removeBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                BookManager.getInstance().unmapBook( activeList, activeBook );
                activeBook = new Book();

                setActiveBookList( activeList );
                setActiveBook();


            }
        });
    }

        private void setActiveBook() {

            titleLabel.setText( "<html><p style=\"width: 230px;text-align: left;\">" + activeBook.getTitle() + "</p></html>" );
            publisherLabel.setText( "<html><p style=\"width: 260px;text-align: left;\">" + "<b> Publisher: </b> " + BookManager.getInstance().getPublishers( activeBook ) + "</p></html>" );
            publishDateLabel.setText( "<html><p style=\"width: 260px;text-align: left;\">" + "<b> Publish Date: </b> " + activeBook.getYear() + "</p></html>" );
            authorsLabel.setText( "<html><p style=\"width: 260px;text-align: left;\">" + "<b> Authors: </b> " + BookManager.getInstance().getAuthors( activeBook ) + "</p></html>" );
            categoriesLabel.setText( "<html><p style=\"width: 260px;text-align: left;\">" + "<b> Categories: </b> " + BookManager.getInstance().getCategories( activeBook ) + "</p></html>" );

        }

        public void setActiveBookList( BookList bookList ) {

        if( bookList != null ) {
            activeList = bookList;
        }

        bookListNameLabel.setText( activeList.getName() );
        bookListDescriptionLabel.setText( activeList.getDescription() );

        jListBooks.setListData( BookManager.getInstance().getBooks( activeList ).toArray() );
        bookLabel.setVisible( true );
    }

    public void initComp() {

        bookListEditor = new BookListEditor( this );
        scrollPaneLists.setBorder( BorderFactory.createEmptyBorder() );
        scrollPaneBooks.setBorder( BorderFactory.createEmptyBorder() );
        jListLists.setBorder( BorderFactory.createEmptyBorder() );

        buildButtons();
        refreshLists();

        rootPanel.setVisible( true );
    }

    private void setBooks() {

    }

    private void buildButtons() {

        FontIcon editIcon = FontIcon.of( MaterialDesignS.SQUARE_EDIT_OUTLINE );
        editIcon.setIconSize( 25 );
        editIcon.setIconColor( Color.BLACK );
        editButton.setIcon( editIcon );

        FontIcon deleteIcon = FontIcon.of( MaterialDesignD.DELETE );
        deleteIcon.setIconSize( 25 );
        deleteIcon.setIconColor( new Color(172, 0, 0) );
        deleteButton.setIcon( deleteIcon );

        removeBookButton.setIcon( deleteIcon );

        FontIcon addIcon = FontIcon.of( MaterialDesignP.PLUS_THICK );
        addIcon.setIconSize( 25 );
        addIcon.setIconColor( new Color(0x06AF00) );
        addListButton.setIcon( addIcon );
    }

    public void refreshLists() {

        bookLists = BookManager.getInstance().getBookLists( ApplicationContext.getInstance().getLoggedUser().getId() );
        jListLists.setListData( bookLists.toArray() );
        jListBooks.setListData( new ArrayList<>().toArray() );
        bookLabel.setVisible( false );
    }


    public JPanel getRootPanel() {
        return rootPanel;
    }

    }
