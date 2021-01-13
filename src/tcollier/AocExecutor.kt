package tcollier

import java.time.Duration
import java.time.Instant

typealias SolutionFunc<T> = (List<T>) -> String

data class TimingInfo(val iterations: Int, val duration: Long) {
    fun asJson(): String {
        val it = this.iterations
        val dur = this.duration * 1000
        return "{\"iterations\":$it,\"duration\":$dur}"
    }
}

public class AocExecutor<T>(val input: List<T>, val part1Func: SolutionFunc<T>, val part2Func: SolutionFunc<T>) {
    fun run(args: Array<String>) {
        if (args.size > 0 && args[args.size - 1] == "--time") {
            val part1Timing = timeFunc(part1Func).asJson()
            val part2Timing = timeFunc(part1Func).asJson()
            println("{\"part1\":$part1Timing,\"part2\":$part2Timing}")
        } else {
            println(part1Func(this.input))
            println(part2Func(this.input))
        }
    }

    private fun continueTiming(iterations: Int, duration: Long): Boolean {
        if (iterations < 100) {
            return duration < 30000
        } else {
            return duration < 100
        }
    }

    private fun timeFunc(func: SolutionFunc<T>): TimingInfo {
        var i = 0
        var runningTime: Long = 0
        while (continueTiming(i, runningTime)) {
            val inputCopy: List<T> = input.toList()
            val startTime = Instant.now()
            func(inputCopy)
            runningTime += Duration.between(startTime, Instant.now()).toMillis()
            i++
        }
        return TimingInfo(i, runningTime)
    }
}
