
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class ArithmeticGameGUI extends JFrame {

    // Core Components
    private JTextField answerField;
    
    private JPanel questionPanel; 
    private JLabel num1Label, operatorLabel, num2Label, countLabel, feedbackLabel;
    private JButton submitButton;
    
    
    private int correctCount = 0; 
    private int wrongCount = 0;   
    private int correctResult;
    private String currentOperator = "+"; 
    private Random random = new Random();
    
    
    private final Color defaultBackgroundColor = UIManager.getColor("Panel.background");

    public ArithmeticGameGUI() {
        // --- 1. Basic Frame Setup (JFrame) ---
        super("Arithmetic Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // Use BorderLayout for the main frame
        
        // --- 2. Title Header ---
        JLabel headerLabel = new JLabel("Arithmetic Game Challenge", JLabel.CENTER);
        headerLabel.setFont(new Font("Inter", Font.BOLD, 24));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(headerLabel, BorderLayout.NORTH);

        // --- 3. Left Control Panel (WEST) ---
        JPanel controlsPanel = createControlsPanel();
        add(controlsPanel, BorderLayout.WEST);

        // --- 4. Main Game Area (CENTER) ---
        JPanel gameAreaPanel = createGameAreaPanel();
        add(gameAreaPanel, BorderLayout.CENTER);
        
        // --- 5. Initial Game Setup ---
        generateNewProblem();
        updateCountsDisplay(); // Initialize the counter display
        
        // --- 6. Finalizing the Frame ---
        pack(); // Resize frame to fit all components
        setMinimumSize(new Dimension(750, 450));
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }
    
   
    private JPanel createControlsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 10, 20)); // Two sections: Operations and Levels
        panel.setBorder(BorderFactory.createTitledBorder("Settings"));
        
        // --- Operations Group ---
        JPanel operationPanel = new JPanel(new GridLayout(6, 1));
        operationPanel.setBorder(BorderFactory.createTitledBorder("Operations"));
        ButtonGroup operationGroup = new ButtonGroup();
        
        String[] ops = {"+", "-", "*", "/", "% (Modulo)"};
        for (String op : ops) {
            JRadioButton opButton = new JRadioButton(op.startsWith("%") ? op : op + " (Op)");
            if (op.equals("+")) {
                opButton.setSelected(true); // Default to addition
            }
            opButton.setActionCommand(op.substring(0, 1).trim()); // Set action command to the symbol
            opButton.addActionListener(e -> {
                currentOperator = e.getActionCommand();
                generateNewProblem(); // Generate a new problem when operator changes
            });
            operationGroup.add(opButton);
            operationPanel.add(opButton);
        }
        
        // --- Level Group ---
        JPanel levelPanel = new JPanel(new GridLayout(4, 1));
        levelPanel.setBorder(BorderFactory.createTitledBorder("Level"));
        ButtonGroup levelGroup = new ButtonGroup();

        String[] levels = {"Level 1 (0-10)", "Level 2 (11-100)", "Level 3 (101-999)"};
        for (int i = 0; i < levels.length; i++) {
            JRadioButton levelButton = new JRadioButton(levels[i]);
            if (i == 0) levelButton.setSelected(true); // Default to Level 1
            levelGroup.add(levelButton);
            levelPanel.add(levelButton);
        }
        
        panel.add(operationPanel);
        panel.add(levelPanel);
        return panel;
    }

    
    private JPanel createGameAreaPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        // --- A. Question Panel (CENTER of mainPanel) ---
        // Store reference in the class field 'questionPanel'
        questionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 50));
        
        Font mathFont = new Font("Monospaced", Font.BOLD, 48);

        num1Label = createMathLabel("0", mathFont);
        operatorLabel = createMathLabel("+", mathFont);
        num2Label = createMathLabel("0", mathFont);
        
        JLabel equalsLabel = createMathLabel("=", mathFont);
        
        answerField = new JTextField(4); // 4 columns wide
        answerField.setFont(mathFont);
        answerField.setHorizontalAlignment(JTextField.CENTER);
        answerField.setMaximumSize(new Dimension(150, 70));
        answerField.addActionListener(this::checkAnswer); // Submit on Enter
        
        
        questionPanel.add(num1Label);
        questionPanel.add(operatorLabel);
        questionPanel.add(num2Label);
        questionPanel.add(equalsLabel);
        questionPanel.add(answerField);
        
        mainPanel.add(questionPanel, BorderLayout.CENTER); 
        
       
        JPanel bottomPanel = new JPanel(new BorderLayout(20, 10)); // Added vertical gap
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        
        JPanel topBottomSection = new JPanel(new BorderLayout(20, 0));

        
        submitButton = new JButton("SUBMIT");
        submitButton.setFont(new Font("Inter", Font.BOLD, 20));
        submitButton.setPreferredSize(new Dimension(150, 50));
        submitButton.addActionListener(this::checkAnswer); // Link to the checkAnswer method
        
        JPanel submitWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        submitWrapper.add(submitButton);
        topBottomSection.add(submitWrapper, BorderLayout.WEST);

       
        countLabel = new JLabel("", JLabel.CENTER);
        countLabel.setFont(new Font("Inter", Font.BOLD, 18));
        topBottomSection.add(countLabel, BorderLayout.CENTER);

        bottomPanel.add(topBottomSection, BorderLayout.NORTH); 

        
        feedbackLabel = new JLabel("Ready to play!", JLabel.CENTER);
        feedbackLabel.setFont(new Font("Inter", Font.ITALIC, 16));
        bottomPanel.add(feedbackLabel, BorderLayout.CENTER); 

       
        JPanel controlButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));

       
        JButton continueButton = new JButton("CONTINUE");
        continueButton.setFont(new Font("Inter", Font.BOLD, 16));
        continueButton.setBackground(new Color(60, 179, 113)); 
        continueButton.setForeground(Color.WHITE);
        continueButton.addActionListener(e -> generateNewProblem());
        controlButtonsPanel.add(continueButton);

        JButton exitButton = new JButton("EXIT GAME");
        exitButton.setFont(new Font("Inter", Font.BOLD, 16));
        exitButton.setBackground(new Color(220, 20, 60)); // Crimson Red
        exitButton.setForeground(Color.WHITE);
        exitButton.addActionListener(e -> System.exit(0)); // Close the application
        controlButtonsPanel.add(exitButton);

        bottomPanel.add(controlButtonsPanel, BorderLayout.SOUTH);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        return mainPanel;
    }
    
   
    private JLabel createMathLabel(String text, Font font) {
        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setFont(font);
        // Add a simple border to mimic the boxes in the sketch
        label.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
        label.setPreferredSize(new Dimension(100, 70));
        return label;
    }

   
    private void generateNewProblem() {
        // Simple range 1-10 for now (based on Level 1 in the original logic)
        int num1 = random.nextInt(10) + 1; 
        int num2 = random.nextInt(10) + 1;
        
        
        if (currentOperator.equals("/") || currentOperator.equals("%")) {
             // Make sure num1 is cleanly divisible by num2, and num2 is not zero.
             if (num2 == 0) num2 = 1;
             num1 = num2 * (random.nextInt(5) + 1); 
        }

        
        if (currentOperator.equals("-") && num1 < num2) {
            int temp = num1;
            num1 = num2;
            num2 = temp;
        }

        num1Label.setText(String.valueOf(num1));
        operatorLabel.setText(currentOperator);
        num2Label.setText(String.valueOf(num2));

        switch (currentOperator) {
            case "+": correctResult = num1 + num2; break;
            case "-": correctResult = num1 - num2; break;
            case "*": correctResult = num1 * num2; break;
            case "/": correctResult = num1 / num2; break;
            case "%": correctResult = num1 % num2; break;
            default: correctResult = 0; break;
        }

        answerField.setText(""); // Clear previous answer
        feedbackLabel.setForeground(Color.GRAY);
        feedbackLabel.setText("Solve the problem!");
        questionPanel.setBackground(defaultBackgroundColor); // Ensure background is reset
        answerField.requestFocusInWindow(); // Put cursor back in the input field
    }
    
    
    private void updateCountsDisplay() {
        // Use HTML to style the counts with colors
        countLabel.setText(String.format(
            "<html><div style='text-align: center;'><b>Correct:</b> <font color='green'>%d</font> | <b>Wrong:</b> <font color='red'>%d</font></div></html>", 
            correctCount, wrongCount));
    }

    
    private void checkAnswer(ActionEvent e) {
        try {
            int userAnswer = Integer.parseInt(answerField.getText().trim());
            
            if (userAnswer == correctResult) {
                correctCount++; 
                
                // --- CELEBRATION EFFECTS (New) ---
                feedbackLabel.setForeground(new Color(255, 69, 0)); // Vibrant Red-Orange
                feedbackLabel.setText("✨ CORRECT! +1 Point! 💥"); // Firework/Sparkle Emojis
                questionPanel.setBackground(new Color(255, 215, 0)); // Flash Gold/Yellow
                
            } else {
                wrongCount++; 
                feedbackLabel.setForeground(new Color(220, 20, 60)); // Crimson Red
                feedbackLabel.setText("Incorrect. The correct answer was " + correctResult);
            }
            
            updateCountsDisplay(); // Update the counts label

           
            Timer timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    questionPanel.setBackground(defaultBackgroundColor); // Reset the background color
                    generateNewProblem();
                    ((Timer)evt.getSource()).stop(); // Stop the timer after one execution
                }
            });
            timer.setRepeats(false);
            timer.start();
            
        } catch (NumberFormatException ex) {
            feedbackLabel.setForeground(Color.ORANGE);
            feedbackLabel.setText("Please enter a valid number.");
            answerField.setText("");
        }
    }

    

    public static void main(String[] args) {
        // Swing components must be created and updated on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            new ArithmeticGameGUI();
        });
    }

}
