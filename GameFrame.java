import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/* The layout of the "GameFrame" consists of 5 panels, 3 grey panels left, right, and up, which form a border,
 * one panel in the center which holds the game,  and the lower panel with information and a reset button
 * There is also a button with additional options for the number of rows, columns and mine
 */
public class GameFrame extends JFrame implements ActionListener {

    ImageIcon icon = new ImageIcon("images\\icon.png");

    GrayPanel[] grayPanel = new GrayPanel[3];
    GamePanel gamePanel;
    JButton settingsButton;

    int rowNum = 9;
    int colNum = 9;
    int minesCount = 10;

    GameFrame(int rowNum, int colNum, int minesCount) {

        this.rowNum = rowNum;
        this.colNum = colNum;
        this.minesCount = minesCount;

        for (int i = 0; i < 3; i++) {
            grayPanel[i] = new GrayPanel();
        }
        settingsButton = new JButton("Options");
        settingsButton.addActionListener(this);
        settingsButton.setFocusable(false);
        settingsButton.setBorder(BorderFactory.createLineBorder(Color.gray));
        settingsButton.setFont(new Font("Arial", Font.PLAIN, 16));
        grayPanel[0].setLayout(new BorderLayout());
        grayPanel[0].add(settingsButton, BorderLayout.WEST);


        gamePanel = new GamePanel(rowNum, colNum, minesCount);
        gamePanel.setBorder(BorderFactory.createBevelBorder(1));
        gamePanel.restartButton.addActionListener(this);

        this.setTitle("Minesweeper");
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(350, 350);
        this.setLayout(new BorderLayout());
        this.add(gamePanel, BorderLayout.CENTER);
        this.add(grayPanel[0], BorderLayout.NORTH);
        this.add(grayPanel[1], BorderLayout.WEST);
        this.add(grayPanel[2], BorderLayout.EAST);
        this.add(gamePanel.infoPanel, BorderLayout.SOUTH);
        this.setLocationRelativeTo(null);
        this.setIconImage(icon.getImage());


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == settingsButton) {
            this.dispose();
            GameOptions gameOptions = new GameOptions();
            gameOptions.setSize(400, 140);
        }
        if (e.getSource() == gamePanel.restartButton) {
            this.dispose();
            if (minesCount == 10) {
                GameFrame gameFrame = new GameFrame(rowNum, colNum, minesCount);
            }
            if (minesCount == 40) {
                GameFrame gameFrame = new GameFrame(rowNum, colNum, minesCount);
                gameFrame.setSize(622, 622);
            }
            if (minesCount == 99) {
                GameFrame gameFrame = new GameFrame(rowNum, colNum, minesCount);
                gameFrame.setSize(900, 600);
            }
            
        }
    }
}
