package com.example.schoolhelpercompose



import ExerciseListProvider.exerciseList
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.schoolhelpercompose.ui.theme.SchoolHelperComposeTheme
import generateSubjectAverageList
import kotlin.random.Random


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SchoolHelperComposeTheme {
                Navigation()
            }
        }
    }
}

// Ekran "Moje Listy Zadań"
@Composable
fun MyTaskListFrag(navController1: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ListOfExercises(navController1)

    }
}

// Ekran "Moje Oceny"
@Composable
fun MyGradesFrag() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ListOfGrades()

    }

}

//Ekran listy zadań
@Composable
fun EachSubTasksFrag(exerciseList: ExerciseList) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ListOfTask(exerciseList)

    }
}

// Definicje tras nawigacji
sealed class Screens(val route: String) {
    object MyTaskListFrag : Screens("task_list")
    object MyGradesFrag : Screens("grades")
    object EachSubTasks : Screens("przedmiot/{listId}") {
        fun createRoute(listId: Int) = "przedmiot/$listId"
    }
}

sealed class BottomBar(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object TaskList : BottomBar(Screens.MyTaskListFrag.route, "Listy Zadań", Icons.Default.Menu)
    object Grades : BottomBar(Screens.MyGradesFrag.route, "Oceny", Icons.Default.List)
}

// Główna nawigacja
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomMenu(navController = navController) },
        content = { BottomNavGraph(navController = navController) }
    )
}

// Dolny pasek nawigacyjny
@Composable
fun BottomMenu(navController: NavHostController) {
    val screens = listOf(
        BottomBar.TaskList,
        BottomBar.Grades
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        screens.forEach { screen ->
            NavigationBarItem(
                label = { Text(text = screen.title) },
                icon = { Icon(imageVector = screen.icon, contentDescription = screen.title) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {navController.navigate(screen.route)}
            )
        }
    }
}

// Definicja tras w nawigacji
@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screens.MyTaskListFrag.route
    ) {
        composable(route = Screens.MyTaskListFrag.route) { MyTaskListFrag(navController) }
        composable(route = Screens.MyGradesFrag.route) { MyGradesFrag() }
        composable(
            route = Screens.EachSubTasks.route,
            arguments = listOf(navArgument("listId") { type = NavType.IntType })
        ) { it ->
            val listId = it.arguments?.getInt("listId") ?: 0
            EachSubTasksFrag(exerciseList[listId])
        }
    }
}

// Lista zadan
@Composable
fun ListOfExercises(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(15.dp)) //
        Text(
            text = "Moje Listy Zadań",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(7.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            items(exerciseList) { exerciseList ->
                ExerciseItem(exerciseList ,navController)
            }
        }
    }
}




// Widok poszczególnego elementu listy
@Composable
fun ExerciseItem(exerciseList: ExerciseList, navController: NavHostController){
    val nb_of_list = rememberSaveable { Random.nextInt(1, 11) }
    val listId = exerciseList.id
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(1.dp)
            .clickable {
                navController.navigate(Screens.EachSubTasks.createRoute(listId))
            },
        shape = RoundedCornerShape(1.dp),

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFDF6E3))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = exerciseList.subject.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,

                )
                Text(

                    text = "Lista $nb_of_list",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(1.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "Liczba zadań: ${exerciseList.exercise.size}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = "Ocena : ${exerciseList.grade}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }


        }
    }
}

// Lista ocen
@Composable
fun ListOfGrades(){
    val grade_sub_pair = generateSubjectAverageList(exerciseList)
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(15.dp)) //
        Text(
            text = "Moje Oceny",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(7.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            items(grade_sub_pair) { grade_sub_pair ->
                GradesItem(grade_sub_pair)
            }
        }
    }
}

// Widok poszczególnego elementu z listy ocen
@Composable
fun GradesItem(
    subjectGradeElement : Pair<Subject, Double>,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(1.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFDF6E3))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(

                    text = subjectGradeElement.first.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "Srednia: " + subjectGradeElement.second.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Liczba list: ${Random.nextInt(1, 11)}",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

//Lista zadań z poszczególnego przedmiotu
@Composable
fun ListOfTask(exerciseList: ExerciseList) {
    var position = 0;
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(15.dp)) //
        Text(
            text = exerciseList.subject.name,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(7.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            itemsIndexed(exerciseList.exercise ) { index, task ->
                eachTaskView(task, index + 1)
            }
        }
    }
}

//Widok poszczególnego zadania
@Composable
fun eachTaskView(exercise: Exercise, position:Int){

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFBF2)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Zadanie ${position}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "pkt:${exercise.points}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF8E8E8E)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = exercise.content,
                fontSize = 16.sp,
                color = Color(0xFF3E3E3E)
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun ExerciseItemPreview(){
//    ListOfTask()
//}




