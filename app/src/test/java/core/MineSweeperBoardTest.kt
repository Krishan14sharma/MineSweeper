package core

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Created by krishan on 18/07/20.
 */
class MineSweeperBoardTest {

    @Test
    fun `test create a Board for different levels`() {
        val mineSweeperBoard = getBegMineSweeperBoard()
        assertThat(mineSweeperBoard.noOfCells).isEqualTo(100)
        assertThat(mineSweeperBoard.getCells().size).isEqualTo(100)

        val mineSweeperIntermediate =
            MineSweeperBoard(LEVEL.INTERMEDIATE, MineSweeperBoardGenerator())
        assertThat(mineSweeperIntermediate.noOfCells).isEqualTo(256)
        assertThat(mineSweeperIntermediate.getCells().size).isEqualTo(
            256
        )

        val mineSweeperHard = MineSweeperBoard(LEVEL.HARD, MineSweeperBoardGenerator())
        assertThat(mineSweeperHard.noOfCells).isEqualTo        (480)
        assertThat(mineSweeperHard.getCells().size).isEqualTo(480)

    }

    @Test
    fun `test minesweeper cell ids correctly and uniquely assigned`() {
        val mineSweeperBoard = MineSweeperBoard(LEVEL.HARD, MineSweeperBoardGenerator())
        var i = 0
        mineSweeperBoard.traverseCells {
            assertThat(it.id).isEqualTo(i)
            i++
        }
    }

    @Test
    fun `print minesweeper board`() {
        val mineSweeperBoard = getBegMineSweeperBoard()
        mineSweeperBoard.traverseCells { cell ->
            cell.prettyPrint()
            if ((cell.id + 1) % LEVEL.BEGINNER.columns == 0) println()
        }
    }

    private fun Cell.prettyPrint() {
        val displayState = state.getDisplayState()
        val prettyState = if (displayState.isEmpty()) {
            " "
        } else if (displayState == "-1") {
            "X"
        } else displayState
        print("$prettyState|")
    }

    @Test
    fun `test user opening a bomb cell notifies user about the wrong move`() {
        val mineSweeperBoardGenerator = MockMineSweeperBoardGenerator()
        val mineSweeperBoard = MineSweeperBoard(LEVEL.BEGINNER, mineSweeperBoardGenerator)
        val cells = mineSweeperBoard.getCells()
        val bombIds = mineSweeperBoardGenerator.bombIds
        val openingCellId = bombIds[0]
        val value = object : MineSweeperBoardListener {
            override fun inCorrectMove(cell: Cell) {
                assertThat(cell.id).isEqualTo(openingCellId)
            }

            override fun correctMove(cell: Cell, openCells: List<Cell>) {
                assert(false)
            }

        }
        mineSweeperBoard.mineSweeperBoardListener = value
        mineSweeperBoard.open(openingCellId)
    }

    @Test
    fun `test user opening a number cell other than 0`() {
        val mineSweeperBoardGenerator = MockMineSweeperBoardGenerator()
        val mineSweeperBoard = MineSweeperBoard(LEVEL.BEGINNER, mineSweeperBoardGenerator)
        val cells = mineSweeperBoard.getCells()
        val bombIds = mineSweeperBoardGenerator.bombIds
        val openingCellId = cells.filter { !bombIds.contains(it.id) }[0].id
        val value = object : MineSweeperBoardListener {
            override fun inCorrectMove(cell: Cell) {
                assert(false)
            }

            override fun correctMove(cell: Cell, openCells: List<Cell>) {
                assertThat(cell.id).isEqualTo(openingCellId)
                assertThat(openCells).isEmpty()
            }
        }
        mineSweeperBoard.mineSweeperBoardListener = value
        mineSweeperBoard.open(openingCellId)
    }

    @Test
    fun `test user opening a 0 cell`() {
        val mineSweeperBoardGenerator = MockMineSweeperBoardGenerator()
        val mineSweeperBoard = MineSweeperBoard(LEVEL.BEGINNER, mineSweeperBoardGenerator)
        val cells = mineSweeperBoard.getCells()
        val bombIds = mineSweeperBoardGenerator.bombIds
        val openingCellId = 20
        val value = object : MineSweeperBoardListener {
            override fun inCorrectMove(cell: Cell) {
                assert(false)
            }

            override fun correctMove(cell: Cell, openCells: List<Cell>) {
                assertThat(cell.id).isEqualTo(openingCellId)
                val openCellIds: List<Int> = openCells.map { it.id }
                print(openCellIds.toString())
                assertThat(openCellIds)
                    .containsExactlyInAnyOrder(21, 22, 23, 24, 30, 31, 40, 41, 50, 51)
            }
        }
        mineSweeperBoard.mineSweeperBoardListener = value
        mineSweeperBoard.open(openingCellId)
    }

    private fun getBegMineSweeperBoard() =
        MineSweeperBoard(LEVEL.BEGINNER, MineSweeperBoardGenerator())


    @Test
    fun `test `() {
    }

}