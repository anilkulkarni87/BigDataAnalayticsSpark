package standardscala

case class TempData(day: Int  ,doy: Int  ,month: Int    ,year: Int  ,precip: Double,snow: Double,
                    tave: Double,tmax: Double,tmin: Double )

object TempData {
  def toDoubleOrNeg(s:String): Double={
    try{
      s.toDouble
    }
    catch{
      case _:NumberFormatException => -1
    }
  }

  def main(args: Array[String]): Unit ={
    val source = scala.io.Source.fromFile("MN212142_9392.csv")
    val lines=source.getLines().drop(1)
    val data = lines.flatMap{ line =>
      val p = line.split(",")
      if(p(7)=="."||p(8)=="."||p(9)==".") Seq.empty else
      Seq(TempData(p(0).toInt,
        p(1).toInt,
        p(2).toInt,
        p(4).toInt,
        toDoubleOrNeg(p(5)),
        toDoubleOrNeg(p(6)),
        p(7).toDouble,
        p(8).toDouble,
        p(9).toDouble))
    }.toArray
    source.close()

    //println(data.length)
    //data.take(5) foreach println


    //Inefficient
    val maxTemp = data.map(_.tmax).max
    val hotDays = data.filter(_.tmax == maxTemp)
    println(s"Hot days are ${hotDays.mkString}")

    val hotDay=data.maxBy(_.tmax)
    println(s"Hot Day 1 is $hotDay")

    val hotDay2 = data.reduceLeft((d1,d2) => if(d1.tmax>=d2.tmax) d1 else d2)
    println(s"Hot Day 2 is $hotDay2")


    val rainyCount=data.count(_.precip>=1.0)
    println(s"There are $rainyCount rainy days. There is ${rainyCount*100.0/data.length} percent")

    val(rainySum, rainyCount2) = data.foldLeft(0.0 -> 0) { case ((sum,cnt),td) =>
    if(td.precip<1.0) (sum, cnt) else (sum+td.tmax,cnt+1)
    }
    println(s"Average rainy temp is ${rainySum/rainyCount2}")


    //continue from part 4 scala.
  }

}