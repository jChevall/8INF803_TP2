
package ex2.Combat1

import entities.{Creature, EdgeLink, Position}
import ex2.Creatures._
import ex2.{Constants, Game}
import org.apache.spark.graphx.{Edge, Graph, VertexId}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ArrayBuffer

object Fight2 extends App  {

  // Initialisation
  val appname = "BBD Devoir 2 -> Combat 1. Solar vs Dragon"
  val master = "local"
  val conf = new SparkConf().setAppName(appname).setMaster(master)
  val sc = new SparkContext(conf)
  sc.setLogLevel("ERROR")
  sc.setCheckpointDir(System.getProperty("java.io.tmpdir"))

  var i:Long = 1
  val protagonistBuffer: ArrayBuffer[(VertexId, Creature)] = ArrayBuffer()
  val rnd = new scala.util.Random

  //Solar
  for (i <- 1 to 1) {
    protagonistBuffer += ((
      i,
      new Solar(
        i.toInt,
        new Position(
          Constants.PitoGangMinX + rnd.nextInt( (Constants.PitoGangMaxX  - Constants.PitoGangMinX ) + 1 ),
          Constants.PitoGangMinY + rnd.nextInt( (Constants.PitoGangMaxY  - Constants.PitoGangMinY ) + 1 )
        )
      )
    ))
  }

  //2x Planetar
  for (i <- 2 to 3) {
    protagonistBuffer += ((
      i,
      new Planetar(
        i.toInt,
        new Position(
          Constants.PitoGangMinX + rnd.nextInt( (Constants.PitoGangMaxX  - Constants.PitoGangMinX ) + 1 ),
          Constants.PitoGangMinY + rnd.nextInt( (Constants.PitoGangMaxY  - Constants.PitoGangMinY ) + 1 )
        )
      )
    ))
  }
  //
  //2x Movanic Deva
  for (i <- 4 to 5) {
    protagonistBuffer += ((
      i,
      new MovanicDeva(
        i.toInt,
        new Position(
          Constants.PitoGangMinX + rnd.nextInt( (Constants.PitoGangMaxX  - Constants.PitoGangMinX ) + 1 ),
          Constants.PitoGangMinY + rnd.nextInt( (Constants.PitoGangMaxY  - Constants.PitoGangMinY ) + 1 )
        )
      )
    ))
  }
  //
  //5x Astral Deva
  for (i <- 6 to 10) {
    protagonistBuffer += ((
      i,
      new AstralDeva(
        i.toInt,
        new Position(
          Constants.PitoGangMinX + rnd.nextInt( (Constants.PitoGangMaxX  - Constants.PitoGangMinX ) + 1 ),
          Constants.PitoGangMinY + rnd.nextInt( (Constants.PitoGangMaxY  - Constants.PitoGangMinY ) + 1 )
        )
      )
    ))
  }

  //Green Great Wyrm Dragon
  for (i <- 11 to 11) {
    protagonistBuffer += ((
      i,
      new GreenGreatWyrmDragon(
        i.toInt,
        new Position(
          Constants.DragonGangMinX + rnd.nextInt( (Constants.DragonGangMaxX  - Constants.DragonGangMinX ) + 1 ),
          Constants.DragonGangMinY + rnd.nextInt( (Constants.DragonGangMaxY  - Constants.DragonGangMinY ) + 1 )
        )
      )
    ))
  }

  //50x Orc Barbarians
  for (i <- 12 to 61) {
    protagonistBuffer += ((
      i,
      new GreataxeOrc(
        i.toInt,
        new Position(
          Constants.DragonGangMinX + rnd.nextInt( (Constants.DragonGangMaxX  - Constants.DragonGangMinX ) + 1 ),
          Constants.DragonGangMinY + rnd.nextInt( (Constants.DragonGangMaxY  - Constants.DragonGangMinY ) + 1 )
        )
      )
    ))
  }

  //10x Angel Slayer
  for (i <- 62 to 71) {
    protagonistBuffer += ((
      i,
      new AngelSlayer(
        i.toInt,
        new Position(
          Constants.DragonGangMinX + rnd.nextInt( (Constants.DragonGangMaxX  - Constants.DragonGangMinX ) + 1 ),
          Constants.DragonGangMinY + rnd.nextInt( (Constants.DragonGangMaxY  - Constants.DragonGangMinY ) + 1 )
        )
      )
    ))
  }



  val relationshipsBuffer: ArrayBuffer[Edge[EdgeLink]] = ArrayBuffer()

  //Generate relationships
  for (j <- 0 to protagonistBuffer.length-2) {
    for {k <- j+1 to protagonistBuffer.length-1} {

      if(protagonistBuffer(j) == protagonistBuffer(k)){}
      else {
        if (protagonistBuffer(j)._2.team == protagonistBuffer(k)._2.team) {
          if(protagonistBuffer(j)._2.team == "Pito's Gang" || protagonistBuffer(k)._2.team == "Pito's Gang") {
            relationshipsBuffer += Edge(protagonistBuffer(j)._2.id.toLong, protagonistBuffer(k)._2.id.toLong, new EdgeLink("friend"))
          } else if (protagonistBuffer(j)._2.name == "Green Great Wyrm Dragon" || protagonistBuffer(k)._2.name == "Green Great Wyrm Dragon"){
            relationshipsBuffer += Edge(protagonistBuffer(j)._2.id.toLong, protagonistBuffer(k)._2.id.toLong, new EdgeLink("friend"))
          }
        } else {
          relationshipsBuffer += Edge(protagonistBuffer(j)._2.id.toLong, protagonistBuffer(k)._2.id.toLong, new EdgeLink("enemy"))
        }
      }
    }
  }

  // Create an RDD for the vertices
  val protagonist: RDD[(VertexId, (Creature))] = sc.parallelize(protagonistBuffer)
  val relationships: RDD[Edge[EdgeLink]] = sc.parallelize(relationshipsBuffer)

  val game = new Game()
  val resultsFight = game.execute(protagonist, relationships, sc, 1000)
}
