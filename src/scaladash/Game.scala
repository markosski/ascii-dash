package scaladash

import scala.io.Source
import scala.util.{Try, Failure, Success}
import scala.collection.mutable.Set

/**
 * Created by marcinkossakowski on 6/2/15.
 *
 * ascii colors http://www.scala-lang.org/api/2.5.1/scala/Console$object.html
 *
 */
class Game(height: Int, width: Int, boardString: String) {
    var board: Array[Thing] = new Array(width*height)
    var size = 0
    var playerPosition = 0
    var treasure = 0
    var moves = 0

    def start(): Unit = {
        for ((char, i) <- boardString.zipWithIndex) {
            val thing: Thing = char.toString match {
                case "#" => new Turf()
                case " " => new Space()
                case "+" => treasure += 1; new Diamond()
                case "O" => new Boulder()
                case "*" => playerPosition = i; new Player()
                case "-" => new HWall()
                case "|" => new VWall()
                case "x" => new PsychoKiller()
                case s: String => throw new Exception("Illegal character: " + s)
            }
            board(i) = thing
        }
        size = board.length
        println("Game of length " + size +  " items has been initialized")
    }

    def getPlayer(): Player = {
        return board(playerPosition) match {
            case player: Player => player
            case _ => throw new Exception("Player died.")
        }
    }

    def move(position: Int, direction: String): Int = {
         return direction match {
             case "up" => position - width
             case "down" => position + width
             case "left" => position - 1
             case "right" => position + 1
             case "stay" => position
         }
    }

    def frame(): Unit = {
        val set: Set[Thing] = Set()

        for (i <- 0 to size-1) {
            val thing: Thing = board(i)

            if (!set.contains(thing)) {
                set += thing
                thing match {
                    case thing: CanMove => {
                        val newPosition: Int = move(i, thing.nextMove)
                        val consumedItem: Try[Thing] = Try(thing.consume(board(newPosition)))

                        if (consumedItem.isSuccess) {
                            thing match {
                                case x: Player => playerPosition = newPosition
                                case x: Thing => None
                            }
                            board(newPosition) match {
                                case x: Diamond => treasure -= 1
                                case x: Thing => None
                            }
                            board(i) = consumedItem.get
                            board(newPosition) = thing
                        }
                    }
                    case _ => None
                }
            }
        }
        if (treasure == 0) throw new GameOver()
    }

    override def toString(): String = {
        var out = ""
        for ((thing, i) <- board.zipWithIndex) {
            if ((i+1) % width == 0) out += thing + "\n"
            else out += thing
        }
        out
    }
}

object Game {
    def apply(fileName: String): Game = {
        var boardString = ""
        var dim: List[Int] = List(0, 0)

        for ((line, i) <- Source.fromFile(fileName).getLines().zipWithIndex) {
            if (i == 0) {
                dim = List(line.split(" ")(0).toInt, line.split(" ")(1).toInt)
            } else {
                boardString += line
            }
        }
        return new Game(dim(0), dim(1), boardString)
    }
}

