package core

import core.BoardGenerator.Index
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Created by krishan on 18/07/20.
 */
class MineSweeperBoardGeneratorTest {

    @Test
    fun `test beginner level board`() {
        val generator = MineSweeperBoardGenerator()
        val board = generator.invoke(LEVEL.BEGINNER)
        var countBombs = 0
        board.forEachIndexed { _, array ->
            array.forEachIndexed { _, cell ->
                if (cell.state.getDisplayState() == "-1") {
                    countBombs++
                }
            }
        }
        assertThat(countBombs).isEqualTo(LEVEL.BEGINNER.bombs)
    }


    /***
     *0   1   2   3   4   5   6   7   8   9
    |X| |X| |X| | | |1| |X| |1| |1| |X| |1| --
    |2| |3| |2| | | |1| |1| |1| |1| |1| |1| --19
    | | | | | | | | |1| |1| |2| |2| |1| | | --29
    | | | | |1| |1| |1| |1| |X| |X| |1| | | --39
    | | | | |1| |X| |1| |1| |3| |3| |2| | | --49
    | | | | |1| |1| |1| | | |1| |X| |2| |1| --59
    |1| |1| | | | | | | | | |1| |1| |2| |X| --69
    |X| |1| | | |1| |1| |1| | | | | |1| |1| --79
    |3| |3| |1| |1| |X| |1| | | |1| |1| |1| --89
    |X| |X| |1| |1| |1| |1| | | |1| |X| |1| --99
     */
    @Test
    fun `check neighbouring cells of a given cell`() {
        val generator = MineSweeperBoardGenerator()
        val bombIds = listOf(0, 1, 2, 5, 8, 36, 37, 43, 57, 69, 70, 84, 90, 91, 98)
        assertThat(
            generator.countNeighbouringBombs(
                bombIds,
                id = 4,
                columnSize = 10,
                rowSize = 10
            ).first
        )
            .isEqualTo(1)
        assertThat(
            generator.countNeighbouringBombs(
                bombIds,
                id = 10,
                columnSize = 10,
                rowSize = 10
            ).first
        )
            .isEqualTo(2)
        assertThat(
            generator.countNeighbouringBombs(
                bombIds,
                id = 89,
                columnSize = 10,
                rowSize = 10
            ).first
        )
            .isEqualTo(1)
        assertThat(
            generator.countNeighbouringBombs(
                bombIds,
                id = 88,
                columnSize = 10,
                rowSize = 10
            ).first
        )
            .isEqualTo(1)
        val countNeighbouringBombs =
            generator.countNeighbouringBombs(bombIds, id = 99, columnSize = 10, rowSize = 10)
        println(countNeighbouringBombs.second)
        assertThat(countNeighbouringBombs.first).isEqualTo(1)
    }

    /***
     *0   1   2   3   4   5   6   7   8   9
    |X| |X| |X| | | |1| |X| |1| |1| |X| |1| --
    |2| |3| |2| | | |1| |1| |1| |1| |1| |1| --19
    | | | | | | | | |1| |1| |2| |2| |1| | | --29
    | | | | |1| |1| |1| |1| |X| |X| |1| | | --39
    | | | | |1| |X| |1| |1| |3| |3| |2| | | --49
    | | | | |1| |1| |1| | | |1| |X| |2| |1| --59
    |1| |1| | | | | | | | | |1| |1| |2| |X| --69
    |X| |1| | | |1| |1| |1| | | | | |1| |1| --79
    |3| |3| |1| |1| |X| |1| | | |1| |1| |1| --89
    |X| |X| |1| |1| |1| |1| | | |1| |X| |1| --99
     */
    @Test
    fun `test cell neighbour ids`() {
        assertThat(getNeighbours(99)).contains(98)
        assertThat(getNeighbours(80)).contains(70, 90, 91)
        assertThat(getNeighbours(7)).contains(8)
        assertThat(getNeighbours(10)).contains(0, 1)
        assertThat(getNeighbours(46)).contains(36, 37, 57)
        assertThat(getNeighbours(30)).isEmpty()
    }

    private fun getNeighbours(cellId: Int): List<Int> {
        val bombIds = listOf(0, 1, 2, 5, 8, 36, 37, 43, 57, 69, 70, 84, 90, 91, 98)
        val generator = MineSweeperBoardGenerator()
        return generator.countNeighbouringBombs(
            bombIds,
            id = cellId,
            columnSize = 10,
            rowSize = 10
        ).second
    }


    @Test
    fun `test encode and decode id `() {
        val mineSweeperBoardGenerator = MineSweeperBoardGenerator()
        val generateUniqueId = mineSweeperBoardGenerator.generateUniqueId(
            rowIndex = 1, columnIndex = 0, columnSize = 10
        )
        assertThat(generateUniqueId).isEqualTo(10)
        val cellIndices = mineSweeperBoardGenerator.getCellIndices(10, 10)
        assertThat(cellIndices).isEqualTo(Index(row = 1, column = 0))

        val generateUniqueId1 = mineSweeperBoardGenerator.generateUniqueId(
            rowIndex = 0, columnIndex = 5, columnSize = 10
        )
        assertThat(generateUniqueId1).isEqualTo(5)
        val cellIndices1 = mineSweeperBoardGenerator.getCellIndices(5, 10)
        assertThat(cellIndices1).isEqualTo(Index(row = 0, column = 5))

        val generateUniqueId2 = mineSweeperBoardGenerator.generateUniqueId(
            rowIndex = 5, columnIndex = 8, columnSize = 10
        )
        assertThat(generateUniqueId2).isEqualTo(58)
        val cellIndices2 = mineSweeperBoardGenerator.getCellIndices(58, 10)
        assertThat(cellIndices2).isEqualTo(Index(row = 5, column = 8))

        val generateUniqueId3 = mineSweeperBoardGenerator.generateUniqueId(
            rowIndex = 0, columnIndex = 0, columnSize = 10
        )
        assertThat(generateUniqueId3).isEqualTo(0)
        val cellIndices3 = mineSweeperBoardGenerator.getCellIndices(0, 10)
        assertThat(cellIndices3).isEqualTo(Index(row = 0, column = 0))

        val cellIndices4 = mineSweeperBoardGenerator.getCellIndices(99, 10)
        assertThat(cellIndices4).isEqualTo(Index(row = 9, column = 9))
    }

    @Test
    fun `test cell at borders`() {
        val generator = MineSweeperBoardGenerator()
        val level = LEVEL.BEGINNER
        val cells = generator.invoke(level)
        val cell = cells[9][9]
        assertThat(cell.isAtRightBorder(level.columns)).isEqualTo(true)
        assertThat(cell.isAtLeftBorder(level.columns)).isEqualTo(false)
        assertThat(cell.isAtTopBorder(level.columns)).isEqualTo(false)
        assertThat(cell.isAtBottomBorder(level.columns, level.rows)).isEqualTo(true)
        val cell1 = cells[8][9]
        assertThat(cell1.isAtRightBorder(level.columns)).isEqualTo(true)
        assertThat(cell1.isAtLeftBorder(level.columns)).isEqualTo(false)
        assertThat(cell1.isAtTopBorder(level.columns)).isEqualTo(false)
        assertThat(cell1.isAtBottomBorder(level.columns, level.rows)).isEqualTo(false)
        val cell2 = cells[0][9]
        assertThat(cell2.isAtRightBorder(level.columns)).isEqualTo(true)
        assertThat(cell2.isAtLeftBorder(level.columns)).isEqualTo(false)
        assertThat(cell2.isAtTopBorder(level.columns)).isEqualTo(true)
        assertThat(cell2.isAtBottomBorder(level.columns, level.rows)).isEqualTo(false)
        val cell3 = cells[0][0]
        assertThat(cell3.isAtRightBorder(level.columns)).isEqualTo(false)
        assertThat(cell3.isAtLeftBorder(level.columns)).isEqualTo(true)
        assertThat(cell3.isAtTopBorder(level.columns)).isEqualTo(true)
        assertThat(cell3.isAtBottomBorder(level.columns, level.rows)).isEqualTo(false)
        val cell4 = cells[9][0]
        assertThat(cell4.isAtRightBorder(level.columns)).isEqualTo(false)
        assertThat(cell4.isAtLeftBorder(level.columns)).isEqualTo(true)
        assertThat(cell4.isAtTopBorder(level.columns)).isEqualTo(false)
        assertThat(cell4.isAtBottomBorder(level.columns, level.rows)).isEqualTo(true)

        val cell5 = cells[4][4]
        assertThat(cell5.isAtRightBorder(level.columns)).isEqualTo(false)
        assertThat(cell5.isAtLeftBorder(level.columns)).isEqualTo(false)
        assertThat(cell5.isAtTopBorder(level.columns)).isEqualTo(false)
        assertThat(cell5.isAtBottomBorder(level.columns, level.rows)).isEqualTo(false)

    }

    @Test
    fun `test cell's all possible neighbours`() {
        val generator = MineSweeperBoardGenerator()
        val level = LEVEL.BEGINNER
        val cells = generator.invoke(level)
        val cell = cells[0][9]
        val neighbours = cell.neighbourIds(level.rows, level.columns)
        assertThat(neighbours).containsExactlyInAnyOrder(8, 18, 19)
        val cell2 = cells[3][2]
        val neighbours2 = cell2.neighbourIds(level.rows, level.columns)
        assertThat(neighbours2).containsExactlyInAnyOrder(31, 21, 41, 33, 23, 43, 42, 22)
    }

}