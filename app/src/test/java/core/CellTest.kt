package core

import org.junit.Test

/**
 * Created by krishan on 18/07/20.
 */
class CellTest {

    @Test
    fun `test create cell initial state`() {
        val cellView = Cell(0, CellState.BOMB)
        assert(cellView.state is Cell.State.Close)
        assert(cellView.state.correct == null)
    }

    @Test
    fun `test cell open changes cell state`() {
        val cellView = Cell(0, CellState.BOMB)
        cellView.open()
        assert(cellView.state is Cell.State.Open)
    }

    @Test
    fun `test cell flag does not change cell state`() {
        val cellView = Cell(0, CellState.BOMB)
        cellView.flag()
        assert(cellView.state is Cell.State.Close)
    }

    @Test
    fun `test bomb cell explodes on opening`() {
        val cellView = Cell(0, CellState.BOMB)
        val state = cellView.open()
        assert(state.correct == false)
    }

    @Test
    fun `test bomb cell does not explode on flagging`() {
        val cellView = Cell(0, CellState.BOMB)
        val state = cellView.flag()
        assert(state.correct == true)
    }

    @Test
    fun `test safe cell opening`() {
        val cellView = Cell(0, CellState.Value(4))
        val state = cellView.open()
        assert(state.correct == true)
        assert(state.getDisplayState() == "4")
    }

    @Test
    fun `test safe cell flagging`() {
        val cellView = Cell(0, CellState.Value(4))
        val state = cellView.flag()
        assert(state.correct == false)
    }

    @Test(expected = IllegalStateException::class)
    fun `test open cell can not be flagged`() {
        val cellView = Cell(0, CellState.Value(4))
        val state = cellView.open()
        cellView.flag()
    }

    @Test
    fun `test flagged cell can be opened`() {
        val cellView = Cell(0, CellState.Value(4))
        val state = cellView.flag()
        val newState = cellView.open()
        assert(newState.correct == true)
    }

    @Test
    fun `test cell id is correctly returned`() {
        val cellView = Cell(1234, CellState.Value(4))
        cellView.flag()
        assert(cellView.id == 1234)
    }

    @Test
    fun `test bomb cell display`() {
        val cellView = Cell(1234, CellState.BOMB)
        val state = cellView.flag()
        assert(state.correct == true)
        assert(state.getDisplayState() == "-1")

    }

}