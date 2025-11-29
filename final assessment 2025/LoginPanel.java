import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    JTextField emailField = new JTextField(20);
    JPasswordField passwordField = new JPasswordField(20);

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (BillingApplication.backgroundImage != null) {
            g.drawImage(BillingApplication.backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
    
    public LoginPanel() {
        setOpaque(false);
        setLayout(new GridBagLayout());
        setBackground(Color.YELLOW);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Title
        JLabel title = new JLabel("Login Account", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(title, gbc);

        emailField.setBackground(BillingApplication.PURPLE_FIELD);
        emailField.setForeground(Color.BLACK);
        passwordField.setBackground(BillingApplication.PURPLE_FIELD);
        passwordField.setForeground(Color.BLACK);
        
        // Set warna untuk button
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(BillingApplication.PURPLE_BUTTON);
        loginButton.setForeground(Color.BLACK);
        
        JButton registerButton = new JButton("Register New Account");
        registerButton.setBackground(BillingApplication.PURPLE_BUTTON);
        registerButton.setForeground(Color.BLACK);
        
        // Email
        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0;
        add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1;
        emailField.setPreferredSize(new Dimension(200, 25));
        add(emailField, gbc);
        
        // Password
        gbc.gridy = 2; gbc.gridx = 0;
        add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1;
        passwordField.setPreferredSize(new Dimension(200, 25));
        add(passwordField, gbc);
        
        // Login button
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 2;
        loginButton.addActionListener(e -> checkLogin());
        add(loginButton, gbc);
        
        // Register button
        gbc.gridy = 4;
        registerButton.addActionListener(e -> BillingApplication.cards.show(BillingApplication.mainPanel, "register"));
        add(registerButton, gbc);
    }
    
    void checkLogin() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        for(user user : BillingApplication.users) {
            if(user.email.equals(email) && user.password.equals(password)) {
                BillingApplication.currentUser = user; // Set the current user
                ((ProfilePanel) BillingApplication.mainPanel.getComponent(4)).updateUserInfo();
                BillingApplication.cards.show(BillingApplication.mainPanel, "homepage");
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Wrong email or password!");
    }
}
