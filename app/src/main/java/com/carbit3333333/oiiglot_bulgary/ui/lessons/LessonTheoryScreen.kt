package com.carbit3333333.oiiglot_bulgary.ui.lessons


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.carbit3333333.oiiglot_bulgary.model.TheoryBlock

@Composable
fun LessonTheoryScreen(
    title: String,
    theory: List<TheoryBlock>,
    onBackClick: () -> Unit,
    onStartClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {

        // 🔹 Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Button(onClick = onBackClick) {
                Text("Назад")
            }
        }

        // 🔹 Контент
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            items(theory) { block ->
                TheoryItem(block)
            }
        }

        // 🔹 Кнопка
        Button(
            onClick = onStartClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Начать упражнения")
        }
    }
}

@Composable
fun TheoryItem(block: TheoryBlock) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            if (block.title != null) {
                Text(
                    text = block.title,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            if (block.text != null) {
                Text(
                    text = block.text,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}