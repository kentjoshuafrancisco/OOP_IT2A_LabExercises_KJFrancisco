import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class ArithmeticGameGUI extends JFrame implements ActionListener {

	// --- GUI Components ---
	private JLabel scoreDisplay;
	private JLabel problemDisplay;
	private JTextField answerInput;
	private JButton submitButton;
	private JButton resetButton;
	private JLabel feedbackBox;
	private JLabel wrongAnswerDisplay; // New component for wrong answer feedback
	private JComboBox<String> operationSelector;
	private JComboBox<String> levelSelector;
	private JPanel gamePanel; // Panel to apply animations/feedback color

	// --- Game State ---
	private int score = 0;
	private int currentAnswer = 0;
	private final Random random = new Random();

	// --- Difficulty Settings ---
	// Structure per Level (13 parameters):
	// + (min1, max1, min2, max2) [0-3]
	// - (minResult, maxResult, maxSubtrahend) [4-6]
	// * (min1, max1, min2, max2) [7-10]
	// / (resultMax, divisorMax) [11-12]
	private final int[][] LEVEL_RANGES = {
		// Level 1: Easy
		{1, 20, 1, 20, 5, 15, 20, 1, 9, 1, 5, 10, 10},
		// Level 2: Medium
		{20, 99, 10, 99, 10, 50, 99, 5, 15, 5, 15, 15, 15},
		// Level 3: Difficult
		{100, 500, 100, 500, 50, 200, 500, 10, 25, 10, 25, 20, 20}
	};
	
	// Custom Colors
	private static final Color PRIMARY_COLOR = new Color(79, 70, 229); // indigo-600
	private static final Color BACKGROUND_COLOR = new Color(240, 244, 248); // off-white/light gray
	private static final Color CARD_COLOR = Color.WHITE;
	private static final Color CORRECT_COLOR = new Color(52, 211, 153); // green-400
	private static final Color INCORRECT_COLOR = new Color(248, 113, 113); // red-400
	private static final Color ERROR_TEXT_COLOR = new Color(185, 28, 28); // red-700

	public ArithmeticGameGUI() {
		super("Arithmetic Master");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 550);
		setLocationRelativeTo(null); // Center the window
		getContentPane().setBackground(BACKGROUND_COLOR);

		// --- Main Panel Setup (The "Card") ---
		gamePanel = new JPanel();
		gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.Y_AXIS));
		gamePanel.setBackground(CARD_COLOR);
		
		// Apply a subtle border and padding for a "card" effect
		Border padding = BorderFactory.createEmptyBorder(25, 25, 25, 25);
		Border shadow = BorderFactory.createLineBorder(new Color(229, 231, 235), 1); // Neutral-200 border
		gamePanel.setBorder(new CompoundBorder(shadow, padding));
		
		// Add gamePanel to the center of the frame
		setLayout(new BorderLayout());
		JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
		wrapper.setBackground(BACKGROUND_COLOR);
		wrapper.add(gamePanel);
		add(wrapper, BorderLayout.CENTER);


		// 1. Title
		JLabel titleLabel = new JLabel("⚡️ Arithmetic Master", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Inter", Font.BOLD, 28));
		titleLabel.setForeground(new Color(30, 41, 59));
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		gamePanel.add(titleLabel);
		gamePanel.add(Box.createRigidArea(new Dimension(0, 20)));

		// 2. Scoreboard Container (Includes Score and Wrong Answer Display)
		JPanel headerContainer = new JPanel();
		headerContainer.setLayout(new BoxLayout(headerContainer, BoxLayout.Y_AXIS));
		headerContainer.setBackground(CARD_COLOR);
		headerContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Score Panel (Layout used to align score left and reset button right)
		JPanel scorePanel = new JPanel(new BorderLayout());
		scorePanel.setBackground(CARD_COLOR);
		
		JLabel scoreLabel = new JLabel("Score:");
		scoreLabel.setFont(new Font("Inter", Font.PLAIN, 18));
		scoreDisplay = new JLabel("0");
		scoreDisplay.setFont(new Font("Inter", Font.BOLD, 20));
		scoreDisplay.setForeground(PRIMARY_COLOR);
		
		JPanel scoreWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
		scoreWrapper.setBackground(CARD_COLOR);
		scoreWrapper.add(scoreLabel);
		scoreWrapper.add(scoreDisplay);
		scorePanel.add(scoreWrapper, BorderLayout.WEST);

		resetButton = new JButton("Reset Game");
		resetButton.setBackground(new Color(239, 68, 68)); // red-500
		resetButton.setForeground(Color.WHITE);
		resetButton.setFocusPainted(false);
		resetButton.setBorderPainted(false);
		resetButton.setFont(new Font("Inter", Font.BOLD, 12));
		resetButton.addActionListener(this);
		scorePanel.add(resetButton, BorderLayout.EAST);
		
		headerContainer.add(scorePanel);

		// --- NEW: Wrong Answer Display (placed under the score) ---
		wrongAnswerDisplay = new JLabel("❌ Wrong Answer! Answer was: ", SwingConstants.LEFT);
		wrongAnswerDisplay.setFont(new Font("Inter", Font.BOLD, 14));
		wrongAnswerDisplay.setForeground(ERROR_TEXT_COLOR);
		// Use a 1-pixel height separator to visually separate it from other elements
		wrongAnswerDisplay.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0)); 
		wrongAnswerDisplay.setVisible(false);
		headerContainer.add(wrongAnswerDisplay);

		gamePanel.add(headerContainer);
		gamePanel.add(Box.createRigidArea(new Dimension(0, 15)));
		gamePanel.add(new JSeparator(SwingConstants.HORIZONTAL));
		gamePanel.add(Box.createRigidArea(new Dimension(0, 15)));


		// 3. Operation and Level Selectors
		JPanel selectorPanel = new JPanel(new GridLayout(1, 2, 10, 0));
		selectorPanel.setBackground(CARD_COLOR);

		// Operation selector using custom renderer for bullet points
		operationSelector = new JComboBox<>(new String[] {
			"random", "+", "-", "*", "/"
		});
		operationSelector.setRenderer(new OperationComboBoxRenderer()); 
		operationSelector.setFont(new Font("Inter", Font.PLAIN, 14));
		operationSelector.addActionListener(this);
		
		// Level selector using custom renderer for bullet points
		levelSelector = new JComboBox<>(new String[] {
			"1", "2", "3"
		});
		levelSelector.setRenderer(new LevelComboBoxRenderer()); 
		levelSelector.setFont(new Font("Inter", Font.PLAIN, 14));
		levelSelector.addActionListener(this);

		selectorPanel.add(operationSelector);
		selectorPanel.add(levelSelector);
		
		gamePanel.add(selectorPanel);
		gamePanel.add(Box.createRigidArea(new Dimension(0, 25)));


		// 4. Problem Display
		JLabel instructionLabel = new JLabel("What is the answer?", SwingConstants.CENTER);
		instructionLabel.setFont(new Font("Inter", Font.PLAIN, 20)); 
		instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		gamePanel.add(instructionLabel);
		gamePanel.add(Box.createRigidArea(new Dimension(0, 10)));

		problemDisplay = new JLabel("? + ?", SwingConstants.CENTER);
		problemDisplay.setFont(new Font("Monospaced", Font.BOLD, 48));
		problemDisplay.setForeground(PRIMARY_COLOR.darker());
		problemDisplay.setBackground(new Color(238, 242, 255)); // indigo-50
		problemDisplay.setOpaque(true);
		// Use a simple border to contain the problem display
		problemDisplay.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(199, 210, 254), 1), // indigo-200
			BorderFactory.createEmptyBorder(10, 20, 10, 20)
		));
		problemDisplay.setAlignmentX(Component.CENTER_ALIGNMENT);
		gamePanel.add(problemDisplay);
		gamePanel.add(Box.createRigidArea(new Dimension(0, 25)));


		// 5. Answer Input and Submit Button
		JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
		inputPanel.setBackground(CARD_COLOR);
		
		answerInput = new JTextField(10);
		answerInput.setFont(new Font("Inter", Font.PLAIN, 20));
		answerInput.setHorizontalAlignment(JTextField.CENTER);
		answerInput.addActionListener(this); // Allow Enter key press
		inputPanel.add(answerInput, BorderLayout.CENTER);

		submitButton = new JButton("Submit");
		// --- CHANGE 1: Set Submit Button to Green ---
		submitButton.setBackground(CORRECT_COLOR.darker()); 
		// ---------------------------------------------
		submitButton.setForeground(Color.WHITE);
		submitButton.setFont(new Font("Inter", Font.BOLD, 18));
		submitButton.setFocusPainted(false);
		submitButton.addActionListener(this);
		inputPanel.add(submitButton, BorderLayout.EAST);
		
		gamePanel.add(inputPanel);
		gamePanel.add(Box.createRigidArea(new Dimension(0, 20)));


		// 6. Feedback Area (for messages like "Please enter a valid number!")
		feedbackBox = new JLabel("", SwingConstants.CENTER);
		feedbackBox.setFont(new Font("Inter", Font.BOLD, 14));
		feedbackBox.setPreferredSize(new Dimension(400, 30));
		feedbackBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		gamePanel.add(feedbackBox);

		// Initial setup
		generateProblem();
		pack(); // Adjust window size based on content
		setVisible(true);
	}

	/**
	 * Helper to get a random integer in a range (inclusive).
	 */
	private int getRandomInt(int min, int max) {
		if (min > max) return min; // Safety check
		return random.nextInt(max - min + 1) + min;
	}

	/**
	 * Generates a new arithmetic problem based on current selections.
	 */
	private void generateProblem() {
		String selectedOperation = (String) operationSelector.getSelectedItem();
		int selectedLevel = levelSelector.getSelectedIndex(); // 0-indexed

		String operator;
		if (selectedOperation.equals("random")) {
			String[] internalOperators = {"+", "-", "*", "/"};
			operator = internalOperators[random.nextInt(internalOperators.length)];
		} else {
			operator = selectedOperation;
		}

		// Use standard symbols for display (× for multiply, ÷ for divide)
		String symbol = operator.equals("*") ? "×" : (operator.equals("/") ? "÷" : operator);

		// Retrieve ranges for the selected level (0-indexed array)
		int[] ranges = LEVEL_RANGES[selectedLevel];
		int num1 = 0, num2 = 0;
		int currentAnswerValue = 0;

		switch (operator) {
			case "+":
				// Ranges: 0-3
				num1 = getRandomInt(ranges[0], ranges[1]); 
				num2 = getRandomInt(ranges[2], ranges[3]);
				currentAnswerValue = num1 + num2;
				break;
			case "-":
				// Ranges: 4-6 (minResult, maxResult, maxSubtrahend)
				int resultSub = getRandomInt(ranges[4], ranges[5]); 
				// Ensure num2 is a reasonable positive number 
				num2 = getRandomInt(5, ranges[6]); 
				num1 = resultSub + num2;
				currentAnswerValue = resultSub;
				break;
			case "*":
				// Ranges: 7-10
				num1 = getRandomInt(ranges[7], ranges[8]); 
				num2 = getRandomInt(ranges[9], ranges[10]); 
				currentAnswerValue = num1 * num2;
				break;
			case "/":
				// Ranges: 11-12 (resultMax, divisorMax)
				// Ensure division always results in an integer
				int result = getRandomInt(2, ranges[11]); 
				num2 = getRandomInt(2, ranges[12]); 
				num1 = result * num2; // Dividend is product of result and divisor
				currentAnswerValue = result;
				break;
		}

		currentAnswer = currentAnswerValue;
		problemDisplay.setText(String.format("%d %s %d", num1, symbol, num2));
		answerInput.setText("");
		answerInput.requestFocusInWindow();
		
		feedbackBox.setText("");
		wrongAnswerDisplay.setVisible(false); // Hide wrong answer message on new problem
		gamePanel.setBackground(CARD_COLOR); 
	}

	/**
	 * Checks the user's answer and updates the score.
	 */
	private void checkAnswer() {
		try {
			int userAnswer = Integer.parseInt(answerInput.getText().trim());

			if (userAnswer == currentAnswer) {
				// Correct Answer
				score++;
				scoreDisplay.setText(String.valueOf(score));
				
				// Hide specific wrong answer feedback
				wrongAnswerDisplay.setVisible(false);
				displayFeedback("✅ Correct! Keep going!", CORRECT_COLOR.darker(), new Color(209, 250, 225));

				// Quick visual feedback using Swing Timer for animation effect
				Color originalColor = CARD_COLOR;
				gamePanel.setBackground(CORRECT_COLOR.brighter());
				
				Timer timer = new Timer(500, (ActionEvent e) -> {
					gamePanel.setBackground(originalColor);
					generateProblem();
					((Timer)e.getSource()).stop();
				});
				timer.setRepeats(false);
				timer.start();

			} else {
				// Incorrect Answer
				score = Math.max(0, score - 1);
				scoreDisplay.setText(String.valueOf(score));
				
				// Show specific wrong answer feedback
				wrongAnswerDisplay.setText(String.format("❌ Wrong Answer! Answer was: %d", currentAnswer));
				wrongAnswerDisplay.setVisible(true);
				
				// Update general feedback (less dramatic text here)
				displayFeedback("Keep trying!", INCORRECT_COLOR.darker(), new Color(254, 226, 226));

				// Quick visual feedback using Swing Timer for animation effect
				Color originalColor = CARD_COLOR;
				gamePanel.setBackground(INCORRECT_COLOR.brighter());
				
				Timer timer = new Timer(1000, (ActionEvent e) -> {
					gamePanel.setBackground(originalColor);
					generateProblem();
					((Timer)e.getSource()).stop();
				});
				timer.setRepeats(false);
				timer.start();
			}
		} catch (NumberFormatException e) {
			wrongAnswerDisplay.setVisible(false);
			displayFeedback("Please enter a valid number!", new Color(180, 83, 9), new Color(253, 230, 138));
		}
	}
	
	/**
	 * Resets the game state and starts a new round.
	 */
	private void resetGame() {
		score = 0;
		scoreDisplay.setText("0");
		wrongAnswerDisplay.setVisible(false);
		generateProblem();
		displayFeedback("Game Reset! New challenge awaits.", new Color(75, 85, 99), new Color(243, 244, 246));
	}

	/**
	 * Displays feedback message with specified colors.
	 */
	private void displayFeedback(String message, Color textColor, Color bgColor) {
		feedbackBox.setText(message);
		feedbackBox.setForeground(textColor);
		feedbackBox.setOpaque(true);
		feedbackBox.setBackground(bgColor);
		feedbackBox.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == submitButton || e.getSource() == answerInput) {
			checkAnswer();
		} else if (e.getSource() == resetButton) {
			resetGame();
		} else if (e.getSource() == operationSelector || e.getSource() == levelSelector) {
			// Reset game when selection changes
			resetGame();
		}
	}

	// --- Custom ComboBox Renderers for Display Names (with bullet points) ---
	private static final String BULLET = "\u25CF "; // Solid circle bullet point

	// Renderer for Operation Selector to show readable names with signs and bullets
	private class OperationComboBoxRenderer extends DefaultListCellRenderer {
		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			if (value != null) {
				String op = (String) value;
				// --- CHANGE 2: Updated text to include sign and name ---
				switch (op) {
					case "random": setText(BULLET + "Random Operation"); break;
					case "+": setText(BULLET + "+ Addition"); break;
					case "-": setText(BULLET + "- Subtraction"); break;
					case "*": setText(BULLET + "× Multiplication"); break; // Using unicode '×'
					case "/": setText(BULLET + "÷ Division"); break; // Using unicode '÷'
				}
				// --------------------------------------------------------
			}
			return this;
		}
	}

	// Renderer for Level Selector to show descriptive names with bullets
	private class LevelComboBoxRenderer extends DefaultListCellRenderer {
		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			if (value != null) {
				String level = (String) value;
				switch (level) {
					case "1": setText(BULLET + "Level 1: Easy"); break;
					case "2": setText(BULLET + "Level 2: Medium"); break;
					case "3": setText(BULLET + "Level 3: Difficult"); break;
				}
			}
			return this;
		}
	}

	// --- Main Method ---
	public static void main(String[] args) {
		// Run GUI construction on the Event Dispatch Thread
		SwingUtilities.invokeLater(() -> {
			// Attempt to set system L&F for a modern look
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				// Failsafe: keep default look
			}
			new ArithmeticGameGUI();
		});
	}
}