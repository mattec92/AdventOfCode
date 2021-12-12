package se.mattec.advent2021

import java.util.*

fun main(args: Array<String>) {
    println(Day10.problem1())
    println(Day10.problem2())
}

object Day10 {

    fun problem1(): String {
        var score = 0

        for (line in data) {
            val queue = ArrayDeque<Char>()
            for (char in line) {
                if (queue.peek() == char.inverse()) {
                    queue.pop()
                } else if (char.isEndParenthesis() && queue.peek().isStartParenthesis()) {
                    score += when (char) {
                        ')' -> 3
                        ']' -> 57
                        '}' -> 1197
                        '>' -> 25137
                        else -> error("Illegal character")
                    }
                    break
                } else {
                    queue.push(char)
                }
            }
        }

        return score.toString()
    }

    private fun Char.isStartParenthesis(): Boolean {
        return setOf('{', '[', '(', '<').contains(this)
    }

    private fun Char.isEndParenthesis(): Boolean {
        return setOf('}', ']', ')', '>').contains(this)
    }

    private fun Char.inverse(): Char {
        return when (this) {
            '{' -> '}'
            '}' -> '{'
            '[' -> ']'
            ']' -> '['
            '(' -> ')'
            ')' -> '('
            '<' -> '>'
            '>' -> '<'
            else -> error("Illegal character")
        }
    }

    fun problem2(): String {
        val scores = mutableListOf<Long>()

        data.forEach { line ->
            var score = 0L

            val queue = ArrayDeque<Char>()
            for (char in line) {
                if (queue.peek() == char.inverse()) {
                    queue.pop()
                } else if (char.isEndParenthesis() && queue.peek().isStartParenthesis()) {
                    return@forEach
                } else {
                    queue.push(char)
                }
            }

            var toAutoComplete = ""
            while (queue.isNotEmpty()) {
                toAutoComplete += queue.pop().inverse()
            }

            toAutoComplete.forEach {
                score = score * 5 + when (it) {
                    ')' -> 1
                    ']' -> 2
                    '}' -> 3
                    '>' -> 4
                    else -> error("Illegal character")
                }
            }

            scores += score
        }

        return scores.sortedBy { it }[scores.size / 2].toString()
    }

    private val data = """
(({({({(({{<{{()<>}}>}}([<[(<>{})(()()))[[{}[]]{<>{}}]>(<(<>[])<[]()>><[{}[]]([]<>)>)]{{({{}[]}{{}{}})}})
[{(<(<<([[[{(([]{})<(){}>)<{<>}(()[])>}]]{[([(()[])[[][]]]<((){})((){})>)({{{}[]}<{}>})]}]){{[[<<{<>}(<
((<[([(({<<<{{<><>}({}<>)}{{<>{}}[[]()]}>>([<[{}{}]>{[()<>]<<>()>}]<(<{}()><[]{}>)<<[]<>>}>)>}[<{<([<><>]<
[<[<({{({{[({[[][]]<{}<>>})<[<{}{}>]([<><>](<>{}))>]{[{[[]{}}{{}{}}}<(<><>)({})>]{({()()}[<>()])
[<{<<({[{{{(<({}()){{}[]}><(<><>)[[]{}]>)}<(([{}{}]{<>[]}))<(<<>[]>){<[]>(<>{})}>>}({{((<>())<
{[([<[<({((([<[]()>{{}()}]<[{}{}]{[]()}>){{<<>[]>}(<<>()>)})([<({}<>)<{}()>>{[()[]]({}[])}]))(([{<
(({[<[((<([[(<[]()>{{}[]})([[]{}]<<><>>)]({{{}()}((){})})]<[<{[]{}}(()<>)>{(()[])<(){}>}]((<()
([<[[((<<({({([]{})<()()>}({()()}([]())))<{(<>[])<()[]>}>}>{{<[[<>()](()<>)](<[]{}><<>()>)
[[{{(<{[[<<{{<()()>(<>())}<{<><>}<<>[]>>}>{<<({}[]){()<>}>([()[]]({}<>})>(({<>()}[[]<>])<{()<>}{<>()}>)}>[[[
<[<{[{{[[(<[{<[]>[[]{}]}<[(){}]<[]<>>)]><[<(<><>)(<>{})>{<<>{}>[[]{}]}]<[[[][]]<<>()>][<<>
<{[<<({[(<[<((()<>)[<><>]){((){})[()[]]}>[{([][]){[]<>)}{[{}[]](<>{})}]]{{{({}[]){()()}}({(){}}{()})}[[{[]
[<([[(<[{<<<<{[]()}<<>{}>><{[]{}}((){})>>>[<<(<>())<{}<>>><({}<>][<>()]>><<[{}[]][<><>]>(<()[]>{{}})>]>}(<<[
(<(<[({([{(<([[]()]<()[]>)[(()())]><({{}[]}<(){}>)[(<>[])(<>[])]>)[<[<<>{}>[{}[]]]<<<>[]>{()()}>>
[{[{<[<(<<<<([()<>]{()<>}){({}[]){{}{}}}>{([[][]]([]()))<<<>{}>{{}[]}>}>{({{[]<>}[<>{}]}[<[]()>((){})]>[([{}
[{{({<<{(<(<(<<><>><<><>>){<{}[]>)>)>)}>>{[<{[<<{(()<>)<{}{}>}{<()()>(()[])}>([<<>[]><()<>>]<[{}{}]<()<>>>)>
<{<{({<{<<<{(({}{})(<>[]))}[([{}()]({}<>))]>[<[<<><>><[]<>>]>]>[[((<[]<>>)[[()[]]<[][]>])<<{{}[
([([(({<<(({{((){})[{}()]}((()()){{}<>})}[[(()())]])(<(((){}){[]()})({{}[]}{{}{}})>{{[<>[]]{[]()}}[[{}<
{[[{[{[{<({[<{[]<>}({}<>)><[<><>]>]<<({}<>)<<>()>>>]<([{<>{}}]<<<>[]><{}()>>){<[()[]][<>{}]>[<<>
<<[<[(<<(<(((<[]<>>){{[]()}(()[])})[<{[][]}(<>())>{<()<>>{()[]}}])(({[[]{}]<{}<>>}(<()<>>[()[]]))<[[
<[<([([([{[<[[<><>]]{([][])[<>{}]}>[{{[][]}{<>()}}(({}[])<[]{}>)]]{<({[]}[()[]])>}}([([(()<>)]{([]{}){{}
{<<[{[{[{(((([<><>](<><>))[<(){}>{<>()}])[{<<>{}>}(<<><>>[()[]])]>({({()<>}({}{}))<[{}[]]{()<>}>}<
{(({((((<[{(<(<>())><([]<>)[{}()]>)[([{}[]]{[][]})[<[]{}>{<>()}]]}<[{{{}<>}[{}[]]}]{<<()>>}>][({(<{}()><{}[]
[([[<<[[{[{<<[{}<>]>(({}())(<>{}))>}]}{{[(({{}{}}[()<>])<(()<>){[]()}>)<<{[]<>}{<>[]}><[[]{}]([][])>>
([<{[(<([{<{[<<><>>[[]()]]<({}<>)[{}{}]>}<<[[]()]{<>{}}><<[][]>[{}()]>>>}{<{<{[]{}}[[]()]>(<
((<<[[<(<[(((<<>{}>(()[]))({[]{}}([]<>)))[{<<>[]><{}[]>}{(<>[]){(){}}}]>[[[[{}<>]<()()>]](<(()<>)(<>)>{[
[(<((<<<[(<<<<[]{}>{()()}>>>{((([]())([]()))){{{[]<>}{<>[]}}{[{}()]{<>()}}}}}]{<<{(([]{}))[<{}<>>({
<<[({[<(<[{(({{}()}{{}<>}))}<<{[[]()][<>{}]}({<>()}<[]<>>)>>][<(<[()<>]>[(<>[])<{}{}>])>[{<[{}{}]([]())>((<>
[{{[<<[<[[({(({}())<<>()>>{{<>}[<><>]}})<<[[<>[]][[]]]({()<>}<()()>)>{{{<><>}}([(){}]<[]>)}>][{<{((){
{[({[[((([{<<<{}{}><()()>><([]{})>><[{()()}{<>{}}]>}][<[<(<>{})(<>{})>((<>{}){{}<>})]<{<[]()>(<>{})}(<
((<{({({({<{<<{}[]>({}{})>[<[]<>>({}[])]}>})})})}({{(<[[([<([]())([][])>][({()()}{{}{}})])[[{
{{{{{[((([({<<<>{}>(()<>)>{[<>[]]{{}()})}[(<[]{}>[<>()])<(()<>)[<>()]>])<({[{}()][()[]]}{[{}<>
{[(([{(<<<[([<{}{}>[{}<>]]<<<>()>[{}()]>)([[<>{}][{}<>]]{{{}()}<<>[]>})]><{{[([]{})[{}[]]]}{({<><
{<{([<[{[{[[[{{}{}}<{}()>]<{{}()}>]]([[([]())[(){}]]<<[]<>>{(){}}>]<[[{}()](()[])]{{()()}}>)}[(<[((
{{{{{[{{(<(((<<>[]>([][])){{()<>}[()<>]})(<{()[]}{<>[]}>({<>()}[(){}])))>)}((<{{<<()[]>(()<>)><<()()>[{
[[[(<[{[{([<<<<>><<>>><[[]{}](()<>)>>]<{[(()<>)[[]<>]]{<[][]>[[]()]>}[<([]<>)[{}{}]>{[(){}]([]
<[<(<{{{{<{[{(()())}[{(){}}{{}()}]]<<[<><>]({})>[(<><>)]>>>({{{(<>[]){()()}}<((){}){()[]}>}({({}()
<[{{[<([[[(<<<()[]>({}())><[{}[]]{[][]}}>)<([{[]()}{{}{}}]{[[]{}][{}<>]})[<<<>{}>><{[]}<<>()>>]>]{
{((<<{[{<[(<[[<><>]{[]<>}][[<>()][{}{}]]>[<{{}()}({}[])>[[<>[]][<>[]]]])<{<({}{})>{<{}<>]{(){}}}
<[{([<<{(([((([]<>))<<{}()>(()[])>)(([()[]]{[]()}))]<[<<{}()><[]{}>><([]<>)<(){}>>][{[<><>][<>
[<(<{[{[[(<{<[<>()]>}([<<>[]>{<><>}]{(()[])(()<>)})>)]((<<(<()<>>{[]{}}){<[]()>}>([<{}<>>([]())
{({[[<<{{[{<{<[]()>}{<{}<>><{}>}>(<{()<>}((){})><{<>{}}([][])>)}][[<{{()()}({}[])}<{<>{}}<<>{}>>>{<<[]()
[{<([<<[{[[<(<<>()><[][]>)>(<<<>{}><{}()>>[[{}<>]<()<>>])]][{([[{}()]]{({}())(()()]})(<<()<>>(()[])>)}<[([[]
[([((([[[(<<{[()<>][{}{}])([<><>]<{}>)>((<{}<>><()<>>)({{}<>}<{}{}>))>[{((()[])[(){}])}({{{}{}
<{[<[[{{<[{([[<><>]]<<{}()>([]<>)>)([(<>[])][({}()){()<>}])}{<([[]<>][<>{}])(<()()>({}[]))>
([{({[({{<((<(()<>){[]{}}><<<>[]><()[]>>)(<{[]<>}[{}[]]><{{}()}[[]()]>))>}}{[[[[<(()())<<>()>
{<<<[<<<{<<{<({}()){[]{}}>}>>}><{<{<[{[][]}(<>)]([[]<>][<><>])>}([{({}[])<{}{}>}]{[[()[]]][[<>[]]<[]{}>]})>}>
[[[(<<(<((([<([]<>)[<>]><([]<>)<{}{}>>]{({{}[]}<()<>>)[<()<>>]}))){{<{<[[][]](<><>)>{<{}{}>{{}[]}}}<[[[]{}]
[[{({[[((((<<(<>{})(()<>)){[<>[]]}>))[(<<<[]<>>{<><>}>>(({{}()}(<>{}))))(<[{[]<>}((){})]{<{}<>>
<<<(([[{{<<({{<><>}<<>{}>})>{[<<<>>[()[]]>[{()[]}{<>[]}]]{{[<><>]}(<{}<>>(<>[]))}}>}}<{<[(({{}{}}{[]()})
{({(({<({[{(<({}<>)[()()]><[<>{}]>){{(()<>){[][]}}([<>()](<>()))}}{[<<{}()>([]{})>[<<>()><<>{}>]][
{<{[(<([[{<({<()[]>[<>[]]}[[<>()]<()()>])>{[[{()<>}{()()}][[{}<>][{}()]]]([{{}<>}]<{<>[]}([])>)}}[<[[<[](
({(<({([{<[[[(<>]({}{})][<()><()[]>]]]<(<[()<>]>{{()[]}[()[]]})([[<>[]][()<>]](({}())([]())))>>}]{(({
{[[<<{[{<[[<((<>[])<{}{}>)>]{<<[(){}]<{}{}>>>[<{()<>}<{}<>>><[()<>]>]}]>><[[({<(<>[]){()<>}>}[[[{}<>]]([[]<>]
<{<<{[{<{([{[[[][]]]}]((<{[]<>}{()}>(<[]{}>[()[]]))<{{()()}[[]{}]}<{<>[]}{<>[]}>]))}<{[([<[][]>
({[({[[[{(<[{(<>())([][])}<([]())({}())}]>({(<()<>>{{}{}})<[()[]]<<>>>}([[{}[]][()]]{{{}[]}<[][]>})))[
[{[{{{([{[({{<[]()>[{}{}]}<{()}<{}<>>>}<[<()[]>{[]()}]>){{{[<>{}][<>[]]}{<()<>>{<>[]}}}[{(<>[])<()[]>}
<<[[{({([([{{(()())<<><>>}}]<(({[]<>})<<(){}><<><>>>)>)(<{{({}[])<[]()>}{({}[])[<>[]]}}>)]<<[<[{()[]}<{}>]
{{[{<(<[<(({<<[]{}]><[[]<>]<<><>>>}<(<(){}>)>)[[([{}{}][[]{}])[<()()>]]<{{{}[]}{{}}}[[[]()]<{}[]>]>])
{<({(<{[[[({(((){})([]()))<(()())([]{})>}[((()()>([][]))])][{[{{{}()}{()()}}[[[]()]{[]<>}]]}{(({[]<>
([{{<<<([<[({<<>[]>{[][]}}(({}{})[<>{}]))]{{((<>{})){<()()>(<>())}}}>]({[([([][])]{[(){}](()[])}){<[{}()}>}
<[{<[<<{[[{[[({}())<{}()>]]([(()<>)][(()())(()[])])}<(<[[][])<()()>>)([[{}](<>[])](<{}()>{[][]
(<([{({{(({{{({}[])}({{}<>}<[]()>)}<<<{}>{()<>}>({<><>}{<>[]})>}({{[<>()][<>[]]}}(<({})<[]()>>)
[{(<[[[<<{{{(([]())([][]))<{()<>}(()<>)>}<[{[]()}[()()]][[[]()][{}()]])}({<(<>()){<><>}>}[[(()()){(){}}]{(<
[(([[{[{((((<<<>()>({}())>{{{}()}[[][]]})<<<{}{}>([])>>)<[{[[]{}]}{<{}{})<{}>}]<(<{}{}>{<>[]}){<[]{}>{
<{<<<((([(<{<<{}[]>[[][]]>[<<><>>]}>)<[[[<()()>{<><>}](<(){}>[[]{}])]][<{{<>()}({}<>)}>{[{<><>)]{(()<>)
(((<(([([[[({<[]>[[][]]}[{<><>}<[]()>])<({[]()}(()[]))>]<[{[{}{}]}(<[]{}><()()>>]<(<()()>)({<><>})>>]<(<
[[{<{({<(((<<{[]()}(()<>)>([{}()][()<>])><[([]<>)[()[]]]{{()()}(<><>)}>){[(<[]>)[[[]{}]{[]()}]]}){[[{<()[
{([{(<{({<((<[()]<()<>>>({{}}[{}<>])))>><{<{<[<>[]]<[]{}>>}<[[{}]({}{})]{(<>())<()()>}>>(<
<[<{{[({{([{{{{}[]}({}<>]}{<[]{}><()[]>}}<[[()][()()]]{{()()}({})}>]{[(<()<>><{}<>>)[[()]{[]{}}]]<{<[]
(<<<[((<[((<[<<>[]>(()<>)]{{[][]}<{}<>>}>[[<{}()>[{}<>]]{{(){}}[{}()]}])<<([{}[]]{(){}})(<[]
({({[<[[{<<([((){}){[]<>}])>({[<()>(<><>)]})>[[({{(){}}}[[<>[]]<[][]>])[<(()())(<><>)>{([])}]]({[([]<>)
<[[[{(([[<[((<<><>>(<>[])}[{<>[]}[<>[]]]){<[<><>]>[(()[])[[]{}]]}](<{<[]<>>(<>[])}((()())<[]<>>)>({<(){}>
[{{{[[{([{{[{(<>())([])}]<(<[]><{}>)>}}{[{[<()[]>{()}][{()<>}{[][]}]}((({}<>)({}()))(<<><>>
[[{[[{{{{{<(<[[]<>]{[]}>)(<<()][()<>]>[{[]()}{<>[]}])>[(<<[]()>{[]<>}>(<[]<>>{<>{}}))[[{<>()}
([[{[[[([(<{[{{}{}}{<>[]}]{{<><>}}}>)<(<{<(){}>[<>[]]}{<{}[]>{{}[]}}>[[({}<>)(()())]<<{}{}><[]()>>
({{{([[({({({{()[]}({}{})}({()<>}[{}<>]))}{{<[{}[]>[[]()]>{[{}()]<[]<>>}}[(<()()><{}{}>)({[]<>}<<><>>)
{{<{{{[{((<[([[]{}]<<>{}>){<<>()>(<>[])}]{{([]()){[]{}}}}><({{{}[]}<[]()>}{<<>[]><[]{}>}>{<[()()](<>
[{([<[<([[[<<(()[]){(){}}>[[{}{}](<><>)]>({[{}()]}<(()<>){()[]}>)]]<<[[(()[])<<>{}>]{<()<>}<{}<>
<[{<({{[([[((<{}>[{}[]])((<>[])([]{})))(([{}[]]{{}()})({{}()}(<>{})))][[(<<>[]>){[{}{}]{[][
<<<<[(({{({([<{}()>{<>[]}](({}<>)<<><>>)){([[]()]{<>{}}){(<><>)}}}){<{[[<>()]]({{}{}}<()()>)}>[[([()](()[]))]
<({[({{{({<{([<>()]){[[]{}]([])}}(<[<>[]][()<>]>([<>[]][<>{}]))>[[((())([]()))<([]<>)((){})>]<
((({[<((<{<([({}{})[{}<>]][<(){}>({}{})])({[[][]]{<><>}}<<<>()><()()>>)>}([[[{<>[]}][{<>[]}
[{[[(((<{<{({(()[]){[]{}}){([]())[<>[]]})[(({}){[]{}})({[]{}}{()()})]}<{({<>{}}){({}<>){()()}}
{(<{[({<(([<<([]{})<(){}>>([{}[]]{{}{}})>]{<{[<>()]{[]<>}}>}))>({{<{<(<>{})([]{})>}<{{()<>}<[][
{(<<[([<[<({([()[]]{<><>})[[<>[]]([][])]}{({[]{}}[[]])(<[]()>)})>]>{<(({[[[]][{}[]]][<{}{}>(()<>)]}<{[{}<>]({
{<({<<{(<{([[<[]<>)[<>[]]](<()[]>)])[[{{()[]}<[][]>}{[()[]][<><>]}][{<{}()>([]<>)}(<<>[]>({}[])
<({[[<{{<[{[<{()<>}>][<[[]<>](<>())>({{}()))]}[((<{}()>([]{}))(([]{})<<><>>))<<{<>{}}[{}[]]>>
{[({[(<([([<[<{}<>>[<><>]]{{<>()}{()()}}><([()<>]<<>()>)({<>[]}({}()))>]<<[[()[]]<<><>>]<((){})(<><
<((({<(<({{{[({}())(()())][(<>)]}[[{{}[]}[<>[]]]([[][]]{()()})]}({{([]{})<[]()>}[(())({}<>)]}<{{[][]}{()()}}
[{{([[([{{{(<<[]()>[()[]]>{{[]<>}[[]()]})[<([]<>)<[][]>>]}}}(([[{(<>{}){[]{}}}{[(){}]{<>[]}}
([[{[{<[([[<[[()[]]([]())]<{<>{}}[<>[]>>>{{(<>())([]<>)}<({}[])<[]{}>>}]{<<{[][]}{<>()}>{(()())<(){}>}><
(<[({<(<(((<[<<>()>{{}()}]{(<>)<()()>}>([(<>[]}<[][]>]{{{}()}(<>)}))[[[{{}[]}{{}}]([[]<>]<{}()>)]])[{<[{[
{<([{(([[[[{((<><>)(<>())){({}())<[]{}>}}<{(<>[]){<>[]}}<{{}<>}[(){}]>>]]][[(<{<{}()>{[]}}[<<>>{{}<>}]>([
<{[[<({[[({([{()()}{[][]}]([[][]]<{}>))}{<[<{}())<[]()>]{(()()){{}<>}}>({[{}<>]{()[]}}<{()<>}
[<<((<(([(<{[[()<>]{{}<>}]([[]()][<>[]])}{(<[]>)((()[]){[]()))}>{{(([][])({}{}))<[(){}]>}})
[<[<{[{{[[[<<((){})<()>><{{}[]}{[]()}>><((()){[][]})>][<{<<>[]>[<>[]]}({<>[]}{()()})><{(<>{})<<>()>}>]][([[
([<[<{<{(((<([{}<>])([<>[]]([][]))>[<([]())<()<>>><<()()>{{}()}>])>[<<<{()<>}<<>[]>>{<{}()
<({{<(<([({<[{<>[]}]<[{}{}]{()[]}>>(({[]()}))}[<<{(){}}([][])>(<()()>{()})>{([[][]]<<>{}>)<<[][]>([]())>}
{[(([<([[<[<{[()]}>[<[<>{}][<><>]>{[()]{<>}}]](<{([][]){[]()}}[{()()}(<>[])]><[[[]<>][[][]]]<
<<({<<<({(<<[<[]()>{[][]}][<{}{}>{[]<>}]>[{{<><>}<()()>}(<{}{}>(<>))]>{[[<()()>[[]()]]{<<>[]><[]{}>}]})
[[[(<<(({[(((((){})(<>{}))<[[]()]{{}{})>)(<[()[]]{[]<>}><[<><>]{<>{}}>))([<({}<>)({}[])>[[(){}]
{[[{<[{([<[<{(<><>)}({{}}([][]))>]{{<[<>{}]>(<<>()>[[]<>])}}>({[<<{}<>><()<>>>(<<>()>{<><>})]}[(<<(
    """.trimIndent()
            .split("\n")

}