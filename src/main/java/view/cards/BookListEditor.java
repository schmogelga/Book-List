package view.cards;

import core.data.BookList;
import core.service.BookManager;
import misc.util.ApplicationContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BookListEditor extends JFrame {

    public static final int MODE_CREATE = 0;
    public static final int MODE_UPDATE = 1;

    private JPanel rootPanel;
    private JTextField textFieldName;
    private JButton submitButton;
    private JTextArea textFieldDescription;
    private JLabel labelFeedback;
    private BookList bookList;
    private int mode;
    private final MyListsPanel myListsPanel;

    public BookListEditor( MyListsPanel myListsPanel ) throws HeadlessException {

        this.myListsPanel = myListsPanel;
        setSize(320, 240 );
        setLocationRelativeTo( null );
        add( rootPanel );


        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if( textFieldName.getText().isEmpty() ) {
                    labelFeedback.setText( "Name cannot be empty!" );
                } else {
                    labelFeedback.setText( "" );
                    performSubmit();
                }
            }
        });
    }

    private void performSubmit() {

        if( mode == BookListEditor.MODE_CREATE ) {
            bookList = BookManager.getInstance().createBookList(ApplicationContext.getInstance().getLoggedUser().getId(), textFieldName.getText(), textFieldDescription.getText() );
        } else if( mode == BookListEditor.MODE_UPDATE ) {

            bookList.setName( textFieldName.getText() );
            bookList.setDescription( ( textFieldDescription.getText() ) );
            BookManager.getInstance().updateBookList( bookList );
        }

        myListsPanel.refreshLists();
        myListsPanel.setActiveBookList( bookList );
        setVisible( false );
    }

    public void setBookList(BookList bookList) {
        this.bookList = bookList;
        textFieldName.setText( bookList.getName() );
        textFieldDescription.setText( bookList.getDescription() );
    }

    public void setMode( int mode ) {
        this.mode = mode;
    }

    public void resetInfo() {
        textFieldDescription.setText( "" );
        textFieldName.setText( "" );
    }
}
