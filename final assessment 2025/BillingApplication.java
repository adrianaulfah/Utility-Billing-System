import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class BillingApplication {
    public static user currentUser;
    public static JFrame window;
    public static JPanel mainPanel;
    public static CardLayout cards;

    public static void playClickSound() {
    try {
        File soundFile = new File("C:\\Users\\adriana\\Downloads\\VSC\\java\\final assessment 2025\\Click.wav");
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);
        clip.start();
    } catch (Exception e) {
        e.printStackTrace(); // Optional for debugging
    }
}

public static JButton createButtonWithSound(String text, ActionListener action) {
    JButton button = new JButton(text);
    button.setBackground(PURPLE_BUTTON);
    button.setForeground(Color.BLACK);
    button.addActionListener(e -> {
        playClickSound();    // play sound
        action.actionPerformed(e); // do the actual button action
    });
    return button;
}


    
    // List to store users
    public static ArrayList<user> users = new ArrayList<>();
    
    // Define color constants at class level
    public static final Color PURPLE_BUTTON = new Color(150, 120, 255);
    public static final Color PURPLE_FIELD = new Color(200, 180, 255);
    public static Image backgroundImage;

    public static MusicPlayer musicPlayer = new MusicPlayer();

    public static void main(String[] args) {

    musicPlayer.playBackgroundMusic("C:\\Users\\adriana\\Downloads\\VSC\\java\\final assessment 2025\\jazzlofi.wav");


    SwingUtilities.invokeLater(() -> new BillingApplication());

    // Launch GUI
    SwingUtilities.invokeLater(() -> {
        new BillingApplication();
    });

        try {
            backgroundImage = new ImageIcon("background.jpg").getImage();
        } catch (Exception e) {
            backgroundImage = null;
        } 

        // Setup window
        window = new JFrame("Utility Billing System");
        window.setSize(800, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Create card layout
        cards = new CardLayout();
        mainPanel = new JPanel(cards);
        mainPanel.setBackground(Color.YELLOW);
        
        // Create all panels
        LoginPanel loginPanel = new LoginPanel();
        RegisterPanel registerPanel = new RegisterPanel();
        HomepagePanel homepagePanel = new HomepagePanel();
        BillPanel billPanel = new BillPanel();
        ProfilePanel profilePanel = new ProfilePanel(BillingApplication.window, 
    () -> cards.show(mainPanel, "homepage"),
    () -> cards.show(mainPanel, "login"));
        
        // Add panels to main panel
        mainPanel.add(loginPanel, "login");
        mainPanel.add(registerPanel, "register");
        mainPanel.add(homepagePanel, "homepage");
        mainPanel.add(billPanel, "bill");
        mainPanel.add(profilePanel, "profile");
        
        // Show login first
        cards.show(mainPanel, "login");
        
        window.add(mainPanel);
        window.setVisible(true);
    } 
}  