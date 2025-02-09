# EmrageTimer

EmrageTimer is a Minecraft plugin that provides a customizable timer.

## Installation

1. Download the latest release from the [releases page](https://github.com/EmrageGHC/EmrageTimer/releases).
2. Place the downloaded JAR file in your server's `plugins` directory.
3. Restart your server.

## Usage

Use the `/timer` command to control the timer. Available subcommands include `pause`, `resume`, `set`, `gradient`, `show`, `stopOnDeath`, `stopOnEmpty`, `saveProfile`, `loadProfile`, and `direction`.

### Commands

- `/timer pause`: Pauses the timer.
- `/timer resume`: Resumes the timer.
- `/timer set <seconds>`: Sets the timer to the specified number of seconds.
- `/timer gradient <color1> <color2>`: Sets the gradient colors.
- `/timer show <true|false>`: Shows or hides the timer.
- `/timer stopOnDeath <true|false>`: Stops the timer on player death.
- `/timer stopOnEmpty <true|false>`: Stops the timer when no players are online.
- `/timer saveProfile <name>`: Saves the current timer settings as a profile.
- `/timer loadProfile <name>`: Loads a saved profile.
- `/timer direction <forwards|backwards>`: Sets the timer direction.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.