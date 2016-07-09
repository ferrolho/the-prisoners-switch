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

object Prisoners extends App with scalax.chart.module.Charting {
	def Simulation(N: Int) = {
		implicit val switch = new Switch
		val prisoners = Leader(N) :: List.fill(N - 1)(new OrdinaryPrisoner)

		def infiniteRandomStream: Stream[Int] = Random.nextInt(N) #:: infiniteRandomStream

		infiniteRandomStream.map(i => prisoners(i).call()).takeWhile(_ == false).size
	}

	if (args.length < 1) println("Wrong number of arguments")
	else {
		val N = args(0).toInt
		val M = if (args.length > 1) args(1).toInt else 1

		val results = (1 to M).par.map { i =>
			val progress = (i * 100 / M) % 25 * 4
			print(s"\r$progress%");
			Simulation(N)
		}

		val data = results.groupBy(_ / 100).mapValues(_.size).toList

		val chart = XYLineChart(data, title = "The prisoners and the switch")
		chart.saveAsPNG("chart.png", (1280, 720))
		//chart.show()
	}
}
