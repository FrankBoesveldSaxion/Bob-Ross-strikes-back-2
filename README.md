# Bob-Ross Strikes Back 2

Description
Every human on earth is dead, all but one; the myth the legend: Bob Ross. He is the only human who can defeat the evil AI.

Objective
Survive as long as possible and score points by defeating enemies. The run ends when the player dies, the final score is shown on the Game Over screen.

Controls (default)
- Move: Arrow keys or W / A / S / D
- Shoot: Space
- Confirm / Menu select: Enter

Game flow
Main Menu → Play → World Map → In-game → On death → Game Over screen → Main menu etc..

Enemies and scoring
- EnemyDrone: fast, low health, lower score.
- EnemyRobot: slower, tougher, higher score.
Defeating enemies increases your score via the game's scoring system.

Where to change behavior
- Enemy config: `EnemyDroneConfig.java`, `EnemyRobotConfig.java`
- Spawn and difficulty: `DifficultySystem.java`
- Bob ross sprite: `SpriteConfig.java`

Build and run
- Build: click on elefant `Tasks -> application -> run`