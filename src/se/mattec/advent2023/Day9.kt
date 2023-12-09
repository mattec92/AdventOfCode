package se.mattec.advent2023

fun main() {
    println(Day9.problem1())
    println(Day9.problem2())
}

object Day9 {

    fun problem1(): String {
        return data.map { sequence ->
            val sequences = mutableListOf(sequence.toMutableList())
            while (!sequences.last().all { it == 0 }) {
                sequences += differences(sequences.last())
            }

            sequences.reversed().forEachIndexed { index, differences ->
                val toExtrapolate = sequences.reversed().getOrNull(index + 1) ?: return@forEachIndexed
                toExtrapolate.add(toExtrapolate.last() + differences.last())
            }

            sequences.first().last()
        }.sum().toString()
    }

    private fun differences(sequence: List<Int>): MutableList<Int> {
        return sequence
            .mapIndexed { index: Int, i: Int -> sequence.getOrNull(index + 1)?.minus(i) }
            .filterNotNull()
            .toMutableList()
    }

    fun problem2(): String {
        return data.map { sequence ->
            val sequences = mutableListOf(sequence.toMutableList())
            while (!sequences.last().all { it == 0 }) {
                sequences += differences(sequences.last())
            }

            sequences.reversed().forEachIndexed { index, differences ->
                val toExtrapolate = sequences.reversed().getOrNull(index + 1) ?: return@forEachIndexed
                toExtrapolate.add(0, toExtrapolate.first() - differences.first())
            }

            sequences.first().first()
        }.sum().toString()
    }

    private val data = """
8 10 9 -2 -17 8 190 771 2208 5345 11753 24409 49063 97014 190779 375658 743112 1477240 2945141 5868120 11637263
-9 -16 -26 -42 -67 -89 -41 278 1345 4163 10710 24787 53710 111805 227699 459451 925644 1868703 3782952 7672020 15562542
28 51 96 170 284 469 798 1415 2573 4692 8485 15300 28049 53554 108059 229471 503402 1116673 2463878 5353430 11394522
10 16 22 28 34 40 46 52 58 64 70 76 82 88 94 100 106 112 118 124 130
8 24 56 121 238 431 742 1256 2137 3671 6309 10700 17701 28348 43769 65017 92798 127066 166454 207507 243680
3 13 33 61 105 197 418 951 2204 5084 11556 25688 55464 115742 232843 451380 844073 1525447 2670475 4539407 7510219
15 17 19 21 23 25 27 29 31 33 35 37 39 41 43 45 47 49 51 53 55
4 5 21 57 125 255 506 977 1818 3241 5531 9057 14283 21779 32232 46457 65408 90189 122065 162473 213033
5 12 36 95 219 453 862 1541 2644 4472 7720 14109 27863 58876 128976 283411 610443 1272472 2551916 4913291 9078231
2 19 48 88 142 226 378 659 1139 1866 2825 3907 4925 5735 6545 8524 14856 32421 74326 163554 338048
3 2 -4 -12 -13 15 119 394 1027 2386 5231 11268 24599 55319 127898 299662 699808 1609223 3620339 7952681 17068804
9 19 41 88 184 362 655 1088 1698 2633 4409 8432 17912 39299 84354 172945 336675 623603 1104781 1884372 3117140
11 10 7 2 -5 -14 -25 -38 -53 -70 -89 -110 -133 -158 -185 -214 -245 -278 -313 -350 -389
19 24 24 19 9 -6 -26 -51 -81 -116 -156 -201 -251 -306 -366 -431 -501 -576 -656 -741 -831
9 32 76 155 293 539 990 1835 3446 6555 12569 24088 45704 85172 155057 274974 474551 797258 1305258 2085449 3256879
4 22 61 126 216 323 429 496 442 104 -778 -2444 -4602 -5001 3844 41336 151368 424562 1035747 2304666 4791100
0 0 9 40 118 299 697 1509 3030 5661 9946 16758 27950 48215 89776 183216 400828 904162 2036175 4492208 9625134
13 25 39 60 106 222 501 1112 2341 4666 8915 16616 30800 57904 112321 227047 477565 1032763 2257999 4913736 10521598
6 22 40 69 146 357 871 2004 4337 8920 17613 33667 62784 115219 210184 385179 715340 1352050 2596670 5035253 9774610
2 9 16 23 30 37 44 51 58 65 72 79 86 93 100 107 114 121 128 135 142
-1 -2 -8 -29 -74 -137 -160 57 1057 4150 12265 31578 74476 164761 346652 700523 1371301 2621840 4937055 9232612 17279510
7 3 -1 -5 -9 -13 -17 -21 -25 -29 -33 -37 -41 -45 -49 -53 -57 -61 -65 -69 -73
18 35 77 152 275 474 797 1337 2301 4165 8004 16198 33941 72398 155095 330403 695128 1437759 2915603 5788900 11248491
7 28 76 166 310 512 772 1122 1725 3072 6312 13746 29508 60444 117184 215382 377075 632084 1019348 1588046 2398322
-3 10 34 68 126 257 565 1229 2528 4882 8928 15661 26685 44639 73889 121610 199423 325802 529526 854522 1366528
6 4 15 51 125 266 551 1157 2441 5079 10347 20719 41097 81174 159640 311114 596708 1118810 2039715 3601703 6142457
12 35 71 120 182 257 345 446 560 687 827 980 1146 1325 1517 1722 1940 2171 2415 2672 2942
-2 4 33 113 285 614 1209 2261 4127 7519 13909 26350 51070 100480 198753 392053 767092 1484370 2836777 5350987 9959291
-6 -2 3 9 20 44 93 183 334 570 919 1413 2088 2984 4145 5619 7458 9718 12459 15745 19644
12 25 49 95 174 290 437 612 857 1353 2630 6055 14955 37103 90000 211717 482514 1066813 2292543 4797075 9788236
-4 4 16 26 29 26 31 88 318 1047 3127 8672 22628 55944 131699 296469 640572 1332626 2675974 5196610 9773547
14 20 26 42 100 265 654 1485 3185 6596 13338 26437 51445 98542 186642 351540 662034 1251569 2380155 4556366 8781478
17 28 52 116 263 555 1079 1958 3381 5702 9728 17440 33609 69151 147721 318136 676961 1408280 2851676 5615220 10758379
27 38 47 50 36 -21 -161 -409 -683 -567 1192 7716 25976 70370 168856 372991 773719 1525352 2880909 5242830 9234076
9 24 52 89 130 174 242 430 1026 2727 7008 16735 37199 77905 155712 300326 563743 1036074 1871316 3328125 5832568
18 30 58 130 290 604 1169 2128 3710 6351 11024 20036 38775 79266 167002 353463 738166 1506184 2990058 5769178 10825356
23 37 62 108 193 352 660 1280 2553 5166 10476 21162 42594 85792 173866 355862 735855 1532471 3197482 6646280 13695427
-7 -9 1 33 93 192 372 760 1668 3760 8302 17494 34850 65539 116523 196223 313307 474021 677271 906407 1116355
4 13 35 70 118 179 253 340 440 553 679 818 970 1135 1313 1504 1708 1925 2155 2398 2654
13 32 68 135 266 535 1087 2173 4187 7702 13502 22607 36288 56069 83713 121189 170617 234188 314056 412199 530246
15 39 72 119 196 333 580 1029 1874 3542 6945 13932 28069 55954 109395 208956 389625 709697 1264414 2206485 3776346
23 48 100 198 372 668 1150 1905 3070 4928 8170 14508 27993 57733 123378 265976 568880 1196482 2462432 4942379 9645683
22 37 58 84 123 216 478 1175 2866 6656 14639 30679 61822 120915 231510 436964 817030 1517731 2805360 5166426 9499429
9 38 78 125 175 224 268 303 325 330 314 273 203 100 -40 -221 -447 -722 -1050 -1435 -1881
10 28 69 152 306 570 993 1634 2562 3856 5605 7908 10874 14622 19281 24990 31898 40164 49957 61456 74850
10 30 66 140 289 566 1040 1795 2928 4546 6762 9690 13439 18106 23768 30473 38230 46998 56674 67080 77949
10 33 71 120 172 215 233 206 110 -83 -405 -892 -1584 -2525 -3763 -5350 -7342 -9799 -12785 -16368 -20620
10 22 42 79 147 285 585 1232 2574 5271 10644 21490 43883 90886 189731 395088 813105 1645355 3271757 6413156 12459158
10 22 42 75 127 213 369 668 1240 2296 4156 7281 12309 20095 31755 48714 72758 106090 151390 211879 291387
-4 -11 -10 7 44 101 174 255 332 389 406 359 220 -43 -466 -1089 -1956 -3115 -4618 -6521 -8884
6 13 27 55 126 305 707 1511 2974 5445 9379 15351 24070 36393 53339 76103 106070 144829 194187 256183 333102
20 33 58 101 165 248 350 501 838 1791 4500 11709 29625 71674 165847 368562 789903 1638140 3296401 6452952 12322160
23 39 62 92 129 173 224 282 347 419 498 584 677 777 884 998 1119 1247 1382 1524 1673
8 23 43 75 141 283 577 1166 2326 4597 9059 17933 35875 72670 148631 305041 623716 1262615 2518947 4938203 9498009
5 6 14 50 148 354 723 1323 2267 3805 6514 11618 21442 39941 73131 129065 216721 342776 504702 677906 793712
17 39 77 145 271 506 944 1760 3284 6161 11714 22748 45250 91860 188860 390266 806371 1661427 3408743 6955423 14089337
8 11 17 19 16 30 134 493 1416 3421 7337 14514 27296 50072 91574 169956 324336 641639 1319313 2809709 6143224
20 40 82 157 276 450 690 1007 1412 1916 2530 3265 4132 5142 6306 7635 9140 10832 12722 14821 17140
26 51 92 152 243 410 767 1551 3215 6604 13286 26131 50219 94068 170936 299469 502108 798261 1187076 1611454 1890399
5 4 -3 -16 -37 -71 -118 -146 -26 614 2709 8433 22564 55138 126495 276803 584082 1198534 2408270 4765262 9325584
16 24 24 20 20 33 68 136 256 466 840 1512 2708 4787 8292 14012 23056 36940 57688 87948 131124
2 4 17 50 116 232 419 702 1110 1676 2437 3434 4712 6320 8311 10742 13674 17172 21305 26146 31772
9 21 50 100 171 262 381 562 889 1527 2760 5036 9019 15648 26203 42378 66361 100921 149502 216324 306491
13 19 47 108 224 444 870 1714 3432 7027 14700 31188 66405 140452 292753 598086 1193703 2323675 4409173 8156732 14720782
9 14 24 54 143 367 864 1895 3980 8163 16475 32679 63396 119726 219493 390258 673259 1128452 1840842 2928308 4551141
2 1 4 10 10 -19 -107 -262 -420 -378 302 2477 7844 19892 45891 100715 213678 440047 879496 1704492 3202470
23 51 106 201 353 596 998 1681 2859 4937 8750 16060 30476 59057 115120 223470 430907 828331 1597490 3107587 6107791
20 41 78 134 207 294 404 583 954 1767 3431 6461 11241 17567 24329 30972 45742 113631 394186 1354910 4215913
24 44 85 162 305 572 1065 1962 3594 6612 12299 23081 43291 80299 146385 262490 466732 834112 1520337 2853824 5518005
13 25 53 116 249 517 1041 2041 3906 7306 13366 23927 41924 71916 120808 198810 320683 507327 787771 1201630 1802099
21 36 51 65 78 93 132 286 825 2400 6393 15550 35227 75980 158961 326806 664625 1338584 2663715 5218371 10030592
-2 10 41 111 263 574 1177 2302 4346 7998 14498 26240 48213 91352 180004 367865 770694 1631114 3442799 7181181 14719595
18 37 69 128 236 416 695 1128 1861 3265 6203 12558 26282 55463 116296 240439 488096 970361 1885949 3579504 6631284
16 22 39 87 214 510 1121 2263 4236 7438 12379 19695 30162 44710 64437 90623 124744 168486 223759 292711 377742
9 12 25 49 89 160 301 608 1312 2951 6717 15102 33027 69744 142059 280147 539226 1025523 1957432 3810972 7658912
20 23 28 51 127 324 759 1624 3256 6337 12400 24960 51801 109240 229543 473023 946556 1830019 3410988 6125145 10594043
13 41 81 141 250 464 868 1574 2715 4435 6875 10155 14352 19474 25430 31996 38777 45165 50293 52985 51702
9 25 60 119 218 394 712 1273 2236 3882 6772 12095 22397 43107 85819 175561 367114 781429 1685305 3661271 7960379
4 -3 -16 -36 -64 -101 -148 -206 -276 -359 -456 -568 -696 -841 -1004 -1186 -1388 -1611 -1856 -2124 -2416
12 22 40 66 101 159 289 616 1428 3364 7800 17594 38458 81425 167274 334550 654272 1257026 2382572 4469306 8312199
16 36 62 92 124 156 186 212 232 244 246 236 212 172 114 36 -64 -188 -338 -516 -724
-7 -6 1 14 33 58 89 126 169 218 273 334 401 474 553 638 729 826 929 1038 1153
21 41 85 171 323 570 943 1470 2169 3039 4049 5125 6135 6872 7035 6208 3837 -795 -8595 -20689 -38453
0 2 3 7 42 189 631 1728 4124 8892 17723 33165 58918 100191 164127 260302 401304 603398 887283 1278947 1810626
6 27 66 133 252 474 900 1733 3392 6747 13590 27567 55991 113275 227215 450074 877438 1679215 3149022 5780661 10383546
9 31 60 105 196 390 771 1445 2548 4325 7416 13619 27615 60490 136517 305923 669051 1423011 2953492 6016853 12099022
10 10 21 57 143 333 754 1693 3747 8052 16589 32520 60419 106134 175917 274623 403748 562914 764993 1088463 1815555
0 11 47 123 250 438 717 1186 2100 4005 7931 15653 30030 55432 98265 167604 275944 440079 682119 1030655 1522082
4 2 5 13 19 5 -59 -207 -444 -647 -311 2028 9979 31542 83085 196006 427388 876388 1708621 3191415 5743531
21 28 30 24 16 36 154 489 1199 2436 4246 6390 8058 7444 1146 -16649 -55239 -128788 -258066 -472604 -813324
2 1 3 8 31 126 434 1265 3232 7490 16224 33728 68807 139949 285947 586660 1201702 2439383 4874513 9541916 18242623
11 15 11 0 -8 20 173 655 1882 4654 10510 22528 47110 97732 202274 416411 846705 1688621 3286965 6229712 11491728
22 34 53 83 132 217 384 759 1656 3781 8578 18756 39018 77011 144606 259984 452078 771652 1319659 2317357 4271044
-1 10 40 92 174 315 592 1169 2349 4640 8836 16114 28148 47241 76476 119887 182651 271302 393968 560632 783418
11 27 70 166 357 711 1348 2493 4581 8479 15978 30885 61394 125101 259370 542362 1135062 2364307 4885498 10000015 20273388
16 34 71 142 262 448 727 1148 1805 2912 5045 9795 21275 49208 114707 260357 566838 1179102 2346051 4478772 8233684
-1 3 10 20 33 49 68 90 115 143 174 208 245 285 328 374 423 475 530 588 649
8 19 45 96 187 347 635 1155 2072 3652 6373 11169 19859 35758 64343 113626 193536 313097 473469 653950 786771
16 27 57 113 198 323 534 953 1841 3719 7634 15727 32332 65876 131823 256818 484209 881806 1555374 2678560 4566449
6 15 32 61 113 206 365 622 1016 1593 2406 3515 4987 6896 9323 12356 16090 20627 26076 32553 40181
12 22 41 78 161 360 818 1788 3675 7089 12934 22598 38391 64551 109515 190999 347381 663416 1328773 2771447 5959410
20 37 57 82 120 202 409 905 1974 4070 7921 14798 27191 50372 95791 188266 381292 790377 1667237 3566548 7718806
13 31 51 84 169 384 851 1731 3207 5457 8622 12766 17791 23206 27605 27891 19281 389 -3571 134438 850205
12 30 69 155 343 727 1450 2720 4851 8376 14327 24862 44585 83254 161329 321456 652505 1340181 2773452 5766664 12017378
7 33 73 127 195 277 373 483 607 745 897 1063 1243 1437 1645 1867 2103 2353 2617 2895 3187
14 28 57 126 266 511 910 1580 2847 5550 11627 25187 54445 115233 237408 476507 934644 1797152 3395149 6309420 11538196
12 20 31 64 149 337 730 1545 3236 6713 13717 27432 53431 101047 185216 328782 565363 943750 1538049 2477213 4031782
4 9 15 22 30 39 49 60 72 85 99 114 130 147 165 184 204 225 247 270 294
8 16 40 86 162 281 478 850 1629 3313 6920 14505 30198 62187 126285 251980 492162 938034 1741020 3143748 5522366
16 27 42 65 100 151 222 317 440 595 786 1017 1292 1615 1990 2421 2912 3467 4090 4785 5556
6 20 54 122 238 416 670 1014 1462 2028 2726 3570 4574 5752 7118 8686 10470 12484 14742 17258 20046
9 24 58 124 248 479 909 1713 3230 6126 11716 22596 43907 85957 169853 339829 690242 1423895 2975313 6264575 13205537
10 30 69 129 215 346 572 997 1808 3310 5967 10449 17685 28922 45790 70373 105286 153758 219721 307905 423939
5 6 12 44 147 410 1009 2300 5014 10641 22129 45070 89636 173785 328969 611348 1125520 2076190 3879935 7401246 14436738
4 23 58 126 255 497 955 1823 3438 6343 11360 19672 32913 53265 83561 127393 189224 274503 389782 542834 742771
-3 3 14 23 25 25 43 111 257 471 648 503 -547 -3539 -10163 -23009 -45867 -84085 -144990 -238377 -377071
24 32 45 77 159 360 827 1863 4068 8584 17521 34717 67143 127585 239871 449153 842175 1587195 3012500 5760444 11088242
-1 -10 -11 17 100 263 524 900 1447 2377 4337 9008 20296 46552 104484 225721 467367 928352 1773957 3271571 5841540
7 9 21 54 125 261 505 927 1647 2882 5033 8829 15541 27268 47277 80348 133031 213663 331917 497560 717981
10 25 59 126 244 435 733 1209 2021 3494 6227 11208 19891 34148 55951 86561 124900 164655 189507 165690 30862
17 39 65 91 129 232 527 1254 2820 5913 11798 23063 45363 91274 188528 397238 845276 1799567 3807034 7968541 16464470
10 5 8 41 136 332 666 1164 1850 2805 4324 7237 13480 27024 55294 111236 216218 403981 725888 1257753 2108568
23 28 45 103 240 500 936 1633 2784 4889 9214 18760 40184 87465 188797 399575 827065 1677498 3345611 6582649 12805268
16 36 75 162 343 693 1348 2572 4878 9227 17344 32245 59237 108076 197887 368248 703127 1380167 2768016 5612597 11386394
9 25 48 72 98 137 208 331 515 741 940 966 564 -667 -3316 -8207 -16451 -29503 -49224 -77948 -118554
8 11 19 51 147 387 916 1974 3939 7405 13332 23309 39939 67244 110732 176273 266070 368619 437421 350083 -164985
1 18 58 126 237 431 788 1443 2601 4552 7686 12508 19653 29901 44192 63641 89553 123438 167026 222282 291421
6 14 44 113 249 499 946 1753 3263 6207 12115 24099 48316 96714 192312 379634 745628 1461428 2864106 5612169 10971811
3 -1 -4 15 107 376 1022 2428 5329 11117 22350 43537 82253 150582 266772 456790 755159 1204011 1848664 2727187 3850311
-2 11 36 86 184 371 731 1440 2846 5587 10754 20106 36344 63451 107105 175172 278286 430523 650176 960638 1391400
2 12 49 136 307 612 1119 1915 3118 4931 7803 12816 22494 42330 83446 166930 330518 638386 1194857 2162772 3787073
14 23 56 129 259 471 826 1484 2816 5579 11168 21959 41757 76363 134274 227530 372722 592175 915320 1380269 2035607
-3 10 37 93 208 427 810 1432 2383 3768 5707 8335 11802 16273 21928 28962 37585 48022 60513 75313 92692
14 26 51 89 140 204 281 371 474 590 719 861 1016 1184 1365 1559 1766 1986 2219 2465 2724
13 18 22 22 18 15 24 62 151 316 582 970 1492 2145 2904 3714 4481 5062 5254 4782 3286
2 9 33 96 243 556 1172 2313 4353 7978 14545 26820 50378 96085 184258 351319 660028 1214703 2183217 3828008 6548853
16 14 18 44 112 244 468 833 1440 2494 4382 7782 13808 24196 41536 69555 113456 180318 279562 423488 627888
26 51 102 207 421 842 1643 3133 5872 10895 20161 37455 70161 132626 252290 480413 910142 1705891 3149624 5711711 10156653
-8 -9 -5 6 26 57 101 160 236 331 447 586 750 941 1161 1412 1696 2015 2371 2766 3202
6 11 36 109 272 579 1093 1890 3099 5048 8652 16275 33429 71844 154659 324750 657532 1279953 2397844 4334305 7582398
12 13 19 49 142 372 874 1886 3822 7408 13943 25807 47456 87359 161694 301190 563352 1053519 1959875 3609765 6557578
3 11 30 60 101 153 216 290 375 471 578 696 825 965 1116 1278 1451 1635 1830 2036 2253
2 5 14 38 86 167 290 464 698 1001 1382 1850 2414 3083 3866 4772 5810 6989 8318 9806 11462
12 14 25 57 123 242 448 813 1520 3071 6796 15951 37865 87827 195703 416649 847748 1652956 3099403 5608869 9829151
11 19 46 109 234 461 861 1576 2893 5363 9976 18403 33316 58797 100847 168006 272095 429091 660146 992761 1462126
12 23 46 76 103 123 168 360 988 2610 6216 13584 28159 57134 115970 237426 488356 999153 2012874 3963868 7599263
10 27 58 116 238 503 1059 2170 4308 8331 15813 29644 55142 102218 189816 355285 674142 1300799 2549636 5050248 10033414
-1 -4 -11 -23 -41 -66 -99 -141 -193 -256 -331 -419 -521 -638 -771 -921 -1089 -1276 -1483 -1711 -1961
25 51 96 173 317 600 1159 2245 4300 8080 14874 26942 48463 87669 161677 307220 603675 1220565 2511928 5198007 10707121
6 28 60 95 122 126 88 -15 -210 -528 -1004 -1677 -2590 -3790 -5328 -7259 -9642 -12540 -16020 -20153 -25014
14 27 46 83 176 401 886 1840 3627 6937 13133 24885 47239 89311 166843 305910 548124 957743 1631160 2709319 4393682
16 28 49 81 128 208 369 702 1349 2526 4627 8552 16517 33764 71801 154073 325302 666144 1315301 2501803 4590846
28 43 66 110 191 336 602 1103 2052 3863 7434 14852 30927 66172 142094 299937 614308 1213403 2307810 4230074 7487335
17 21 36 76 171 394 901 1991 4208 8544 16875 32885 63920 124479 242406 469310 897323 1685023 3096214 5556282 9732049
20 27 37 61 114 218 416 815 1700 3806 8912 21041 48724 109025 234339 483377 958252 1830189 3377111 6037213 10483638
0 13 36 73 134 248 489 1018 2144 4402 8643 16149 28862 50022 85992 151103 279532 552483 1156778 2504561 5466066
7 5 0 -5 0 33 133 396 1045 2545 5774 12261 24502 46365 83595 144430 240339 386893 604780 920975 1370076
13 9 9 34 128 365 862 1820 3630 7095 13832 26934 52012 98876 184552 339505 621668 1148550 2168444 4210603 8385165
-5 -12 -23 -30 -18 35 158 387 765 1342 2175 3328 4872 6885 9452 12665 16623 21432 27205 34062 42130
5 9 17 39 105 283 715 1694 3815 8241 17147 34472 67267 128237 240613 447347 827905 1527761 2808205 5127421 9268133
25 37 57 106 230 518 1137 2397 4859 9508 18054 33532 61594 113281 210702 398013 761477 1464303 2804527 5307542 9869150
11 17 46 114 241 455 798 1334 2159 3413 5294 8074 12117 17899 26030 37278 52595 73145 100334 135842 181657
4 -1 -4 12 78 257 677 1583 3423 7014 13911 27274 53877 108552 223482 466589 976170 2023577 4123518 8225636 16048444
0 1 0 -3 -8 -15 -24 -35 -48 -63 -80 -99 -120 -143 -168 -195 -224 -255 -288 -323 -360
11 18 29 49 92 206 522 1339 3257 7370 15531 30701 57394 102230 174608 287511 458455 710594 1073993 1587081 2298296
13 15 9 -12 -48 -79 -45 193 934 2793 6973 15712 33032 66133 128347 245886 473338 927958 1861532 3802331 7819427
2 6 10 14 18 22 26 30 34 38 42 46 50 54 58 62 66 70 74 78 82
11 27 52 97 186 362 709 1407 2852 5898 12324 25725 53226 108808 219736 438752 866576 1692276 3264166 6213201 11668991
4 13 42 103 207 362 569 822 1123 1528 2245 3810 7372 15123 30914 61103 115686 209767 365428 614065 999261
15 21 37 86 203 434 831 1439 2272 3276 4278 4921 4586 2303 -3346 -14328 -33259 -63497 -109221 -175488 -268259
11 16 28 64 160 393 913 1979 3985 7464 13083 21703 34688 54819 88416 149606 268111 502480 961366 1836266 3450112
19 28 52 114 257 565 1198 2452 4861 9361 17538 31985 56799 98260 165752 273013 439839 694418 1076536 1641980 2468565
15 15 13 12 12 1 -45 -137 -191 184 2178 8829 27103 71775 172317 384631 810367 1627833 3140255 5849526 10566770
12 11 5 -6 -22 -43 -69 -100 -136 -177 -223 -274 -330 -391 -457 -528 -604 -685 -771 -862 -958
14 26 38 50 62 74 86 98 110 122 134 146 158 170 182 194 206 218 230 242 254
13 19 31 63 138 294 607 1239 2530 5172 10540 21334 42843 85426 169298 333601 653532 1273267 2471552 4797589 9360217
11 3 -4 4 47 149 343 691 1325 2507 4689 8539 14934 25131 41979 74636 152691 362315 929342 2395398 5968245
18 22 40 91 193 361 613 998 1668 3038 6128 13283 29669 66335 146381 317205 674569 1409654 2901085 5895585 11864268
11 37 82 161 309 586 1087 1974 3560 6490 12081 22905 43732 83003 155088 283716 507161 886051 1515060 2540273 4184711
25 39 52 67 92 144 269 587 1366 3134 6880 14520 30088 62684 133303 289715 637379 1401437 3047748 6514061 13642856
4 -4 -20 -50 -102 -185 -302 -435 -512 -331 602 3393 10381 26381 61265 135375 290656 612718 1272684 2603156 5230544
16 37 62 103 197 413 863 1725 3295 6108 11215 20791 39404 76543 151493 302640 605473 1208410 2406047 4790866 9565986
18 28 36 48 72 117 192 305 462 666 916 1206 1524 1851 2160 2415 2570 2568 2340 1804 864
6 31 75 155 307 590 1091 1932 3290 5463 9049 15351 27179 50290 95789 183908 349686 651191 1181055 2082235 3569067
13 24 44 80 152 311 679 1524 3380 7215 14639 28129 51229 88660 146248 230547 347999 503434 697670 923926 1162710
10 22 32 51 114 303 788 1898 4249 8981 18195 35742 68605 129235 239371 436181 782284 1383028 2418714 4208909 7345156
7 30 81 174 320 532 841 1335 2242 4084 7930 15770 31017 59118 108216 189751 318817 514002 796327 1186766 1701670
14 27 59 134 293 607 1198 2270 4163 7468 13293 23865 43815 82762 160259 314926 620952 1217673 2362755 4528740 8580085
8 12 16 20 24 28 32 36 40 44 48 52 56 60 64 68 72 76 80 84 88
8 0 -4 18 98 273 583 1076 1829 3005 4987 8667 16029 31260 62764 126655 252582 493110 938366 1738280 3135532
-3 4 19 42 73 112 159 214 277 348 427 514 609 712 823 942 1069 1204 1347 1498 1657
14 24 32 37 53 130 397 1139 2922 6787 14546 29230 55761 101947 179931 308262 514798 840698 1345812 2115835 3271653
7 23 56 112 206 369 661 1190 2131 3727 6237 9780 14024 17718 18228 11631 -5265 -28077 -27602 87844 547546
23 36 62 118 231 444 832 1544 2899 5581 11011 22063 44521 90194 183657 376560 777027 1608138 3321360 6812948 13832967
8 0 -5 4 43 136 319 644 1176 1961 2930 3727 3573 1607 -1170 4025 48104 214582 698717 1914790 4680631
6 17 37 64 93 116 122 97 24 -117 -349 -698 -1193 -1866 -2752 -3889 -5318 -7083 -9231 -11812 -14879
-2 -7 -2 33 136 369 821 1608 2870 4765 7460 11119 15888 21877 29139 37646 47262 57713 68554 79133 88552
18 20 30 66 151 310 570 972 1603 2653 4499 7817 13726 23977 41217 69385 114336 184842 294188 462668 721393
3 0 -3 -6 -9 -12 -15 -18 -21 -24 -27 -30 -33 -36 -39 -42 -45 -48 -51 -54 -57
16 28 38 46 52 56 58 58 56 52 46 38 28 16 2 -14 -32 -52 -74 -98 -124
    """.trimIndent()
        .split("\n")
        .map { it.split(" ").map { it.toInt() } }

}