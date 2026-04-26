package com.imglmd.physicsexps.presentation

fun Double.round4(): Double {
    return kotlin.math.round(this * 10_000) / 10_000
}

fun normalizePoints(
    points: List<Pair<Double, Double>>
): List<Pair<Double, Double>> {
    return points.map { (x, y) ->
        x.round4() to y.round4()
    }
}


fun downsamplePoints(
    points: List<Pair<Double, Double>>,
    maxPoints: Int = 100
): List<Pair<Double, Double>> {
    if (points.size <= maxPoints) return points

    val step = points.size.toFloat() / maxPoints

    return List(maxPoints) { i ->
        points[(i * step).toInt()]
    }
}