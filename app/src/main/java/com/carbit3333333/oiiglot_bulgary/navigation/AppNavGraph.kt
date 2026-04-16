package com.carbit3333333.oiiglot_bulgary.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.carbit3333333.oiiglot_bulgary.ui.home.HomeScreen
import com.carbit3333333.oiiglot_bulgary.ui.lessons.LessonResultScreen
import com.carbit3333333.oiiglot_bulgary.ui.lessons.LessonScreen
import com.carbit3333333.oiiglot_bulgary.ui.lessons.LessonSessionScreen
import com.carbit3333333.oiiglot_bulgary.ui.lessons.LessonsScreen
import com.carbit3333333.oiiglot_bulgary.viewmodel.LessonResultViewModel

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destinations.HOME
    ) {
        composable(Destinations.HOME) {
            HomeScreen(
                onStartClick = {
                    navController.navigate(Destinations.LESSONS)
                }
            )
        }

        composable(Destinations.LESSONS) {
            LessonsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onLessonClick = { lessonId ->
                    navController.navigate(Destinations.lessonDetailsRoute(lessonId))
                }
            )
        }

        composable(
            route = "${Destinations.LESSON_DETAILS}/{lessonId}",
            arguments = listOf(
                navArgument("lessonId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val lessonId = backStackEntry.arguments?.getInt("lessonId") ?: 0

            LessonScreen(
                lessonId = lessonId,
                onBackClick = {
                    navController.popBackStack()
                },
                onStartExerciseClick = { id ->
                    navController.navigate(Destinations.lessonSessionRoute(id))
                }
            )
        }

        composable(
            route = "${Destinations.LESSON_SESSION}/{lessonId}",
            arguments = listOf(
                navArgument("lessonId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val lessonId = backStackEntry.arguments?.getInt("lessonId") ?: 0

            LessonSessionScreen(
                lessonId = lessonId,
                onBackClick = {
                    navController.popBackStack()
                },
                onLessonFinished = { correctCount, wrongCount ->
                    navController.navigate(
                        Destinations.lessonResultRoute(
                            lessonId = lessonId,
                            correctCount = correctCount,
                            wrongCount = wrongCount
                        )
                    )
                }
            )
        }

        composable(
            route = "${Destinations.LESSON_RESULT}/{lessonId}/{correctCount}/{wrongCount}",
            arguments = listOf(
                navArgument("lessonId") {
                    type = NavType.IntType
                },
                navArgument("correctCount") {
                    type = NavType.IntType
                },
                navArgument("wrongCount") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val lessonId = backStackEntry.arguments?.getInt("lessonId") ?: 0
            val correctCount = backStackEntry.arguments?.getInt("correctCount") ?: 0
            val wrongCount = backStackEntry.arguments?.getInt("wrongCount") ?: 0

            val viewModel: LessonResultViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsState()

            LaunchedEffect(lessonId, correctCount, wrongCount) {
                viewModel.loadLessonResult(
                    lessonId = lessonId,
                    correctCount = correctCount,
                    wrongCount = wrongCount
                )
            }

            uiState.result?.let { result ->
                LessonResultScreen(
                    result = result,
                    hasNextLesson = uiState.hasNextLesson,
                    onRetryClick = {
                        navController.navigate(Destinations.lessonSessionRoute(lessonId))
                    },
                    onNextLessonClick = {
                        val nextLessonId = uiState.nextLessonId ?: return@LessonResultScreen
                        navController.navigate(Destinations.lessonDetailsRoute(nextLessonId))
                    },
                    onBackToLessonsClick = {
                        navController.navigate(Destinations.LESSONS) {
                            popUpTo(Destinations.LESSONS) {
                                inclusive = false
                            }
                        }
                    }
                )
            }
        }
    }
}