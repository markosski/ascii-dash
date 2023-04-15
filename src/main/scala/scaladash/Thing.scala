package scaladash
import scala.util.Random
/**
 * Created by marcinkossakowski on 6/2/15.
 *
 */


trait Thing {
    override def toString(): String
}

trait CanMove {
    val allowableMoves = List("up", "down", "left", "right", "stay")
    def nextMove: String
    def consume(obj: Thing): Thing
}

trait HasValue {
    val points: Int
}

class Player extends Thing with CanMove {
    var score = 0
    var lives = 3
    var moves: List[String] = List()

    def nextMove(): String = {
        val move = moves.head
        moves = moves.tail
        return move
    }

    override def toString() = Console.GREEN + "*" + Console.RESET
    def consume(thing: Thing): Thing = {
        return thing match {
            case obj: Diamond => score += obj.points; new Space()
            case obj: Turf => score += obj.points; new Space()
            case obj: Space => score += obj.points; thing
            case _ => throw new IllegalMove
        }
    }
}

class PsychoKiller extends Thing with CanMove {
    def nextMove(): String = {
        return allowableMoves(Random.nextInt(5))
    }

    def consume(thing: Thing): Thing = {
        return thing match {
            case obj: Player => new Space
            case obj: Space => thing
            case _ => throw new IllegalMove
        }
    }

    override def toString() = Console.RED + "*" + Console.RESET
}

class Diamond extends Thing with HasValue with CanMove {
    val points = 30

    def nextMove(): String = {
        // Can only move down to simulate gravity
        return "down"
    }
     
    override def toString() = Console.YELLOW + "+" + Console.RESET

    def consume(thing: Thing): Space = {
        return thing match {
            case item: Space => item
        }
    }
}

class Boulder extends Thing with CanMove {
    var velocity = 0

    def nextMove(): String = {
        // Can only move down to simulate gravity
        return "down"
    }

    override def toString() = "o"
    def consume(thing: Thing): Thing = {
         thing match {
            case thing: Space => velocity += 1; thing
            case _: PsychoKiller | _: Player => {
                if (velocity >= 1) {
                    velocity = 0
                    new Space
                } else {
                    // reset velocity
                    velocity = 0
                    throw new IllegalMove
                }
            }
            case _ => {
                // reset velocity
                velocity = 0
                throw new IllegalMove
            }
        }
    }
}

class Turf extends Thing {
    val points = -2
    override def toString() = "#"
}

class Space extends Thing {
    val points = -1
    override def toString() = " "
}

class HWall extends Thing {
    override def toString() = "═"
}

class VWall extends Thing {
    override def toString() = "║"
}


