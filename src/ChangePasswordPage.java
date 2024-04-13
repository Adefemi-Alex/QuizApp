import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ChangePasswordPage extends JFrame implements ActionListener {
    String username;
    JPasswordField newPasswordField, confirmPasswordField;
    JButton changeButton,exitButton;
    Connection connection;
    Statement statement;

    public ChangePasswordPage(String username) {
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/game.png"));
        setIconImage(icon.getImage());
        this.username = username;
        setTitle("Change Password");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
//        getContentPane().setBackground(new Color(70, 130, 180));
        setLayout(new BorderLayout());
        setUndecorated(true);

        JPanel navBar = new JPanel();
        navBar.setBackground(new Color(70, 130, 180));
        navBar.setBorder(new EmptyBorder(5, 10, 5, 10));
        navBar.setLayout(new BorderLayout());
        // Create the panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(70, 130, 180));


        JLabel newPasswordLabel = new JLabel("New Password:");
        newPasswordLabel.setForeground((new Color(  236, 234, 230)));
        newPasswordField = new JPasswordField();
        panel.add(newPasswordLabel);
        panel.add(newPasswordField);


        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setForeground((new Color(  236, 234, 230)));
        confirmPasswordField = new JPasswordField();
        panel.add(confirmPasswordLabel);
        panel.add(confirmPasswordField);


        changeButton = new JButton("Change Password");
        changeButton.addActionListener(this);
        changeButton.setBackground((new Color(  236, 234, 230)));
        changeButton.setForeground(new Color(70, 130, 180));
        panel.add(changeButton);

        JLabel logo = new JLabel("change password");
        logo.setForeground((new Color(  236, 234, 230)));
        logo.setFont(new Font("Garamond", Font.ITALIC, 18));
        logo.setHorizontalAlignment(JLabel.CENTER);
        exitButton = new JButton("Back");
        exitButton.setBackground((new Color(  236, 234, 230)));

        exitButton.setForeground(new Color(70, 130, 180));

        exitButton.setFocusPainted(false);

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UsernameCheckPage().setVisible(true);
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

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == changeButton) {
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (newPassword.equals(confirmPassword) && newPassword.length() >= 3) {
                LoadSql();
                try {
                    String query = "UPDATE Scoreboard SET Password = ? WHERE Username = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, newPassword);
                    preparedStatement.setString(2, username);
                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(this, "Password changed successfully.");
                        dispose();
                        new LoginPage();
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to change password. Please try again.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error changing password: " + ex.getMessage());
                }
            } else if (newPassword.length() < 3) {
                JOptionPane.showMessageDialog(this, "Password cannot be null or less than three characters.");
            }
            else {
                JOptionPane.showMessageDialog(this, "Passwords do not match. Please try again.");
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
