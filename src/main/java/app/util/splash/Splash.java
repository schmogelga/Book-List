package app.util.splash;

import static java.lang.Thread.sleep;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;

public class Splash {

    private JWindow frame;
    private JProgressBar progressBar;
    private JLabel progressInfo;
    private int counter;
    private int delay;
    private String imagePath;

    public Splash( String imagePath ) {
        SplashPanel splashPanel = new SplashPanel();
        splashPanel.setImagePath(imagePath);

        this.frame = new JWindow();
        this.frame.add(splashPanel);
        this.frame.pack();
        this.frame.setLocationRelativeTo(null);

        this.imagePath = imagePath;
        this.progressBar = splashPanel.getProgressBar();
        this.progressInfo = splashPanel.getProgressInfo();
        this.counter = 0;
        this.delay = 1000;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void showSplash() {
        frame.setVisible(true);
        try {
            sleep(delay);
        } catch (InterruptedException ex) {
            Logger.getLogger(Splash.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void dispose() {
        frame.dispose();
    }

    public void setSteps(int number) {
        progressBar.setMaximum(number);
    }

    public void next() {
        next("Inicializando a aplicação...");
    }

    public void next(String text) {
        counter++;
        progressBar.setValue(counter);
        progressInfo.setText(" " + text);
        try {
            sleep(delay);
        } catch (InterruptedException ex) {
            Logger.getLogger(Splash.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
