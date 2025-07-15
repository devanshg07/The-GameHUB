# GameHub: JavaFX Minigame Collection

A desktop application featuring four classic minigames—SnakeBall, FlappyBall, CatchyBalls, and BallPong—bundled in a modern JavaFX interface with sound effects and a login system.

## 🎯 How It Works

### Architecture
- **Frontend & Backend**: JavaFX desktop application (single codebase)
- **UI**: JavaFX Scenes, custom styling, and layout
- **Sound**: Java Sound API for effects and background music
- **Assets**: WAV audio, JPEG/JPG images, FXML sample

### Process Flow
1. User launches the application (run `LoginApp`)
2. Login screen appears (username + password: `12345`)
3. On successful login, user is taken to the GameHub main menu
4. User selects one of four minigames:
    - SnakeBall
    - FlappyBall
    - CatchyBalls
    - BallPong
5. Each game launches in its own window with unique controls and scoring
6. Sound effects and music play throughout the experience

## 🛠️ Tools & Technologies Used

- **Java 8+**
- **JavaFX** - UI framework for desktop apps
- **Java Sound API** - For WAV playback
- **FXML** (sample only) - Declarative UI (not required to run)
- **Custom assets** - WAV, JPEG, JPG

## 🚀 How to Run

### Prerequisites
- Java 8 or higher (with JavaFX libraries)
- JavaFX SDK (if not bundled with your JDK)

### 1. Clone/Download the Project
```bash
git clone <your-repo-url>
cd The-GameHUB
```

### 2. Compile the Source Code
```bash
javac -d bin src/*.java
```

### 3. Run the Application
```bash
java -cp bin;src;path/to/javafx-sdk/lib/* LoginApp
```
Replace `path/to/javafx-sdk/lib/*` with your JavaFX SDK path if needed.

## 📁 Project Structure
```
The-GameHUB/
├── src/                  # Java source code
│   ├── LoginApp.java     # Entry point, login/authentication
│   ├── GameHub.java      # Main menu hub
│   ├── SnakeBall.java    # Snake minigame
│   ├── FlappyBall.java   # Flappy Bird-style minigame
│   ├── CatchyBalls.java  # Reflex/clicker minigame
│   ├── BallPong.java     # Pong minigame
│   ├── SoundManager.java # Sound/music manager
│   ├── Balls.java        # Ball base class
│   ├── BlueBall.java     # Blue ball logic
│   ├── ColoredBall.java  # Colored ball logic
│   └── Rectangle.java    # Rectangle/pipe logic
├── bin/                  # Compiled .class files
├── kahoot.wav            # Login music
├── venture.wav           # Main menu/game music
├── heheheha.wav          # Success sound
├── downer_noise.wav      # Failure sound
├── balloon-bop-medium-228519.wav # Click sound
├── silence.wav           # (Unused/optional)
├── sigma.jpeg            # Game over/funny image
├── gradient.jpg          # (Unused/optional)
├── sample.fxml           # Sample FXML layout (not required)
└── README.md             # This file
```

## 🎮 Features

### User Interface
- **Modern JavaFX UI**: Custom-styled scenes, buttons, and backgrounds
- **Login System**: Username + password ("12345")
- **Main Menu**: 2x2 grid of game buttons, color-coded
- **Exit buttons**: Always accessible

### Minigames
- **SnakeBall**: Classic snake game with WASD controls, eat apples, grow, avoid walls/self
- **FlappyBall**: Flappy Bird-style, click to jump, avoid pipes, score increases per pipe
- **CatchyBalls**: Reflex game, click colored balls, avoid missing, score and miss counter
- **BallPong**: Pong for two players, keyboard controls (W/S and Up/Down), score tracking

### Sound & Visuals
- **Background Music**: Plays in login and main menu
- **Sound Effects**: Success, failure, and click sounds
- **Game Over Images**: Fun image on loss in some games
- **Colorful Themes**: Each game has a unique color scheme

## 🔧 Customization

- **Add More Games**: Extend `GameHub.java` and add new JavaFX Application classes
- **Change Assets**: Replace WAV/JPEG files for new sounds/images
- **UI Styling**: Edit inline styles in Java code or use FXML/CSS

## 🐛 Troubleshooting

- **JavaFX Not Found**: Ensure JavaFX SDK is on your classpath
- **No Sound**: Check your system's audio and file paths
- **Login Fails**: Password is hardcoded as `12345`
- **Game Window Too Small**: All games use 500x400px by default

## 🤝 Contributing
Pull requests and suggestions welcome! Add new games, improve UI, or refactor code.

## 📄 License
This project is open source and available under the MIT License.
