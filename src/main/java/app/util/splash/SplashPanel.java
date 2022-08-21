package app.util.splash;

import javax.swing.*;
import java.awt.*;

public class SplashPanel extends JPanel {

    public SplashPanel() {
        initComponents();
       
    }

    private void initComponents()
    {
        imageLabel = new JLabel();
        jPanel1 = new JPanel();
        progressBar = new JProgressBar();
        progressInfo = new JLabel();

        setBorder( BorderFactory.createLineBorder(new Color(0, 0, 0)));
        setLayout(new BorderLayout());

        imageLabel.setBackground(new Color(255, 255, 255));
        //imageLabel.setIcon(new ImageIcon( getClass().getResource( getImagePath() ) ) ) ); // NOI18N
        add(imageLabel, BorderLayout.CENTER);

        jPanel1.setLayout(new BorderLayout());
        jPanel1.add(progressBar, BorderLayout.PAGE_START);

        progressInfo.setFont( new Font( "JetBrains Mono Medium", 0, 14 ) ); // NOI18N
        progressInfo.setText(" Inicialização da aplicação...");
        jPanel1.add(progressInfo, BorderLayout.PAGE_END);

        add(jPanel1, BorderLayout.PAGE_END);
    }

    public void setImagePath( String imagePath )
    {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        imageLabel.setIcon(new ImageIcon( loader.getResource( imagePath ) ) ); // NOI18N
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public JLabel getProgressInfo()
    {
        return progressInfo;
    }

    private JLabel imageLabel;
    private JPanel jPanel1;
    private JProgressBar progressBar;
    private JLabel progressInfo;
}
