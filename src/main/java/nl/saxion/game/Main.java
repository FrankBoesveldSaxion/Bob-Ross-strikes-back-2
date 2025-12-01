package nl.saxion.game;

import nl.saxion.game.game.screens.WorldMap;
import nl.saxion.game.game.screens.MainMenuScreen;
import nl.saxion.gameapp.GameApp;

public class Main {
    public static void main(String[] args) {
        // Add screens
        GameApp.addScreen("MainMenuScreen", new MainMenuScreen());
        GameApp.addScreen("WorldScreen", new WorldMap());


        // Start game loop and show main menu screen
        GameApp.start("Bob Ross Strikes Back 2", 800, 450, 60, false, "MainMenuScreen");
    }
}
