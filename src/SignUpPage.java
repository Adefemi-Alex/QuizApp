import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SignUpPage extends JFrame implements ActionListener{
    JLabel usernameLabel, passwordLabel, fullNameLabel;
    JTextField fullNameField, usernameField;
    JPasswordField passwordField;
    JButton Submit, adminButton, exitButton;
    JCheckBox showPasswordCheckbox;
    Connection connection;
    Statement statement;
    public SignUpPage() {
        // Set up the window
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/game.png"));
        setIconImage(icon.getImage());
        // Frame
        setTitle("Quiz App");
        setSize(500, 600);
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

        JLabel logo = new JLabel("Guessing Game");
        logo.setForeground(new Color(245, 245, 245));
        logo.setFont(new Font("Fira Code", Font.ITALIC, 18));
        logo.setHorizontalAlignment(JLabel.CENTER);
        adminButton = new JButton("Admin");
        exitButton = new JButton("Back");

        adminButton.setBackground(new Color(236,234,230));
        exitButton.setBackground(new Color(236,234,230));
        adminButton.setForeground(new Color(70, 130, 180));
        exitButton.setForeground(new Color(70, 130, 180));
        adminButton.setFocusPainted(false);
        exitButton.setFocusPainted(false);

        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminLoginPage();
                dispose();
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginPage().setVisible(true);
                dispose();
            }
        });

        navBar.add(adminButton, BorderLayout.WEST);
        navBar.add(logo, BorderLayout.CENTER);
        navBar.add( exitButton, BorderLayout.EAST);
        add(navBar, BorderLayout.NORTH);

        // Login Panel
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(new Color(173, 216, 230));

        Font formFont = new Font("Fira Code", Font.TRUETYPE_FONT, 15);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 5, 7, 5);


        JLabel title = new JLabel("SIGN UP");
        title.setFont(new Font("Cursive", Font.TYPE1_FONT, 19));
        title.setForeground(new Color(70, 130, 180));
        title.setHorizontalAlignment(JLabel.CENTER);

        fullNameLabel = new JLabel("FULLNAME");
        fullNameLabel.setFont(formFont);
        fullNameField = new JTextField(10);
        fullNameField.setFont(formFont);
        usernameLabel = new JLabel("USERNAME");
        usernameLabel.setFont(formFont);
        usernameField = new JTextField(10);
        usernameField.setFont(formFont);

        passwordLabel = new JLabel("PASSWORD");
        passwordLabel.setFont(formFont);
        passwordField = new JPasswordField(10);
        passwordField.setFont(formFont);
        showPasswordCheckbox = new JCheckBox("Show Password");
        showPasswordCheckbox.setFont(new Font("Cursive", Font.TYPE1_FONT, 15));
        showPasswordCheckbox.addActionListener(showPassword());

        Submit = new JButton("Sign Up");
        Submit.setBackground(new Color(70, 130, 180));
        Submit.setPreferredSize(new Dimension(80,30));
        Submit.setForeground(Color.WHITE);
        Submit.setFocusPainted(false);
        Submit.addActionListener(signUp());

        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(fullNameLabel,gbc);

        gbc.gridx = 1;
        loginPanel.add(Box.createVerticalStrut(10), gbc);
        loginPanel.add(fullNameField,gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.WEST;
        loginPanel.add(usernameLabel, gbc);


        gbc.gridx = 1;
        loginPanel.add(usernameField, gbc);

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
        gbc.gridy=0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(Box.createVerticalStrut(20), gbc);
        loginPanel.add(title, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        loginPanel.add(Box.createVerticalStrut(30), gbc);

        loginPanel.add(Submit, gbc);

        loginPanel.setBackground(Color.WHITE);
//        loginPanel.setBorder(new EmptyBorder(5,8,2,8));

        loginPanel.setPreferredSize(new Dimension(300, 350));

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
    public ActionListener showPassword() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBox cb = (JCheckBox) e.getSource();
                passwordField.setEchoChar(cb.isSelected() ? 0 : '*');
            }
        };
    }
    public ActionListener exit() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginPage();
                dispose();
            }
        };
    }
    public ActionListener signUp() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Button Clicked");

                if (!fullNameField.getText().isEmpty() && !usernameField.getText().isEmpty() && passwordField.getPassword().length > 0) {

                    System.out.println("All fields are filled");

                    char[] passwordChars = passwordField.getPassword();
                    String passwordString = new String(passwordChars);

                    if (fullNameField.getText().length() >= 3 && usernameField.getText().length() >= 3 && passwordString.length() >= 3) {

                        LoadSql();
                        System.out.println("loaded");


                        try {
                            String retrieve = "SELECT * FROM Scoreboard WHERE Username = '"+ usernameField.getText() + "'";
                            ResultSet resultSet = statement.executeQuery(retrieve);

                            //To Process the result set and check if account already exists;
                            if (!resultSet.next()) {
                                try {
                                    String query = "INSERT INTO Scoreboard(Fullname, Username,Password) VALUES('" +
                                            fullNameField.getText() + "',  '" + usernameField.getText() + "', '" + passwordString + "')";
                                    int rowsAffected = statement.executeUpdate(query);

                                    if (rowsAffected > 0) {
                                        // Successful insert
                                        JOptionPane.showMessageDialog(null, "Successful Signup");
                                        new LoginPage();
                                        dispose();
                                    } else {
                                        // Failed insert
                                        JOptionPane.showMessageDialog(null, "Failed to sign up. Please try again.");
                                    }
                                } catch (SQLException e1) {
                                    e1.printStackTrace();
                                }


                            }else{
                                JOptionPane.showMessageDialog(null,"Account already Exists");
                            }


                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "Fields must not contain less than two characters");

                    }
                }else{
                    JOptionPane.showMessageDialog(null, "All fields must be filled");
                }

            }
        };
    }

    public void LoadSql() {
        String url = "jdbc:mysql://localhost:3306/Quiz_App";
        String user = "root";
        String password = "";
        try {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                System.err.println("Error finding Class");
            }
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();

        } catch (SQLException e1) {
            e1.printStackTrace();
            System.out.println("Xampp isn't open");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}