package core

import kotlin.js.JsName

/**
 * Created by krishan on 18/07/20.
 */

data class Cell(val id: Int, @JsName("value") private val value: Value) {

    @JsName("state")
    var state: State = State.Close(value)
        private set

    fun open(): State.Open {
        if (state is State.Open) throw IllegalStateException("Already opened this cell")
        val correct = value !is Value.BOMB
        state = State.Open(value, correct = correct)
        return state as State.Open
    }

    fun flag(): State.Flag {
        if (state is State.Open) throw IllegalStateException("Action already performed")
        val correct = value is Value.BOMB
        state = State.Flag(value, correct = correct)
        return state as State.Flag
    }

    fun unFlag(): State.Close {
        if (state is State.Open) throw IllegalStateException("Action already performed")
        state = State.Close(value)
        return state as State.Close
    }

    override fun toString(): String {
        return id.toString()
    }

    @JsName("State")
    sealed class State(private val value: Value) {
        class Close(private val value: Value) :
            State(value)

        class Open(private val value: Value, val correct: Boolean?) :
            State(value)

        class Flag(private val value: Value, val correct: Boolean?) :
            State(value)

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



