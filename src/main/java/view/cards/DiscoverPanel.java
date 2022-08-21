package view.cards;

import com.google.gson.JsonObject;
import core.data.Author;
import core.data.Book;
import core.data.BookList;
import core.data.Category;
import core.service.BookManager;
import core.service.RequestProvider;
import misc.util.ApplicationContext;
import org.kordamp.ikonli.materialdesign2.MaterialDesignA;
import org.kordamp.ikonli.materialdesign2.MaterialDesignH;
import org.kordamp.ikonli.swing.FontIcon;
import view.MainFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DiscoverPanel {

    private JPanel rootPanel;
    private JPanel discoverPanel;
    private JPanel leftPanel;
    private JPanel RightPanel;
    private JTextField textFieldSearch;
    private JButton nextButtom;
    private JList jList;
    private JButton homeButton;
    private JButton previousButton;
    private JComboBox comboBoxSearch;
    private JLabel resultsLabel;
    private JPanel coverPanel;
    private JLabel titleLabel;
    private JLabel ISBN10Label;
    private JLabel ISBN13Label;
    private JLabel publisherLabel;
    private JLabel authorsLabel;
    private JLabel categoriesLabel;
    private JLabel publishDateLabel;
    private JButton detaildButton;
    private JButton descriptionButton;
    private JPanel detailCard;
    private JPanel descriptionCard;
    private JLabel descriptionLabel;
    private JPanel cardPanel;
    private JScrollPane descriptionJScrollPanel;
    private JButton addToListButton;
    private JPanel addToListCard;
    private JList listsJList;
    private JScrollPane scrollPane;
    private JButton addButton;
    private JLabel selectLabel;
    private List<JButton> buttons = new ArrayList();
    private MainFrame mainFrame;
    private List<Book> books;
    private Book activeBook;
    private int bookListIndex;
    private int maxResults;
    private JLabel coverLabel;
    private ImageIcon defaultCover;

    private final String[] fields = {
            "intitle",
            "inauthor",
            "inpublisher",
            "subject",
            "isbn"
    };

    public DiscoverPanel( MainFrame mainFrame ) {
        this.mainFrame = mainFrame;

        textFieldSearch.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);

                if( textFieldSearch.getText().equals( "Search" ) ) {
                    textFieldSearch.setText( "" );
                }

                textFieldSearch.setForeground( Color.BLACK );
            }
        });
        textFieldSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if( e.getKeyCode() == KeyEvent.VK_ENTER ) {
                    performSearch();
                }
            }
        });
        nextButtom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if( maxResults < 10 ) {
                    bookListIndex = 0;
                } else {
                    bookListIndex = bookListIndex + 10 > maxResults ? maxResults - 10 : bookListIndex + 10;
                }
                performSearch();
            }
        });
        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                bookListIndex -= bookListIndex - 10 < 0 ? bookListIndex : 10;
                performSearch();
            }
        });
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookListIndex = 0;
                performSearch();
            }
        });

        jList.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                selectLabel.setText( "Select a list and click 'ADD'" );
                activeBook = (Book) jList.getSelectedValue();
                setActiveBook();
            }
        });

        detaildButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setActiveButton( detaildButton );
                descriptionCard.setVisible( false );
                addToListCard.setVisible( false );
                detailCard.setVisible( true );
            }
        });

        descriptionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setActiveButton( descriptionButton );
                detailCard.setVisible( false );
                addToListCard.setVisible( false );
                descriptionCard.setVisible( true );
            }
        });

        addToListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setActiveButton(addToListButton);
                detailCard.setVisible( false );
                descriptionCard.setVisible( false );
                addToListCard.setVisible( true );

                selectLabel.setText( "Select a list and click 'ADD'" );
                selectLabel.setForeground( new Color( 150, 150, 150 ) );

                updateLists();
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBook();
            }
        });
    }

    private void addBook() {
        BookList bookList = (BookList) listsJList.getSelectedValue();
        if( bookList == null || activeBook == null ) {
            selectLabel.setText( "Select a book and a List!" );
            selectLabel.setForeground( Color.RED );
        } else {

            BookManager.getInstance().addToList( activeBook, bookList );

            selectLabel.setText( "Book added!" );
            selectLabel.setForeground( new Color( 150, 150, 150 ) );

        }
    }

    private void updateLists() {

        listsJList.setListData( BookManager.getInstance().getBookLists( ApplicationContext.getInstance().getLoggedUser().getId() ).toArray() );
    }

    public  void setActiveButton( JButton activeButton ) {

        for( JButton button : this.buttons ) {
            button.setForeground( Color.BLACK );
            button.setBackground( Color.WHITE );
        }

        activeButton.setForeground( Color.WHITE );
        activeButton.setBackground( new Color( 0x06AF00 ) );
    }

    private void performSearch() {

        JsonObject json = RequestProvider.getInstance().searchBooks( fields[comboBoxSearch.getSelectedIndex()], textFieldSearch.getText(), bookListIndex );
        if( json != null ) {
            maxResults = json.get( "totalItems" ).getAsInt();
            resultsLabel.setText( "Found " + maxResults + " results" );
            if( maxResults == 0 ) {
                jList.setListData( new Book[]{} );

            } else {
                books = RequestProvider.getInstance().getBooks( json );
                jList.setListData( books.toArray() );
            }
        }
    }

    public void initComp() {

        buttons.add( descriptionButton );
        buttons.add( detaildButton );
        buttons.add(addToListButton);

        scrollPane.setBorder( BorderFactory.createEmptyBorder() );
        scrollPane.getViewport().setBackground(Color.WHITE);

        descriptionJScrollPanel.setBorder( BorderFactory.createEmptyBorder() );
        descriptionJScrollPanel.getViewport().setBackground(Color.WHITE);

        jList.setBorder( BorderFactory.createEmptyBorder() );
        jList.setFixedCellHeight( 30 );
        jList.setFixedCellWidth( 250 );

        try {

            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream input = loader.getResourceAsStream( "images/defaultCover.png" );

            BufferedImage image = ImageIO.read( input );
            defaultCover = new ImageIcon( image.getScaledInstance( 150, 220, Image.SCALE_DEFAULT) );

        } catch (IOException e) {
            e.printStackTrace();
        }
        coverLabel = new JLabel( defaultCover );
        coverPanel.add( coverLabel );

        comboBoxSearch.addItem( "Title" );
        comboBoxSearch.addItem( "Author" );
        comboBoxSearch.addItem( "Publisher" );
        comboBoxSearch.addItem( "Category" );
        comboBoxSearch.addItem( "ISBN" );

        textFieldSearch.setBorder( BorderFactory.createMatteBorder( 0,0, 1, 0, Color.gray ) );
        bookListIndex = 0;
        buildButtons();
        rootPanel.setVisible( true );
    }

    private void setActiveBook() {

        StringBuilder authors = new StringBuilder();
        for( Author author : activeBook.getAuthors() ) {
            authors.append((authors.length() == 0) ? author.getName() : "; " + author.getName());
        }

        StringBuilder categories = new StringBuilder();
        for( Category category : activeBook.getCategories() ) {
            categories.append((categories.length() == 0) ? category.getName() : "; " + category.getName());
        }

        titleLabel.setText( "<html><p style=\"width: 230px;text-align: center;\">" + activeBook.getTitle() + "</p></html>" );
        publisherLabel.setText( "<html><p style=\"width: 260px;text-align: left;\">" + "<b> Publisher: </b> " + activeBook.getPublisher().getName() + "</p></html>" );
        publishDateLabel.setText( "<html><p style=\"width: 260px;text-align: left;\">" + "<b> Publish Date: </b> " + activeBook.getYear() + "</p></html>" );
        authorsLabel.setText( "<html><p style=\"width: 260px;text-align: left;\">" + "<b> Authors: </b> " + authors + "</p></html>" );
        categoriesLabel.setText( "<html><p style=\"width: 260px;text-align: left;\">" + "<b> Categories: </b> " + categories + "</p></html>" );
        ISBN10Label.setText( "<html><p style=\"width: 260px;text-align: left;\">" + "<b> ISBN-10: </b> " + activeBook.getISBN_10() + "</p></html>" );
        ISBN13Label.setText( "<html><p style=\"width: 260px;text-align: left;\">" + "<b> ISBN-13: </b> " + activeBook.getISBN_13() + "</p></html>" );
        setBookCover( activeBook.getThumbnail() );

        descriptionLabel.setText( "<html><p style=\"width: 300px;text-align: left;\">" + activeBook.getDescription() + "</p></html>" );
    }

    private void setBookCover( String urlString ) {

        BufferedImage image = null;
        try {

            URL url = new URL( urlString );
            image = ImageIO.read( url );
            Image newImage = image.getScaledInstance( 150, 220, Image.SCALE_DEFAULT);
            coverLabel.setIcon( new ImageIcon(newImage) );

        } catch (IOException e) {
            coverLabel.setIcon( defaultCover );
            e.printStackTrace();
        }
    }

    private void buildButtons() {

        FontIcon previousIcon = FontIcon.of( MaterialDesignA.ARROW_LEFT_BOLD );
        previousIcon.setIconSize( 25 );
        previousIcon.setIconColor( new Color( 0x06AF00 ) );
        previousButton.setIcon( previousIcon );

        FontIcon homeIcon = FontIcon.of( MaterialDesignH.HOME );
        homeIcon.setIconSize( 25 );
        homeIcon.setIconColor( Color.BLACK );
        homeButton.setIcon( homeIcon );

        FontIcon nextIcon = FontIcon.of( MaterialDesignA.ARROW_RIGHT_BOLD );
        nextIcon.setIconSize( 25 );
        nextIcon.setIconColor( new Color(0x06AF00) );
        nextButtom.setIcon( nextIcon );

    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
