package core

/**
 * Created by krishan on 20/07/20.
 */
/*
 0   1   2   3   4   5   6   7   8   9
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
internal class MockMineSweeperBoardGenerator : MineSweeperBoardGenerator() {
    val bombIds = listOf(0, 1, 2, 5, 8, 36, 37, 43, 57, 69, 70, 84, 90, 91, 98)

    override fun randomBombIds(level: LEVEL): List<Int> {
        if (level == LEVEL.INTERMEDIATE) {
            return bombIds
        } else throw IllegalStateException("Mocking Not supported")
    }

}
