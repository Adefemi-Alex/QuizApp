import com.mysql.cj.Session;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GuessingGame extends JFrame implements ActionListener {
    static JLabel timerLabel;
    private static int countdown = 5;
    private static Timer timer;
    Date currentDate = new Date();
    JButton[] numberButtons;
    JButton exitButton;
    static JLabel Label1;
    int correctNumber;
    int level = 1;
    int attemptsLeft = 2;
    boolean isCompleted = false;
    JPanel buttonPanel;

    String Username, Password, Fullname;
    int Score, existingScore;
    int currentScore = 0;
    Connection connection;
    Statement statement;

    public GuessingGame(String username, String password, int score) {
        Username = username;
        Password = password;
        Score = score;

        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/game.png"));
        setIconImage(icon.getImage());
        setSize(400, 400);
        setTitle("Guessing Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        getContentPane().setBackground(new Color(70, 130, 180));

        buttonPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        numberButtons = new JButton[4];
        for (int i = 0; i < 4; i++) {
            numberButtons[i] = new JButton();
            numberButtons[i].addActionListener(this);
            buttonPanel.add(numberButtons[i]);
            numberButtons[i].setPreferredSize(new Dimension(300, 50));
        }

        exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitGame();
            }
        });
        exitButton.setPreferredSize(new Dimension(80, 40));

        Label1 = new JLabel("Click for a random number (1-100)");
        Label1.setHorizontalAlignment(JLabel.CENTER);
        Label1.setForeground(new Color(245, 245, 245));
        Font fnt = new Font("Arial", Font.ITALIC, 20);
        Label1.setFont(fnt);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        generateRandomNumbers();
        timerLabel = new JLabel("Time left: " + String.format("%02d", countdown) + " sec");
        timerLabel.setHorizontalAlignment(JLabel.CENTER);
        bottomPanel.add(exitButton, BorderLayout.CENTER);
        bottomPanel.add(timerLabel, BorderLayout.EAST);
        Font timerFont = new Font("Arial", Font.BOLD, 16);
        timerLabel.setFont(timerFont);
        add(Label1, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        startTimer();
        setUndecorated(true);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void generateRandomNumbers() {
        Random random = new Random();
        Set<Integer> uniqueNumbers = new HashSet<>();
        while (uniqueNumbers.size() < 4) {
            uniqueNumbers.add(random.nextInt(100) + 1);
        }
        correctNumber = (int) uniqueNumbers.toArray()[random.nextInt(4)];
        Integer[] randomNumbers = uniqueNumbers.toArray(new Integer[0]);
        for (int i = 0; i < 4; i++) {
            numberButtons[i].setText(String.valueOf(randomNumbers[i]));
        }
    }
    public void LoadSql() {
        String url = "jdbc:mysql://localhost:3306/quiz_app";
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



    public String  getDate(){

        // Format for date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return  dateFormat.format(currentDate);
    }
    public String getTime(){
        // Format for time
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        return timeFormat.format(currentDate);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();
        try {
            int guessedNumber = Integer.parseInt(clickedButton.getText());

            if (guessedNumber == correctNumber) {
                Label1.setText("Congratulations! You guessed right.");
                pauseTimer();
                currentScore += 1;
                System.out.println("Score = " + currentScore);
                setColorForButtons(Color.GREEN);
                System.out.println(correctNumber);
                level++;
                nextGame();

            } else {
                attemptsLeft--;
                if (attemptsLeft == 0) {
                    showcolor();
                    Label1.setText("Sorry, you have made 2 wrong guesses");
                    level++;
                    pauseTimer();
                    nextGame();
                } else {
                    Label1.setText("Attempts left: " + attemptsLeft);
                    clickedButton.setEnabled(false);
                    clickedButton.setBackground(Color.RED);
                }
            }
        } catch (NumberFormatException ex) {
            System.err.println("Invalid button text: " + clickedButton.getText());
        }
    }

    public void setColorForButtons(Color color) {
        for (JButton button : numberButtons) {
            button.setEnabled(false);
            if (button.getText().equals(String.valueOf(correctNumber))) {
                button.setBackground(Color.green);
            }
        }
    }

    private void nextGame() {
        isCompleted = !(level <= 10);
        if(!isCompleted){
            JOptionPane.showMessageDialog(null, "Move to Level " + level);
            pauseTimer();
            resetStats();
            startTimer();
            updateTimer();
            generateRandomNumbers();
            for (JButton button : numberButtons) {
                button.setEnabled(true);
                button.setBackground(null);
            }
            Label1.setText("Guess a number between 1 and 100");
            System.out.println("Level = " + level);
        }else{
            JOptionPane.showMessageDialog(null, "Your score is " + currentScore);
            exitGame();
        }
    }

    private void updatecurrentScore() {
        LoadSql();
        try {
            String getSession = "SELECT Session from " + Username;
            ResultSet resultSet = statement.executeQuery(getSession);

            int Session = 0;
            if (resultSet.next()) {
                Session = resultSet.getInt("Session");
            }
            String updateScore = "UPDATE Scoreboard SET Score = " + (Score += currentScore) + " WHERE Username = '" + Username + "'";
            String query = (Score == 0 && Session == 1) ?
                    "UPDATE " + Username + " SET Score = " + currentScore + " WHERE Session = 1" :
                    "INSERT INTO " + Username +" (Date, Time, Score) VALUES ('" +
                            getDate() + "', '" + getTime() + "', '" + currentScore + "')";


            System.out.println(query);
            statement.executeUpdate(updateScore);
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        try {
//            String request = "SELECT Fullname, Score FROM Scoreboard WHERE Username = '" + Username + "' AND Password = '" + Password + "'";
//            ResultSet resultSet = statement.executeQuery(request);
//            if(resultSet.next()){
//                Fullname = resultSet.getString("Fullname");
//                existingScore = resultSet.getInt("Score");
//            }
//
//
//
//        } catch (SQLException e1) {
//
//            e1.printStackTrace();
//        }
    }


    private void exitGame(){
        pauseTimer();
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Play again? ",
                "Play Again", JOptionPane.YES_NO_CANCEL_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            // Reset timer and countdown if the user chooses to play again
            if(level > 10){
                updatecurrentScore();
            }
            level = 1;
            nextGame();
        }else if(option == JOptionPane.NO_OPTION){
            System.out.println(level);
            if(level > 10) {
                updatecurrentScore();
            }
            new MainFrame(Username, Password, Score).setVisible(true);
            dispose();
        }else{
            if(level <= 10){
                startTimer();
            }else{
                new MainFrame(Username, Password, Score).setVisible(true);
                dispose();
            }
        }

    }
    private void startTimer() {
        timer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                countdown--;

                if (countdown == 0) {
                    timer.stop();
                    Label1.setText("TIME'S UP. YOU LOSE");
                    showcolor();
                    level++;
                    nextGame();
                }
                updateTimer();
            }
        });
        timer.start();
    }

    private void pauseTimer() {
        if (timer != null) {
            timer.stop();
            updateTimer();
        }
    }
    private void resetStats(){
        attemptsLeft = 2;
        countdown = 5;
    }
    public void updateTimer() {
        timerLabel.setText("Time left: " + String.format("%02d", countdown) + " sec");
    }

    public void showcolor() {
//        Label1.setText("The correct answer is: " + correctNumber);
        setColorForButtons(Color.RED);
    }


}