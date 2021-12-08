package se.mattec.advent2021

fun main(args: Array<String>) {
    println(Day8.problem1())
    println(Day8.problem2())
}

object Day8 {

    fun problem1(): String {
        return data.sumBy { it.second.count { it.length in setOf(2, 4, 3, 7) } }.toString()
    }

    fun problem2(): String {
        val observations = data
        return observations.sumBy { observation ->
            val patterns = observation.first.map { it.toSet() }
            val output = observation.second.map { it.toSet() }

            val known = mutableMapOf(
                    1 to patterns.find { it.size == 2 },
                    4 to patterns.find { it.size == 4 },
                    7 to patterns.find { it.size == 3 },
                    8 to patterns.find { it.size == 7 }
            )

            val unknown = patterns.filter { !known.containsValue(it) }.toMutableList()

            known[9] = unknown.find { it.containsAll(known[4]!!) && it != known[8] }
            unknown.remove(known[9])

            known[0] = unknown.find { it.containsAll(known[1]!!) && it.size == 6 }
            unknown.remove(known[0])

            known[3] = unknown.find { it.containsAll(known[1]!!) && it.size == 5 }
            unknown.remove(known[3])

            known[6] = unknown.find { it.size == 6 }
            unknown.remove(known[6])

            known[5] = unknown.find { it.count { known[4]!!.contains(it) } == 3 }
            unknown.remove(known[5])

            known[2] = unknown[0]

            val knownInverse = known.entries.associate { it.value!! to it.key }

            output.map { knownInverse[it]!! }.joinToString("").toInt()
        }.toString()
    }

    private val data = """
dbcfeag cgaed fe bfgad aefcdb efa efgda gcef dcaebg dfeagc | fae cfge fae baefdc
ea bfecadg bgafcd deac ebcag eab debfag eabgdc bcgfe bagdc | cdbagf fagbcd bae fcegb
fbgdec cbgeaf cbfag bd bgda cgbfda dbf fecda bfadc cbedagf | gecfdb cbfga bacgef dfb
geabdf cfbge dcbeaf aebdf dgea acdgfbe fag gfbea ag cabdfg | dfeabc dbaefc fdaebc fga
gcae cefdg abdcgfe cg fcg afcegd dbgfea agdfe dcafbg bcdef | gc caefdg gcf egdcf
efgcb fbac dbefg bce efacg fbgcdae cb egfbca dgfaec dcgbea | fgecb bcfa bc efcag
efcgdb da bdefg dfegba fbacg gbcdea dbafceg adb feda fadgb | da ad cgabde cebdga
ca fcabegd facd agbed acg cfadeg acdeg agcefb cdfgeb efcdg | cdeafg cag efgadc gacbfe
fdacgb cfa cfage gabce dfagce edagfb dcbfeag fc fced gfdae | gabdef fdegba gaedcf fac
acgdbf faedcbg fa cbgdae fgbdae aedbg adf fbea cfdge fedga | fad egdcf feab bfcagd
ged cadgf gcdafeb cabgfd cgedf aedc ebfcg faebgd ed fcadeg | dfgca gabefd dafgc ecfgd
bcedga dceba gcadfbe dfec adcbfe fda fabge df fbdae bcfgda | adgcfbe fbgae dgebafc gacedb
fgcabde edbcfg efcdga dfbcea dbaeg fea fa dbaef acfb dbfec | aecbfd aedcfb bdaef abfc
cfbd gefad dce fdeac dc ecabfd dgabce gabdecf afbce fagceb | bdfc bcgaed bgdace afegd
acfgde egdfb dcaf gecfd ecdbgfa gcdbae cefgba dcg gcefa cd | eadcbg cdfega fdgeac dgc
cadfgb edcgbfa dc cgd fegcb dfabg cdaebg dafc fcbdg gbdeaf | afdc adcf befdag gfcdb
deafg ebafdcg gcdfba bge eb fgcbd gbefac bdce fdebg gdcfeb | cbdagf bdce gdfeb fgead
acbdef bg agebdf afbcg fgbeca edafcgb eacfb gba ebgc fgcad | begdaf gcbe ebdfag dabefg
ga agc bdegcaf gefbc cfdea dafgec fagce dgebca aefbdc gdfa | acfedbg bfaecdg gca facedg
abdg ad egcabf ebagf afd gdfceab dcebf dagbef cdgafe deabf | bfedc cbedf gdab afd
efbadc cafbd eba fegda fcbe dbefa efcbdag adbcge badfcg be | be agdfe cedbag dcbeaf
adegb cgafb badgcfe fged fgaebd eabgf bceafd eaf eacbdg fe | bdgeac adgbfe bdgae acfgbed
dbfega befag gbfdec gacfed fcgdabe bgf deagf bf dafb bceag | bdfa cagbe gceba agbdfe
fecagd cbfeagd fdecb fg fgbde ebgda cagdbe fge gfab agefdb | geabcd gdbea ebgafd egdab
ab agdcfe dcfgb cba adefcb adfbc bdae ebacgf aegbdcf fcade | gfecda dfabc edab bfcad
bcfae ebd gefdacb fdceg dbaegc gdbf gcefbd cedfb adfgce bd | gebdcf bd dgfb ebdcgf
gbcafde cdaef dfe ecbf egcda fadgcb bfdac dacfbe bgefda ef | dcgafb dfaebg fbec agbdfc
fe cfedab dcfgeb gdbef gadeb cegf fgcbd fbe abdcgf ebacgfd | debfg abecfd adbefc gdcbfe
ad cbfeda acegfb gdcaeb adcgfbe bda abefc dcabf fgcdb afde | gdcbf cedbaf dbagec becfa
acegb efcgab edcfgab aebdc gce ge fage egcdfb agfbc cbdfga | eg cafegb fcbeag acbgef
bcaf agefdc bcegaf eaf bdgfe agfdecb efgab af abegc dgacbe | defbg dgaebc eaf cebga
edabcf bdeg gd cadebg dgcab gfcaed febdgca cdg beacd cagbf | befadcg fbgca afdecg bgceda
gadfeb ag gfda cbeda fbeacg bgdcaef dgfeb eag gbdea fegcbd | cabefg degbcf gdbef bcdgef
efbcda cgfd dgefac dfcgbae degaf adg debacg aedcf gd aebgf | ecfad cgbade aegdcb gfbae
bdc cdfgb cabgf gabd fdebagc eabgcf bd abdcef gfcabd fgedc | faecdb fgecd cbfdag bcfadg
dfagbec ceg egfad fdeabg acedg afedcg gc bcgaef eacdb fdcg | gcfd eadbc dbegcfa eabcd
gfb egfba eadcfb gf adfg dgebfa dacfegb eagcb cbedgf fadeb | edfba afgebd adbegf efcbda
fec caegd efgb cgaedfb gfbdec gfdbc ef cfdgab fdcge fbaedc | abcdef gcfbd bfegdac gcead
gefdba abcef ead gedcab ad bfdecg cbged ebcad cafbdeg cadg | defcgb ade bgecd ceabf
cabg adgefbc cb ebafg febgc fbc gdcfe cafedb egfabc aebdgf | bcdafeg ecbafd adbecf fbcge
acged febgd agdcfb bdceg gdbecf bc bgc cfeb cfgbaed bgefad | dceag efbc cbfdeg ebcdfg
gab adgcbef cebfg abdce dcbefg bafdgc abefgc aegf ag cgbea | gaceb aegf fgcbe cgebf
fa baf cadfbge dbgef ebacgd dfceba adbec fdeba cfae dacgfb | dabcef dafcbe acgdbe ecdfab
fgeca gecadfb aecdfb fb gcbed dfgb cgdbea ecfgb gfbdec efb | abcedg bf bf agedcfb
fg aefgd bafcde gcfe bdgae gfa fedca cagdfb cfaedg ebgacfd | dcfgab dgfea gf cafdgb
afbcge debgc cafdbg ae eadf afdgb gbdeaf aeb cgfebad abgde | deaf cbafge cbegd aedbg
fbc afcegb fadgc fbeda cbed ecdafb cfdab cb fegdcab bdaefg | cgfad ecbd edafb cgafd
fegbcd abcf gfabedc bgcad gfacd gedab efdagc dfgcab cdb bc | fbca cdbag cdfgab cgedbfa
gfca gbdef eadcfg ebdcag dcg afecd gc decafbg fcegd bcfade | gcefd bcdafge cdefg edfgc
gdae ad gecfd dcfaeb dfegcb gdcaf fdacge adf agbcf cebfgad | dgebafc edfgc da cbgafed
ceg febgdca bfgdea gc adcfge cefbag agdc edfcb dfgce gadef | ceg ebdagf agdfe afbgce
dabegcf bcdfag gdecb gdbeca gaebd fbdce bdagef cg bgc ecag | dfcabg abfdge gadbe cg
dacefb efc gfaebc gbfe egcda agfbdc beadfcg fe fbcga cgafe | ef fbaedc ef efc
cbgae faedbg fegab agc ebdgc eacf bgeafc ca cgdbafe gadcbf | eagfb aebgf gcbfad gfeba
dcf dacbg bdcaef dfeacg gacfe cdfag fgde faecgb daebcgf df | fcd fecadb egacf deabcf
egdfa dfaec gdab ga agf dbefga dgebf afbgecd bgafec bgefcd | fegbd gdfbe begdfc efcbadg
cfd edafbc afbd cadbeg fgecbad gcaef dacef df dbcae efcdgb | adbf cabde baegdc cgbeda
cbag gc cdgbe defbg agebdfc becda dbgcae cdg efacbd fecgad | fcegabd abgc dacbe cdaebg
bf dacgf dfb befc fbedcg gebdc dbacefg abdegf gcdfb bedacg | bfec feagcbd egdcb daegbf
aebgfdc gadcf gcdfeb dagce agdeb acfe bgacdf dec gfecad ec | ceadg edcag fdcga cdbagef
befdgca dgcbf fedagc be bfe dcefb ecfdba acfed ebca ebgfda | dfbcae bafgdce bgcdf egbdfa
bfa bdagfc dbega bfgea eagfdb dbgace bcefg dcbgeaf fa defa | ebcgdaf aecgdbf fgcadb fa
agcdeb cgf agbdcef bgdfe efac cgefb abefgc cagdbf fc gebca | fegbc acfe becga deagbc
adbfce agebd eadcbfg ega abedf fgab dgebc dgfcea ag agdebf | gdfcabe ebadf ga bfag
gcebfd feacdbg acegfb afgce gbac geb cdaefg daefb gb fgbae | agefb gb adfbe efabd
gadbfc ebgad dgecba fgae eacgdbf af ebcfd afd bdeaf gadefb | fa bdgaef gdbacf af
bfeacd edgcabf dg gcabd cdaeb acfgb bcgdae dgae cgd bgfdec | bedac acbed abcdfge dgc
cdage dgabec cbadg ge acfde edg becfagd gbec adbcgf fbeadg | cdbgea cgdaeb cegad gdeac
ab gbaf cdegba fceadbg gfbecd bca eadcf fgeabc becfa cebfg | cagfeb cefabg fcbeg eadcgb
afdec bcaefg ca abcd efcdg aec aebdf cdafbe begdaf eadgbfc | cdfeg afdce edfac gdfabe
faebd fed df bgdcaef cbdega gdbeaf fbecgd badge afebc fadg | dcebfg dgaf fd cdegfba
abdgf efg fe gdeabf cdbge cfadeg gdbef cafgdeb fbea bgcadf | bgdfe fabe gbfadc gbfcda
agdfe cfbd dgfca cd abgfc cdgbea gbecfa gbadcef cda dfgbac | cegfba fbcd bfdc dca
acdb bdcega dbfcge aedfgb dec fcega dc adegc fgceabd gaedb | badeg bdecgf cd cdageb
abe ab dbecfag adgec agbf eadbg cbeafd fbcegd gadbfe bfgde | ebgfd efdgb gceadbf ba
dgfceba egbcaf fgc bfgdca cbgfe cg faebc agce gdfeb debcaf | bedfac edbfg aegc eabcdgf
bdfac dcfeb dbcefa dbgcaef fbae egdcb fgaecd fec cdbafg ef | bdfagc dcfabg gdbfeca eabf
gfca dbcaf bfcgd fcgdeab degcb abcedf cbagfd gfb fg fdgabe | gcaf bfg fbg gf
efcagd dbag fgd defgacb aebfg dbgfe bdeafg edfbc gd fgecab | fbeag ecgfabd degacf debgfac
efcbg bacfe gbdce fge acegbdf cgdf aebdfg dgbcae ebcdgf gf | caefb cbfdage ecfgb bgedc
fgdeb dcbfg fgae fbeagd edbfa aegdbc egb gafecdb bfdeca eg | dgbcea bdaef gfbde dfgbea
bdcaf gdafc cbf bdefa cb fgceda bdfagc fcbgde afcgedb cbga | gbca fbdca cegfdb fcagd
cfebgd cedgfa dcfabge dbfgc dgeb ge fcegb gfe abfdcg fecab | fge fabec gfbadec bfceg
aeb ab gebfd abcdef edcag cdegaf gaedbc bgfecda debag gbac | beacgd bdfeg afgecd gedbf
ab gebfadc acfge edafbc abegc dcegab gbecfd gbda cab ebcdg | dagb cegfa bca bac
gaedb ebcd fgcea cba dcgafb febadg egcab adcbge efgadcb cb | dfcagb cbed bagce dbce
ca dgfceb bfeacd fcbgd bdgca cfag gaedb abc fcbagd fgecadb | cgdfab cfag bac cbfegad
gade gafcb bfgecd bge ecbda gdcbea eg beacg fdaecb agfbecd | dgae bgfca abegc cfdebg
eac cdbef ac gfdae dagc afcedgb dcfae dafcge dbfega caebfg | gafceb dbeacgf cdga fbcde
dbagfc bgedca df afgec daf beagdfc aefcbd bedac acfed dbfe | bedca cdaef baecfd befcad
fdacb baedc acedfb bacedg fd gfcab ecdf daf fedgba befgdac | egabcd cedab eabdfg adf
efdgc dfecga adfbe ac cage adc gfbdec cagbfd efdabgc caefd | efdab efdgcab ca cedfg
ca acgf cadbgfe egfcab gefab cea defbc fecab edbcga fbgdae | cfeba efdcbag dacebg agefcdb
bcedfa agecd ebgf fcbdg fde cgfed acgbfd cagfdeb ef cgbdef | fgeb bcfdg def ef
bfcdea ga fbdegac gecdf cfdab bcgadf facdg aegbcd gac afgb | cbadfg facdb afbg dfecba
fdcae afeg fa bceda dfebcga afcgdb dgecf acf fgceda cbfdeg | fa bdfcga fcbegd efacd
efgbcd cf bdgace fgbad dfc fceabd dbaegfc bdcgf becdg gfce | bcedaf adbegc badgec bgcfd
efcba ebdfga fda gdefbc da facde dacg fgedc gfeabcd acdefg | da bgafde cegfd edafgb
daegb cbge bgcaed afgced ge cdabe fdbcaeg fcbeda bgfda eag | gae egcb egbc egdafcb
cefda fadg bfedac bedfacg dgecfa eag ga fegcb becagd gcefa | dcfea eag cedfa bgfcaed
dcfe gbeafd df bdcaf ebfcag fcedagb cdbag dcfaeb eabcf fbd | dfce geacbf cefd ebdacf
efgbc agbd gd eadgcf becdfa bdace dge debgc adecgb bfdgcae | bgcfe bedcg abgd gdba
cdbf edbfgc feadg cd ced gaecdb degfc efcgb fcbgaed befcga | cfabgde fgdcbe efacgb abgcef
ae caegfd eadgc fedgba fecgd gbacd efac aed afcbged cgdbef | ae eacf abdegcf eda
fc afcge dbgecf adcgfe eafbg gdebacf eagcd gfc aecdgb afcd | cfg dgeac afbge edgcba
aedcg gfd cafgebd fg eadbgc adfcb cefg gabefd dacfg efcgda | dgeca afgedb cfdga aefgcd
bfgaed facedb fbec dbgca cagfbde egcfda efdac acbfd fbd fb | efbc agcdb fagdbe bacdf
fac dcae fgbda fagbce bdfac cbdfe ca cadbfe aegcdfb cebdfg | cdea cfbdeg ac afdcb
geabcfd gfcaeb gcbead eba facbg fdgcab ea bgafe fdgbe caef | bagef cafgb afbeg cbgaf
gcfdeb ceg fabcge edbag fcabdg bcgaf ceaf ec agefbdc cbega | bgafc baecgdf bcfged gbdfeac
cf adgef cfgbaed bfdc gfc egcdfb dcgaeb degbc fcedg ecagfb | fdbc cgdbe gdabce gfdce
bc afgcb cfdga dfcb cbdgea bgc efcgdab cfedga beafg bagcfd | bc bc cbfadg adcbge
egdfb fa agfbe eacf befagc fba fbedacg cadebg agcbe cfgbda | bagfec bcdgaf cbfgad dgcbfa
acgbde acgdbef cba ca bcgfd cade egdafb aebgfc agbed adgcb | fdgbc gbadef gfcdbae edfgab
egbac efacgd gfbd fbcdag fecbad dgc badcf gfcebda dbcag dg | bacdg facbegd cgd dfcgae
adgcf fbgcda efcgda dcbea fedg cfebag fae ef acdef gecbfda | fcagd gcadf gcefdba gefabc
fegdba ba cedgaf gbade bfad egfda gba cadbgef gbced fcaegb | gedaf bdega abgde bafd
gbacef gdab defgc bagfdc dfb afgcb efdbac bd dfbcg befcgad | cdgef bd gbfac ecbgfad
fgacd ef dagfce ecdfa fae adfbgc cegf febdga ceabd bgacedf | abcgfed gfce fae efacgdb
fbgdc afedg gcbafd gfedbc cfdag acd fbdeacg acbfde cgab ac | gdcbf cedfbg dacgf dcafg
becf feadb abfedc dfcageb gcbda bafedg aec daecfg acdbe ec | abdef fdeacg ace ecbf
dc cbfegda bfeadg dbcg gabfd adc dbcfga bface fgcade fdabc | dacbgf dc cdfba dc
bc bfgedc fbc adfbceg cegb degcf eabdf defcb cdfgae fcagbd | bcefd gceb fcagde cbfgad
ge cabgd egca egd adgbecf bfdagc decbag agbde deafb ebdgfc | cagdfb bgecda cgedba fabdcg
cg cfagbd gcedfa efdbc gcd gbefda fegad efdcg caegdbf agce | fgced dgfacb befdagc cefgda
bfedgc bec ce fcaebd efabg gdec cgfdb aebdfcg bfgcda cgbfe | agebf bgdcfe ec agcbdf
db dfcbge edgfca gabd dcb agfcd bafcd afbgcd ebafc edgfbca | acfdg cbdfa afceb gbda
cadegbf egdfbc adc acbdg dgbcf adgfbc da cgfdae fbda ebcga | dgacb bagdc fgcbd aegbc
geacf abc gfedba cbadfg dbcg gfdba cagfb cb bfdcae adfecbg | cb eadfgb bcfag dgcb
abdfcge adbeg badcef egcf fg fgb fbdec gebfd abgfcd gfebdc | cbeadgf cfdbag gfdbe gf
facdbe acgfbd dbc bd abgcd adceg bcegfa fdgb bfgca badcfeg | ceafdb bfcag dcgba bgdf
gcda cadbe abcedf cabeg edbgcf begcda begaf cg acbdefg gbc | dbace gdbeac dacfgbe abdce
cgf gf fadegbc afgcde abgced fecba ceagf dcagbf fgde cdaeg | gdebacf gf gdfe cfega
abdef bc gecaf dbcgfae cbfg efcba bac acebfg degcba dcegfa | bcfadeg afceb edfgac gcabed
bfcged bgdac becad fagc aegbdfc gacfdb dbgfc dafgbe ga bga | fgdecab abdcg cefabdg egdbfa
abcfg baecf agec fcgaeb agedbf ag dacfbeg bag eafcdb fcgdb | bcdfea efdabg ga fdbgc
ba baf dfbgce cgab cefgdab efcad efacbg cgfeb ebafgd cafeb | fdcegb abf baf ecabf
cegafd egdcba gbedf cgfde cfad ecf fcegadb eagdc bgaefc cf | bfged defgb gbfed efc
gedfa abdce fadbe ebfc fba badgce bf gabefcd fdbgac befcad | bcdgaf fdbgca bfa fgbdca
bacef cg dgabec agfc edfgb cge agfdbec fcabeg cgebf abfdce | decfbga cagf adcgbe dgefb
eafgd ce fbgaced ecfga eafdgc gdce cea cefdba cbfag bdgafe | eadfbg dgec efgca degfa
decgabf fecd acd acbegf agdec gabed gacbfd faceg cd fgeadc | dc dcafge abecgf dc
egd fdaebg gcbeadf fcdge ge adecf gbce dgfbac dcfgeb gdbcf | fdcae gfdbea bdfcg eg
ad cbafe dabe dfa gbfcea ceafd dcbagf fabdce bfceadg dfgec | cebaf aecbgdf cgdbfa fbaec
fc cdgbae gfcbad badcg fcdba feadb fac cgefad fdgbeac gfbc | cf cfabd caf gdfcea
fdgace edac cdfag de dcfge facebgd cgebf dfebag def bafgdc | fdgac gacfbed edgacf dgcfa
gedfac acedb fbcadge acgefb dbgf fdaeb dgafe fgaedb bf fba | fb bf bf agefcd
gefbdca fagbed cgaeb gf ebdfa gdbf afebg feg feadcb geacfd | egf fedab gaefb eagbf
efadgbc bfag edgcb cga cefab afbegc ga gaebc eafcdb daefgc | ga acg gcfbade dgecb
efcgbd gebcf gcadbf agfce fcdeagb gfa ageb aefdc ga gacbef | gcebaf geba acfde fga
ga adbg fdeac gfa fdgbcae dgfabc cfgdb fegbdc fcabge agcdf | adbcfg efadc agcdf cgdbf
febdc dacfeb gbfce cfgae bge bgdf dgcfbea baecdg gb ecfgbd | gb fcedb becdf gb
bgacfd cdfabeg adgce bcegd acdefg gb cgb bgae ecbadg bdfec | adcbfg gdfcea deafbcg ceadfg
bc gdfeac fbaec egfcba dacbeg cab gacbfde eabfd faceg fgcb | afbegc cdgabe bagecf ebcdga
fd dfe fdgae abegfd dbcaeg dfgb caedbf efcag dageb dbfeagc | dgfb gaedbf gfbd dgfcabe
agefbc aefdb abdge cdef bfdcga dacbf fe gbdcefa eabcdf efb | cdbefa adfcbg fecagdb ebf
gbade adecfg abegcdf dcbfa cgbafd cfeb dfaebc fe feabd efa | dbcfea bcfe fecb bafde
dfba dfc efbac cgead ecfgab gdfecb edacbf df afced bgecfad | afdce bcfegd efgbac adgce
abfcg egacf bceafg dabgc fgb fb fbecadg fbce gafcde gfdbea | abdgc defagb cbef egcaf
caegb decgbfa cebf edacg cgfbae gbc gfbea baedgf bc bfcdag | agebc bgcae cbg gefba
ad gadf feacg aed agbfce egcda fedcba abcdgef bcdge gecafd | gedfca ad febgac dcaebf
cegbd bgcfda dbfeag acef gcead cagfde dcfga gae fagbced ae | defgab dacgf cebdg gecdfa
fag edfcg cdfa fa ceagfd fdgabe gebac egbfcd egdcfab gcaef | acebg fag fgdec dfegcb
abe bcdfa acdbgf befd eadbc acbfgde eb fgcaeb gacde fcdbea | eadbc afdgbc bgaefc cabfdeg
afdgce dgabe dgfbe bdegfc dfg agcbdfe fd abegfc dcfb bfgce | debag bedgf ebdcfg cgebafd
dgfcea eagcbd gfceb adgb cgdeb dg eacdbf bedcafg dge abedc | ecbgf dg dbfcgea ebcad
dgecf badfce egcdfab efabd edfca ac badc gbcfae eagdfb caf | feadc febad dacef caf
gfb bacgde acbdgf afbd bdgca bf bgfdcea ecfag dgfecb fbcag | fbgdcae gdfbca fbg fgaec
bcdg dc gedabc dec cdeag cbedaf gfaebc dafge bcegdaf agceb | dfage bcefad cfbage fdgae
gc cag agbec dacbe dcbg edcbgaf gdcfae dacebg eabfg febacd | gc cbdg gc fedcba
gfdbace edbc ecfgab efcdbg ce fdcabg fedga egc cbdgf gedfc | ecdb edgfbc eagcfb cdafbg
afceg bgdeaf daceg degacb fe dfec adfgec acdgefb gcbfa gfe | efg gcfea dcfe fcgea
bdca gcfdbea dc acbedf gbfde cagefd cfebag dcf bdcfe ebafc | eafbc bcfage defcb agcbfe
cabefg adgeb fd dacf afgcb bgefdca decbfg fbd dafgb dfagbc | gbfca cbeagf cfbgad fbd
bacgef defcga afdgebc facebd afebc badc bdfeg dc dbfec dce | adbc eafdcb dabc fgedb
cdf decb fagec dc dcgbaf bacfgde gdfeb efbadg efgbdc cfged | ecgfa efgbd fedgab dc
faebgd egdfcb gecadb gdbce ag aeg cgeab gacd faecb debcafg | ag fdeabg decbg ag
eagc bdegf ga gdaef adg cbgdaf cfaegd dabfce adcegbf dfeac | acedfg agd cega bgefd
cfgbe ceadf ab cbefag cegfbd bega egdfcab fba gbfacd efcba | ceafbg abf ecfba ba
fgdce ecgbaf gebd bfg fdbac dfacge fbegdc cgbdf dacbfeg gb | aebcgfd cdabf ebdg egdcaf
febda edagf egf gdcf bcgafed fg agcbfe caged gcadeb dgaefc | cegbad gcdf gaedc ebdgca
eabgf fbdcg dfecag acebgf bafdge fbgce ecf eacb begfacd ec | fbgea gcefb fegcad decfag
fabgcd febdac acgdf beagc dgaec de eafcdg dcfbeag dae fdge | fcdgea ceadg fegd bdcaef
bcafeg ecbad dcfe ecbgafd gdefab dfabce de cadgb ebd fcaeb | bed bgeafd fgeacb ecdfba
afcgeb gcebf cdbfag cfea bdgfe cbgaf ec gfabecd bedgca egc | gce dacfbg cebfg afec
ecdafb cfeag bdgaf febga egabfc eb gbec fcebdga eab dfegac | bea gbafd eab befdac
fgbacd fbcedga fgc fg gdaf bcdgef cdfba fabced cgbfa gcabe | egcadfb adgf gbcefd gcf
dfbcga caefbd ac gaedbfc afgc afgdb bdgce dgcab fdgaeb dca | afbgd cagf adc bfedga
bdgce dbf cgeadfb fd gfbde dfbecg gaebf dacgfb cdebga dcef | fegbd fgbde bgfde fcegbd
gbda egadbf bfegd acgfde bfgaec gabef cfbde aefdbcg dg ged | abgefd gdefba deg gfeba
afebcd degcfb dce dcfgb degf bdcgafe bdcge gcabdf de cbaeg | de bagec gcdeb ebagc
eadcfg dfc gcbfade cd edafb fbcge afdbec agdbef ecfbd bdac | efdab agefdc befad defabc
agbedf degbfc begcd ceagdf cgabe ecd dcbf ebfdg abecgfd cd | fdebg fdgbe cdgeb bagdef
aefdb degf fdbaeg egb agebd bcfage gcedfab bdagc ge febacd | bgfdae fdge acfdeb deafb
cegfba dfcea gbea bac ebafc gdcebf gebcf ba gfdbac gacfdbe | febac aefcb cbfgda cafed
ec ecfab feadbg ecb dabfc cbfegd dbafgec feabg caefgb ecag | acbefg afbeg fecgabd bcfad
fd decag fagbe beafdg egdacbf gbfcde dbfa agfbec dgf geadf | bfda befgcda fgd adfb
af eabfdg edabc dgfabc efga fdbae fcagbed abf gbcdfe efgdb | gefa gfabcd fba gcdfab
dbfacg degcb gecdaf adcbg bafegcd dfbag gbdefa ac cabf agc | bcadg afbc bdgfa abcdg
efab cbedg aedfbc bfcad geacfd ea fabdcg adbfgce ade ecabd | fdceba bfceda ecgdfa aefcdg
    """.trimIndent()
            .split("\n")
            .map {
                val line = it.split(" | ")
                line[0].split(" ") to line[1].split(" ")
            }

}