package se.mattec.advent2021

import sumByLong

fun main() {
    println(Day16.problem1())
    println(Day16.problem2())
}

object Day16 {

    fun problem1(): String {
        val p = extract(data)
        return p.versionSum().toString()
    }

    fun problem2(): String {
        val p = extract(data)
        return p.value().toString()
    }

    private fun extract(input: String): Package {
        val version = input.substring(0, 0 + 3).toLong(2)
        val typeId = input.substring(3, 3 + 3).toLong(2)
        when (typeId) {
            // Literal
            4L -> {
                var payload = ""
                var index = 0
                do {
                    val lastGroup = input[6 + index] == '0'
                    payload += input.substring(7 + index, 7 + 4 + index)
                    index += 5
                } while (!lastGroup)
                return Package(version, typeId, payload.toLong(2), emptyList(), 3 + 3 + index)
            }
            // Operator
            else -> {
                val lengthTypeId = input.substring(6, 7)
                when (lengthTypeId) {
                    "0" -> {
                        val numberOfBits = input.substring(7, 7 + 15).toInt(2)
                        val packages = mutableListOf<Package>()
                        var string = input.substring(3 + 3 + 1 + 15, 3 + 3 + 1 + 15 + numberOfBits)
                        while (string.isNotEmpty()) {
                            val p = extract(string)
                            packages += p
                            string = string.substring(p.length)
                        }
                        return Package(version, typeId, null, packages, 3 + 3 + 1 + 15 + numberOfBits)
                    }
                    "1" -> {
                        val numberOfSubPackages = input.substring(7, 7 + 11).toInt(2)
                        val packages = mutableListOf<Package>()
                        var string = input.substring(3 + 3 + 1 + 11)
                        while (packages.size < numberOfSubPackages) {
                            val p = extract(string)
                            packages += p
                            string = string.substring(p.length)
                        }
                        return Package(version, typeId, null, packages, 3 + 3 + 1 + 11 + packages.sumBy { it.length })
                    }
                }
            }
        }
        error("Should have returned by now.")
    }

    class Package(
            val version: Long,
            val typeId: Long,
            val payload: Long?,
            val packages: List<Package>,
            val length: Int
    ) {

        fun versionSum(): Long {
            return version + packages.sumByLong { it.versionSum() }
        }

        fun value(): Long {
            return when (typeId) {
                0L -> packages.sumByLong { it.value() }
                1L -> packages.fold(1L) { acc, p -> acc * p.value() }
                2L -> packages.minBy { it.value() }!!.value()
                3L -> packages.maxBy { it.value() }!!.value()
                4L -> payload!!
                5L -> if (packages[0].value() > packages[1].value()) 1 else 0
                6L -> if (packages[0].value() < packages[1].value()) 1 else 0
                7L -> if (packages[0].value() == packages[1].value()) 1 else 0
                else -> error("Unknown typeId.")
            }
        }
    }

    private fun String.hexToBin(): String {
        return this
                .replace("0".toRegex(), "0000")
                .replace("1".toRegex(), "0001")
                .replace("2".toRegex(), "0010")
                .replace("3".toRegex(), "0011")
                .replace("4".toRegex(), "0100")
                .replace("5".toRegex(), "0101")
                .replace("6".toRegex(), "0110")
                .replace("7".toRegex(), "0111")
                .replace("8".toRegex(), "1000")
                .replace("9".toRegex(), "1001")
                .replace("A".toRegex(), "1010")
                .replace("B".toRegex(), "1011")
                .replace("C".toRegex(), "1100")
                .replace("D".toRegex(), "1101")
                .replace("E".toRegex(), "1110")
                .replace("F".toRegex(), "1111")
    }

    private val data = """
220D700071F39F9C6BC92D4A6713C737B3E98783004AC0169B4B99F93CFC31AC4D8A4BB89E9D654D216B80131DC0050B20043E27C1F83240086C468A311CC0188DB0BA12B00719221D3F7AF776DC5DE635094A7D2370082795A52911791ECB7EDA9CFD634BDED14030047C01498EE203931BF7256189A593005E116802D34673999A3A805126EB2B5BEEBB823CB561E9F2165492CE00E6918C011926CA005465B0BB2D85D700B675DA72DD7E9DBE377D62B27698F0D4BAD100735276B4B93C0FF002FF359F3BCFF0DC802ACC002CE3546B92FCB7590C380210523E180233FD21D0040001098ED076108002110960D45F988EB14D9D9802F232A32E802F2FDBEBA7D3B3B7FB06320132B0037700043224C5D8F2000844558C704A6FEAA800D2CFE27B921CA872003A90C6214D62DA8AA9009CF600B8803B10E144741006A1C47F85D29DCF7C9C40132680213037284B3D488640A1008A314BC3D86D9AB6492637D331003E79300012F9BDE8560F1009B32B09EC7FC0151006A0EC6082A0008744287511CC0269810987789132AC600BD802C00087C1D88D05C001088BF1BE284D298005FB1366B353798689D8A84D5194C017D005647181A931895D588E7736C6A5008200F0B802909F97B35897CFCBD9AC4A26DD880259A0037E49861F4E4349A6005CFAD180333E95281338A930EA400824981CC8A2804523AA6F5B3691CF5425B05B3D9AF8DD400F9EDA1100789800D2CBD30E32F4C3ACF52F9FF64326009D802733197392438BF22C52D5AD2D8524034E800C8B202F604008602A6CC00940256C008A9601FF8400D100240062F50038400970034003CE600C70C00F600760C00B98C563FB37CE4BD1BFA769839802F400F8C9CA79429B96E0A93FAE4A5F32201428401A8F508A1B0002131723B43400043618C2089E40143CBA748B3CE01C893C8904F4E1B2D300527AB63DA0091253929E42A53929E420
    """.trimIndent()
            .hexToBin()

}
