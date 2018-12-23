package ex2

import entities._
import exo2.UtilsCreature
import org.apache.spark.SparkContext
import org.apache.spark.graphx._
import org.apache.spark.rdd.RDD
import org.json4s.DefaultFormats


class Game extends Serializable {


  /**
    * Lancement de la simulation de combat
   */
  def execute(protagonist: RDD[(VertexId, (Creature))],
              relationships: RDD[Edge[EdgeLink]],
              sc: SparkContext,
              maxIterations: Int
             ): Graph[Creature, EdgeLink] = {

    var graph = Graph(protagonist, relationships)
    var roundCounter = 0
    // Utilisation de : join stratégie
    val fields = new TripletFields(true, true, true)
    implicit val formats: DefaultFormats.type = DefaultFormats

    /**
      * Boucle qui itèrent les actions des protagonistes
      */
    def gameLoop() {

      while (true) {
        roundCounter+=1
        println("Round : " + roundCounter)

        // Fait un checkpoint du graph tous les 5 rounds
        if(roundCounter%5==0) graph.checkpoint()
        var roundGraph = graph

        // 0. Supprimer les acteurs tués
        roundGraph = roundGraph.subgraph(vpred = (_, attr) =>  attr.hp > 0)

        // 1. Déplacement et regénération
        if(roundCounter > 1){
          val newVertices = roundGraph.vertices.map(vertex => {
            vertex._2.regenerate()
            vertex._2.move()
            vertex._2.hurtInRound = false
            vertex
          })
          roundGraph = Graph(newVertices, roundGraph.edges)
        }

        // 2. Recherche des cibles
        val targetMessages = roundGraph.aggregateMessages[List[Creature]](
          sendTargetMsg,
          mergeTargetMsg,
          fields
        )
        roundGraph = roundGraph.joinVertices(targetMessages) {

          (_, fighter, allTargets) => {
            val newFighter = UtilsCreature.create(fighter)
            newFighter.setTargets(allTargets)
            newFighter
          }
        }

        // 3. Envoi et réception des dégats
        val damageMessages = roundGraph.aggregateMessages[Int](
          sendDamageMsg,
          mergeDamageMsg,
          fields
        )
        roundGraph = roundGraph.joinVertices(damageMessages) {
          (_, damageReceiver, damages) => {
            if(damages>0){
              val newDamageReceiver = UtilsCreature.create(damageReceiver)
              newDamageReceiver.takeDamage(damages)
              newDamageReceiver
            }else
              damageReceiver
          }
        }

        // 4. Conditions de fin de la simulation

        // Récupération du nombre d'alliés et ennemis toujours en vie
        val nbDragonGang = roundGraph.vertices.filter{ vertex => vertex._2.team == "Dragon's Gang" && vertex._2.hp > 0}.count
        val nbPitoGang = roundGraph.vertices.filter{ vertex =>  vertex._2.team == "Pito's Gang" && vertex._2.hp > 0}.count

        // Conditions d'arrêt: victoire des ennemis ou des alliés
        if(nbDragonGang == 0){
          println(" Bataille terminé : Pito peut dormir tranquille")
          return
        }
        else if(nbPitoGang == 0){
          println(" Bataille terminé : Pito est tué")
          return
        }
        else if (roundCounter == maxIterations) return

        graph = roundGraph
      }
    }

    // Réitère sur la boucle
    gameLoop()
    graph
  }


  // Fonctions utilisés dans les aggregates messages

  /**
    * Gère les cibles
    * Si la relation est un ennemi
    */
  def sendTargetMsg(triplet: EdgeContext[Creature, EdgeLink, List[Creature]]) {

    if(triplet.attr.relation == "enemy") {
      triplet.sendToSrc(List(triplet.dstAttr))
      triplet.sendToDst(List(triplet.srcAttr))
    }
  }

  /**
    * Gère les cible
    * Compare les cibles, renvoi le monstre le plus interessant
    */
  def mergeTargetMsg(monsterAccum: List[Creature], newMonster: List[Creature]): List[Creature] = {
    monsterAccum:::newMonster
  }

  /**
    * Gestion des dégats
    */
  def sendDamageMsg(triplet: EdgeContext[Creature, EdgeLink, Int]) {

    // Condition : La relation est dans la liste des targets
    if(triplet.srcAttr.targets.map(target => target.id).contains(triplet.dstAttr.id)) {
      triplet.sendToDst(triplet.srcAttr.attackTarget(triplet.dstAttr))
    }

    if(triplet.dstAttr.targets.map(target => target.id).contains(triplet.srcAttr.id)) {
      triplet.sendToSrc(triplet.dstAttr.attackTarget(triplet.srcAttr))
    }
  }

  /**
    * Fusionne les dégats
    * @param damage1
    * @param damage2
    * @return
    */
  def mergeDamageMsg(damage1: Int, damage2: Int): Int = {
    damage1 + damage2
  }

}