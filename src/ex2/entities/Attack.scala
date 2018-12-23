package entities

import java.io.Serializable

class Attack(
              var name: String,
              var minDamages: Int,
              var maxDamages: Int,
              var accuracies: List[Int],
              var minDistance: Int
            ) extends Serializable {

  private var attackNumber: Int = 0

  def getDamage(targetArmor: Int) : Int = {
    val random = scala.util.Random
    val doIHit = random.nextInt(20-1) + accuracies(attackNumber)
    var damage = random.nextInt(maxDamages - minDamages) + minDamages
    if(doIHit < targetArmor) damage = 0
    if(attackNumber < accuracies.size - 1) attackNumber += 1
    damage
  }

}
