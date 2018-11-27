import java.io.Serializable

// TODO : Create RangedAttack, MeleeAttack and Spell Class.
class Creature (val id: Int, val name : String, var hp : Int, val armor : Int,
                val rangedAttack : Array[Attack], val meleeAttack : Array[Attack], val speed : Int,
                val spells : Array[Spell]) extends Serializable {

  // ID : Identifiant
  // Name : Nom
  // Hp : Point de vie de la créature
  // Armor : Point d'armure de la créature
  // RangedAttack : Attaque à porté (arc)
  // MeleeAttack : Attaque au corps à corps
  // Speed : Vitesse de déplacement (distance de marche par tour)

  /**
    *
    * @return
    */
  override def toString: String = s"id : $id name : $name hp : $hp"

  /**
    *
    * @param hp
    */
  def isDead (hp : Int){
    if(hp < 0){
      return true
    }else{
      return false
    }
  }

  /**
    *
    */
  def chooseAction(): Unit ={
    // TODO : Entre se déplacer, rangedAttack, spell ou meleeAttack.
  }

  /**
    *
    * @param spell
    */
  def addSpell(spell : String): Unit ={
    // TODO
  }

  /**
    *
    * @param dmg
    */
  def takeDamage(dmg : Attack): Unit ={
    val touch = dmg.touch;
    if(isTouch(touch)){
      hp -= dmg.getDgt();
    }
  }

  /**
    *
    * @param touch
    * @return
    */
  def isTouch(touch : Int): Boolean = {
    if(touch > armor ){
      return true;
    }else{
      return false;
    }
  }
}
