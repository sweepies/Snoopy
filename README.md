![Snoopy](https://sweepyoface.github.io/Snoopy/logo.png)

![Build status](https://api.travis-ci.org/sweepyoface/Snoopy.svg?branch=master)
[![Spigot](https://img.shields.io/badge/Spigot-Project%20Page-yellow.svg)](https://www.spigotmc.org/resources/snoopy.43288/)
[![JDK](https://img.shields.io/badge/JDK-1.8-blue.svg)](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html)
![License](https://img.shields.io/github/license/sweepyoface/ItemToken.svg)

Snoopy is a plugin that notifies staff members when veins of ore are found. It is useful for detecting suspicious activity such as X-ray mods.

By default, anyone with the `snoopy.getnotified` permission will be sent a message like the following:
![Message screenshot](https://sweepyoface.github.io/Snoopy/message.png)

Blocks that are manually placed get flagged and do not trigger the message.

Every message in the plugin is configurable with templates. You could remove the prefix, location, and light level information if you wanted to configure it to be sent to everyone, for example.

# Commands
| Command | Aliases |Permission | Arguments | Description |
| --- | --- | --- | --- | --- |
| `help` | `h` | snoopy.help | None | Prints the Snoopy help. |
| `reload`| `rl` |  snoopy.reload | None | Reloads the configuration file. |
| `version`| `ver` | snoopy.version | None | Prints the Snoopy version. |

# Permissions
| Permission | Description |
| --- | --- |
| `snoopy.getnotified` | The permission to be notified when a vein is found. |

# Downloading
You can download the [latest version](https://github.com/sweepyoface/Snoopy/releases/latest) from the [releases page](https://github.com/sweepyoface/Snoopy/releases).

Development builds are available on [Jenkins](https://ci.sweepy.pw/job/Snoopy/). (currently unavailable)

# Compiling
1. Install [Apache Maven](https://maven.apache.org/).
2. Clone this repository.
3. Run `mvn clean package`.
4. The compiled jar will be in the `target` directory.
