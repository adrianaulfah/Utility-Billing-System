import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BillPanel extends JPanel {
    // Constants with more descriptive names
    private static final double ELECTRICITY_RATE = 0.218; // RM per kWh
    private static final double BASE_CHARGE = 10.00; // RM
    private static final Color ACCENT_COLOR = new Color(255, 20, 147); // Bright pink color instead of steel blue
    
    // UI Components
    private JTextField energyUsageField;
    private JLabel billTotalLabel;
    private JComboBox<PaymentOption> paymentMethodDropdown;
    private JTable paymentHistoryTable;
    
    public BillPanel() {
        configurePanel();
        initComponents();
    }
    
    private void configurePanel() {
        setOpaque(true);
        setBackground(new Color(255, 192, 203)); // Medium pink background
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
    }
    
    private void initComponents() {
        // Set UIManager properties for pink tabs BEFORE creating the tabbed pane
        UIManager.put("TabbedPane.selected", new Color(255, 182, 193)); // Light pink for selected tab
        UIManager.put("TabbedPane.background", new Color(255, 192, 203)); // Medium pink for tab background
        UIManager.put("TabbedPane.unselectedBackground", new Color(255, 182, 193)); // Pink for unselected tabs
        UIManager.put("TabbedPane.tabAreaBackground", new Color(255, 192, 203)); // Pink for tab area
        
        // Create tabbed interface with custom styling
        JTabbedPane mainTabs = new JTabbedPane(JTabbedPane.TOP);
        mainTabs.setOpaque(true);
        mainTabs.setBackground(new Color(255, 192, 203)); // Medium pink background
        mainTabs.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Add tabs with icons (optional)
        mainTabs.addTab("Calculate Bill", createCalculatorTab());
        mainTabs.addTab("Payment History", createHistoryTab());
        mainTabs.addTab("Make Payment", createPaymentTab());
        
        add(mainTabs, BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);
    }
    
    // ==================== Tab Components ====================
    
    private JPanel createCalculatorTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(true);
        panel.setBackground(new Color(255, 192, 203)); // Medium pink background
        
        // Header with custom styling
        panel.add(createSectionHeader("Bill Calculator"), BorderLayout.NORTH);
        
        // Calculator form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(true);
        formPanel.setBackground(new Color(255, 192, 203)); // Medium pink background
        formPanel.setBorder(createSectionBorder("Enter Usage Details"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.LINE_START;
        
        // Energy usage input
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Energy Usage (kWh):"), gbc);
        
        gbc.gridx = 1;
        energyUsageField = new JTextField(15);
        energyUsageField.setToolTipText("Enter your monthly electricity consumption");
        formPanel.add(energyUsageField, gbc);
        
        // Rate display
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Current Rate:"), gbc);
        
        gbc.gridx = 1;
        formPanel.add(createInfoLabel(String.format("RM %.3f per kWh", ELECTRICITY_RATE)), gbc);
        
        // Base charge
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Base Charge:"), gbc);
        
        gbc.gridx = 1;
        formPanel.add(createInfoLabel(String.format("RM %.2f", BASE_CHARGE)), gbc);
        
        // Total calculation
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Estimated Total:"), gbc);
        
        gbc.gridx = 1;
        billTotalLabel = createInfoLabel("RM 0.00");
        formPanel.add(billTotalLabel, gbc);
        
        // Calculate button
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        formPanel.add(createActionButton("Calculate", this::calculateBillAction), gbc);
        
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(createPaymentOptionsPanel(), BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createHistoryTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(true);
        panel.setBackground(new Color(255, 192, 203)); // Medium pink background
        
        panel.add(createSectionHeader("Payment History"), BorderLayout.NORTH);
        
        // Table with sample data
        String[] columns = {"Date", "Amount", "Method", "Status"};
        Object[][] data = {
            {"2023-10-15", 120.50, "Online Banking", "Paid"},
            {"2023-09-15", 115.75, "E-wallet", "Paid"},
            {"2023-08-15", 105.20, "Credit Card", "Paid"},
            {"2023-07-15", 98.30, "Store", "Paid"}
        };
        
        paymentHistoryTable = new JTable(data, columns);
        customizeTableAppearance();
        
        JScrollPane scrollPane = new JScrollPane(paymentHistoryTable);
        scrollPane.setOpaque(true);
        scrollPane.setBackground(new Color(255, 192, 203)); // Medium pink background
        scrollPane.getViewport().setOpaque(true);
        scrollPane.getViewport().setBackground(new Color(255, 192, 203)); // Medium pink background
        scrollPane.setBorder(createSectionBorder("Recent Transactions"));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Add export button
        JButton exportBtn = createActionButton("Export History", e -> exportHistory());
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(true);
        bottomPanel.setBackground(new Color(255, 192, 203)); // Medium pink background
        bottomPanel.add(exportBtn);
        
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createPaymentTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(true);
        panel.setBackground(new Color(255, 192, 203)); // Medium pink background
        
        panel.add(createSectionHeader("Make Payment"), BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        formPanel.setOpaque(true);
        formPanel.setBackground(new Color(255, 192, 203)); // Medium pink background
        formPanel.setBorder(createSectionBorder("Payment Details"));
        
        // Current bill display
        formPanel.add(createInfoLabel("Amount Due: RM 120.50", JLabel.CENTER));
        
        // Payment method selection
        formPanel.add(new JLabel("Select Payment Method:"));
        paymentMethodDropdown = new JComboBox<>(PaymentOption.values());
        paymentMethodDropdown.setRenderer(new PaymentOptionRenderer());
        formPanel.add(paymentMethodDropdown);
        
        // Payment button with confirmation
        JButton payButton = BillingApplication.createButtonWithSound("Confirm Payment", e -> processPayment());
        formPanel.add(payButton);
        
        panel.add(formPanel, BorderLayout.CENTER);
        return panel;
    }
    
    // ==================== Helper Components ====================
    
    private JPanel createPaymentOptionsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setOpaque(true);
        panel.setBackground(new Color(255, 192, 203)); // Medium pink background
        panel.setBorder(createSectionBorder("Available Payment Methods"));
        
        for (PaymentOption option : PaymentOption.values()) {
            JButton optionBtn = new JButton(option.toString());
            optionBtn.setPreferredSize(new Dimension(120, 40));
            optionBtn.addActionListener(e -> showPaymentInstructions(option));
            stylePaymentOptionButton(optionBtn);
            panel.add(optionBtn);
        }
        
        return panel;
    }
    
    private JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(true);
        footer.setBackground(new Color(255, 192, 203)); // Medium pink background
        
        // Back button with black font
        JButton backButton = BillingApplication.createButtonWithSound("Back to Home", 
            e -> BillingApplication.cards.show(BillingApplication.mainPanel, "homepage"));
        styleFooterButton(backButton);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        buttonPanel.setOpaque(true);
        buttonPanel.setBackground(new Color(255, 192, 203)); // Medium pink background
        buttonPanel.add(backButton);
        
        footer.add(buttonPanel, BorderLayout.EAST);
        return footer;
    }
    
    // ==================== Custom Styling Methods ====================
    
    private JLabel createSectionHeader(String text) {
        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));
        return label;
    }
    
    private Border createSectionBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR, 1),
            title
        );
        border.setTitleFont(new Font("Segoe UI", Font.ITALIC, 12));
        border.setTitleColor(ACCENT_COLOR);
        return border;
    }
    
    private JLabel createInfoLabel(String text) {
        return createInfoLabel(text, JLabel.LEADING);
    }
    
    private JLabel createInfoLabel(String text, int alignment) {
        JLabel label = new JLabel(text, alignment);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return label;
    }
    
    private JButton createActionButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(ACCENT_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        button.addActionListener(action);
        return button;
    }
    
    private void stylePaymentOptionButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        button.setBackground(new Color(255, 182, 193)); // Light pink background
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR, 1), // Pink border
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }
    
    private void styleFooterButton(AbstractButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setBackground(null);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.setForeground(Color.BLACK); // Made the "Back to Home" button text black
    }
    
    private void customizeTableAppearance() {
        paymentHistoryTable.setFillsViewportHeight(true);
        paymentHistoryTable.setRowHeight(25);
        paymentHistoryTable.setShowGrid(false);
        
        // Custom renderer for status column
        paymentHistoryTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
                
                if (column == 3) { // Status column
                    c.setForeground("Paid".equals(value) ? 
                        new Color(0, 100, 0) : Color.RED);
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                }
                
                // Alternate row coloring - changed to pink tones
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? 
                        new Color(255, 240, 245) : Color.WHITE); // Light pink instead of light blue
                }
                
                return c;
            }
        });
    }
    
    // ==================== Business Logic ====================
    
    private void calculateBillAction(ActionEvent e) {
        String input = energyUsageField.getText().trim();
        
        if (input.isEmpty()) {
            showErrorDialog("Please enter your energy usage");
            return;
        }
        
        try {
            double usage = Double.parseDouble(input);
            if (usage < 0) {
                showErrorDialog("Usage cannot be negative");
                return;
            }
            
            double total = calculateElectricityBill(usage);
            updateBillDisplay(total);
        } catch (NumberFormatException ex) {
            showErrorDialog("Invalid number format");
        }
    }
    
    private double calculateElectricityBill(double kWh) {
        return kWh * ELECTRICITY_RATE + BASE_CHARGE;
    }
    
    private void updateBillDisplay(double amount) {
        billTotalLabel.setText(String.format("RM %.2f", amount));
    }
    
    private void processPayment() {
        PaymentOption selected = (PaymentOption) paymentMethodDropdown.getSelectedItem();
        
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Pay RM 120.50 using " + selected + "?",
            "Confirm Payment",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            recordPayment(120.50, selected);
            showMessageDialog("Payment Successful!", "Success");
            BillingApplication.cards.show(BillingApplication.mainPanel, "homepage");
        }
    }
    
    private void recordPayment(double amount, PaymentOption method) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(new Date());
        
        DefaultTableModel model = (DefaultTableModel) paymentHistoryTable.getModel();
        model.addRow(new Object[]{date, amount, method, "Paid"});
    }
    
    private void exportHistory() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Payment History");
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            // In a real app, implement actual export logic
            showMessageDialog(
                "Export functionality would save to:\n" + 
                fileChooser.getSelectedFile().getPath(),
                "Export Started"
            );
        }
    }
    
    private void showPaymentInstructions(PaymentOption option) {
        String message = switch (option) {
            case E_WALLET -> "Scan QR code with your e-wallet app";
            case ONLINE_BANKING -> "Log in to your online banking";
            case CREDIT_CARD -> "Enter card details securely";
            case STORE -> "Visit any Speedmart/ 9-Eleven/ KK-Mart outlet to make payment";
        };
        
        showMessageDialog(message, option + " Instructions");
    }
    
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(
            this, message, "Error", JOptionPane.ERROR_MESSAGE
        );
    }
    
    private void showMessageDialog(String message, String title) {
        JOptionPane.showMessageDialog(
            this, message, title, JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    // ==================== Enums and Custom Classes ====================
    
    private enum PaymentOption {
        E_WALLET("E-Wallet (TnG, Shopee)"),
        ONLINE_BANKING("Online Banking"),
        CREDIT_CARD("Credit/Debit Card"),
        STORE("Store");
        
        private final String displayName;
        
        PaymentOption(String displayName) {
            this.displayName = displayName;
        }
        
        @Override
        public String toString() {
            return displayName;
        }
    }
    
    private static class PaymentOptionRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            if (value instanceof PaymentOption option) {
                setText(option.toString());
                setIcon(getOptionIcon(option));
            }
            
            return this;
        }
        
        private Icon getOptionIcon(PaymentOption option) {
            // In a real app, you'd use actual icons
            return new ImageIcon(); // Placeholder
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (BillingApplication.backgroundImage != null) {
            g.drawImage(BillingApplication.backgroundImage, 
                0, 0, getWidth(), getHeight(), this);
        }
    }

}
