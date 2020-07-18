package core

/**
 * Created by krishan on 18/07/20.
 */

class Cell(val id: Int, private val cellState: CellState) {

    fun open(): State {
        val correct = cellState !is CellState.BOMB
        state = State.Open(cellState, correct = correct)
        return state
    }

    fun flag(): State {
        if (state is State.Open) throw IllegalStateException("Action already performed")
        val correct = cellState is CellState.BOMB
        state = State.Close(cellState, correct = correct)
        return state
    }

    var state: State = State.Close(cellState, null)
        private set


    sealed class State(private val cellState: CellState, val correct: Boolean?) {
        class Close(private val cellState: CellState, correct: Boolean?) :
            State(cellState, correct)

        class Open(private val cellState: CellState, correct: Boolean?) :
            State(cellState, correct)

        open fun getDisplayState(): String {
            return when (cellState) {
                CellState.BOMB -> "-1"
                is CellState.Value -> {
                    if (cellState.number == 0) return ""
                    else cellState.number.toString()
                }

            }
        }
    }

}

sealed class CellState {
    object BOMB : CellState()
    class Value(val number: Int) : CellState()
}



