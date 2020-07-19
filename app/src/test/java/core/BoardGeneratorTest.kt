package core

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
        board.forEachIndexed { i, array ->
            array.forEachIndexed { j, cell ->
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
        assertThat(generator.countNeighbouringBombs(bombIds, id = 4, columnSize = 10).first)
            .isEqualTo(1)
        assertThat(generator.countNeighbouringBombs(bombIds, id = 10, columnSize = 10).first)
            .isEqualTo(2)
        assertThat(generator.countNeighbouringBombs(bombIds, id = 89, columnSize = 10).first)
            .isEqualTo(1)
        assertThat(generator.countNeighbouringBombs(bombIds, id = 88, columnSize = 10).first)
            .isEqualTo(1)
        val countNeighbouringBombs =
            generator.countNeighbouringBombs(bombIds, id = 99, columnSize = 10)
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
        return generator.countNeighbouringBombs(bombIds, id = cellId, columnSize = 10).second
    }
}