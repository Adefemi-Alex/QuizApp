import javax.management.remote.JMXConnectorServerProvider;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminPage extends JFrame {
    JButton editButton, exitButton, saveButton, deleteButton;
    DefaultTableModel model;
    private JTable dataTable;
    private JScrollPane scrollPane;
    Statement statement;
    String[][] data;
    String[] columns;
    Boolean isEditable = false;

    Boolean valid = false;
    List<int[]> editedCells = new ArrayList<>();
    Connection connection;

    public AdminPage() {
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/game.png"));
        setIconImage(icon.getImage());
        // Setting up the window
        setTitle("Admin Page");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel navBar = new JPanel();
        navBar.setBackground(new Color(70, 130, 180));
        navBar.setBorder(new EmptyBorder(5, 3, 5, 3));
        navBar.setLayout(new BorderLayout());

        editButton = new JButton("Enable Edit Mode");
        exitButton = new JButton("Back");
        saveButton = new JButton("Save Changes");

        editButton.setBackground(new Color(70, 130, 180));
        exitButton.setBackground(new Color(70, 130, 180));
        saveButton.setBackground(new Color(70, 130, 180));

        editButton.setForeground(Color.WHITE);
        exitButton.setForeground(Color.WHITE);
        saveButton.setForeground(Color.WHITE);

        editButton.setFocusPainted(false);
        exitButton.setFocusPainted(false);
        saveButton.setFocusPainted(false);

        editButton.addActionListener(edit());
        exitButton.addActionListener(exit());
        saveButton.addActionListener(saveChanges());

        deleteButton = new JButton("Delete Selected Rows");
        deleteButton.setBackground(new Color(70, 130, 180));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(deleteRows());

        navBar.add(editButton, BorderLayout.WEST);
        navBar.add(saveButton, BorderLayout.CENTER);
        navBar.add(exitButton, BorderLayout.EAST);
        navBar.add(deleteButton, BorderLayout.SOUTH);
        add(navBar, BorderLayout.NORTH);

        // Tabel panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Fetching data from the database
        data = fetchDataFromDatabase();
        columns = new String[]{"Id", "Fullname", "Username", "Score", "Password"};

        model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return isEditable;
            }
        };

        model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (isEditable && e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    int col = e.getColumn();
                    int[] editedCell = {row, col};
                    if (!editedCells.contains(editedCell)) {
                        editedCells.add(editedCell);
                    }
                }
            }
        });

        // Creating a JTable with the fetched data
        dataTable = new JTable(model);
        dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        dataTable.setFillsViewportHeight(true);

        // Creating a JScrollPane to display the table
        scrollPane = new JScrollPane(dataTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Add the table panel to the frame
        add(tablePanel);
        setUndecorated(true);
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
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Scoreboard");

            // Get the number of rows in the ResultSet
            int rowCount = 0;
            if (resultSet.last()) {
                rowCount = resultSet.getRow();
                resultSet.beforeFirst(); // Move the cursor back to the beginning
            }

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

    public ActionListener edit() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isEditable = !isEditable;
                if (isEditable) {
                    editButton.setText("Disable Edit Mode");
                } else {
                    editButton.setText("Enable Edit Mode");
                }
                valid = true;
                for (int row = 0; row < model.getRowCount(); row++) {
                    for (int col = 0; col < model.getColumnCount(); col++) {
                        model.fireTableCellUpdated(row, col); // Refresh the table cells
                    }
                }
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

    public ActionListener saveChanges() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDatabase();
            }
        };
    }

    private void updateDatabase() {
        String url = "jdbc:mysql://localhost:3306/quiz_app";
        String username = "root";
        String password = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);

            if (editedCells.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No changes were made.");
                return;
            }

            // Iterate or loop through the list of edited cells and update the corresponding records
            for (int[] editedCell : editedCells) {
                int row = editedCell[0];
                int col = editedCell[1];
                String columnName = columns[col]; // For Getting the column name from the array

                // To Check if the column is "Score"
                Object newValueObject = dataTable.getValueAt(row, col);
                String newValue = (newValueObject != null) ? newValueObject.toString() : null;

                if (!columnName.equals("Score") && !columnName.equals("id")) {
                    if (newValue != null && newValue.length()>= 3) {
                        String update = "UPDATE Scoreboard SET " + columnName + "=? WHERE id=?";
                        try (PreparedStatement statement = connection.prepareStatement(update)) {
                            statement.setString(1, newValue);
                            statement.setString(2, data[row][0]);
                            statement.executeUpdate();
                        } catch (SQLException err) {
                            err.printStackTrace();
                        }
                        valid = true;
                    }else{
                        valid = false;

                    }
                }else {
                    String update = "UPDATE Scoreboard SET " + columnName + "=? WHERE id=?";
                    try (PreparedStatement statement = connection.prepareStatement(update)) {
                        statement.setObject(1, newValueObject); // Handle null value
                        statement.setString(2, data[row][0]);
                        statement.executeUpdate();
                    } catch (SQLException err) {
                        err.printStackTrace();
                    }
                    valid = true;
                }
            }

            connection.close();

            editedCells.clear();

            if (valid) {
                JOptionPane.showMessageDialog(null, "Changes saved to the database");
            } else if (!valid) {
                JOptionPane.showMessageDialog(null, "Invalid Data. Hint: the value is less than three or null");
            }

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    public ActionListener deleteRows() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = dataTable.getSelectedRows();

                if (selectedRows.length > 0) {
                    int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete selected rows?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);

                    if (choice == JOptionPane.YES_OPTION) {
                        deleteRowsFromDatabase(selectedRows);
                        // Refresh the table after deletion
                        refreshTable();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select rows to delete");
                }
            }
        };
    }

    private void deleteRowsFromDatabase(int[] selectedRows) {
        String url = "jdbc:mysql://localhost:3306/quiz_app";
        String username = "root";
        String password = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);

            for (int i : selectedRows) {
                String idToDelete = data[i][0];
                String deleteQuery = "DELETE FROM Scoreboard WHERE id=" + idToDelete;

                try (Statement statement = connection.createStatement()) {
                    statement.executeUpdate(deleteQuery);
                } catch (SQLException err) {
                    err.printStackTrace();
                }
            }

            connection.close();
            JOptionPane.showMessageDialog(null, "Selected rows deleted successfully");
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void refreshTable() {
        model.setRowCount(0); // Clear the table model
        data = fetchDataFromDatabase(); // Fetch data again
        for (String[] row : data) {
            model.addRow(row); // Add the updated data to the table model
        }
    }

}
