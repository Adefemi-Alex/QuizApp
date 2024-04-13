import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class History extends JFrame {
    JButton exitButton;
    String Username, Password;
    int Score;
    DefaultTableModel model;
    private JTable dataTable;
    private JScrollPane scrollPane;
    Connection connection;
    Statement statement;
    String[][] data;
    String[] columns;

    public History(String username, String password, int score) {
        Username = username;
        Password = password;
        Score = score;
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/game.png"));
        setIconImage(icon.getImage());
        // Setting up the window
        setTitle("Admin Page");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);

        JLabel logo = new JLabel("Your History");
        logo.setForeground(new Color(245, 245, 245));
        logo.setFont(new Font("Fira Code", Font.ITALIC, 18));
        logo.setHorizontalAlignment(JLabel.CENTER);

        JPanel navBar = new JPanel();
        navBar.setBackground(new Color(70, 130, 180));
        navBar.setBorder(new EmptyBorder(5, 3, 5, 3));
        navBar.setLayout(new BorderLayout());
        navBar.add(logo, BorderLayout.CENTER);

        exitButton = new JButton("Back");
        exitButton.setBackground(new Color(236,234,230));
        exitButton.setForeground(new Color(70, 130, 180));
        exitButton.setFocusPainted(false);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MainFrame(Username, Password, Score);
                dispose();
            }
        });

        navBar.add(exitButton, BorderLayout.EAST);
        add(navBar, BorderLayout.NORTH);

        // Fetching data from the database
        data = fetchDataFromDatabase();

        // Show JOptionPane if no history is found and go back to the main frame
        if (data == null || data.length == 0) {
            JOptionPane.showMessageDialog(this, "You have no history.", "No History", JOptionPane.INFORMATION_MESSAGE);
            new MainFrame(Username, Password, Score);
            dispose();
            return;
        }

        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        columns = new String[]{"Session", "Date", "Time", "Score"};

        model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Creating a JTable with the fetched data
        dataTable = new JTable(model);
        dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        dataTable.setFillsViewportHeight(true);

        // Creating a JScrollPane to display the table
        scrollPane = new JScrollPane(dataTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Add the table panel to the frame
        add(tablePanel);
        setVisible(true);
    }

    private String[][] fetchDataFromDatabase() {
        String url = "jdbc:mysql://localhost:3306/quiz_app";
        String username = "root";
        String password = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);

            // Creating a statement with a TYPE_SCROLL_INSENSITIVE result set type
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            // Execute a query to fetch data from the Users table
            String query = "SELECT * FROM " + Username;

            ResultSet resultSet = statement.executeQuery(query);

            // Check if ResultSet contains no records
            if (!resultSet.next() || resultSet.getInt("Score") == 0) {
                return new String[0][];
            }

            // Get the number of rows in the ResultSet
            resultSet.last();
            int rowCount = resultSet.getRow();
            resultSet.beforeFirst(); // Move the cursor back to the beginning

            // Get the number of columns in the ResultSet
            int columnCount = resultSet.getMetaData().getColumnCount();

            // Create a 2D array to store the data
            String[][] data = new String[rowCount][columnCount];

            // Populate the data array with values from the ResultSet
            int row = 0;
            while (resultSet.next()) {
                for (int col = 0; col < columnCount; col++) {
                    data[row][col] = resultSet.getString(col + 1);
                }
                row++;
            }

            // Close the resources
            resultSet.close();
            statement.close();

            return data;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
