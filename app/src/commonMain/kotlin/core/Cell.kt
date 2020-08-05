package core

/**
 * Created by krishan on 18/07/20.
 */

data class Cell(val id: Int, private val value: Value) {

    fun open(): State {
        if (state is State.Open) throw IllegalStateException("Already opened this cell")
        val correct = value !is Value.BOMB
        state = State.Open(value, correct = correct)
        return state
    }

    fun flag(): State {
        if (state is State.Open) throw IllegalStateException("Action already performed")
        val correct = value is Value.BOMB
        state = State.Close(value, correct = correct)
        return state
    }

    var state: State = State.Close(value, null)
        private set

    override fun toString(): String {
        return id.toString()
    }

    sealed class State(private val value: Value, val correct: Boolean?) {
        class Close(private val value: Value, correct: Boolean?) :
            State(value, correct)

        class Open(private val value: Value, correct: Boolean?) :
            State(value, correct)

        open fun getDisplayState(): String {
            return when (value) {
                Value.BOMB -> "-1"
                is Value.NUMBER -> {
                    if (value.number == 0) return ""
                    else value.number.toString()
                }

            }
        }
    }

}

sealed class Value {
    object BOMB : Value()
    class NUMBER(val number: Int) : Value()
}



