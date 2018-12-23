package ex2.Creatures

import entities.{Attack, Creature, Position}

class GreataxeOrc (id: Int, position: Position)
  extends Creature(
      id,
      "Greataxe Orc",
      42,
      15,
      new Attack("throwing haxe", 8, 13, List(5), 110),
      new Attack("greathaxe", 11, 22, List(11), 5),
      List(("ft", 5)),
      null,
      "Dragon's Gang",
      position,
      0,
      42
  )
