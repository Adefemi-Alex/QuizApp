import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomePage extends JFrame implements ActionListener {
    JButton exitButton;
    JButton startGameButton;
    String Username, Password;
    int Score;
    public WelcomePage(String username, String password, int score) {
        Username = username;
        Password = password;
        Score = score;

        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/game.png"));
        setIconImage(icon.getImage());
        setSize(400, 400);
        setTitle("Welcome to Guessing Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        getContentPane().setBackground(new Color(70, 130, 180));
        JPanel navBar = new JPanel();
        navBar.setBackground(new Color(70, 130, 180));
        navBar.setBorder(new EmptyBorder(5, 10, 5, 10));
        navBar.setLayout(new BorderLayout());
        exitButton = new JButton("Back");
        exitButton. setBackground(new Color(  236, 234, 230));
        exitButton.setForeground(new Color(70, 130, 180));
        exitButton.setFocusPainted(false);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MainFrame(Username, Password, Score).setVisible(true);
                dispose();
            }
        });
        navBar.add(exitButton, BorderLayout.EAST);
        JLabel welcomeLabel = new JLabel("<html><center>Welcome to the Guessing Game!<br><br>Rules:<br>" +
                "1. Click for a random number (1-100).<br>" +
                "2. Guess the correct number.<br>" +
                "3. You have 2 attempts.<br>" +
                "4. You have 5 seconds per guess.<br>" +
                "5. Ensure completion of levels; otherwise, your score will not be counted. <br>" +
                "<br>Good Luck!</center>" +
                "</html>");
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        welcomeLabel.setForeground(new Color(245, 245, 245));
        Font welcomeFont = new Font("Arial", Font.PLAIN, 18);
        welcomeLabel.setFont(welcomeFont);

        startGameButton = new JButton("Start Game");
        startGameButton.addActionListener(this);
        startGameButton.setPreferredSize(new Dimension(150, 50));
        startGameButton.setBackground(new Color(70, 130, 180));
        startGameButton.setForeground(new Color(254, 254, 254));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(startGameButton);

        add(navBar, BorderLayout.NORTH);
        add(welcomeLabel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);


        setUndecorated(true);
        setLocationRelativeTo(null);
        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startGameButton) {
            new GuessingGame(Username, Password, Score).setVisible(true);
            dispose(); // Close the welcome page
        }
    }

}
