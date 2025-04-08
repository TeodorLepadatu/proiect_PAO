# KARDS - EUROPEAN THEATER

A turn-based card battle game implemented in Java, where players use cards with unique abilities to attack, heal, and strategize their way to victory. The game supports both human and AI players with varying difficulty levels.

## Features

- **Game Modes**:
  - Player vs Player (PvP)
  - Player vs AI (PvAI)
  - AI vs AI (AIVsAI)
- **AI Difficulty Levels**: Easy, Medium, Hard, and Impossible.
- **Card Types**:
  - **Attacking Cards**: Deal damage to opponent's cards.
  - **Healing Cards**: Restore health to friendly cards.
- **Dynamic Gameplay**: Players take turns playing cards, with a limited number of moves per turn.

## How to Play

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/card-battle-game.git
   cd card-battle-game
   ```

2. Compile the game:
   ```bash
   javac src/*.java
   ```

3. Run the game:
   ```bash
   java -cp src Main
   ```

4. Follow the on-screen instructions to select the game mode and difficulty level.

## Compile the Game

To compile the game, run the following command:

```bash
javac src/*.java
```

## Run the Game

To run the game, execute the following command:

```bash
java -cp src Main
```

Follow the on-screen instructions to select the game mode and difficulty level.

## Game Rules

- Each player starts with a hand of cards.
- Players take turns playing cards, with a limited number of moves per turn.
- **Attacking Cards**: Target opponent's cards to reduce their health.
- **Healing Cards**: Target friendly cards to restore their health.
- The game ends when one player's cards are all defeated.

## Project Structure

```
src/
├── Main.java          # Entry point of the game
├── GameEngine.java    # Core game logic
├── GenericPlayer.java # Base class for players
├── HumanPlayer.java   # Human player implementation
├── AIPlayer.java      # AI player implementation
├── Card.java          # Base class for cards
├── AttackingCard.java # Attacking card implementation
├── HealingCard.java   # Healing card implementation
```

## Requirements

- Java 17 or higher

## Contributing

Contributions are welcome! Feel free to open issues or submit pull requests to improve the game.

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.
```
