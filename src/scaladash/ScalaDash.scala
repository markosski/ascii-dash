package scaladash
import scala.util.{Try, Failure, Success}
import scala.util.control.Breaks._

/**
 * Created by marcinkossakowski on 6/2/15.
 *
 * Here we initialize the game. Level must be provided through input arg.
 * example: scala com.mkoss.scaladash.ScalaDash ../../../resource/level.txt
 */
object ScalaDash extends App {
    val inputFileName = args(0)
    val game = Game(inputFileName)
    game.start()
    println()
    println("### Welcome to SCALA-DASH ###")
    println("Use keys: w - up, s - down, a - left, d - right, p - stay in place, x - exit game")
    println("*Press ENTER to continue*")
    println()
    print(game)

    while(true) {
        val c: Int = Console.in.read()
        val directionActions: String = c match {
            case 97 => "left"               // a
            case 100 => "right"             // d
            case 119 => "up"                // w
            case 115 => "down"              // s
            case 112 => "stay"              // p
            case _ => ""
        }

        val otherActions: String = c match {
            case 120 => "quit"              // x
            case _ => ""
        }

        if (otherActions == "quit") {
            println("You exit the game"); break
        }

        if (directionActions != "") {
            val player = Try(game.getPlayer) match {
                case Success(p) => p
                case Failure(e) => println(e.getMessage); break
            }

            player.moves = List(directionActions)

            val tryGame = Try(game.frame())
            tryGame match {
                case Success(_) => {
                    print("\033[2J") // This should clear the screen in terminal
                    println("Score: " + player.score + ", Treasure left: " + game.treasure)
                    print(game)
                    println()
                }
                case Failure(e) => println(e); break
            }
        }
    }
}
