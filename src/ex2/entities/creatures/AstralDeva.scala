package ex2.Creatures

import entities.{Attack, Creature, Position}

class AstralDeva (id: Int, position: Position)
  extends Creature(
      id,
      "Astral Deva",
      172,
      29,
      null,
      new Attack("+2 disrupting warhammer", 15, 22, List(26, 21, 16), 5),
      List(("ft", 9)),
      null,
      "Dragon's Gang",
      position,
      0,
      172
  ){
}