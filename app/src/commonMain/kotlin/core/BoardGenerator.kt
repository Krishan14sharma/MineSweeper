package core

class BoardGenerator() {

    operator fun invoke(level: LEVEL): Array<Array<Cell>> {
        return fillCells(level)
    }

    private fun fillCells(level: LEVEL): Array<Array<Cell>> {
        val bombIds = randomBombIds(level)
        return Array(level.rows) { i ->
            Array(level.columns) { j ->
                val id = j + level.columns * i
                if (bombIds.contains(id)) {
                    Cell(id, Value.BOMB)
                } else {
                    val value = countNeighbouringBombs(bombIds, id, level.columns)
                    Cell(id, Value.NUMBER(value))
                }
            }
        }
    }

    private fun randomBombIds(level: LEVEL): List<Int> {
        val totalCells = level.columns * level.rows
        return (0 until totalCells).shuffled().take(level.bombs).sorted()
    }


    internal fun countNeighbouringBombs(bombIds: List<Int>, id: Int, columnSize: Int): Int {
        val n1 = id - 1
        val n2 = id + 1
        val n3 = id - columnSize
        val n4 = n3 - 1
        val n5 = n3 + 1
        val n6 = id + columnSize
        val n7 = n6 - 1
        val n8 = n6 + 1
        val arrayOfNeighbours = arrayOf(n1, n2, n3, n4, n5, n6, n7, n8)
        var totalBombs = 0
        arrayOfNeighbours.forEach {
            if (bombIds.contains(it)) {
                totalBombs++
            }
        }
        return totalBombs
    }


}
