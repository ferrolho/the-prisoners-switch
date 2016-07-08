import util.Random

object Switch {
	private var state = false

	def isUp = state
	def isDown = !isUp
	def toggle() = { state = !state }
}

trait Prisoner {
	def call(): Boolean
}

object OrdinaryPrisoner {
	private var nextId = 0
	private def inc() = { nextId += 1; nextId }
}

case class OrdinaryPrisoner(val id: Int = OrdinaryPrisoner.inc()) extends Prisoner {
	private var hasToggledTheSwitch = false

	def call() = {
		if (Switch.isDown && !hasToggledTheSwitch) {
			Switch.toggle()
			hasToggledTheSwitch = true
		}

		false
	}

	override def toString(): String = "Prisoner no. " + id;
}

case class Leader(val N: Int) extends Prisoner {
	private var counter = 0

	def call() = {
		if (Switch.isUp) {
			Switch.toggle()
			counter += 1
		}

		counter == N - 1
	}
}

object Prisoners extends App {
	if (args.length != 1) {
		println("Missing N")
	} else {
		val N = args(0).toInt

		val prisoners = Leader(N) :: List.fill(N - 1)(OrdinaryPrisoner())

		def infiniteRandomStream: Stream[Int] = Random.nextInt(N) #:: infiniteRandomStream

		val callsCounter = infiniteRandomStream.map(i => prisoners(i).call()).takeWhile(_ == false).size

		println(s"Prisoners freed after $callsCounter calls.")
	}
}