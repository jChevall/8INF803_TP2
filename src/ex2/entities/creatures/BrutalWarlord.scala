package ex2.Creatures

import entities.{Attack, Creature, Position}

class BrutalWarlord (id : Int, position: Position)
  extends Creature (id,
    "Brutal Warlordr",
    141,
    27,
    new Attack("+1 vicious flail", 11, 18, List(20, 15, 10), 5),
    new Attack("mwk throwing axe", 6, 11, List(19), 110),
    List(("ft", 5)),
    null,
    "Dragon's Gang",
    position,
    0,
    141
  )