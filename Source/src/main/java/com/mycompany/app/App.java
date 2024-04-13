package com.mycompany.app;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth = 600;

        showSplashScreen1();

        // Show the main menu
        int option = showMainMenu();

        if (option == JOptionPane.YES_OPTION) {
            // Start the game
            startGame(boardWidth);
        } else {
            // Quit the program
            System.exit(0);
        }

        // Show the splash screen after main menu
        Instructions();
    }

    private static void Instructions() {
        BufferedImage image = null;
        try {
            File imageFile = new File("C:\\Users\\JLNQo\\Desktop\\Inst.png");
            image = ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }


        ImageIcon icon = new ImageIcon(image);

        // Show splash screen with image
        JOptionPane.showMessageDialog(
                null,
                "",
                "The Worm Game! - Game Instruction",
                JOptionPane.INFORMATION_MESSAGE,
                icon // Pass the ImageIcon here
        );
    }


    private static void showSplashScreen1() {
        // Load image from a local file
        BufferedImage image = null;
        try {
            File imageFile = new File("C:\\Users\\JLNQo\\Desktop\\SplashScreen.png");
            image = ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ImageIcon icon = new ImageIcon(image);

        JOptionPane.showMessageDialog(
                null,
                "",
                "The Worm Game! V 1.1",
                JOptionPane.INFORMATION_MESSAGE,
                icon
        );
    }


    private static int showMainMenu() {
        //array of options
        String[] options = {"Proceed"};

        //main menu options
        int option = JOptionPane.showOptionDialog(
                null,
                "Welcome to The Worm Game!",
                "The Worm Game! V 1.1",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        return option;
    }

    private static void startGame(int boardWidth) {
        JFrame frame = new JFrame("The Worm Game! - V 1.1");
        frame.setVisible(true);
        frame.setSize(boardWidth, boardWidth);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SnakeGame snakeGame = new SnakeGame(boardWidth, boardWidth);
        frame.add(snakeGame);
        frame.pack();
        snakeGame.requestFocus();
    }
}
