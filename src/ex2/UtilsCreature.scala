package exo2

import entities.Creature
import ex2.Creatures._


object UtilsCreature {

  def create(creature: Creature): Creature ={

    var newCreature: Creature = null

    creature match {
      case anglerSlayer:AngelSlayer => newCreature = new AngelSlayer(anglerSlayer.id, anglerSlayer.position)
      case astralDeva:AstralDeva => newCreature = new AstralDeva(astralDeva.id, astralDeva.position)
      case brutalWarlord:BrutalWarlord => newCreature = new BrutalWarlord(brutalWarlord.id, brutalWarlord.position)
      case doubleAxeFury:DoubleAxeFury => newCreature = new DoubleAxeFury(doubleAxeFury.id, doubleAxeFury.position)
      case greataxeOrc:GreataxeOrc => newCreature = new GreataxeOrc(greataxeOrc.id, greataxeOrc.position)

      case greenGreatWyrmDragon: GreenGreatWyrmDragon =>{

        var dragon = new GreenGreatWyrmDragon(greenGreatWyrmDragon.id, greenGreatWyrmDragon.position)
        dragon.focus = greenGreatWyrmDragon.focus
        newCreature = dragon
      }

      case movanicDeva: MovanicDeva => newCreature = new MovanicDeva(movanicDeva.id, movanicDeva.position)
      case orcWorgRider: OrcWorgRider => newCreature = new OrcWorgRider(orcWorgRider.id, orcWorgRider.position)
      case planetar: Planetar => newCreature = new Planetar(planetar.id, planetar.position)
      case solar: Solar => newCreature = new Solar(solar.id, solar.position)
    }

    newCreature.position = creature.position
    newCreature.targets = creature.targets
    newCreature.hp = creature.hp
    newCreature.hurtInRound = creature.hurtInRound

    newCreature
  }

}
