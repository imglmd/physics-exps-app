<div align="center">
<img width="" src="assets/icon.png"  width=160 height=160  align="center">

# PhysicsExps

### Android-приложение для расчета и изучения физических экспериментов.

</div>

## Возможности

- Каталог экспериментов с поиском и группировкой по категориям.

- Ввод параметров эксперимента и локальный расчет результатов.
  
- Экран результата с графиками, формулами и шагами решения.
  
- История запусков экспериментов (Room, локальная БД).
  
- Сравнение нескольких запусков по входным данным, результатам и графикам.
  
- Работа с медиа через backend API.
  
- Автоматическая регистрация устройства для авторизации в API.

## Скриншоты

<table>
  <tr>
    <td><img src="assets/1.jpg" width="250"/></td>
    <td><img src="assets/2.jpg" width="250"/></td>
    <td><img src="assets/3.jpg" width="250"/></td>
  </tr>
  <tr>
    <td><img src="assets/4.jpg" width="250"/></td>
    <td><img src="assets/5.jpg" width="250"/></td>
    <td><img src="assets/6.jpg" width="250"/></td>
  </tr>
  <tr>
    <td><img src="assets/7.jpg" width="250"/></td>
    <td><img src="assets/8.jpg" width="250"/></td>
    <td><img src="assets/9.jpg" width="250"/></td>
  </tr>
</table>

## Поддерживаемые эксперименты

В проекте реализованы:

- Свободное падение (`FreeFallExperiment`)
- Движение тела, брошенного под углом (`ProjectileMotionExperiment`)
- Маятник (математический) (`PendulumExperiment`)
- Физический маятник (`PhysicalPendulumExperiment`)
- Пружинный маятник (`SpringPendulumExperiment`)
- Гармонические колебания (`HarmonicVibrationsExperiment`)
- Закон Кулона (`CoulombsLawExperiment`)
- Закон Джоуля-Ленца (`JouleLenzExperiment`)
- Эффект Доплера (`DopplerEffectExperiment`)
- Радиоактивный распад (`RadioactiveDecayExperiment`)

## Технологии

- Kotlin, Coroutines, Flow
- Jetpack Compose + Material 3 Expressive
- Navigation 3
- Koin (DI)
- Room + KSP
- Retrofit + Kotlinx Serialization
- Coil
- Vico Charts
- Latex Renderer

## Архитектура

Проект разделен на слои:

- `presentation` - ui, экраны, ViewModel, навигация
- `domain` - модели, реестр экспериментов, use-case'ы, валидация
- `data` - repository-реализации, Room, remote API, mappers
- `di` - Koin-модули
