import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RegisterPanel extends JPanel {
    JTextField nameField = new JTextField(20);
    JTextField emailField = new JTextField(20);
    JPasswordField passwordField = new JPasswordField(20);
    JTextField phoneField = new JTextField(20);
    JTextField icField = new JTextField(20);
    JTextField addressField = new JTextField(20);
    
    public RegisterPanel() {
        setOpaque(false);
        setLayout(new GridBagLayout());
        setBackground(Color.YELLOW);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Title
        JLabel title = new JLabel("Create New Account", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(title, gbc);

        // Apply your color scheme to all components
        Color PURPLE_FIELD = BillingApplication.PURPLE_FIELD;
        Color PURPLE_BUTTON = BillingApplication.PURPLE_BUTTON;
        
        // Style all text fields
        Component[] fields = {nameField, emailField, passwordField, phoneField, icField, addressField};
        for (Component field : fields) {
            field.setBackground(PURPLE_FIELD);
            field.setForeground(Color.BLACK);
            ((JTextField)field).setPreferredSize(new Dimension(200, 25));
        }

        // Create buttons with your style
        JButton registerButton = BillingApplication.createButtonWithSound("Register", e -> registerUser());
        JButton backButton = createButton("Back to Login", PURPLE_BUTTON, 
            e -> BillingApplication.cards.show(BillingApplication.mainPanel, "login"));

        // Form layout
        String[] labels = {"Name:", "Email:", "Password:", "Phone Number:", "IC Number:", "Address:"};
        Component[] components = {nameField, emailField, passwordField, phoneField, icField, addressField};
        
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i+1; gbc.gridwidth = 1;
            add(new JLabel(labels[i]), gbc);
            
            gbc.gridx = 1;
            add(components[i], gbc);
        }

        // Register button
        gbc.gridy = labels.length+1; gbc.gridx = 0; gbc.gridwidth = 2;
        add(registerButton, gbc);
        
        // Back button
        gbc.gridy++;
        add(backButton, gbc);
    }
    
    private JButton createButton(String text, Color bg, ActionListener action) {
        JButton button = new JButton(text);
        button.setBackground(bg);
        button.setForeground(Color.BLACK);
        button.addActionListener(action);
        return button;
    }
    
    void registerUser() {
        // Get all field values
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String phone = phoneField.getText().trim();
        String ic = icField.getText().trim();
        String address = addressField.getText().trim();
        
        // Validate all fields
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || 
            phone.isEmpty() || ic.isEmpty() || address.isEmpty()) {
            showError("Please fill in all fields!");
            return;
        }
        
        // Check for existing email
        for (user user : BillingApplication.users) {
            if (user.email.equalsIgnoreCase(email)) {
                showError("Email already registered!");
                return;
            }
        }
        
        // Create and store new user
        BillingApplication.users.add(new user(name, email, password, phone, ic, address));
        
        // Show success and return to login
        JOptionPane.showMessageDialog(this, 
            "Registration successful!", 
            "Success", 
            JOptionPane.INFORMATION_MESSAGE);
        
        BillingApplication.cards.show(BillingApplication.mainPanel, "login");
        clearFields();
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void clearFields() {
        nameField.setText("");
        emailField.setText("");
        passwordField.setText("");
        phoneField.setText("");
        icField.setText("");
        addressField.setText("");
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (BillingApplication.backgroundImage != null) {
            g.drawImage(BillingApplication.backgroundImage, 
                0, 0, getWidth(), getHeight(), this);
        }
    }
}
