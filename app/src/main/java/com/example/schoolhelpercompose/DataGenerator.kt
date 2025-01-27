import com.example.schoolhelpercompose.Exercise
import com.example.schoolhelpercompose.ExerciseList
import com.example.schoolhelpercompose.Subject
import kotlin.random.Random


fun generateData() : List<ExerciseList>{


    val subjects = listOf(
        Subject("Matematyka"),
        Subject("PUM"),
        Subject("Fizyka"),
        Subject("Elektronika"),
        Subject("Algorytmy")
    )

    fun generateRandomTask(): String{
        val tasks = listOf(
            "Lorem ipsum dolor sit amet.",
            "Consectetur adipiscing elit.",
            "Vivamus luctus urna sed urna ultricies ac tempor dui sagittis."
        )
        return tasks.random()
    }

    return List(20) { index ->
        val exercises = List(Random.nextInt(1, 11)) {

            val points =  listOf(3.0, 3.5, 4.0, 4.5, 5.0)
            Exercise(generateRandomTask(), points.random())
        }
        val subject = subjects.random()
        val possibleGrades = listOf(3.0, 3.5, 4.0, 4.5, 5.0)

        val grade = possibleGrades.random()

        ExerciseList(index, exercises, subject, grade)
    }
}

object ExerciseListProvider{
    var exerciseList = generateData()
}


fun generateSubjectAverageList(exerciseLists: List<ExerciseList>): List<Pair<Subject, Double>> {
    return exerciseLists
        .groupBy { it.subject }
        .map { (subject, exercises) ->
            val average = exercises.map { it.grade }.average()
            val roundedAverage = String.format("%.2f", average).toDouble()
            subject to roundedAverage
        }
}

