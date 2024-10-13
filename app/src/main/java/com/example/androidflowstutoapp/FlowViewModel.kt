package com.example.androidflowstutoapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class FlowViewModel: ViewModel() {

    //This is a cold flow: it needs collector to emmit
    //unlike hotFlow who emits anyway
    val countDownFlow = flow<Int> {
        //flow is a function (or more specifically, a builder function) that creates and returns an instance of Flow<T>.
        val startingValue = 100
        var currentValue = startingValue
        emit(currentValue)
        while (currentValue > 0) {
            delay(1000L)
            currentValue--
            emit(currentValue)
        }
    }

    /* StateFlow
    //Here: StateFlow, used to keep State, are like liveData but without context awareness, can't detect when activity goes to background for example
    //Its like liveData => it will nullify its collector or its observers, it will keep a single value
    //NB: underscore _x mean x is mutable
    private val _stateFlow =
        MutableStateFlow(0) // Its private so only the viewModel can change and mutable (eventhough val since its object it internal state can change)
    val stateFlow =
        _stateFlow.asStateFlow()            // immutable version of our stateFlow that our UI can collect or subscribe to

    fun incrementValue() {
        _stateFlow.value++
    }
    */


    //Here: ShareFlow is designed to emit one time event: like channels
    //NB: Weird but they need to be defined before collecting and emission in code
    private val _sharedFlow = MutableSharedFlow<Int>(replay = 5) //Replay: it will keep 5 emission in cached, now u can emit before collect execution
    val sharedFlow = _sharedFlow.asSharedFlow()


    fun squareNumber(number: Int) {
        viewModelScope.launch {
            _sharedFlow.emit(number * number)
        }
    }


    init {
        //transformOperatorFlow()
        //terminalOperatorFlow()
        //combineFlows()
        //realWorldFlowExample()
        //hotFlow: StateFlow check UI and XML, SharedFlow as below:


        //Here: if squareNumber called before collect called, this will throw error
        squareNumber(3)


        viewModelScope.launch {
            sharedFlow.collect {
                delay(2000L)
                println("FirstFlow: The collected number is $it")
            }
        }
        viewModelScope.launch {
            sharedFlow.collect {
                delay(3000L)
                println("SecondFlow: The collected number is $it")
            }
        }




    }








}
/*
    private fun realWorldFlowExample(){
        val flow = flow{
            delay(250L)
            emit("Appetizer")
            delay(1000L)
            emit("Main dish")
            delay(100L)
            emit("Desert")
        }
        viewModelScope.launch {
            flow.onEach {
                println("mFlow: $it is delivered ++ ")
            }
                //.buffer()
                //Collect here, will slow down the emission of flows value. => hence the Use buffer to run the emission and collect in different coroutine
                //Meaning: collect will not run sequentially, even if its not finished yet, next emission ill be done

                //.conflate()
                //Same as buffer + If you have two emission at the same time, collect will go for the latest(resamble collectLatest)
                //Difference will be that collectLatest will drop handling current emission for new emission

                .collectLatest{
                    println("mFlow: Now eating $it")
                    delay(500L)
                    println("mFlow: Finished eating $it")
                }
        }
    }

    private fun combineFlows() {
        val flow1 = flow {
            emit(1)
            delay(500L)
            emit(2)
        }
        val flow2 = (1..5).asFlow()

        viewModelScope.launch {
            flow1
                .flatMapConcat { value ->
                    flow {
                        emit(value + 1)
                        delay(500L)
                        emit(value + 2)
                    }
                }
                .collect { value ->
                    println("b FlatteningFlows value: $value")
                }
        }
    }


    private fun terminalOperatorFlow(){
       viewModelScope.launch {
           val reducedFlowValue = countDownFlow.reduce { accumulator, value -> accumulator + value  }
           println("this is the last time reduce value is:  $reducedFlowValue")

           //Here: Fold is basically the same as reduce but with initial value
           //val foldedFlowValue = countDownFlow.fold(10){acc, value -> acc + value }

           //Here: the most used operator simply collecting value
           //countDownFlow.collect{ value -> println("Do something") }

           //Here: count operator
           //val countedFlowValue = countDownFlow.count{ it % 2 == 0 }

       }

   }

    //Here using flow with:
    // Transformer op: onEach, filter, map
    private fun transformOperatorFlow() {
        viewModelScope.launch {
            countDownFlow
                .filter { time -> time % 2 == 0 }
                .map { time -> time * time }
                .onEach { it -> println("Collection value now $it") }
        }
    }
}


*/