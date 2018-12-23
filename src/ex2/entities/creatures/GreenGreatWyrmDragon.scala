package ex2.Creatures

import entities.{Attack, Creature, Position}
import ex2.Constants

class GreenGreatWyrmDragon (id : Int, position: Position)
  extends Creature (id,
    "Green Great Wyrm Dragon",
    391,
    37,
    null,
    new Attack("tail slap", 25, 27, List(31), 5),
    List(("ft", 15)),
    new Attack("acid", 26, 27, List(999), 70),
    "Dragon's Gang",
    position,
    15,
    391,
    3
  )
{

  // Fonction de l' "Intelligence du Dragon"

  var focus:Creature = null

  /**
    * Override de la sélection de la cible (On vise les Solar en priorité)
    * @param newTargets
    */
  override def setTargets(newTargets: List[Creature]) {
    newTargets.find(target => target.name == "Solar") match {
      case Some(solar) => this.focus = solar
      case None => this.focus = null
    }

    super.setTargets(newTargets)
  }

  /**
    * Overrride de la direction du Dragon
    * @return
    */
  override def computeNormalizedDirection(): Position = {

    //Choose which target to aim
    var targetToFollow:Creature = null

    if(focus != null){
      targetToFollow = focus
    }
    else if(targets.nonEmpty) targetToFollow = targets(0)

    if(targetToFollow != null) {

      val desiredVelocity = new Position(targetToFollow.position.x - this.position.x, targetToFollow.position.y - this.position.y)
      val normalizedDesiredVelocity = desiredVelocity.normalize()
      val distanceToTarget = desiredVelocity.getDistance()

      if(distanceToTarget < Constants.nearTargetRadius){

        if(distanceToTarget > Constants.stickDistance){
          normalizedDesiredVelocity.x *= (distanceToTarget/Constants.nearTargetRadius)
          normalizedDesiredVelocity.y *= (distanceToTarget/Constants.nearTargetRadius)
        }
        else{
          normalizedDesiredVelocity.x = 0
          normalizedDesiredVelocity.y = 0
        }
      }
      normalizedDesiredVelocity
    }
    else new Position(0,0)
  }

}
