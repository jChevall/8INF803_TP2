package ex2.Creatures

import entities.{Attack, Creature, Position}

class DoubleAxeFury (id : Int, position: Position)
  extends Creature (id,
    "Double Axe Fury",
    142,
    17,
    new Attack("+1 orc double axe", 11, 18, List(19, 14, 9), 5),
    new Attack("mwk composite longbow", 7, 14, List(16,11,6), 110),
    List(("ft", 7)),
    null,
    "Dragon's Gang",
    position,
    0,
    142
  )
