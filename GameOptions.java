import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*  This code will display a small windows with options that the player can select, when the player clicks
    "Done" a new GameFrame will be created 
*/

public class GameOptions extends JFrame implements ActionListener {

    ImageIcon icon = new ImageIcon("images/icon.png");

    int colNum = 0;
    int rowNum = 0;
    int minesCount = 0; 
    JRadioButton button9910 = new JRadioButton("9 rows, 9 columns, 10 mines");
    JRadioButton button161640 = new JRadioButton("16 rows, 16 columns, 40 mines");
    JRadioButton button163099 = new JRadioButton("16 rows, 30 columns, 99 mines");
    JButton buttonDone = new JButton("Done"); 

    GameOptions() {

        this.setIconImage(icon.getImage());
        this.setLocationRelativeTo(null);
        this.setTitle("Game Options");
        this.setSize(400, 140);
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(4, 1));
        this.add(button9910);
        this.add(button161640);
        this.add(button163099);
        this.add(buttonDone);


        ButtonGroup buttonGroup = new ButtonGroup();

        buttonGroup.add(button9910);
            button9910.setFocusable(false);
            button9910.addActionListener(this);

        buttonGroup.add(button161640);
            button161640.setFocusable(false);
            button161640.addActionListener(this);

        buttonGroup.add(button163099);
            button163099.setFocusable(false);
            button163099.addActionListener(this);

        buttonDone.setFocusable(false);
        buttonDone.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button9910) {
            rowNum = 9;
            colNum = 9;
            minesCount = 10;
        }
        if (e.getSource() == button161640) {
            rowNum = 16;
            colNum = 16;
            minesCount = 40;
        }
        if (e.getSource() == button163099) {
            rowNum = 16;
            colNum = 30;
            minesCount = 99;
        }        
        if (e.getSource() == buttonDone && minesCount == 10) {
            GameFrame gameFrame = new GameFrame(rowNum, colNum, minesCount);
            this.dispose();
        }
        if (e.getSource() == buttonDone && minesCount == 40) {
            GameFrame gameFrame = new GameFrame(rowNum, colNum, minesCount);
            gameFrame.setSize(622, 622);
            this.dispose();
        }
        if (e.getSource() == buttonDone && minesCount == 99) {
            GameFrame gameFrame = new GameFrame(rowNum, colNum, minesCount);
            gameFrame.setSize(900, 600);
            this.dispose();
        }   
    }
}
