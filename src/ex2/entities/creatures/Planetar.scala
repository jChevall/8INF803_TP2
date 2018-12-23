package ex2.Creatures

import entities.{Attack, Creature, Position}

class Planetar (id : Int, position: Position)
  extends Creature (id,
      "Planetar",
      229,
      32,
      null,
      new Attack("+3 holy greatsword", 18, 21, List(27, 22, 17), 5),
      List(("ft", 5)),
      null,
      "Pito's Gang",
      position,
      10,
      229
  )