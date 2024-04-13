import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class MainFrame extends JFrame implements ActionListener {
    String Username, Password;
    int Score;
    Connection connection;
    Statement statement;
    public MainFrame(String username, String password, int score) {
        Username = username;
        Password = password;
        Score = score;
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/game.png"));
        setIconImage(icon.getImage());
        setTitle("Game Menu");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);
        // Create components
        createComponents();

        setVisible(true);
    }

    private void createComponents() {
        // Navigation Bar
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(new Color(70, 130, 180));
        navBar.setBorder(new EmptyBorder(5, 10, 5, 10));

        JButton exitButton = new JButton("Log out");
        exitButton.setBackground(new Color(  236, 234, 230));
        exitButton.setForeground(new Color(70, 130, 180));
        exitButton.setFocusPainted(false);


        exitButton.addActionListener(this);
//        String capitalize;
//        char[] user = Username.toCharArray();
//        for(int i=0; i < user.length; i++){
//            user[i] = user[i].toUpper()
//        }

        JLabel logo = new JLabel("Hello "+Username.substring(0,1).toUpperCase()+Username.substring(1));
        logo.setFont(new Font("Arial", Font.BOLD, 20));
        logo.setForeground(Color.WHITE);
        logo.setHorizontalAlignment(JLabel.CENTER);

        navBar.add(logo, BorderLayout.CENTER);
        navBar.add(exitButton, BorderLayout.EAST);
        add(navBar, BorderLayout.NORTH);

        // Main content
        JPanel mainPanel = new JPanel(new GridLayout(4, 1, 0, 20));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JButton playGameBtn = new JButton("Play Game");
        JButton checkInfo = new JButton("Check info");
        JButton checkHistoryBtn = new JButton("Check History");
        JButton deleteAccountBtn = new JButton("Delete Account");

        // Style buttons
        styleButton(playGameBtn);
        styleButton(checkInfo);
        styleButton(checkHistoryBtn);
        styleButton(deleteAccountBtn);

        // Add buttons to the main panel
        mainPanel.add(playGameBtn);
        mainPanel.add(checkInfo);
        mainPanel.add(checkHistoryBtn);
        mainPanel.add(deleteAccountBtn);

        add(mainPanel, BorderLayout.CENTER);
        createTable();
    }

    private void createTable() {
        LoadSql();
        try {
            // Use the database
            statement.executeUpdate("USE quiz_app");

            // Create the table if it doesn't exist
            String createTableQuery = "CREATE TABLE IF NOT EXISTS " + Username + " ("
                    + "Session INT AUTO_INCREMENT PRIMARY KEY,"
                    + "Date VARCHAR(255) NOT NULL,"
                    + "Time VARCHAR(50) NOT NULL,"
                    + "Score INT DEFAULT 0)";
            statement.executeUpdate(createTableQuery);

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error creating database and table: " + e.getMessage());
        }
    }
    private void styleButton(JButton button) {
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.addActionListener(this);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        switch (actionCommand) {
            case "Delete Account":
                int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete your account?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(this, "Account Deleted!");
                    deleteAccount();
                    new LoginPage();
                    dispose();
                }
                break;
            case "Play Game":
                JOptionPane.showMessageDialog(this, "Playing Game...");
                new WelcomePage(Username, Password, Score);
                dispose();
                break;
            case "Check History":
                JOptionPane.showMessageDialog(this, "Checking History...");
                new History(Username, Password, Score);
                dispose();
                break;
            case "Check info":
                checkUserInfo();
                break;
            case "Log out":
                int confirmExit = JOptionPane.showConfirmDialog(this, "Are you sure you want to Log out?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirmExit == JOptionPane.YES_OPTION) {
                    new LoginPage();
                    dispose();
                }
                break;
        }


    }
    public void deleteAccount(){
        LoadSql();
        try {
            String query = "DELETE FROM Scoreboard WHERE username = '" + Username + "'AND password = '" + Password +" '";
            statement.execute(query);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public void checkUserInfo(){
        LoadSql();
        try {
            String query = "SELECT * FROM Scoreboard WHERE username = '" + Username + "'";
            ResultSet resultSet = statement.executeQuery(query);
             int id = 0;
            String Fullname = "";
            int Score =0;
            if(resultSet.next()){
                 id = resultSet.getInt("id");
                 Fullname = resultSet.getString("Fullname");
                 Score = resultSet.getInt("Score");
            }

            JOptionPane.showMessageDialog(null, " Id: "+ id + "\n Fullname: "+ Fullname + "\n Username: "
                    +Username + "\n Score: "+ Score);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

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
}
