# Snoopy
![Build status](https://api.travis-ci.org/sweepyoface/Snoopy.svg?branch=master)
[![Spigot](https://img.shields.io/badge/Spigot-Project%20Page-yellow.svg)]()
[![JDK](https://img.shields.io/badge/JDK-1.8-blue.svg)](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html)
![License](https://img.shields.io/github/license/sweepyoface/ItemToken.svg)

Snoopy is a plugin that notifies staff members when certain blocks are mined. It is mostly useful for detecting people using X-ray mods.

By default, anyone with the `snoopy.getnotified` permission will be sent a message similar to the following:
![Message screenshot](https://sweepyoface.github.io/Snoopy/message.png)

It will also check for blocks that have been placed, and they won't be counted.

# Commands
| Command | Permission | Arguments | Description
| --- | --- | --- | --- |
| `/snoopy help (alias: /snoopy h)` | snoopy.help | N/A | Prints the Snoopy help. |
| `/snoopy reload` (alias: /snoopy rl) | snoopy.reload | N/A | Reloads the configuration file. |
| `/snoopy version` (alias: /snoopy ver) | snoopy.version | N/A | Prints the Snoopy version. |
| N/A | snoopy.getnotified | N/A | The permission to be notified when a block is mined. |

# Downloading
You can download the latest build from [Jenkins](https://ci.amberfall.science/job/Snoopy/).

# Building
1. Install [Apache Maven](https://maven.apache.org/).
2. Clone this repository.
3. Run `mvn clean package`.
4. The compiled jar will be in the `target` directory.