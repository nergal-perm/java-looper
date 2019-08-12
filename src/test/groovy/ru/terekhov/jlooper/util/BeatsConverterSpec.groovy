package ru.terekhov.jlooper.util

import spock.lang.Specification

class BeatsConverterSpec extends Specification {
    def "should convert the first beat to zero ms"() {
        given:
            def tempo = 60
            def timeSignature = [4,4]
            def beat = "1:0.0"
            def offset = 0

        when:
            def result = BeatsConverter.toMilliseconds(tempo, timeSignature, beat, offset)

        then:
            result == 0
    }
}
