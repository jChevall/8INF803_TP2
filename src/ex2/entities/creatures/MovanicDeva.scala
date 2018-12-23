package ex2.Creatures

import entities.{Attack, Creature, Position}

class MovanicDeva (id : Int, position: Position)
  extends Creature (id,
      "Movanic Deva",
      126,
      24,
      null,
      new Attack("+1 flaming greatsword", 8, 13, List(17, 12, 7), 5),
      List(("ft", 7)),
      null,
      "Pito's Gang",
      position,
      15,
      126
  )