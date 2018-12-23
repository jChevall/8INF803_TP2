package entities

import java.io.Serializable

import ex2.Constants

class Creature (var id: Int,
                var name : String,
                var hp : Int,
                var armor : Int,
                var rangedAttack : Attack = null,
                var meleeAttack : Attack = null,
                var speed : List[(String, Int)] = null,
                var spells : Attack = null,
                var team : String,
                var position: Position = new Position(),
                var regeneration : Int = 0,
                var hpMax: Int,
                var maxTargets: Int = 1,
                var targets: List[Creature] = List.empty[Creature],
                var hurtInRound:Boolean = false)
  extends Serializable {

  /**
    * Fonction toString
    *
    * @return
    */
  override def toString: String = s"id : $id name : $name hp : $hp"

  /**
    * Test du status (Vivant/Mort) de l'entité
    *
    * @param hp
    */
  def isDead(hp: Int) {
    if (hp < 0) {
      return true
    } else {
      return false
    }
  }

  /**
    * Prise de dégat, réduction des points de vie
    * Mise à 0 si les dégats sont supérieur au point de vie restant
    *
    * @param amount
    */
  def takeDamage(amount: Int): Unit = {
    println(this.name + " take " + amount + " damage.")
    this.hurtInRound = true
    this.hp -= amount
    if (this.hp < 0){
      this.hp = 0
      println(this.name + " is dead.")
    }
  }

  /**
    * Attaque de la cible
    * @param target
    * @return
    */
  def attackTarget(target: Creature): Int = {

    println(this.name + " try to beat " + target.name)
    var attack: Attack = null
    val distance = Position.distanceCreature(this.position, target.position)

    if (this.meleeAttack != null && distance <= this.meleeAttack.minDistance) attack = this.meleeAttack;
    // else if(special != null && distance <= special.minDistance) attack = special
    else if (this.rangedAttack != null && distance <= this.rangedAttack.minDistance) attack = this.rangedAttack
    else return 0

    attack.getDamage(target.armor)

  }

  /**
    *
    * @return
    */
  def computeNormalizedDirection(): Position = {

    // Always move to the closer ennemy
    if (targets.nonEmpty) {

      val desiredVelocity = new Position(targets(0).position.x - this.position.x, targets(0).position.y - this.position.y)
      val normalizedDesiredVelocity = desiredVelocity.normalize()
      val distanceToTarget = desiredVelocity.getDistance()

      if (distanceToTarget < Constants.nearTargetRadius) {

        if (distanceToTarget > Constants.stickDistance) {
          normalizedDesiredVelocity.x *= (distanceToTarget / Constants.nearTargetRadius)
          normalizedDesiredVelocity.y *= (distanceToTarget / Constants.nearTargetRadius)
        }
        else {
          normalizedDesiredVelocity.x = 0
          normalizedDesiredVelocity.y = 0
        }
      }
      normalizedDesiredVelocity
    }
    else new Position(0, 0)
  }

  /**
    * Mouvements
    */
  def move() {
    val normalizeDirection = computeNormalizedDirection()
    this.position.x += normalizeDirection.x * this.speed(0)._2
    this.position.y += normalizeDirection.y * this.speed(0)._2
  }


  def setTargets(newTargets: List[Creature]) {
    this.targets = newTargets.sortBy((target) => Position.distanceCreature(this.position, target.position)).take(maxTargets)
  }

  /**
    * Régénration
    * On test pour ne pas dépasser les points de vie max
    */
  def regenerate() {
    this.hp = this.hp + this.regeneration
    if (this.hp > this.hpMax) this.hp = this.hpMax
  }
}
