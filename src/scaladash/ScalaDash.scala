package scaladash
import scala.util.{Try, Failure, Success}
import scala.util.control.Breaks._
import scala.io.Source
import java.lang.Thread
import java.io.{PrintWriter, File}

/**
 * Created by marcinkossakowski on 6/2/15.
 *
 * Here we initialize the game. Level must be provided through input arg.
 * example: scala com.mkoss.scaladash.ScalaDash ../../../resource/level.txt
 */
object ScalaDash extends App {
    val outFile: String = "out.txt"
    val inputFileName: String = args(0)
    val maybeActionSeqenceFile: Option[String] = Try(args(1)).toOption

    val game = Game(inputFileName)
    game.start()
    
    println()
    println("### Welcome to ASCII-DASH ###")
    println("Use keys: w - up, s - down, a - left, d - right, p - stay in place, x - exit game")
    println("*Press ENTER to continue*")
    println()
    print(game)

    /**
     * Render scene based on single move.
     */
    def renderScene(inputAction: Int): Unit = {
        val directionActions: String = inputAction match {
            case 97 => "left"               // a
            case 100 => "right"             // d
            case 119 => "up"                // w
            case 115 => "down"              // s
            case 112 => "stay"              // p
            case _ => ""
        }

        val otherActions: String = inputAction match {
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

    /**
     * Ask user to enter next move.
     * Record all moves to a file.
     */
    def userMoveInput = {
        val out = new PrintWriter(new File(outFile))

        breakable {
            while(true) {
                val c: Int = Console.in.read()
                out.write(c.toChar.toString)
                renderScene(c)
            }    
        }
        
        println("Writing moves and exiting.")
        out.close
    }

    /**
     * Execute inputs.
     */ 
    maybeActionSeqenceFile match {
        case Some(fileName) => {
            for ( line <- Source.fromFile(fileName).getLines() ) {
                renderScene(line(0).toInt) // line here should be single character
                Thread.sleep(250)
            }
        }
        case None => userMoveInput
    }

}
