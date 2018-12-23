package ex2.Creatures

import entities.{Attack, Creature, Position}

class Solar (id : Int, position: Position)
  extends Creature (id,
    "Solar",
    360,
    44,
    new Attack("+5 Composite Longbow", 16, 20, List(31, 26, 21, 16), 110),
    new Attack("+5 Dancing Greatsword", 21, 24, List(35, 30, 25, 20), 5),
      List(("ft", 9)),
    null,
    "Pito's Gang",
    position,
    15,
    360
  ){
  override def move(){}
}
