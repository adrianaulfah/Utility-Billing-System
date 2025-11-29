import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HomepagePanel extends JPanel {
public HomepagePanel() {
    setOpaque(false);
    setLayout(new GridLayout(3, 2, 15, 15));
    setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    // Create buttons
    String[] buttons = {
            "Pay Bill",
            "View Bill",
            "Profile",
            "Submit Feedback",
            "Logout"
    };

    for(String text : buttons) {
    JButton btn = BillingApplication.createButtonWithSound(text, new ButtonListener());
    add(btn);
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

    class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();
            
            if(cmd.equals("Pay Bill")) {
                BillingApplication.cards.show(BillingApplication.mainPanel, "bill"); 

            }
            else if(cmd.equals("Profile")) {
                ((ProfilePanel) BillingApplication.mainPanel.getComponent(4)).updateUserInfo();
                BillingApplication.cards.show(BillingApplication.mainPanel, "profile");
            
            
            }
            else if(cmd.equals("Logout")) {
                BillingApplication.cards.show(BillingApplication.mainPanel, "login");
            
            
            }
            
            
            else if(cmd.equals("View Bill")) {
                BillingApplication.cards.show(BillingApplication.mainPanel, "viewbill");
                JFrame frame = new JFrame("Utility Bill Receipt");
                frame.setSize(400, 350);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JTextArea area = new JTextArea();
                area.setEditable(false);
                area.setBackground(BillingApplication.PURPLE_FIELD);
                area.setForeground(Color.BLACK);

                area.setFont(new Font("Monospaced", Font.PLAIN, 12));

                area.setText(
        "            TENAGA NASIONAL BERHAD (TNB)\n" +
        "         ------------------------------------\n" +
        "Customer Name  : Ror Binti Grr\n" +
        "IC Number      : 900101-14-1234\n" +
        "Address        : somewhere kat Malaysia\n" +
        "\n" +
        "Account No     : 4567890123\n" +
        "Billing Period : 01/05/2025 - 31/05/2025\n" +
        "Meter No       : E1234567\n" +
        "Tariff         : Domestic\n" +
        "\n" +
        "Prev Reading   : 12450 kWh\n" +
        "Current Reading: 12620 kWh\n" +
        "Usage (kWh)    : 170 kWh\n" +
        "\n" +
        "--------------------------------------------\n" +
        "Usage Charges:\n" +
        "  First 200 kWh @ RM0.218      = RM 37.06\n" +
        "  ICPT Rebate                  = -RM 2.50\n" +
        "  SST (0%)                     = RM 0.00\n" +
        "--------------------------------------------\n" +
        "TOTAL AMOUNT DUE              = RM 34.56\n" +
        "Due Date                      : 15/06/2025\n" +
        "Status                        : Unpaid\n" +
        "--------------------------------------------\n" +
        "Please pay before the due date to avoid\n" +
        "disruption of service.\n"
                );


                JButton back = new JButton("Back");
                back.setBackground(BillingApplication.PURPLE_BUTTON);
                back.setForeground(Color.BLACK);
                back.addActionListener(ev -> frame.dispose());

                frame.add(new JScrollPane(area), BorderLayout.CENTER);
                frame.add(back, BorderLayout.SOUTH);
                frame.setVisible(true);
            }


            else if(cmd.equals("Notifications")) {
                BillingApplication.cards.show(BillingApplication.mainPanel, "notifications");

            }
            else if(cmd.equals("Submit Feedback")) {
                BillingApplication.cards.show(BillingApplication.mainPanel, "feedback");

                JFrame frame = new JFrame("Feedback");
                frame.setSize(350, 250);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JTextArea area = new JTextArea();
                area.setBackground(BillingApplication.PURPLE_FIELD);
                area.setForeground(Color.BLACK);

                JButton submit = new JButton("Submit");
                JButton back = new JButton("Back");

                submit.setBackground(BillingApplication.PURPLE_BUTTON);
                back.setBackground(BillingApplication.PURPLE_BUTTON);

                submit.addActionListener(ev -> {
                    JOptionPane.showMessageDialog(frame, "Feedback submitted!");
                     frame.dispose();
                });

                back.addActionListener(ev -> frame.dispose());

                JPanel panel = new JPanel(new BorderLayout());
                panel.setBackground(BillingApplication.PURPLE_FIELD);
                panel.add(new JScrollPane(area), BorderLayout.CENTER);

                JPanel buttonPanel = new JPanel();
                buttonPanel.setBackground(BillingApplication.PURPLE_FIELD);
                buttonPanel.add(submit);
                buttonPanel.add(back);

                panel.add(buttonPanel, BorderLayout.SOUTH);

                frame.add(panel);
                frame.setVisible(true);


    }
}
            
        }