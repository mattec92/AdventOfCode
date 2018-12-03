package se.mattec.advent2018

fun main(args: Array<String>) {
    println(Day2.problem1())
    println(Day2.problem2())
}

object Day2 {

    fun problem1(): Int {
        val ids = data
                .split("\n")
                .map { it.groupBy { it } }

        val twoFound = ids.filter { it.values.find { it.size == 2 } != null }.size
        val threeFound = ids.filter { it.values.find { it.size == 3 } != null }.size

        return twoFound * threeFound
    }

    fun problem2(): String {
        val ids = data.split("\n")

        val diffingByOne = ids.mapNotNull { outer -> ids.find { it.oneDiffPosition(outer) != -1 } }
        val first = diffingByOne[0]
        val second = diffingByOne[1]
        val index = first.oneDiffPosition(second)

        return first.substring(0, index) + first.substring(index + 1, first.length)
    }

    private fun String.oneDiffPosition(other: String): Int {
        var diffs = 0
        var diffIndex = -1
        for (index in 0 until this.length) {
            if (this[index] != other[index]) {
                diffs++
                diffIndex = index
                if (diffs == 2) {
                    return -1
                }
            }
        }
        return diffIndex
    }

    val data = """
revtoubfniyhzsgxdowjwkqglp
revtcgbfniyhzsvxdomjwkqmlp
cevtcubfniyhqsgxdoakwkqmlp
revtcubfniyhzsgxdtavwkqmep
reutcuboniyhzmgxdoajwkqmlp
revtcubfniyhzsgxxqajmkqmlp
rwvtcvbfniyhzsgxdozjwkqmlp
qevtcbbfniyhzsgxdoljwkqmlp
rnvtcmbfniyhzsuxdoajwkqmlp
revtcubfsiyhzsgxdaaewkqmlp
revtcubfpiyhesgxhoajwkqmlp
revtcubfnivhzsuxdoljwkqmlp
retpcubwniyhzsgxdoajwkqmlp
revrcubfniyhzsgxdyajhkqmlp
revtcbbfniyhzsixdoajwvqmlp
revtcubfniyhzsgxdoanmkqmpp
jevtoubfnuyhzsgxdoajwkqmlp
rpwtcubfniehzsgxdoajwkqmlp
revhcubfniyhnsgxdoajwkxmlp
revtcubfniyhzswxdodjwkqvlp
reotcubfciyhzsgxdnajwkqmlp
revtcubfniyhzsgxdbatwsqmlp
rlvbcubfniyhzssxdoajwkqmlp
rentcubfnyyhzsgxdozjwkqmlp
revtcuufniyhasgxdohjwkqmlp
jevtcubfniyhxsgxdoajwkqwlp
ravtcubfnryhzfgxdoajwkqmlp
reltcubfnvyhzsgxdoajwkumlp
revtrubfnschzsgxdoajwkqmlp
uevtcubanichzsgxdoajwkqmlp
revtcubfniyhzdpxdoajwwqmlp
revtcubfhiyhzsgxdoajgkqplp
revtcubfniyxzygxdoajwkqmld
revtcunfniyfzsgxdoajwkqwlp
reqtcubfniyhzsgxdoajwfqmlj
revtcubfniyhzagedaajwkqmlp
revthuefniyhzsgxroajwkqmlp
revtcrbfodyhzsgxdoajwkqmlp
revtcubfniyhxsgxdlajwuqmlp
revtrubfnirhzsgxdokjwkqmlp
revtiubfniyhzagudoajwkqmlp
jevtcubfniyhusgxsoajwkqmlp
reetcubfniyhzsgxdoajvmqmlp
pestcubfniyhzsgxdoajwkqglp
revtcubfniyhzsgxdoiowkqalp
revscubfniyhzsgxdoajwkoplp
revtcubfnoyhzsgxdlajwkymlp
rkvtcubfniyhzsgxdoajzkqhlp
revtuubfniyhzsgxdojjwkqglp
revtcubmniyhzsgydoajwkzmlp
revtcybfnijhzsgxvoajwkqmlp
rxftcubfnkyhzsgxdoajwkqmlp
gertcubfniyhzsgxjoajwkqmlp
revtcabfniygzdgxdoajwkqmlp
levgcubfniyhzsgxdoalwkqmlp
revtcubfniyhzslxdofjwkqxlp
revtcybwniyhzsgxdoajwkqmlx
devtcubpniyhzsgqdoajwkqmlp
pevtcjbfniyhzsbxdoajwkqmlp
revtcubfeiehzsgxdoafwkqmlp
revwcubfniyhzsgxdoawekqmlp
revtcubfniyussgxdoawwkqmlp
revtcuafnczhzsgxdoajwkqmlp
revtaubfniyhusgxdoajwkqilp
revtcubfnidhzxgxdoajwkqmlt
revtcubfniyhzsexdmajwnqmlp
revtcubfnhyhzsgxdwxjwkqmlp
revtalbfniyhzsgxdoajwbqmlp
revtcubfniyazsgxdoajwkqcvp
rcvtcubfniyhzwgxdoajwkqmsp
revthubfniyhzxgxdoalwkqmlp
revtcubfniyazsoxgoajwkqmlp
revtcubkriyhzsgtdoajwkqmlp
revtcubfniyhzsgxgeajwgqmlp
heftcubfniypzsgxdoajwkqmlp
revtclbfniyhzsgxdowjnkqmlp
revtcubfnifhzsgxdoamwkqmhp
revncubfniyhzsgxdoxjwiqmlp
reitcurfniyhzsgxdoajwkrmlp
revtfmbfniyhzsgxdoajwkqmbp
revtcubfniahzsgxdoajwkqhtp
rejtcubfhiyhzsgxdoajwkqmfp
revtcuxfqiyhzsgxdoajwkqmlh
revtcuzfniwhzsgxdoajwkqmcp
revtcubfniyhzsmxdotjwkqmlx
revtcubfniyhzzgxmoajwkqulp
revtcuaffiyhzsgxdoajwkqmlj
revtcxbfniyhzsaxdoajwkqflp
revtjubfniyhzcrxdoajwkqmlp
revtcunfniyhzsgxdfajwoqmlp
revtcubfpiytzswxdoajwkqmlp
revtcubfniyhzsgxdorjwiqmtp
oevtcubfniyhzsgidoajwkqmlt
revccubzniyhztgxdoajwkqmlp
reircubfniwhzsgxdoajwkqmlp
revtcubfniyhzsgxhyajwkqvlp
revtcubfnpyhzsgxdoajwkvblp
revtduvfniyhzsixdoajwkqmlp
revtcebfniyhzsgydpajwkqmlp
revtcubftiyhzsgxwkajwkqmlp
revtcdbfniyuzsgxdoajlkqmlp
revtcubfnvydjsgxdoajwkqmlp
cevtcupfniypzsgxdoajwkqmlp
revtcubfniyhzsgoeonjwkqmlp
revtcsbfniyhzsgxdoyjwdqmlp
revtcubfriyhzugxdoakwkqmlp
revtcadfniohzsgxdoajwkqmlp
revrcubfniyhzsguxoajwkqmlp
ruvtcubfniyhzsxxdoahwkqmlp
aevtcubfniyhzsgcdoajwkqdlp
revtcubgniyhzwgxdoajpkqmlp
revtcubfniyhzegxdoajwkumsp
rlvtcubzniyhzsgxdoajwkqzlp
revtfubfniyhzxgbdoajwkqmlp
revtcubfniyszssxdoajwkymlp
revtcubfniyhzsgxdoarskzmlp
rewtcubfniyhzsgxdoajwkpmlh
revtcubbniyhzsfxdxajwkqmlp
yeitcubfniyhzsgxdrajwkqmlp
revtcubfniyhzsrxnoajwkemlp
revtcuefnqyhzsgxdoajwkqmbp
revtcubfniyhzsuxdoajwdqnlp
revtcujfnifhzsgxdoaswkqmlp
revtcuyfniyhzsgxdoaswklmlp
reeacubfniyhzsgxdoajwkqmfp
revtcubvniyhzsgxdoauwkqmls
revtpubkniyhzsgxdoajvkqmlp
revtcubfnpyhzsgxdoavnkqmlp
revtcobfnvyhzsfxdoajwkqmlp
gevtcubfniyhzsgxcoajwkqmld
rivtcubfniyhzqgxdpajwkqmlp
rettgubfngyhzsgxdoajwkqmlp
revtcuhfccyhzsgxdoajwkqmlp
rertarbfniyhzsgxdoajwkqmlp
rhftcybfniyhzsgxdoajwkqmlp
revtcvjfniyhzsgxboajwkqmlp
reetcubfnikhzsgxdoajwkqmsp
revtwubfniyhusgxdoajwkqelp
revtcdbfniyyzsgxdwajwkqmlp
revtcurfniyhzsgxduajwkqmtp
revtcuafneyhzsgxduajwkqmlp
rpvtcubfziyhzsgxdoajwkqmep
mevtcubfniyhzssxdoaywkqmlp
reptcubfniypzsgsdoajwkqmlp
revtcubfniyhnsgxdoajwcqelp
revtcutfniyhzsdxdoajwkqmlr
rpvtcuafniyhzsgxqoajwkqmlp
revncubfniyhzsgxdoajwkqkpp
rertcabfniyhzsgxdoejwkqmlp
revockbfniymzsgxdoajwkqmlp
revtsubfniyczsgxdoajwkqplp
revrcubpniyhzbgxdoajwkqmlp
revrculfniyhzsgxdoajwkrmlp
revtlubfniyhzsgxdiajhkqmlp
ravtcubfniyhzsgxdoajwftmlp
revtcunfxiyhzssxdoajwkqmlp
revscubfniypzsgxroajwkqmlp
mevtzubfniyhysgxdoajwkqmlp
reitcubfniyuzogxdoajwkqmlp
revycubfniyhwsgxdoajwkqmlg
revtcubfnyyhzsgxdoajwkomqp
zevtcutfniyhzsgxcoajwkqmlp
revtwubfniylzsgxdjajwkqmlp
oevtcubfniyhzsgxdoaowkzmlp
revtcubfniyhzsgxdxajwwqclp
revtcuafniyhzsgxdlacwkqmlp
revtcubfniyhzsgxdqrjlkqmlp
revmcubfnvyhzsgxduajwkqmlp
rgvvcubfniyhzxgxdoajwkqmlp
revtcubfniyhzsgxdoakwiqmlz
reztcubfniyhzsgxddajwnqmlp
revtcrbfnayhzsgxdoajwxqmlp
revtcuboncyxzsgxdoajwkqmlp
revtczbfniybxsgxdoajwkqmlp
yevtcubfniyhcsdxdoajwkqmlp
reztcmbfniyhzsgxcoajwkqmlp
restcubfliyhzsbxdoajwkqmlp
restcubkniyhzsgxdomjwkqmlp
reokhubfniyhzsgxdoajwkqmlp
rejtiubfniyhzsnxdoajwkqmlp
revtcubfuiyjzsgxdoajykqmlp
revscubfniyhzsixdoajwkqhlp
revtjuzfniyhzsgxdoajwkqilp
revtcubfziyhzsgxdoajhgqmlp
revtcubiniyhzsgldoacwkqmlp
revtcubfngyhisgxdoajwkqmkp
ruvtcubfniyhzsgxloajwkqplp
rtvtcubfniqbzsgxdoajwkqmlp
revtcubfniyhzegxdffjwkqmlp
revtcumsniyhzsgxdoajwkqmsp
rmvtcubfnhyhzsgxsoajwkqmlp
revtcbbfniyhzsgxdoajwkqzgp
rebtcjufniyhzsgxdoajwkqmlp
rephcubfniyhzvgxdoajwkqmlp
revtcpbfniyxzsgxdoajwkqmls
revjcubfniyizsgxdoajwkqmcp
revtcuqfniyhzsgxdoavwkqmdp
rettcubfniyhzsgxdoojwkqmbp
rkvtcubfmuyhzsgxdoajwkqmlp
revtcubcniyhzngxdoajlkqmlp
revxcubfpiyfzsgxdoajwkqmlp
revtcubfniyhzsgkkoajwklmlp
revtcubfniyhzsbxdoajwqqslp
zecycubfniyhzsgxdoajwkqmlp
revtcubfniyhzsggaoajwksmlp
revtcubffiyhzspxdoajwkqmmp
ruvtcubfniyhzsgxdoajwkamlu
revtcubfnmyhzsgxjoajwuqmlp
revtcubfniyhisgxdoajwkqjgp
revthubfniyhzsgxdoajwkeolp
ryvtgubfniyhzsgidoajwkqmlp
reitiubfniyhzsgxdoajwkqmbp
rektcubfniyhzsfxdoajpkqmlp
revbcubfniykzsgxdoajwkqwlp
revzyubfniyhzkgxdoajwkqmlp
ravtcubfniyhzsgxdoajwkhmap
revtcubfnfyhzsgxdvpjwkqmlp
rhvtcnbfnibhzsgxdoajwkqmlp
revtctbfniywzsgxroajwkqmlp
revtcubfniyhzsfmdoabwkqmlp
sevtcubfniynzsgxpoajwkqmlp
revtcnbfniyhzzgxdoajwzqmlp
revtcoofniyhzsgxdoajwkqmrp
revtcubfaiynysgxdoajwkqmlp
revtlubfniyizsnxdoajwkqmlp
revtcubfnwyzzsgxdoajwkqmzp
revtqubfjiyhzsgxdoajwkrmlp
revtaubfniyhpsgxdoajwkqilp
revncuufniwhzsgxdoajwkqmlp
revtcubfngyhisgxdoauwkqmlp
revtcubynqyhzdgxdoajwkqmlp
revtcubfniykzsgxdoyjwkqmla
revttubfniytzsghdoajwkqmlp
rerzcujfniyhzsgxdoajwkqmlp
revtcubtniydzsgxdoajwkpmlp
revecubfniyhzsvxsoajwkqmlp
revtcuvfniyhzsgsdaajwkqmlp
revtcubfniyxzsgxdoajtkzmlp
revtcukfxiyhzsgxdofjwkqmlp
revtcubfnayhzugxdqajwkqmlp
revtcbbfniyizsgxdoajwkqmop
revtcubfnzyhzsgxdoajwoqmpp
reitcnbfniyqzsgxdoajwkqmlp
rektcubfniyhzsgxdgijwkqmlp
revtcubfniyhpsaxdoajdkqmlp
ckvtcubfniyhzsgxeoajwkqmlp
revtcubfniyhzsgxdhajzknmlp
revscubfniyhrsgxdoajwwqmlp
revtcubfilyhzsgxdpajwkqmlp
fevtcubyniyhzsgxdoajwkqmpp
    """.trimIndent()

}