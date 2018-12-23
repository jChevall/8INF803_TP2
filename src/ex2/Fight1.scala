
package ex2.Combat1

import java.lang.Math.PI

import entities.{Creature, EdgeLink, Position}
import ex2.Creatures._
import ex2.{Constants, Game}
import org.apache.spark.graphx.{Edge, Graph, VertexId}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ArrayBuffer

object Fight1 extends App  {

  // Initialisation
  val appname = "BBD Devoir 2 -> Combat 1. Solar vs Orcs"
  val master = "local"
  val conf = new SparkConf().setAppName(appname).setMaster(master)
  val sc = new SparkContext(conf)
  sc.setLogLevel("ERROR")
  sc.setCheckpointDir(System.getProperty("java.io.tmpdir"))

  var i:Long = 1
  val protagonistBuffer: ArrayBuffer[(VertexId, Creature)] = ArrayBuffer()

  // L'armé du Dragon sont en arc de cercle autour du Solar
  val goodGuyNumber = 1.0
  val badGuyNumber = 14.0
  var goodGuyInterval:Double = 360.0/goodGuyNumber
  var badGuyInterval:Double = 360.0/badGuyNumber

  var goodGuyAngle:Double = 0.0
  var badGuyAngle:Double = 0.0

  var goodGuyRad:Double = goodGuyAngle * (PI / 180.0)
  var badGuyRad:Double = badGuyAngle* (PI / 180.0)

  // Initialisation des créatures

  // Initialisation SOLAR
  for (i <- 1 to 1) {
    protagonistBuffer += ((
      1L,
      new Solar(
        1,
        new Position(
          Constants.PitoGangCircleRadius * Math.cos(goodGuyRad),
          Constants.PitoGangCircleRadius * Math.sin(goodGuyRad)
        )
      )
    ))

    goodGuyAngle += goodGuyInterval
    goodGuyRad = goodGuyAngle * (PI / 180.0)
  }

  // Initialisation Worgs Rider
  for (i <- 2 to 10) {
    protagonistBuffer += ((
      i,
      new OrcWorgRider(
        i.toInt,new Position(
          Constants.DragonGangCircleRadius * Math.cos(badGuyRad),
          Constants.DragonGangCircleRadius * Math.sin(badGuyRad)
        )
      )
    ))

    badGuyAngle += badGuyInterval
    badGuyRad = badGuyAngle * (PI / 180.0)
  }

  // Initialisation Warlord
  for (i <- 11 to 11) {
    protagonistBuffer += ((
      i,
      new BrutalWarlord(
        i.toInt,
        new Position(
          Constants.DragonGangCircleRadius * Math.cos(badGuyRad),
          Constants.DragonGangCircleRadius * Math.sin(badGuyRad)
        )
      )
    ))

    badGuyAngle += badGuyInterval
    badGuyRad = badGuyAngle * (PI / 180.0)
  }

  // Initialisation Orc
  for (i <- 12 to 15) {
    protagonistBuffer += ((
      i,
      new DoubleAxeFury(
        i.toInt,
        new Position(
          Constants.DragonGangCircleRadius * Math.cos(badGuyRad),
          Constants.DragonGangCircleRadius * Math.sin(badGuyRad)
        )
      )
    ))

    badGuyAngle += badGuyInterval
    badGuyRad = badGuyAngle * (PI / 180.0)
  }


  // Initialisation des relations
  val relationshipsBuffer: ArrayBuffer[Edge[EdgeLink]] = ArrayBuffer()

  for (j <- 0 to protagonistBuffer.length-2) {
    for {k <- j+1 to protagonistBuffer.length-1} {
      if(protagonistBuffer(j) == protagonistBuffer(k)){}
      else {
        if (protagonistBuffer(j)._2.team == protagonistBuffer(k)._2.team) {
        } else {
          relationshipsBuffer += Edge(protagonistBuffer(j)._2.id.toLong, protagonistBuffer(k)._2.id.toLong, new EdgeLink("enemy"))
        }
      }
    }
  }


  // Création des RDD
  val protagonist: RDD[(VertexId, (Creature))] = sc.parallelize(protagonistBuffer)
  val relationships: RDD[Edge[EdgeLink]] = sc.parallelize(relationshipsBuffer)


  // Lancement de la simulation
  val game = new Game()
  val resultsFight = game.execute(protagonist, relationships, sc, 1000)

}