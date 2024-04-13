import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdminLoginPage extends JFrame implements ActionListener{
    JButton loginButton, exitButton;
    JLabel usernameLabel, passwordLabel , signIn;
    JTextField usernameField;
    JPasswordField passwordField;
    JCheckBox showPasswordCheckbox;

    Connection connection;
    Statement statement;
    public AdminLoginPage() {
        // Frame
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/game.png"));
        setIconImage(icon.getImage());
        setTitle("Quiz App");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(70, 130, 180));
        setResizable(false);
        setLayout(new BorderLayout());


        setUndecorated(true);
        // Navigation Bar
        JPanel navBar = new JPanel();
        navBar.setBackground(new Color(70, 130, 180));
        navBar.setBorder(new EmptyBorder(5, 10, 5, 10));
        navBar.setLayout(new BorderLayout());

        JLabel logo = new JLabel("Admin Login ");
        logo.setForeground(new Color(245, 245, 245));
        logo.setFont(new Font("Fira Code", Font.ITALIC, 18));
        logo.setHorizontalAlignment(JLabel.CENTER);
        exitButton = new JButton("Back");
        exitButton.setBackground(new Color(236,234,230));

        exitButton.setForeground(new Color(70, 130, 180));

        exitButton.setFocusPainted(false);

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginPage().setVisible(true);
                dispose();
            }
        });

        navBar.add(logo, BorderLayout.CENTER);
        navBar.add( exitButton, BorderLayout.EAST);
        add(navBar, BorderLayout.NORTH);

        // Login Panel
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(new Color(70, 130, 180));

        Font formFont = new Font("Fira Code", Font.BOLD, 15);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(formFont);
        usernameField = new JTextField(10);
        usernameField.setFont(formFont);

        passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(formFont);
        passwordField = new JPasswordField(10);
        showPasswordCheckbox = new JCheckBox("Show Password");
        showPasswordCheckbox.setFont(formFont);
        showPasswordCheckbox.addActionListener(showPassword());

        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                char[] PasswordChar = passwordField.getPassword();
                String passwordString = new String(PasswordChar);

                if(passwordString.equals("Admin") && usernameField.getText().equals("Admin")){
                    new AdminPage();
                    dispose();
                }else{
                    JOptionPane.showMessageDialog(null, "Incorrect Password");
                }
            }
        });


        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        loginPanel.add(usernameLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        loginPanel.add(usernameLabel, gbc);


        gbc.gridx = 1;
        loginPanel.add(usernameField, gbc);
        gbc.gridy++;
        loginPanel.add(Box.createVerticalStrut(10), gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        loginPanel.add(Box.createVerticalStrut(10), gbc);

        loginPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        loginPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(Box.createVerticalStrut(20), gbc);

        loginPanel.add(showPasswordCheckbox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        loginPanel.add(Box.createVerticalStrut(20), gbc);

        loginPanel.add(loginButton, gbc);

        loginPanel.setBackground(Color.WHITE);
        loginPanel.setBorder(new EmptyBorder(8,8,8,8));




        // Add login panel to a wrapper panel
        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setBackground(new Color(70, 130, 180));

        GridBagConstraints gridBag = new GridBagConstraints();

        gridBag.gridy = 0;
        wrapperPanel.add(loginPanel, gridBag);



        // Add wrapper panel to the frame
        add(wrapperPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
    public ActionListener showPassword() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBox cb = (JCheckBox) e.getSource();
                passwordField.setEchoChar(cb.isSelected() ? 0 : '*');
            }
        };
    }
}