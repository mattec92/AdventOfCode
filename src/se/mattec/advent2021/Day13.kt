package se.mattec.advent2021

fun main(args: Array<String>) {
    println(Day13.problem1())
    println(Day13.problem2())
}

object Day13 {

    fun problem1(): String {
        val initialSheet = initialSheet()
        val firstInstruction = data.second[0]

        if (firstInstruction.first != "x") {
            throw IllegalArgumentException("Input expects first instruction to be a left fold.")
        }

        val outputSheet = foldLeft(initialSheet, firstInstruction.second)

        return outputSheet.flatten().count { it }.toString()
    }

    fun problem2(): String {
        var sheet = initialSheet()

        data.second.forEach {
            if (it.first == "x") {
                sheet = foldLeft(sheet, it.second)
            } else if (it.first == "y") {
                sheet = foldUp(sheet, it.second)
            }
        }

        print(sheet)

        return ""
    }

    private fun foldLeft(sheet: Array<Array<Boolean>>, foldAt: Int): Array<Array<Boolean>> {
        val newMaxX = sheet[0].size - (sheet[0].size - foldAt)
        val outputSheet = Array(sheet.size) { Array(newMaxX) { false } }

        val lowestX = newMaxX + 1 - sheet[0].size + foldAt
        for (y in 0 until sheet.size) {
            for (x in lowestX until foldAt) {
                val right = sheet[y][x]
                val inputX = sheet[0].size - 1 - x + lowestX
                val left = sheet[y][inputX]
                outputSheet[y][x] = right || left
            }
        }

        return outputSheet
    }

    private fun foldUp(sheet: Array<Array<Boolean>>, foldAt: Int): Array<Array<Boolean>> {
        val newMaxY = sheet.size - (sheet.size - foldAt)
        val outputSheet = Array(newMaxY) { Array(sheet[0].size) { false } }

        val lowestY = newMaxY + 1 - sheet.size + foldAt
        for (y in lowestY until foldAt) {
            for (x in 0 until sheet[0].size) {
                val top = sheet[y][x]
                val inputY = sheet.size - 1 - y + lowestY
                val bottom = sheet[inputY][x]
                outputSheet[y][x] = top || bottom
            }
        }

        return outputSheet
    }

    private fun initialSheet(): Array<Array<Boolean>> {
        val maxX = data.first.maxBy { it.first }!!.first
        val maxY = data.first.maxBy { it.second }!!.second

        val sheet = Array(maxY + 1) { Array(maxX + 1) { false } }

        data.first.forEach {
            sheet[it.second][it.first] = true
        }

        return sheet
    }

    private fun print(sheet: Array<Array<Boolean>>) {
        sheet.forEach {
            it.forEach {
                print(if (it) "#" else " ")
            }
            print("\n")
        }
    }

    private val data = """
309,320
32,761
258,108
70,486
499,474
1038,537
1054,80
1247,114
192,572
1262,628
669,250
534,756
319,52
1230,98
758,858
556,535
1089,522
1037,201
474,726
447,63
649,884
590,471
661,212
1307,634
517,680
31,602
473,511
427,838
1213,70
572,658
1305,116
574,796
154,243
939,588
261,273
417,215
99,842
850,317
63,114
483,616
940,183
1293,544
1293,444
390,537
454,889
129,431
206,610
53,133
1181,46
156,618
564,495
1054,856
298,729
636,588
1282,180
596,152
65,812
659,410
746,623
1103,63
1203,781
169,323
512,605
418,763
729,812
626,725
1228,645
1017,400
661,233
1282,240
308,140
157,570
279,606
626,180
1155,791
992,854
85,295
1042,70
547,289
657,287
542,403
1228,249
612,239
184,571
892,763
813,149
271,602
952,653
353,680
1139,500
159,254
522,613
472,98
825,323
557,518
398,733
446,201
483,418
25,232
460,169
1245,82
862,78
827,394
448,816
753,180
769,154
1083,54
576,89
855,18
566,357
334,126
1278,761
1197,740
1156,203
171,588
684,169
42,877
80,878
1111,18
964,544
957,680
380,731
552,844
1225,779
562,458
1128,98
356,852
495,717
868,602
358,653
1225,752
796,70
714,742
989,564
460,689
3,260
479,486
408,641
1155,502
375,431
709,413
1084,98
1233,239
1104,526
1221,280
256,609
321,576
1305,794
1225,142
1153,681
54,761
391,544
636,236
661,380
862,463
159,522
1056,40
199,876
917,82
1238,579
667,156
1303,196
420,408
1252,294
353,774
1225,164
430,484
1096,403
1116,725
648,826
840,795
592,68
1190,834
28,617
510,210
591,292
1151,45
686,577
893,231
909,700
957,568
1077,682
838,364
1014,739
1154,618
1026,732
745,614
1258,192
438,500
850,169
102,74
932,353
684,546
85,779
418,579
729,728
48,266
872,36
649,128
1240,486
1104,15
729,530
408,305
246,352
776,60
1304,544
920,537
139,330
85,516
167,74
353,568
569,730
768,403
657,735
1110,889
227,392
311,712
10,889
460,849
1073,616
729,455
893,278
1151,254
708,478
1116,45
750,124
407,649
383,219
1304,96
967,56
1307,410
407,693
709,761
897,30
1116,515
579,21
129,848
11,436
499,52
802,117
296,269
391,96
1213,424
216,394
430,410
77,239
1208,410
0,194
1088,523
882,796
318,40
1240,869
990,544
1084,546
1295,504
811,392
403,514
457,64
1179,5
1310,194
102,858
25,186
927,227
523,441
684,770
1222,427
493,761
483,868
1195,158
65,516
714,70
371,511
335,278
661,757
815,522
251,66
97,424
301,344
398,197
420,325
430,260
356,841
304,278
455,18
1076,549
915,812
1151,177
1052,786
661,234
745,280
893,530
428,796
1225,295
1153,12
1299,436
219,65
848,688
497,149
584,82
30,353
818,750
53,89
405,227
49,378
997,726
1069,590
89,280
648,516
376,490
1190,610
654,700
972,317
821,133
1285,186
1213,266
1203,410
542,491
596,294
490,770
1203,333
89,663
957,214
152,45
492,750
221,372
771,319
171,476
825,352
626,322
448,463
1260,162
766,394
534,610
435,513
408,589
402,10
1293,14
1,581
1154,462
517,478
666,5
73,364
734,89
1051,364
382,761
5,548
438,52
445,400
704,266
952,155
1159,10
236,763
428,684
626,124
473,2
534,284
666,794
960,840
818,465
131,889
120,610
470,795
577,154
974,406
497,772
748,10
1246,423
20,571
113,740
391,798
594,820
97,628
525,64
15,488
641,250
1151,864
508,677
808,493
534,138
972,884
917,621
618,229
1240,25
1231,649
800,96
967,728
853,64
999,504
641,168
338,789
624,577
1064,253
1154,126
1017,848
335,616
912,815
107,410
485,519
157,233
490,124
1282,404
402,467
222,523
1116,281
1292,588
716,248
676,284
815,298
325,805
321,347
1235,773
962,60
316,789
336,406
875,154
432,140
935,288
895,682
1113,830
893,663
637,285
343,728
413,864
1300,506
787,441
812,618
508,117
991,842
107,289
1181,848
442,740
1171,330
940,250
596,742
70,25
662,826
1266,649
1026,698
356,393
1305,330
383,870
370,644
880,484
1285,232
1103,719
7,166
719,292
132,689
800,82
736,529
711,227
940,15
663,614
584,408
483,306
1282,490
5,234
1047,350
268,70
574,529
907,514
192,770
272,89
1222,693
288,106
343,838
483,250
115,127
1278,133
381,805
1211,842
53,413
1049,273
180,502
644,100
810,243
432,523
52,702
930,646
989,576
1252,824
189,680
438,858
815,717
57,137
480,877
1198,717
97,824
70,522
375,288
909,252
189,640
1258,731
479,38
1208,484
535,175
1278,581
273,425
1257,133
6,511
741,142
589,306
872,352
591,154
908,10
393,621
442,602
807,548
574,753
1084,684
812,768
870,17
1304,383
1300,100
1158,401
883,838
85,107
1290,571
321,800
502,493
28,796
1305,548
981,313
487,596
555,394
644,521
676,106
503,346
601,481
1026,172
1305,234
807,884
103,287
897,331
1175,693
1154,768
1049,586
720,647
892,579
989,458
313,726
922,484
75,773
1151,796
909,812
562,79
909,728
102,820
922,410
18,588
462,688
674,306
79,649
561,660
1240,522
109,257
1091,513
159,596
510,96
720,471
606,266
555,838
30,801
964,684
1295,497
1198,177
890,345
848,770
890,325
880,410
65,378
989,542
460,577
601,805
226,98
865,400
636,795
939,511
510,82
763,561
870,877
1128,796
893,364
17,444
649,233
700,693
649,795
1235,386
741,730
508,217
841,684
472,796
499,551
840,323
370,711
754,535
748,589
5,330
420,345
875,484
1156,243
562,589
107,333
719,740
606,628
256,597
443,64
1052,108
443,830
382,133
127,466
1139,196
611,642
738,658
44,649
1139,418
754,359
872,500
649,661
1061,292
1004,442
155,551
107,561
479,794
249,740
15,390
610,693
1121,214
214,403
594,248
1230,424
572,197
643,458
393,532
1293,880
565,588
338,884
370,183
85,752
1257,89
745,343
165,133
88,467
1299,710
413,331
671,497
10,58
202,58
457,830
428,210
798,605
1293,798
647,728
1227,537
335,52
977,235
823,820
487,820
417,663
1307,260
77,655
83,537
1143,596
470,323
1164,565
417,168
181,63
657,607
1145,761
661,788
718,68
929,805
1063,649
552,484
1064,430
748,527
897,864
1225,115
736,141
1279,602
741,164
288,340
420,486
662,511
552,74
102,36
1054,814
522,241
276,310
912,285
28,240
1225,107
1290,60
746,495
661,554
667,448
574,365
182,350
184,157
445,494
28,654
1061,17
1153,233
497,513
1151,98
791,693
499,103
875,381
952,269
972,105
930,163
730,427
1217,10
530,127
28,277
1295,182
348,732
130,516
186,819
919,350
5,116
912,814
1009,792
1292,210
552,50
811,24
155,103
329,313
432,754
1146,592
207,719
146,565
1305,547
776,284
306,442
1108,506
503,884
912,79
1098,292
858,127
135,693
676,340
1056,406
1154,798
256,38
644,5
972,577
490,322
716,646
565,551
1230,16
99,500
1300,388
813,12
634,323
964,530
1056,488
726,25
990,63
7,390
159,45
544,484
321,352
651,484
284,722
934,404
236,355
1135,408
780,127
788,241
92,365
671,390
1034,310
589,826
989,683
766,410
438,542
321,318
157,625
1076,408
853,830
247,649
1074,315
381,761
954,729
644,889
534,834
569,752
1146,302
268,376
427,504
733,154
976,798
544,394
70,372
256,149
499,842
575,649
497,823
519,693
954,841
960,278

fold along x=655
fold along y=447
fold along x=327
fold along y=223
fold along x=163
fold along y=111
fold along x=81
fold along y=55
fold along x=40
fold along y=27
fold along y=13
fold along y=6
    """.trimIndent()
            .split("\n\n")
            .let {
                it[0].split("\n").map { it.split(",").let { it[0].toInt() to it[1].toInt() } } to
                        it[1].split("\n").map { it.split("fold along ")[1].split("=").let { it[0] to it[1].toInt() } }

            }

}