package view.cards;

import core.Filter.*;
import core.data.Author;
import core.data.Book;
import core.data.Category;
import core.service.BookManager;
import misc.util.ApplicationContext;
import org.kordamp.ikonli.materialdesign2.MaterialDesignA;
import org.kordamp.ikonli.materialdesign2.MaterialDesignH;
import org.kordamp.ikonli.materialdesign2.MaterialDesignR;
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

public class MyBooksPanel {

    private JPanel rootPanel;
    private JPanel myBooksPanel;
    private JPanel leftPanel;
    private JPanel RightPanel;
    private JTextField textFieldSearch;
    private JButton nextButtom;
    private JList jListBooks;
    private JButton homeButton;
    private JButton previousButton;
    private JComboBox comboBoxSearch;
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
    private JButton refreshButton;
    private JLabel listLabel;
    private JList listsJList;
    private List<JButton> buttons = new ArrayList();
    private MainFrame mainFrame;
    private List<Book> books;
    private Book activeBook;
    private int startIndex;
    private int endIndex;
    private JLabel coverLabel;
    private ImageIcon defaultCover;

    private final String[] fields = {
            "intitle",
            "inauthor",
            "inpublisher",
            "subject",
            "isbn"
    //        "list"
    };

    private List<Book> userBooks;

    public MyBooksPanel(MainFrame mainFrame ) {
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
                    startIndex = 0;
                    endIndex = 10;
                    performSearch();
                }
            }
        });
        nextButtom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if( userBooks.size() > endIndex + 10 ) {
                    startIndex += 10;
                    endIndex += 10;
                } else {
                    startIndex = userBooks.size() - userBooks.size() % 10;
                    endIndex = userBooks.size();
                }

                performSearch();
            }
        });
        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if( startIndex - 10 < 0 ){
                    startIndex = 0;
                    endIndex = 10;
                } else {
                    startIndex -= 10;
                    endIndex = startIndex + 10;
                }

                performSearch();
            }
        });
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startIndex = 0;
                endIndex = 10;
                performSearch();
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

        detaildButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setActiveButton( detaildButton );
                descriptionCard.setVisible( false );
                detailCard.setVisible( true );
            }
        });

        descriptionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setActiveButton( descriptionButton );
                detailCard.setVisible( false );
                descriptionCard.setVisible( true );
            }
        });
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textFieldSearch.setText( "" );
                startIndex = 0;
                endIndex = 10;
                performSearch();
            }
        });
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

        BookFilter filter = null;

        if( !textFieldSearch.getText().isEmpty() )
        {
            switch( comboBoxSearch.getSelectedItem().toString() ){
                case "Title":
                    filter = new TitleFilter( textFieldSearch.getText() );
                    break;
                case "Publisher":
                    filter = new PublisherFilter( textFieldSearch.getText() );
                    break;
                case "Author":
                    filter = new AuthorFilter( textFieldSearch.getText() );
                    break;
                case "Category":
                    filter = new CategoryFilter( textFieldSearch.getText() );
                    break;
                case "ISBN":
                    filter = new ISBNFilter( textFieldSearch.getText() );
                    break;
                //case "List":
                //    filter = new ListFilter( textFieldSearch.getText(), ApplicationContext.getInstance().getLoggedUser() );
                //    break;
            }

            getBooks();
            getBooks( filter );
            jListBooks.setListData( userBooks.subList( startIndex, endIndex ).toArray() );

        } else {
            getBooks();
            jListBooks.setListData( userBooks.subList( startIndex, endIndex < 0 ? 0 : endIndex ).toArray() );
        }
    }

    public void initComp() {

        buttons.add( descriptionButton );
        buttons.add( detaildButton );

        descriptionJScrollPanel.setBorder( BorderFactory.createEmptyBorder() );
        descriptionJScrollPanel.getViewport().setBackground(Color.WHITE);

        getBooks();
        jListBooks.setBorder( BorderFactory.createEmptyBorder() );
        jListBooks.setFixedCellHeight( 30 );
        jListBooks.setFixedCellWidth( 250 );
        jListBooks.setListData( userBooks.subList( startIndex, endIndex ).toArray() );

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
       // comboBoxSearch.addItem( "List" );

        textFieldSearch.setBorder( BorderFactory.createMatteBorder( 0,0, 1, 0, Color.gray ) );
        startIndex = 0;
        buildButtons();
        rootPanel.setVisible( true );
    }

    private void getBooks() {
        getBooks( null );
    }

    private void getBooks( BookFilter filter ) {

        if( filter == null ) {
            userBooks = BookManager.getInstance().getBooks( ApplicationContext.getInstance().getLoggedUser() );
        } else {
            userBooks = BookManager.getInstance().getBooks( userBooks, filter );
        }

        if( endIndex > userBooks.size() ) {
            endIndex = userBooks.size();
        }
    }

    private void setActiveBook() {

        titleLabel.setText( "<html><p style=\"width: 230px;text-align: center;\">" + activeBook.getTitle() + "</p></html>" );
        publisherLabel.setText( "<html><p style=\"width: 260px;text-align: left;\">" + "<b> Publisher: </b> " + BookManager.getInstance().getPublishers( activeBook ) + "</p></html>" );
        publishDateLabel.setText( "<html><p style=\"width: 260px;text-align: left;\">" + "<b> Publish Date: </b> " + activeBook.getYear() + "</p></html>" );
        authorsLabel.setText( "<html><p style=\"width: 260px;text-align: left;\">" + "<b> Authors: </b> " + BookManager.getInstance().getAuthors( activeBook ) + "</p></html>" );
        categoriesLabel.setText( "<html><p style=\"width: 260px;text-align: left;\">" + "<b> Categories: </b> " + BookManager.getInstance().getCategories( activeBook ) + "</p></html>" );
        ISBN10Label.setText( "<html><p style=\"width: 260px;text-align: left;\">" + "<b> ISBN-10: </b> " + activeBook.getISBN_10() + "</p></html>" );
        ISBN13Label.setText( "<html><p style=\"width: 260px;text-align: left;\">" + "<b> ISBN-13: </b> " + activeBook.getISBN_13() + "</p></html>" );
        listLabel.setText( "<html><p style=\"width: 260px;text-align: left;\">" + "<b> List: </b> " + BookManager.getInstance().getLists( activeBook, ApplicationContext.getInstance().getLoggedUser() ) + "</p></html>" );
        descriptionLabel.setText( "<html><p style=\"width: 300px;text-align: left;\">" + activeBook.getDescription() + "</p></html>" );
        setBookCover( activeBook.getThumbnail() );
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

        FontIcon refreshIcon = FontIcon.of( MaterialDesignR.REFRESH );
        refreshIcon.setIconSize( 25 );
        refreshIcon.setIconColor( new Color( 0x06AF00 ) );
        refreshButton.setIcon( refreshIcon );

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
