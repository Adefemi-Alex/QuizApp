import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class UsernameCheckPage extends JFrame implements ActionListener {
    JTextField usernameField;
    JButton checkButton, exitButton;;
    Connection connection;
    Statement statement;

    public UsernameCheckPage() {
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/game.png"));
        setIconImage(icon.getImage());
        setTitle("Check Username");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(70, 130, 180));
        setLayout(new BorderLayout());
        setUndecorated(true);

        JPanel navBar = new JPanel();
        navBar.setBackground(new Color(70, 130, 180));
        navBar.setBorder(new EmptyBorder(5, 10, 5, 10));
        navBar.setLayout(new BorderLayout());
        // Create the panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(70, 130, 180));

        // Username label and text field
        JLabel usernameLabel = new JLabel("Enter Username:");
        usernameLabel.setForeground(new Color(244,244,244));
        usernameField = new JTextField();
        panel.add(usernameLabel);
        panel.add(usernameField);

        // Check button
        checkButton = new JButton("Check Username");
        checkButton.addActionListener(this);
        checkButton.setBackground(new Color(236,234,230));
        checkButton.setForeground(new Color(70, 130, 180));
        panel.add(checkButton);

        JLabel logo = new JLabel(" username verification");
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
        // Add panel to the frame
        add(panel, BorderLayout.CENTER);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == checkButton) {
            String username = usernameField.getText().trim();
            if (!username.isEmpty()) {
                LoadSql();
                try {
                    String query = "SELECT * FROM Scoreboard WHERE Username = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, username);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        // Username exists, open change password frame
                        new ChangePasswordPage(username);
                        dispose();
                    } else {
                        // Username does not exist
                        JOptionPane.showMessageDialog(this, "Account does not exist. Please sign up.");
                        new SignUpPage();
                        dispose();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error checking username: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a username.");
            }
        }
    }

    public void LoadSql() {
        String url = "jdbc:mysql://localhost:3306/Quiz_App";
        String user = "root";
        String password = "";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to database: " + e.getMessage());
        }
    }


}
