package com.imglmd.physicsexps.domain.model


/**
 *  шаг решения задачи
 *  используется для пошагового объяснения (как в Photomath)
 */
sealed interface SolutionStep {


    /**
     * теория, используеться в начале решения или перед важным шагом,
     * чтобы объяснить идею (без вычислений и формул)
     */
    data class Theory(
        val title: String, // заголовок блока ("Идея", "Что происходит?")
        val body: String // основной текст объяснения
    ): SolutionStep

    /**
     * формула без подстановки чисел, используеться чтобы показать закон, уравнение, вывод
     * обычно идет перед вычислениями
     *
     * expression должен быть в формате LaTeX!!!
     */
    data class Formula(
        val description: String, // описание какое-то
        val expression: String, // ф-ла (LaTeX)
    ): SolutionStep

    /**
     * подстановка чисел в формулу + результат, используеться для вычислений
     *
     * оба поля должны быть в LaTeX
     * пример:
     * expression = "L = 10 \\cdot 6.7"
     * result = "L = 67 \\text{ м}"
     */
    data class Substitution(
        val description: String,
        val expression: String, // ф-ла с подставленными числами (LaTeX)
        val result: String // результат вычисления (LaTeX)
    ): SolutionStep

    /**
     * финальный результат, используеться в конце решения
     * может быть несколько
     */
    data class Result(
        val quantity: PhysicalQuantity
    ): SolutionStep
}