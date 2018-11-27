import java.io.Serializable
import scala.util.Random

// TODO : Choose stat require
class Attack (val id: Int, val name : String, val range : Int,
              val touch : Int, val nbLance : Int, val nbFace : Int,
              val addDmg : Int, val crit : Int) extends Serializable{

  val random = scala.util.Random;
  var critical : Boolean = false;

  // Exemple +6(1d8+2/x3)
  // Le touché = 1d20 + 6
  // Dégat = 1d8+2 ( *3 si premier dé à 20)

  /**
    *
    */
  def getDgt(): Int ={
    var sumDgt = addDmg;
    for(a <- 1 to nbLance){
      sumDgt += random.nextInt(nbFace);
    }
    if(critical == true){
      sumDgt = sumDgt * crit;
    }
    return  sumDgt;
  }

  /**
    *
    */
  def getTouch(): Unit ={
    val x = random.nextInt(20);
    isCrit(x);
    return x + touch;
  }

  /**
    *
    * @param x
    */
  def isCrit(x : Int): Unit ={
    if(x == 20){
      critical = true;
    }else{
      critical = false;
    }
  }
}
