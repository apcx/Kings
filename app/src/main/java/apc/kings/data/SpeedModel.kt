package apc.kings.data

object SpeedModel {

    private val models = mapOf( // http://bbs.ngacn.cc/read.php?tid=12677614
            2 to intArrayOf(17, 16, 92, 168, 274, 378, 532, 698, 910, 1168, 1532)
    )

    @JvmStatic fun getAttackCd(model: Int, speed: Int) = models[model]!!.run {
        for (index in size - 1 downTo 0) {
            if (speed >= this[index]) {
                return@run this[0] - index
            }
        }
        this[0]
    } * 66
}