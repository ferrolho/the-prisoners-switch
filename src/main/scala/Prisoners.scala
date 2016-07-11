import scalax.chart._
import util.Random

class Switch {
	private var state = false

	def isUp = state
	def isDown = !isUp
	def toggle() = { state = !state }
}

sealed trait Prisoner {
	def call(): Boolean
}

class OrdinaryPrisoner(implicit s: Switch) extends Prisoner {
	private var hasToggledTheSwitch = false

	def call() = {
		if (s.isDown && !hasToggledTheSwitch) {
			s.toggle()
			hasToggledTheSwitch = true
		}

		false
	}
}

case class Leader(val N: Int)(implicit s: Switch) extends Prisoner {
	private var counter = 0

	def call() = {
		if (s.isUp) {
			s.toggle()
			counter += 1
		}

		counter == N - 1
	}
}

sealed trait DayAwarePrisoner {
	def call(day: Int): Boolean
}

trait SimulationApp[P] extends App with scalax.chart.module.Charting {
	def createPrisoners(N: Int)(implicit switch: Switch): List[P]
	def call(prisoner: P, day: Int): Boolean

	def simulate(N: Int): Int = {
		implicit val switch = new Switch
		val prisoners = createPrisoners(N)

		def infiniteRandomStream: Stream[Int] = Random.nextInt(N) #:: infiniteRandomStream

		infiniteRandomStream
			.map(prisoners)
			.zipWithIndex
			.map((call _).tupled)
			.takeWhile(_ == false)
			.size
	}

	def chartPrefix: String

	def run(): Unit = {
		if (args.length < 1) println("Wrong number of arguments")
		else {
			val N = args(0).toInt
			val M = if (args.length > 1) args(1).toInt else 1

			val numCores = Runtime.getRuntime().availableProcessors()

			val results = (1 to M).par.map { i =>
				val progress = (i * 100 / M) % (100 / numCores) * numCores
				print(s"\r$progress%");
				simulate(N)
			}

			val data = results.groupBy(_ / 100).mapValues(_.size).toList

			val chart = XYLineChart(data, title = "The prisoners and the switch")
			chart.saveAsPNG(s"$chartPrefix-$N-$M.png", (1280, 720))
			//chart.show()
		}
	}
}

object Prisoners extends SimulationApp[Prisoner] {
	def createPrisoners(N: Int)(implicit switch: Switch) =
		Leader(N) :: List.fill(N - 1)(new OrdinaryPrisoner)
	
	def call(prisoner: Prisoner, day: Int) = prisoner.call()

	val chartPrefix = "chart"
	run()
}

object DayAwarePrisoners extends SimulationApp[DayAwarePrisoner] {
	def createPrisoners(N: Int)(implicit switch: Switch) = ???

	def call(prisoner: DayAwarePrisoner, day: Int) = prisoner.call(day)

	val chartPrefix = "dayaware"
	run()
}
