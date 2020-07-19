package core

interface BoardGenerator {
    operator fun invoke(level: LEVEL): Array<Array<Cell>>
}

class MineSweeperBoardGenerator : BoardGenerator {

    override operator fun invoke(level: LEVEL): Array<Array<Cell>> {
        return fillCells(level)
    }

    private fun fillCells(level: LEVEL): Array<Array<Cell>> {
        val bombIds = randomBombIds(level)
        return Array(level.rows) { i ->
            Array(level.columns) { j ->
                val id = generateUniqueId(j, level.columns, i)
                if (bombIds.contains(id)) {
                    Cell(id, Value.BOMB)
                } else {
                    val value = countNeighbouringBombs(bombIds, id, level.columns).first
                    Cell(id, Value.NUMBER(value))
                }
            }
        }
    }

    private fun generateUniqueId(columnIndex: Int, columnSize: Int, rowIndex: Int): Int {
        return columnIndex + columnSize * rowIndex
    }

    internal fun randomBombIds(level: LEVEL): List<Int> {
        val totalCells = level.columns * level.rows
        return (0 until totalCells).shuffled().take(level.bombs).sorted()
    }


    internal fun countNeighbouringBombs(bombIds: List<Int>, id: Int, columnSize: Int)
            : Pair<Int, List<Int>> {

        //neighbours to right
        val r1 = id + 1
        val r2 = id - columnSize + 1
        val r3 = id + columnSize + 1

        // neighbours to left
        val l1 = id - 1
        val l2 = id - columnSize - 1
        val l3 = id + columnSize - 1

        // top
        val t1 = id + columnSize
        // bottom
        val b1 = id - columnSize
        val rightColumn = arrayOf(r1, r2, r3)
        val leftColumn = arrayOf(l1, l2, l3)

        val arrayOfNeighbours = mutableListOf(*leftColumn, *rightColumn, t1, b1)

        if (isAtRightBorder(id, columnSize)) {
            arrayOfNeighbours.removeAll(rightColumn)
        } else if (isAtLeftBorder(id, columnSize)) {
            arrayOfNeighbours.removeAll(leftColumn)
        }


        var totalBombs = 0
        val bombsNear = mutableListOf<Int>()
        arrayOfNeighbours.forEach {
            if (bombIds.contains(it)) {
                totalBombs++
                bombsNear.add(it)
            }
        }
        return totalBombs to bombsNear
    }

    private fun isAtRightBorder(id: Int, columnSize: Int) = (id + 1) % columnSize == 0

    private fun isAtLeftBorder(id: Int, columnSize: Int) = id % columnSize == 0


}
