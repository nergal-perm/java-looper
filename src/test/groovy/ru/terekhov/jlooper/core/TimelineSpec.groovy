package ru.terekhov.jlooper.core

import spock.lang.Specification

class TimelineSpec extends Specification {
    def "Пустой трек длится 0 миллисекунд"() {
        given:
            Timeline target = new Timeline()
        expect:
            target.duration == 0
    }

    def "У пустого трека нет составных частей" () {
        given:
            Timeline target = new Timeline()
        expect:
            target.parts.isEmpty()
    }

    def "Можно добавить одну часть в трек"() {
        given:
            Timeline target = new Timeline()
            Part part = new Part(
                startBeat: "1:0.0",
                durationBeats: "4:0.0",
                tempo: 60,
                timeMeasure: [4, 4]
            )
        when:
            target.addPart(part)
        then:
            target.startTime == "1:0.0"
            target.parts.size() == 1


    }
}
