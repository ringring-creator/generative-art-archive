sr 		= 		48000
kr 		= 		48000 ; kontrol rate, k-cycle. idetical to sample rate in this orchestra
nchnls 	= 		2
0dbfs   =       1

; Voice sound is 3 types made by vocal tract
; vocal cord tremors
; friction in the oral cavity
; Plosive by opening and closing the oral cavity

; Exciter is to decide voice tone
; Resonator is to sing vowels

; The formant is a mountain of amplitude of the frequency spectrum
; consisting of the harmonic series, the non-harmonic series, and the noise frequency.

instr formantSopranoA
giSine          ftgen       0, 0, 8192, 10, 1
giSigmoid       ftgen       0, 0, 1024, 19, 0.5, 0.5, 270, 0.5

; Table D.16. soprano “a”
; from URL: http://www.csounds.com/manual/html/MiscFormants.html
ifre1   =   800
ifre2   =   1150
ifre3   =   2900
ifre4   =   3900
ifre5   =   4950
; amplitude of formant
iamp1   =  p5
iamp2   =  iamp1 - 6
iamp3   =  iamp1 - 32
iamp4   =  iamp1 - 20
iamp5   =  iamp1 - 50
; bandwidth of formant
iwid1   =  80
iwid2   =  90
iwid3   =  120
iwid4   =  130
iwid5   =  140

amod    oscil   20, 5, giSine

ar1 fof ampdb(iamp1), p4 + amod, ifre1, 0, iwid1, 0.003, 0.02, 0.007, 15, giSine, giSigmoid, p3
ar2 fof ampdb(iamp2), p4 + amod, ifre2, 0, iwid2, 0.003, 0.02, 0.007, 15, giSine, giSigmoid, p3
ar3 fof ampdb(iamp3), p4 + amod, ifre3, 0, iwid3, 0.003, 0.02, 0.007, 15, giSine, giSigmoid, p3
ar4 fof ampdb(iamp4), p4 + amod, ifre4, 0, iwid4, 0.003, 0.02, 0.007, 15, giSine, giSigmoid, p3
ar5 fof ampdb(iamp5), p4 + amod, ifre5, 0, iwid5, 0.003, 0.02, 0.007, 15, giSine, giSigmoid, p3

ares    sum ar1, ar2, ar3, ar4, ar5
outs ares, ares
endin

giNoteNumber[]         fillarray 60, 62, 64, 65, 67, 69, 71, 72

instr playFormantSopranoA
index   =   0
iStart  =   0
while   index < lenarray(giNoteNumber) do
    iFreq   =   mtof:i(giNoteNumber[index])
    schedule "formantSopranoA", iStart, 2, iFreq, 50
    iStart +=   2
    index  +=   1
od
endin

instr formantSopranoI
giSine          ftgen       0, 0, 8192, 10, 1
giSigmoid       ftgen       0, 0, 1024, 19, 0.5, 0.5, 270, 0.5

; Table D.18. soprano “i”
; from URL: http://www.csounds.com/manual/html/MiscFormants.html
ifre1   =   270
ifre2   =   2104
ifre3   =   2950
ifre4   =   3900
ifre5   =   4950
; amplitude of formant
iamp1   =  p5
iamp2   =  iamp1 - 12
iamp3   =  iamp1 - 26
iamp4   =  iamp1 - 26
iamp5   =  iamp1 - 44
; bandwidth of formant
iwid1   =  60
iwid2   =  90
iwid3   =  100
iwid4   =  120
iwid5   =  120

amod    oscil   20, 5, giSine

ar1 fof ampdb(iamp1), p4 + amod, ifre1, 0, iwid1, 0.003, 0.02, 0.007, 15, giSine, giSigmoid, p3
ar2 fof ampdb(iamp2), p4 + amod, ifre2, 0, iwid2, 0.003, 0.02, 0.007, 15, giSine, giSigmoid, p3
ar3 fof ampdb(iamp3), p4 + amod, ifre3, 0, iwid3, 0.003, 0.02, 0.007, 15, giSine, giSigmoid, p3
ar4 fof ampdb(iamp4), p4 + amod, ifre4, 0, iwid4, 0.003, 0.02, 0.007, 15, giSine, giSigmoid, p3
ar5 fof ampdb(iamp5), p4 + amod, ifre5, 0, iwid5, 0.003, 0.02, 0.007, 15, giSine, giSigmoid, p3

ares    sum ar1, ar2, ar3, ar4, ar5
outs ares, ares
endin


instr playFormantSopranoI
index   =   0
iStart  =   0
while   index < lenarray(giNoteNumber) do
    iFreq   =   mtof:i(giNoteNumber[index])
    schedule "formantSopranoI", iStart, 2, iFreq, 50
    iStart +=   2
    index  +=   1
od
endin

instr formantSopranoU
giSine          ftgen       0, 0, 8192, 10, 1
giSigmoid       ftgen       0, 0, 1024, 19, 0.5, 0.5, 270, 0.5

; Table D.20. soprano “u”
; from URL: http://www.csounds.com/manual/html/MiscFormants.html
ifre1   =   325
ifre2   =   700
ifre3   =   2700
ifre4   =   3800
ifre5   =   4950
; amplitude of formant
iamp1   =  p5
iamp2   =  iamp1 - 16
iamp3   =  iamp1 - 35
iamp4   =  iamp1 - 40
iamp5   =  iamp1 - 60
; bandwidth of formant
iwid1   =  50
iwid2   =  60
iwid3   =  170
iwid4   =  180
iwid5   =  200

amod    oscil   20, 5, giSine

ar1 fof ampdb(iamp1), p4 + amod, ifre1, 0, iwid1, 0.003, 0.02, 0.007, 15, giSine, giSigmoid, p3
ar2 fof ampdb(iamp2), p4 + amod, ifre2, 0, iwid2, 0.003, 0.02, 0.007, 15, giSine, giSigmoid, p3
ar3 fof ampdb(iamp3), p4 + amod, ifre3, 0, iwid3, 0.003, 0.02, 0.007, 15, giSine, giSigmoid, p3
ar4 fof ampdb(iamp4), p4 + amod, ifre4, 0, iwid4, 0.003, 0.02, 0.007, 15, giSine, giSigmoid, p3
ar5 fof ampdb(iamp5), p4 + amod, ifre5, 0, iwid5, 0.003, 0.02, 0.007, 15, giSine, giSigmoid, p3

ares    sum ar1, ar2, ar3, ar4, ar5
outs ares, ares
endin


instr playFormantSopranoU
index   =   0
iStart  =   0
while   index < lenarray(giNoteNumber) do
    iFreq   =   mtof:i(giNoteNumber[index])
    schedule "formantSopranoU", iStart, 2, iFreq, 50
    iStart +=   2
    index  +=   1
od
endin

instr formantSopranoE
giSine          ftgen       0, 0, 8192, 10, 1
giSigmoid       ftgen       0, 0, 1024, 19, 0.5, 0.5, 270, 0.5

; Table D.17. soprano “e”
; from URL: http://www.csounds.com/manual/html/MiscFormants.html
ifre1   =   350
ifre2   =   2000
ifre3   =   2800
ifre4   =   3600
ifre5   =   4950
; amplitude of formant
iamp1   =  p5
iamp2   =  iamp1 - 20
iamp3   =  iamp1 - 15
iamp4   =  iamp1 - 40
iamp5   =  iamp1 - 56
; bandwidth of formant
iwid1   =  80
iwid2   =  100
iwid3   =  120
iwid4   =  150
iwid5   =  200

amod    oscil   20, 5, giSine

ar1 fof ampdb(iamp1), p4 + amod, ifre1, 0, iwid1, 0.003, 0.02, 0.007, 15, giSine, giSigmoid, p3
ar2 fof ampdb(iamp2), p4 + amod, ifre2, 0, iwid2, 0.003, 0.02, 0.007, 15, giSine, giSigmoid, p3
ar3 fof ampdb(iamp3), p4 + amod, ifre3, 0, iwid3, 0.003, 0.02, 0.007, 15, giSine, giSigmoid, p3
ar4 fof ampdb(iamp4), p4 + amod, ifre4, 0, iwid4, 0.003, 0.02, 0.007, 15, giSine, giSigmoid, p3
ar5 fof ampdb(iamp5), p4 + amod, ifre5, 0, iwid5, 0.003, 0.02, 0.007, 15, giSine, giSigmoid, p3

ares    sum ar1, ar2, ar3, ar4, ar5
outs ares, ares
endin


instr playFormantSopranoE
index   =   0
iStart  =   0
while   index < lenarray(giNoteNumber) do
    iFreq   =   mtof:i(giNoteNumber[index])
    schedule "formantSopranoE", iStart, 2, iFreq, 50
    iStart +=   2
    index  +=   1
od
endin

instr formantSopranoO
giSine          ftgen       0, 0, 8192, 10, 1
giSigmoid       ftgen       0, 0, 1024, 19, 0.5, 0.5, 270, 0.5

; Table D.19. soprano “o”
; from URL: http://www.csounds.com/manual/html/MiscFormants.html
ifre1   =   450
ifre2   =   800
ifre3   =   2830
ifre4   =   3800
ifre5   =   4950
; amplitude of formant
iamp1   =  p5
iamp2   =  iamp1 - 11
iamp3   =  iamp1 - 22
iamp4   =  iamp1 - 22
iamp5   =  iamp1 - 50
; bandwidth of formant
iwid1   =  40
iwid2   =  80
iwid3   =  100
iwid4   =  120
iwid5   =  120

amod    oscil   20, 5, giSine
;   fof amp,          basic frequency, center frequency, octave coefficient, band width, env attack, env dur, env delay, amount of overlap, sineTable, envTable, fof dur
ar1 fof ampdb(iamp1), p4 + amod,       ifre1,            0,                  iwid1,      0.003,      0.02,    0.007,     15,                giSine,    giSigmoid, p3
ar2 fof ampdb(iamp2), p4 + amod,       ifre2,            0,                  iwid2,      0.003,      0.02,    0.007,     15,                giSine,    giSigmoid, p3
ar3 fof ampdb(iamp3), p4 + amod,       ifre3,            0,                  iwid3,      0.003,      0.02,    0.007,     15,                giSine,    giSigmoid, p3
ar4 fof ampdb(iamp4), p4 + amod,       ifre4,            0,                  iwid4,      0.003,      0.02,    0.007,     15,                giSine,    giSigmoid, p3
ar5 fof ampdb(iamp5), p4 + amod,       ifre5,            0,                  iwid5,      0.003,      0.02,    0.007,     15,                giSine,    giSigmoid, p3

ares    sum ar1, ar2, ar3, ar4, ar5
outs ares, ares
endin

instr playFormantSopranoO
index   =   0
iStart  =   0
while   index < lenarray(giNoteNumber) do
    iFreq   =   mtof:i(giNoteNumber[index])
    schedule "formantSopranoO", iStart, 2, iFreq, 50
    iStart +=   2
    index  +=   1
od
endin
