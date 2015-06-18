# scala-dash
ScalaDash is a game similar to famous Boulder Dash. Game is very simple and easy to extend with new levels and rules.
Rules of the game are following.
Player has to collect all the diamonds (+) to finish the level. There are guards (x) that can kill you if they run into you.
Player can also die from being crushed by a boulder (O). Guards can also get crushed by a falling boulder.
Elements in the game are:
- "*" - player
- "x" - guard
- "O" - boulder
- "#" - filler, will substract 1 form score
- "+" - diamond, will add 15 to score
- "|" - vertical wall
- "-" - horizontal wall

![Alt text](https://github.com/martez81/scala-dash/blob/master/resource/img/screenshot1.png)

## Requirements
- OSX or Linux, not sure about Windows
- Scala and JVM environment installed (tested on Scala 2.11.4)

## Install
- clone repo `git clone https://github.com/martez81/scala-dash.git`
- switch to scala-dash dir `cd scala-dash`
- create dir for compiled source `mkdir out` 
- compile `scalac src/scaladash/* -d out`
- run game `scala -cp out scaladash.ScalaDash resource/level.txt`
- enjoy
