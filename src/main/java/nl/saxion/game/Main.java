package nl.saxion.game;

import nl.saxion.game.game.entities.Score;
import nl.saxion.game.game.screens.WorldMap;
import nl.saxion.game.game.screens.MainMenuScreen;
import nl.saxion.game.game.screens.GameOverScreen;
import nl.saxion.gameapp.GameApp;



public class Main {
    public static void main(String[] args) {
        // Add screens

        GameApp.addScreen("MainMenuScreen", new MainMenuScreen());
        GameApp.addScreen("WorldScreen", new WorldMap());
        GameApp.addScreen("GameOverScreen", new GameOverScreen());


        // Start game loop and show main menu screen
        GameApp.start("Bob Ross Strikes Back 2", 800, 450, 60, true, "MainMenuScreen");
    }
}
