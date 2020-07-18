package core

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Created by krishan on 18/07/20.
 */
class BoardGeneratorTest {

    @Test
    fun `test beginner level board`() {
        val generator = BoardGenerator()
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

    @Test
    fun `check neighbouring cells of a given cell`() {
        val generator = BoardGenerator()
        val bombIds = listOf(0, 1, 2, 5, 8)
        assertThat(generator.countNeighbouringBombs(bombIds, id = 4, columnSize = 4)).isEqualTo(4)
        assertThat(generator.countNeighbouringBombs(bombIds, id = 6, columnSize = 4)).isEqualTo(3)
        assertThat(generator.countNeighbouringBombs(bombIds, id = 15, columnSize = 4)).isEqualTo(0)
        assertThat(generator.countNeighbouringBombs(bombIds, id = 15, columnSize = 4)).isEqualTo(0)
        assertThat(generator.countNeighbouringBombs(bombIds, id = 12, columnSize = 4)).isEqualTo(1)

    }

}