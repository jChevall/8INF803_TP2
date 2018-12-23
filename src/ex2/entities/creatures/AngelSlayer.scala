package ex2.Creatures

import entities.{Attack, Creature, Position}

class AngelSlayer (id: Int, position: Position)
  extends Creature(
    id,
    "Angel Slayer",
    112,
    26,
    new Attack("mwk composite longbow", 7, 14, List(19,14,9), 110),
    new Attack("double axe", 8, 15, List(21,16,11), 5),
    List(("ft", 7)),
    null,
    "Dragon's Gang",
    position,
    0,
    112
  ){
}