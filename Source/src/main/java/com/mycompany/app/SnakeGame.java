package com.mycompany.app;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    int boardWidth;
    int boardHeight;
    int tileSize = 25;

    //snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    //food
    Tile food;
    Random random;

    //game logic
    int velocityX;
    int velocityY;
    Timer gameLoop;

    boolean gameOver = false;

    int speedUpIncrement = 100; //score increment for speeding up the game
    int initialDelay = 100;
    int highScore;
    private Image snakeHeadImage;
    private Image snakeBodyImage;
    private Image backgroundImage;
    private Image foodImage;
    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<>();

        snakeHeadImage = new ImageIcon("C:\\Users\\JLNQo\\Desktop\\SnakeHead.png").getImage();
        snakeBodyImage = new ImageIcon("C:\\Users\\JLNQo\\Desktop\\SnakeBody.png").getImage();
        backgroundImage = new ImageIcon("C:\\Users\\JLNQo\\Desktop\\BackDrop.png").getImage();
        foodImage = new ImageIcon("C:\\Users\\JLNQo\\Desktop\\FOOD.gif").getImage();

        food = new Tile(10, 10);
        random = new Random();
        placeFood();

        velocityX = 1;
        velocityY = 0;

        //game timer
        gameLoop = new Timer(initialDelay, this);
        gameLoop.start();

    }




    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        draw(g);
    }

    public void draw(Graphics g) {
        //Food
        g.setColor(Color.red);
        g.drawImage(foodImage, food.x*tileSize, food.y*tileSize, tileSize, tileSize, this);

        //Snake Head
        g.setColor(Color.green);
        g.drawImage(snakeHeadImage, snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, this);

        //Snake Body
        for (Tile snakePart : snakeBody) {
            g.drawImage(snakeBodyImage, snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, this);
        }



        //Score

        g.setFont(new Font("Arial", Font.BOLD, 22));
        if (gameOver) {
            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("GAME OVER! " , tileSize*7-35, tileSize*8);
            g.setFont(new Font("Arial", Font.BOLD, 22));
            g.setColor(Color.white);
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize*10-0, tileSize*12);

            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString(" [ Press Space to Try Again ] ", tileSize*7-1,tileSize*20);

            g.setColor(Color.yellow);
            g.setFont(new Font("Arial", Font.BOLD, 22));
            g.drawString("High Score: " + highScore, tileSize*9-0, tileSize*13);
        }
        else {
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize*2, tileSize*3);
        }
    }


    public void placeFood(){
        food.x = random.nextInt(boardWidth/tileSize);
        food.y = random.nextInt(boardHeight/tileSize);
    }

    public void move() {
        //eat food
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        //move snake body
        for (int i = snakeBody.size()-1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) { //right before the head
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else {
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }
        //move snake head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        //game over conditions
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);


            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }

        if (snakeHead.x*tileSize < 0 || snakeHead.x*tileSize > boardWidth || //passed left border or right border
                snakeHead.y*tileSize < 0 || snakeHead.y*tileSize > boardHeight ) { //passed top border or bottom border
            gameOver = true;
        }
    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();

        if (snakeBody.size() > highScore) {
            highScore = snakeBody.size();
        }

        if (snakeBody.size() == 5) {
            speedUpGame();
        }
        if (snakeBody.size() == 10) {
            speedUpGame();
        }

        if (gameOver) {
            repaint();
            gameLoop.stop();
        }
    }

    private void speedUpGame() {
        int newDelay = gameLoop.getDelay() - 10;
        gameLoop.setDelay(Math.max(newDelay, 50));
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver && e.getKeyCode() == KeyEvent.VK_SPACE) {
            // Reset the game
            newGame();
        } else {
            // Handle arrow keys
            if (e.getKeyCode() == KeyEvent.VK_W && velocityY != 1) {
                velocityX = 0;
                velocityY = -1;
            } else if (e.getKeyCode() == KeyEvent.VK_S && velocityY != -1) {
                velocityX = 0;
                velocityY = 1;
            } else if (e.getKeyCode() == KeyEvent.VK_A && velocityX != 1) {
                velocityX = -1;
                velocityY = 0;
            } else if (e.getKeyCode() == KeyEvent.VK_D && velocityX != -1) {
                velocityX = 1;
                velocityY = 0;
            }
        }
    }

    private void newGame() {
        snakeHead = new Tile(5, 5);
        snakeBody.clear();
        placeFood();
        velocityX = 1;
        velocityY = 0;
        gameOver = false;

        gameLoop.restart();


    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}








}