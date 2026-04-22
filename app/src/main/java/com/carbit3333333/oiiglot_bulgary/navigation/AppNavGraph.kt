package com.carbit3333333.oiiglot_bulgary.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.carbit3333333.oiiglot_bulgary.data.dictionary.PersonalDictionaryRepository
import com.carbit3333333.oiiglot_bulgary.ui.dictionary.DictionaryScreen
import com.carbit3333333.oiiglot_bulgary.ui.dictionary.FlashcardTrainingScreen
import com.carbit3333333.oiiglot_bulgary.ui.dictionary.WordEditorScreen
import com.carbit3333333.oiiglot_bulgary.ui.lessons.LessonResultScreen
import com.carbit3333333.oiiglot_bulgary.ui.lessons.LessonScreen
import com.carbit3333333.oiiglot_bulgary.ui.lessons.LessonSessionScreen
import com.carbit3333333.oiiglot_bulgary.ui.lessons.LessonsScreen
import com.carbit3333333.oiiglot_bulgary.viewmodel.FlashcardTrainingViewModel
import com.carbit3333333.oiiglot_bulgary.viewmodel.LessonResultViewModel
import com.carbit3333333.oiiglot_bulgary.viewmodel.WordEditorViewModel

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destinations.LESSONS
    ) {
        composable(Destinations.LESSONS) {
            LessonsScreen(
                onDictionaryClick = {
                    navController.navigate(Destinations.DICTIONARY_LIST)
                },
                onLessonClick = { lessonId ->
                    navController.navigate(Destinations.lessonDetailsRoute(lessonId))
                }
            )
        }

        composable(Destinations.DICTIONARY_LIST) {
            DictionaryScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onAddWordClick = {
                    navController.navigate(Destinations.dictionaryEditRoute())
                },
                onTrainAllClick = {
                    navController.navigate(Destinations.dictionaryTrainingRoute())
                },
                onTrainGroupClick = { group ->
                    navController.navigate(
                        Destinations.dictionaryTrainingRoute(
                            groupId = group.id,
                            groupName = group.name
                        )
                    )
                },
                onWordClick = { wordId ->
                    navController.navigate(Destinations.dictionaryEditRoute(wordId))
                }
            )
        }

        composable(
            route = Destinations.DICTIONARY_EDIT,
            arguments = listOf(
                navArgument("wordId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) { backStackEntry ->
            val context = LocalContext.current
            val repository = remember(context) { PersonalDictionaryRepository(context) }
            val wordId = backStackEntry.arguments?.getLong("wordId")?.takeIf { it > 0L }
            val wordEditorViewModel: WordEditorViewModel = viewModel(
                factory = WordEditorViewModel.provideFactory(
                    repository = repository,
                    wordId = wordId,
                )
            )

            WordEditorScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                viewModel = wordEditorViewModel,
            )
        }

        composable(
            route = Destinations.DICTIONARY_TRAINING,
            arguments = listOf(
                navArgument("groupId") {
                    type = NavType.LongType
                    defaultValue = -1L
                },
                navArgument("groupName") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val context = LocalContext.current
            val repository = remember(context) { PersonalDictionaryRepository(context) }
            val groupId = backStackEntry.arguments?.getLong("groupId")?.takeIf { it > 0L }
            val groupName = backStackEntry.arguments?.getString("groupName")?.takeIf { it.isNotBlank() }
            val flashcardTrainingViewModel: FlashcardTrainingViewModel = viewModel(
                factory = FlashcardTrainingViewModel.provideFactory(
                    repository = repository,
                    groupId = groupId,
                    groupName = groupName,
                )
            )

            FlashcardTrainingScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onFinishClick = {
                    navController.navigate(Destinations.DICTIONARY_LIST) {
                        popUpTo(Destinations.DICTIONARY_LIST) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                },
                viewModel = flashcardTrainingViewModel,
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
