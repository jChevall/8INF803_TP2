

package ex2.Creatures

import entities.{Attack, Creature, Position}

class OrcWorgRider (id : Int, position: Position)
  extends Creature (id,
    "Orc Worg Rider",
    13,
    18,
    new Attack("mwk battleaxe", 4, 12, List(6), 5),
    new Attack("shortbow", 1, 6, List(4), 110),
    List(("ft", 3)),
    null,
    "Dragon's Gang",
    position,
    0,
    13
  )