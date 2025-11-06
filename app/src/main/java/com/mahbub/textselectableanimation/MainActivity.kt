package com.mahbub.textselectableanimation

import TextSelectionAnimation
import TextTypeAndSelect
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hashtag.generator.ai.post.writer.presentation.component.CustomLoader
import com.mahbub.textselectableanimation.component.TypingAnimation
import com.mahbub.textselectableanimation.ui.theme.TextSelectableAnimationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TextSelectableAnimationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        Spacer(modifier = Modifier.height(36.dp))
                        TypingAnimation()

                        Spacer(modifier = Modifier.height(36.dp))
                        TextSelectionAnimation()
                        Spacer(modifier = Modifier.height(36.dp))


                        TextTypeAndSelect()

                        Spacer(modifier = Modifier.height(36.dp))

                        Box(modifier = Modifier.fillMaxWidth()
                            .height(300.dp)){
                           // CustomLoader()
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TextSelectableAnimationTheme {
        Greeting("Android")
    }
}