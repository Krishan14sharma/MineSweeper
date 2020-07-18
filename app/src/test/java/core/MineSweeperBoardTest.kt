package core

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Created by krishan on 18/07/20.
 */
class MineSweeperBoardTest {

    @Test
    fun `test create a Board for different levels`() {
        val mineSweeperBoard = MineSweeperBoard(LEVEL.BEGINNER, BoardGenerator())
        assertThat(mineSweeperBoard.noOfCells).isEqualTo(100)
        assertThat(mineSweeperBoard.cells.size * mineSweeperBoard.cells[0].size).isEqualTo(100)

        val mineSweeperIntermediate = MineSweeperBoard(LEVEL.INTERMEDIATE, BoardGenerator())
        assertThat(mineSweeperIntermediate.noOfCells).isEqualTo(256)
        assertThat(mineSweeperIntermediate.cells.size * mineSweeperIntermediate.cells[0].size).isEqualTo(
            256
        )

        val mineSweeperHard = MineSweeperBoard(LEVEL.HARD, BoardGenerator())
        assertThat(mineSweeperHard.noOfCells).isEqualTo(480)
        assertThat(mineSweeperHard.cells.size * mineSweeperHard.cells[0].size).isEqualTo(480)

    }

    @Test
    fun `test minesweeper cell ids correctly and uniquely assigned`() {
        val mineSweeperBoard = MineSweeperBoard(LEVEL.HARD, BoardGenerator())
        var i = 0
        print(mineSweeperBoard.cells.contentDeepToString())
        mineSweeperBoard.traverseCells {
            assertThat(it.id).isEqualTo(i)
            i++
        }
    }

    @Test
    fun `print minesweeper board`() {
        val mineSweeperBoard = MineSweeperBoard(LEVEL.BEGINNER, BoardGenerator())
        mineSweeperBoard.traverseCells { cell ->
            print(" ")
            cell.prettyPrint()
            if ((cell.id + 1) % LEVEL.BEGINNER.columns == 0) println()
        }
    }

    fun Cell.prettyPrint() {
        val displayState = state.getDisplayState()
        val prettyState = if (displayState.isEmpty()) {
            " "
        } else if (displayState == "-1") {
            "X"
        } else displayState
        print("|$prettyState|")
    }

}