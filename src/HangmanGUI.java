import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class HangmanGUI extends JFrame implements ActionListener {
    private ArrayList<String> words;
    private static final int MAX_TRIES = 6;
    private String word;
    private char[] letters;
    private boolean[] guessed;
    private int tries;
    private JLabel wordLabel;
    private JLabel triesLabel;
    private JLabel messageLabel;
    private JTextField guessField;
    private JButton guessButton;

    public HangmanGUI(ArrayList<String> words) {
        super("Hangman");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(1000, 600);
        getContentPane().setBackground(Color.BLACK);

        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Menu");

        JMenuItem quitMenuItem = new JMenuItem("Quit");
        quitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        gameMenu.add(quitMenuItem);
        menuBar.add(gameMenu);
        setJMenuBar(menuBar);

        JLabel headingLabel = new JLabel("Hangman");
        headingLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 100));
        headingLabel.setHorizontalAlignment(JLabel.CENTER);
        headingLabel.setForeground(Color.YELLOW);
        add(headingLabel, BorderLayout.NORTH);
        this.words = words;

        wordLabel = new JLabel();
        wordLabel.setFont(new Font("Arial", Font.PLAIN, 40));
        wordLabel.setHorizontalAlignment(JLabel.CENTER);
        add(wordLabel, BorderLayout.CENTER);
        wordLabel.setForeground(Color.WHITE);

        JPanel bottomPanel = new JPanel(new GridLayout(2,2));
        triesLabel = new JLabel("Tries left: " + MAX_TRIES);
        triesLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15));
        triesLabel.setForeground(Color.WHITE);
        bottomPanel.add(triesLabel);

        messageLabel = new JLabel("Guess a letter:");
        messageLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15));
        messageLabel.setForeground(Color.WHITE);
        bottomPanel.add(messageLabel);

        guessField = new JTextField();
        bottomPanel.add(guessField);

        guessButton = new JButton("Guess");
        guessButton.addActionListener(this);
        bottomPanel.add(guessButton);
        bottomPanel.setBackground(Color.BLACK);
        add(bottomPanel, BorderLayout.SOUTH);

        startNewGame();
    }

    public void startNewGame() {
        Random random = new Random();
        word = words.get(random.nextInt(words.size()));
        letters = new char[word.length()];
        guessed = new boolean[word.length()];
        tries = 0;
    
        for (int i = 0; i < word.length(); i++) {
            letters[i] = word.charAt(i);
        }
    
        updateWordLabel();
        triesLabel.setText("Tries left: " + (MAX_TRIES - tries));
        messageLabel.setText("Guess a letter:");
        guessField.setText("");
        guessButton.setEnabled(true);
        guessField.requestFocus();
    
        // Add image panel to the center of the JFrame
        JPanel imagePanel = new JPanel();
        imagePanel.setBackground(Color.BLACK);
        add(imagePanel, BorderLayout.WEST);
        // Add images to the image panel
        ImageIcon[] images = {
            new ImageIcon("D:/JAVA PROJECT/Images/Hangman0.png"),
            new ImageIcon("D:/JAVA PROJECT/Images/Hangman1.png"),
            new ImageIcon("D:/JAVA PROJECT/Images/Hangman2.png"),
            new ImageIcon("D:/JAVA PROJECT/Images/Hangman3.png"),
            new ImageIcon("D:/JAVA PROJECT/Images/Hangman4.png"),
            new ImageIcon("D:/JAVA PROJECT/Images/Hangman5.png"),
            new ImageIcon("D:/JAVA PROJECT/Images/Hangman6.png")
        };
        JLabel imageLabel = new JLabel(images[tries]);
        imagePanel.add(imageLabel);
    
        guessButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean found = false;
                for (int i = 0; i < letters.length; i++) {
                    String guessText = guessField.getText();
                    char guess = guessText.charAt(0);
                    if (letters[i] == guess) {
                        guessed[i] = true;
                        found = true;
                    }
                }
                if (!found) {
                    tries++;
                    if (tries == MAX_TRIES) {
                        // Update image to display last state of hangman
                        imageLabel.setIcon(images[MAX_TRIES]);
                    } 
                    else {
                        // Update image to display current state of hangman
                        imageLabel.setIcon(images[tries]);
                    }
                } 
                else {
                    // Update image to display current state of hangman
                    imageLabel.setIcon(images[tries]);

                }
            }
        });
    }
    
    private void updateWordLabel() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < letters.length; i++) {
            if (guessed[i]) {
                sb.append(letters[i]);
            } 
            else {
                sb.append("_");
            }
            sb.append(" ");
        }
        wordLabel.setText(sb.toString());
    }

    public void actionPerformed(ActionEvent e) {
        String guessText = guessField.getText();
        if (guessText.length() == 1) {
            char guess = guessText.charAt(0);
            boolean found = false;
            for (int i = 0; i < letters.length; i++) {
                if (letters[i] == guess) {
                    guessed[i] = true;
                    found = true;
                }
            }
            if (!found) {
                triesLabel.setText("Tries left: " + (MAX_TRIES - tries));
                if (tries == MAX_TRIES) {
                    messageLabel.setText("Sorry, you ran out of tries. The word was " + word);
                    guessButton.setEnabled(false);
                    messageLabel.setForeground(Color.yellow);
                } 
                else {
                    messageLabel.setText("Incorrect guess.");
                    messageLabel.setForeground(Color.RED); // set color to red
                }
            } 
            else if (isGuessed()) {
                messageLabel.setText("Congratulations! You guessed the word.");
                guessButton.setEnabled(false);
                messageLabel.setForeground(Color.yellow);
            } 
            else 
            {
                messageLabel.setText("Correct guess.");
                messageLabel.setForeground(Color.GREEN); // set color to green
            }
            guessField.setText("");
            guessField.requestFocus();
            updateWordLabel();
        }
    }  
    private boolean isGuessed() {
        for (boolean b : guessed) {
            if (!b) {
                return false;
            }
        }
        return true;
    }  
    public static void main(String[] args) {
        ArrayList<String> words = readWordsFromFile("D:/JAVA PROJECT/src/words.txt");
        HangmanGUI game = new HangmanGUI(words);
        game.setVisible(true);
    } 
    private static ArrayList<String> readWordsFromFile(String fileName) {
        ArrayList<String> words = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                words.add(line.trim());
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);}
        return words;
    }
}