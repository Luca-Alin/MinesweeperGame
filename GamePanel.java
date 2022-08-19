import java.awt.*;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.event.MouseInputListener;

import java.awt.event.*;
import java.util.*;

/* The "GamePanel" holds everything needed for the game to work
 *
 *                              THE GRAPHICAL USER INTERFACE
 * 
 * It has two main components: a big panel with labels and buttons and a smaller panel that holds:
 *      - a counter (the first number shown on this counter is the number of mines, adding a flag 
 * will decrease the number by 1)
 *      - a restart button
 *      - a timer, that counts the seconds that have passed since the game started
 * The bigger panel has an array of label, and a label contains:
 *      - text, which may be an empty string ("") or a number, from 1 to 8, according to the number of 
 * neighboring mines
 *      - may have an image with a bomb, check the "mineGenerator()" function documentation
 *      - each label has a button, that hides the text or the image
 * The button: 
 *      - if you left click a button, it will disappear
 *      - if you right click a button, it's image will change to a red flag, and the button will not be
 * clickable. Right clicking the button again will reverse this
 * 
 *                                      THE FUNCTIONS
 *      
 *  mineGenerator() - it is used to generate the random locations of the mines
 *                  - it's triggered on the player's first click on one of the buttons
 *                  - a HashMap ("noMinesLocations") is used to store the locations where there shouldn't be
 * any mine (when the player clicks the first button, there shouldn't be any mine above that button, under, 
 * right, left...)
 *                  - an ArrayList ("putMinesHere") that stores the locations of the mines
 *                  - in order not to asign to mines to the same square, after a mine is placed in the 
 * ArrayList it is also placed in the HashMap
 *                  - after asigning the mines, a for-loop checks the entire map, counts how many mines
 * neighbor each square, and asigns a number to that square. The squares with mines aren't asigned a number
 *  
 *  mapcleaner() - it's purpose is to "clean" (remove buttons that cover labels) the "blank" squares 
 * (squares without numbers or mines)
 *               - it's triggered every time the player clicks a blank square
 *               - it's a recursive function, if the square is blank then it's button is removed, then it
 * removes the buttons of the neighbors utill a border of numbers is created
 * 
 * gameOver() - it's triggered if the player clicks a mine
 *            - the player won't be able to click anything else
 * 
 * winCheck() - it's triggered if the player wins
 *   
 */

public class GamePanel extends JPanel implements MouseInputListener {

    GrayPanel infoPanel;
    Border grayBorder = BorderFactory.createLineBorder(Color.gray);

    JLabel flagsPlaced;
    int flagsPlacedNumber;

    JButton restartButton;
    ImageIcon happyFace = new ImageIcon("Images/happyFace.png");
    ImageIcon gameWon = new ImageIcon("Images/gameWonFace.png");
    ImageIcon gameOver = new ImageIcon("Images/gameOverFace.png");
    ImageIcon scaredFace = new ImageIcon("Images/scaredFace.png");

    ImageIcon numberOne = new ImageIcon("Images/numberOne.png");
    ImageIcon numberTwo = new ImageIcon("Images/numberTwo.png");
    ImageIcon numberThree = new ImageIcon("Images/numberThree.png");
    ImageIcon numberFour = new ImageIcon("Images/numberFour.png");
    ImageIcon numberFive = new ImageIcon("Images/numberFive.png");
    ImageIcon numberSix = new ImageIcon("Images/numberSix.png");
    ImageIcon numberSeven = new ImageIcon("Images/numberSeven.png");
    ImageIcon numberEight = new ImageIcon("Images/numberEight.png");
    
    JLabel elapsedTimeLabel;
    int elapsedTime = 0;
    Timer timer;

    JLabel[] label;
    ImageIcon bombImage = new ImageIcon("Images/bomb.png");
    ImageIcon clickedBombImage = new ImageIcon("Images/clickedBomb.png");
    JButton[] button;
    boolean[] buttonWasFlagged;
    ImageIcon buttonImage = new ImageIcon("Images\\defaultButton.png");
    ImageIcon flagImage = new ImageIcon("Images\\markedTile.png");




    int rowNum;
    int colNum;
    int minesCount;
    GamePanel(int rowNum, int colNum, int minesCount) {
        this.rowNum = rowNum;
        this.colNum = colNum;
        this.minesCount = minesCount;
        this.setBackground(Color.gray);
        this.setLayout(new GridLayout(rowNum, colNum));
        this.setBorder(BorderFactory.createBevelBorder(1));

        flagsPlaced = new JLabel("", SwingConstants.CENTER);
        flagsPlacedNumber = minesCount;
        flagsPlaced.setText(flagsPlacedNumber + "");
        flagsPlaced.setFont(new Font("Arial", Font.BOLD, 20));
        flagsPlaced.setForeground(Color.red);
        flagsPlaced.setBackground(Color.black);
        flagsPlaced.setOpaque(true);
        flagsPlaced.setBorder(BorderFactory.createLineBorder(Color.gray, 2));

        restartButton = new JButton();
        restartButton.addMouseListener(this);
        restartButton.setFocusable(false);
        restartButton.setBorder(grayBorder);
        restartButton.setIcon(happyFace);
        restartButton.setBackground(new java.awt.Color(75, 75, 75));
        
        
        elapsedTimeLabel = new JLabel("", SwingConstants.CENTER);
        elapsedTimeLabel.setBackground(Color.black);
        elapsedTimeLabel.setOpaque(true);
        elapsedTimeLabel.setForeground(Color.red);
        elapsedTimeLabel.setBorder(BorderFactory.createLineBorder(Color.gray, 2));
        elapsedTimeLabel.setText(elapsedTime + "");
        elapsedTimeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                elapsedTime += 1000;
                elapsedTimeLabel.setText(elapsedTime / 1000 + "");

            }
        });

        infoPanel = new GrayPanel();
        infoPanel.setLayout(new GridLayout(1, 3));
        infoPanel.add(flagsPlaced);
        infoPanel.setBorder(grayBorder);
        infoPanel.add(restartButton);
        infoPanel.add(elapsedTimeLabel);


        buttonWasFlagged = new boolean[rowNum * colNum];
        for (int i = 0; i < rowNum * colNum; i++) {
            buttonWasFlagged[i] = false;
        }
        
        label = new JLabel[rowNum * colNum];
        button = new JButton[rowNum * colNum];
        for (int i = 0; i < rowNum * colNum; i++) {
            button[i] = new JButton();
            button[i].setBackground(new java.awt.Color(150, 150, 150));
            button[i].addMouseListener(this);
            label[i] = new JLabel("", SwingConstants.CENTER);
            label[i].setFont(new Font("Arial", Font.PLAIN, 0));
            label[i].setLayout(new GridLayout(1, 1));
            label[i].add(button[i]);
            label[i].setOpaque(true);
            label[i].setBackground(Color.gray);
            label[i].setBorder(BorderFactory.createBevelBorder(1));
            this.add(label[i]);
        }
    }

    
    
    HashMap<Integer, Integer> noMinesLocations;
    int noMinesLocationsIndex = 0;
    ArrayList<Integer> putMinesHere;
    void MineGenerator(int firstClick) {

        noMinesLocations = new HashMap<>();
        noMinesLocations.put((firstClick - (colNum + 1)), noMinesLocationsIndex++);
        noMinesLocations.put((firstClick - colNum), noMinesLocationsIndex++);
        noMinesLocations.put((firstClick - colNum + 1), noMinesLocationsIndex++);
        noMinesLocations.put((firstClick - 1), noMinesLocationsIndex++);
        noMinesLocations.put(firstClick, noMinesLocationsIndex++);
        noMinesLocations.put((firstClick + 1), noMinesLocationsIndex++);
        noMinesLocations.put((firstClick + (colNum - 1)), noMinesLocationsIndex++);
        noMinesLocations.put((firstClick + colNum), noMinesLocationsIndex++);
        noMinesLocations.put((firstClick + colNum + 1), noMinesLocationsIndex++);


        putMinesHere = new ArrayList<>();
        Random random = new Random();
        int minesIndex = 0;
        while (minesIndex != minesCount) {
            int randomMine = Math.abs(random.nextInt()) % (rowNum * colNum);
            if (!(noMinesLocations.containsKey(randomMine))) {
                noMinesLocations.put(randomMine, noMinesLocationsIndex++);
                putMinesHere.add(randomMine);
                minesIndex++;
            }
        }

        for (int i = 0; i < putMinesHere.size(); i++) {
            label[putMinesHere.get(i)].setIcon(bombImage);
        }

        // the code bellow will add numbers around the mines
        int neighbouringMinesCount = 0;
        for (int i = 0; i < rowNum * colNum; i++) {
            neighbouringMinesCount = 0;
            //checks the top left square
            if (i == 0 && !(putMinesHere.contains(i))) {
                if (putMinesHere.contains(i + 1)) {
                    neighbouringMinesCount++;
                }
                if (putMinesHere.contains(i + colNum)) {
                    neighbouringMinesCount++;
                }
                if (putMinesHere.contains(i + colNum + 1)) {
                    neighbouringMinesCount++;
                }
                //checks the top right square
            } else if (i == colNum - 1 && !(putMinesHere.contains(i))) {
                if (putMinesHere.contains(i - 1)) {
                    neighbouringMinesCount++;
                }
                if (putMinesHere.contains(i + (colNum - 1))) {
                    neighbouringMinesCount++;
                }
                if (putMinesHere.contains(i + colNum)) {
                    neighbouringMinesCount++;
                }
                //checks the low left square
            } else if (i == (rowNum * colNum) - colNum && !(putMinesHere.contains(i))) {
                if (putMinesHere.contains(i - colNum)) {
                    neighbouringMinesCount++;
                }
                if (putMinesHere.contains(i - colNum + 1)) {
                    neighbouringMinesCount++;
                }
                if (putMinesHere.contains(i + 1)) {
                    neighbouringMinesCount++;
                }
                //checks the low right square
            } else if (i == (rowNum * colNum) - 1 && !(putMinesHere.contains(i))) {
                if (putMinesHere.contains(i - (colNum + 1))) {
                    neighbouringMinesCount++;
                }
                if (putMinesHere.contains(i - colNum)) {
                    neighbouringMinesCount++;
                }
                if (putMinesHere.contains(i - 1)) {
                    neighbouringMinesCount++;
                }
                //checks every left square, except top and bottom
            } else if (i % colNum == 0 && !(putMinesHere.contains(i))) {
                if (putMinesHere.contains(i - colNum)) {
                    neighbouringMinesCount++;
                }
                if (putMinesHere.contains(i - colNum + 1)) {
                    neighbouringMinesCount++;
                }
                if (putMinesHere.contains(i + 1)) {
                    neighbouringMinesCount++;
                }
                if (putMinesHere.contains(i + colNum)) {
                    neighbouringMinesCount++;
                }
                if (putMinesHere.contains(i + colNum + 1)) {
                    neighbouringMinesCount++;
                }
            //checks every right square, except top and bottom
            } else if ((i + 1) % colNum == 0 && !(putMinesHere.contains(i))) {
                if (putMinesHere.contains(i - (colNum + 1))) {
                    neighbouringMinesCount++;
                }
                if (putMinesHere.contains(i - colNum)) {
                    neighbouringMinesCount++;
                }
                if (putMinesHere.contains(i - 1)) {
                    neighbouringMinesCount++;
                }
                if (putMinesHere.contains(i + (colNum - 1))) {
                    neighbouringMinesCount++;
                }
                if (putMinesHere.contains(i + colNum)) {
                    neighbouringMinesCount++;
                }
                //checks the rest of squares
            } else if (!(putMinesHere.contains(i))) {
                if (putMinesHere.contains(i - (colNum + 1))) {
                    neighbouringMinesCount++;
                }
                if (putMinesHere.contains(i - colNum)) {
                    neighbouringMinesCount++;
                }
                if (putMinesHere.contains(i - colNum + 1)) {
                    neighbouringMinesCount++;
                }
                if (putMinesHere.contains(i - 1)) {
                    neighbouringMinesCount++;
                }
                if (putMinesHere.contains(i + 1)) {
                    neighbouringMinesCount++;
                }
                if (putMinesHere.contains(i + (colNum - 1))) {
                    neighbouringMinesCount++;
                }
                if (putMinesHere.contains(i + colNum)) {
                    neighbouringMinesCount++;
                }
                if (putMinesHere.contains(i + colNum + 1)) {
                    neighbouringMinesCount++;
                }
            }

            if (neighbouringMinesCount != 0) {
                label[i].setText(neighbouringMinesCount + "");
                switch (neighbouringMinesCount) {
                    case 1:
                        label[i].setIcon(numberOne);
                        break;
                    case 2:
                        label[i].setIcon(numberTwo);
                        break;
                    case 3:
                        label[i].setIcon(numberThree);
                        break;
                    case 4:
                        label[i].setIcon(numberFour);
                        break;
                    case 5:
                        label[i].setIcon(numberFive);
                        break;
                    case 6:
                        label[i].setIcon(numberSix);
                        break;
                    case 7:
                        label[i].setIcon(numberSeven);
                        break;
                    case 8:
                        label[i].setIcon(numberEight);
                        break;
                }
            }
        }
    }


    int mapCleaner(int i) {
        if (!buttonWasFlagged[i]) {
            if (button[i].isVisible() == true) {
                button[i].setVisible(false);
            }
            if (label[i].getText() != "") {
                return 0;
            }
            // left
            if (i % colNum != 0 && button[i - 1].isVisible()) {
                mapCleaner(i - 1);
            }
            // right
            if ((i + 1) % colNum != 0 && button[i + 1].isVisible()) {
                mapCleaner(i + 1);
            }
            // low
            if (i + colNum <= rowNum * colNum - 1 && button[i + colNum].isVisible()) {
                mapCleaner(i + colNum);
            }
            // up
            if (i - colNum >= 0 && button[i - colNum].isVisible()) {
                mapCleaner(i - colNum);
            }
            // lowLeft
            if (((i + colNum - 1) + 1) % colNum != 0 && (i + colNum - 1) + 1 <= rowNum * colNum - 1
                    && button[i + colNum - 1].isVisible()) {
                mapCleaner(i + colNum - 1);
            }
    
            // upLeft
            if (i - colNum - 1 >= 0 && ((i - colNum - 1) + 1) % colNum != 0 && button[i - colNum - 1].isVisible()) {
                mapCleaner(i - colNum - 1);
            }
            // lowRight
            if ((i + colNum + 1) % colNum != 0 && i + colNum + 1 <= rowNum * colNum - 1
                    && button[i + colNum + 1].isVisible()) {
                mapCleaner(i + colNum + 1);
            }
            // upRight
            if ((i - colNum + 1) % colNum != 0 && i - colNum + 1 >= 0 && button[i - colNum + 1].isVisible()) {
                mapCleaner(i - colNum + 1);
            }
        }
        return 1;
    }

    // triggers if the player clicks on a mine
    boolean gameOverCheck = false;
    void gameOver() {
        timer.stop();
        for (int i = 0; i < putMinesHere.size(); i++) {
            button[putMinesHere.get(i)].setVisible(false);
            gameOverCheck = true;
        }
        restartButton.setIcon(gameOver);
    }

    boolean winCheck() {
        int k = 0;
        for (int i = 0; i < rowNum * colNum; i++) {
            if (!button[i].isVisible()) {
                k++;
            }
        }
        if (k == rowNum * colNum - minesCount) {
            timer.stop();
            gameOverCheck = true;
            restartButton.setIcon(gameWon);
            return true;
        }
        return false;
    }


    boolean firstSquareWasNotCliked = true;
    @Override
    public void mouseClicked(MouseEvent e) {
        if (!gameOverCheck && SwingUtilities.isLeftMouseButton(e)) {
            for (int i = 0; i < rowNum * colNum; i++) {
                if (e.getSource() == button[i] && buttonWasFlagged[i] == false) {
                    button[i].setVisible(false);
                    if (firstSquareWasNotCliked) {
                        MineGenerator(i);
                        firstSquareWasNotCliked = false;
                        mapCleaner(i);
                        timer.start();
                    }
                    if (label[i].getText() == "") {
                        mapCleaner(i);
                    }
                    if (putMinesHere.contains(i)) {
                        label[i].setIcon(clickedBombImage);
                        gameOver();

                    }
                    if (winCheck()) {
                        restartButton.setText("Well Done");
                    }
                }

            }
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!gameOverCheck && SwingUtilities.isRightMouseButton(e)) {
            for (int i = 0; i < rowNum * colNum; i++) {
                if (e.getSource() == button[i]) {
                    if (buttonWasFlagged[i] == false) {
                        button[i].setIcon(flagImage);
                        buttonWasFlagged[i] = true;
                        flagsPlacedNumber -= 1;
                        flagsPlaced.setText(flagsPlacedNumber + "");
                    } else {
                        button[i].setIcon(buttonImage);
                        buttonWasFlagged[i] = false;
                        flagsPlacedNumber += 1;
                        flagsPlaced.setText(flagsPlacedNumber + "");
                    }
                }
            }
        }
        if (!gameOverCheck && SwingUtilities.isLeftMouseButton(e)) {
            for (int i = 0; i < rowNum * colNum; i++) {
                if (e.getSource() == button[i]) {
                    restartButton.setIcon(scaredFace);
                }
            }
        }


    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!gameOverCheck) {
            for (int i = 0; i < rowNum * colNum; i++) {
                if (e.getSource() == button[i]) {
                    restartButton.setIcon(happyFace);
                }
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub

    }

}
