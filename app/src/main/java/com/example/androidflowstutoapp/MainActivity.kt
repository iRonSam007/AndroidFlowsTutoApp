package com.example.androidflowstutoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidflowstutoapp.ui.theme.AndroidFlowsTutoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //From LifeCycle viewModel Compose
            val viewModel = viewModel<FlowViewModel>()
            //This is usually not practical to get Compose state from Flow
            //Usually u want to listen to flows and then decide in UI thread what to recompose..
            myUI(viewModel)

            /* Hot flow: StateFlow
            val counter = viewModel.stateFlow.collectAsState(0)
            Box(modifier = Modifier.fillMaxSize()){
                Button(onClick = { viewModel.incrementValue() }) {
                    Text(text = "counter: ${counter.value}")
                }
            }
            */

            /*
            val time = viewModel.countDownFlow.collectAsState(initial = 10)
            Text(
                text = time.value.toString(),
                fontSize = 30.sp
            )
            */
        }
    }
}



@Composable
fun myUI(vm: FlowViewModel){
    val timeState = vm.countDownFlow.collectAsState(initial = 10)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.2f)
            .background(Color.Green),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text("Hello Issam", modifier = Modifier
            .border(5.dp, Color.Black)
            .padding(5.dp))
        Text(
            text = timeState.value.toString(),
            modifier = Modifier
                .border(5.dp, Color.Black)
                .padding(5.dp)
        )

    }

}

//Collect flowState for XML: to be safe: TODO: check how to safely collect flow state in an XML project