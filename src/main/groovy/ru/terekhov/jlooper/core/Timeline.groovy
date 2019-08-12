package ru.terekhov.jlooper.core

import groovy.transform.Immutable

class Timeline {
    List<Part> parts = []

    String getStartTime() {
        "1:0.0"
    }

    int getDuration() {
        0
    }

    def addPart(Part part) {
        this.parts.add(part)
    }
}

@Immutable
class Part {
    String startBeat
    String durationBeats
    int tempo
    int[] timeMeasure
}