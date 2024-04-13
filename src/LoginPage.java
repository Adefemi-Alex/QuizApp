import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.*;

public class LoginPage extends JFrame implements ActionListener, MouseListener {
    JButton adminButton, loginButton, exitButton;
    JLabel usernameLabel, passwordLabel , signIn, forgotPassword;
    JTextField usernameField;
    JPasswordField passwordField;
    JCheckBox showPasswordCheckbox;
    Connection connection;
    Statement statement;

    public LoginPage() {
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
        exitButton = new JButton("Exit");
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
                int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit? ",
                        "Play Again", JOptionPane.YES_NO_OPTION);
                if(option == JOptionPane.YES_OPTION){
                    dispose();
                }
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

        JLabel title = new JLabel("LOGIN");
        title.setFont(new Font("Cursive", Font.TYPE1_FONT, 19));
        title.setForeground(new Color(70, 130, 180));
        title.setHorizontalAlignment(JLabel.CENTER);
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

        // Forgot Password Link
        forgotPassword = new JLabel(" Forgot Password?");
        forgotPassword.setForeground(Color.BLACK);
        forgotPassword.setFont(new Font("Cursive", Font.TYPE1_FONT, 14));
        forgotPassword.addMouseListener(this);


        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setPreferredSize(new Dimension(80,30));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(loginListener());

        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        loginPanel.add(usernameField, gbc);

        gbc.gridy++;
        loginPanel.add(Box.createVerticalStrut(5), gbc); // Reduce vertical spacing

        gbc.gridx = 0;
        gbc.gridy++;
        loginPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        loginPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        loginPanel.add(forgotPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        loginPanel.add(showPasswordCheckbox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(title, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        loginPanel.add(loginButton, gbc);

        loginPanel.setBackground(Color.WHITE);
//        loginPanel.setBorder(new EmptyBorder(5,8,2,8));

        loginPanel.setPreferredSize(new Dimension(300, 350));
        JPanel signupSection = new JPanel();
        signIn = new JLabel("Sign Up");
        signIn.setForeground(new Color(245, 245, 245));
        signIn.setFont(new Font("Cursive", Font.TYPE1_FONT, 16));

        signIn.addMouseListener(this);
        JLabel info = new JLabel("Do not have an account?");
        info.setFont(new Font("Cursive", Font.TYPE1_FONT, 16));
        info.setForeground(Color.WHITE);
        signupSection.add(info);
        signupSection.add(signIn);
        signupSection.setBackground(new Color(70, 130, 180));


        // Add login panel to a wrapper panel
        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setBackground(new Color(70, 130, 180));

        GridBagConstraints gridBag = new GridBagConstraints();

        gridBag.gridy = 0;
        wrapperPanel.add(loginPanel, gridBag);
        gridBag.gridy = 1;
        wrapperPanel.add(signupSection, gridBag);

        // Add wrapper panel to the frame
        add(wrapperPanel, BorderLayout.CENTER);

        setVisible(true);

        createDatabaseAndTable();
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

    public ActionListener loginListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                // Your logic here
                System.out.println(usernameField.getText());
                char[] PasswordChar = passwordField.getPassword();
                String passwordString = new String(PasswordChar);
                System.out.println("SELECT * FROM Scoreboard WHERE Username = '"+ usernameField.getText() + "' AND Password = '" + passwordString+ "'");

                if (!usernameField.getText().isEmpty() && !passwordString.isEmpty()) {
                    LoadSql();
                    // TODO  ... I WANT TO USE THE SELECT FIRST ...TO FIND THE EXACT PASSWORD AND USERNAME;
                    try {
                        String query = "SELECT * FROM Scoreboard WHERE Username = '"+ usernameField.getText() + "' AND Password = '" + passwordString + "'";
                        ResultSet resultSet = statement.executeQuery(query);

                        //To process the result set
                        if (resultSet.next()) {
                            String Username = resultSet.getString("Username");
                            String Password = resultSet.getString("Password");
                            int Score = resultSet.getInt("Score");
                            new MainFrame(Username, Password, Score).setVisible(true);
                            dispose();

                        }else{
                            JOptionPane.showMessageDialog(null, "Incorrect Username or Password");
                        }


                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                }else{
                    JOptionPane.showMessageDialog(null, "Username or Password cannot be Empty");
                }
            }
        };
    }

    public void LoadSql() {
        String url = "jdbc:mysql://localhost:3306/quiz_App";
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
        }
    }

    private void createDatabaseAndTable() {
        String url = "jdbc:mysql://localhost:3306/";
        String user = "root";
        String password = "";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();

            // Create the database if it doesn't exist
            String createDatabaseQuery = "CREATE DATABASE IF NOT EXISTS quiz_app";
            statement.executeUpdate(createDatabaseQuery);

            // Use the database
            statement.executeUpdate("USE quiz_app");

            // Create the table if it doesn't exist
            String createTableQuery = "CREATE TABLE IF NOT EXISTS Scoreboard ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "Fullname VARCHAR(255) NOT NULL,"
                    + "Username VARCHAR(50) NOT NULL,"
                    + "Score INT DEFAULT 0,"
                    + "Password VARCHAR(50) NOT NULL)";
            statement.executeUpdate(createTableQuery);

            statement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error creating database and table: " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == signIn) {
            new SignUpPage();
            dispose();
        } else if (e.getSource() == forgotPassword) {
//            JOptionPane.showMessageDialog(this, "Forgot Password clicked!");
            new UsernameCheckPage();
            dispose();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getSource() == signIn) {
            signIn.setForeground(Color.black);
        } else if (e.getSource() == forgotPassword) {
            forgotPassword.setForeground(new Color(70, 130, 180));
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource() == signIn) {
            signIn.setForeground(new Color(245, 245, 245));
        } else if (e.getSource() == forgotPassword) {
            forgotPassword.setForeground(Color.BLACK);
        }
    }

    public static void main(String[] args) {
        new LoginPage();
    }
}