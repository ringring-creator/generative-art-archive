sr 		= 		48000
kr 		= 		48000 ; kontrol rate, k-cycle. idetical to sample rate in this orchestra
nchnls 	= 		2
0dbfs   =       1

; you should not use frequency more than sr / 2.

;const values
giNoteNumber[]  fillarray 60, 62, 64, 65, 67, 69, 71, 72

; value interpolation
giInterpoTable ftgen 0, 0, -8, -2, 0.1, 0.5, -0.3, 0.6, -0.2, 1.0, -0.5, 0.2

instr noInterpoOscil
index   =   0
iStart  =   0
while   index < lenarray(giNoteNumber) do
    schedule "playMidiNoteOscil", iStart, 2, giNoteNumber[index], giInterpoTable
    iStart +=   2
    index  +=   1
od
endin

instr playMidiNoteOscil
    ares    oscil 1.0, p4, p5
    outs    ares, ares
endin

instr interpoOscil
index   =   0
iStart  =   0
while   index < lenarray(giNoteNumber) do
    schedule "playMidiNoteOscili", iStart, 2, giNoteNumber[index], giInterpoTable
    iStart +=   2
    index  +=   1
od
endin

instr playMidiNoteOscili
iFreq   =   mtof:i(p4)
ares    oscili 1.0, iFreq, p5
outs    ares, ares
endin

; timbre does not depend on phase
giSineZero ftgen   0, 0, 2^10, 10, 1, 0, 1/2, 1, 1/2, 1, -1/2, -1, -1/2, 0
giSine90  ftgen   0, 0, 2^10, 10, 1, 1/2, 1, 1/2, 1, -1/2, -1, -1/2, 0, 1/2

instr playOsciliSine
schedule "playMidiNoteOscili", 0, 5, 60, giSineZero
schedule "playMidiNoteOscili", 6, 5, 60, giSine90
endin

instr simpleOcsil
ares    oscil p4, p5
outs    ares, ares
endin



giSine       ftgen       0, 0, 8192, 10, 1
giTriangle   ftgen       0, 0, 512, 7, 0, 10, 1, 502, 0

; additive synthesis
instr simpleAddiSynth
printf_i    "giTriangle = %d, \n", 1, giTriangle
kenv1   oscil1   0, 1, p3, giTriangle
kenv2   oscil1   0, 1, p3/2.0, giTriangle
kenv3   oscil1   0, 1, p3/4.0, giTriangle
kenv4   oscil1   0, 1, p3/8.0, giTriangle
kenv5   oscil1  0, 1, p3/16.0, giTriangle
kenv6   oscil1   0, 1, p3/32.0, giTriangle

args1    oscili  10000/32767*kenv1, p4, giSine
args2    oscili  6000/32767*kenv2, p4, giSine
args3    oscili  4500/32767*kenv3, p4, giSine
args4    oscili  2000/32767*kenv4, p4, giSine
args5    oscili  500/32767*kenv5, p4, giSine
args6    oscili  250/32767*kenv6, p4, giSine
ares    sum     args1, args2, args3, args4, args5, args6
outs    ares, ares
endin

instr playMidiSimpleAddiSynth
index   =   0
iStart  =   0
while   index < lenarray(giNoteNumber) do
    iFreq   =   mtof:i(giNoteNumber[index])
    schedule "simpleAddiSynth", iStart, 2, iFreq
    iStart +=   2
    index  +=   1
od
endin

instr synthByRandom
kenv    oscil1  0, 1, p3, giTriangle ; amplitude envelop
krand   randi   0.1, 5  ; random ratio
;randh frequency random
; rand white noise

ares    oscili  10000/32767*(1+krand), p4, giSine
outs    ares, ares
endin

instr playMidiSynthByRandom
index   =   0
iStart  =   0
while   index < lenarray(giNoteNumber) do
    iFreq   =   mtof:i(giNoteNumber[index])
    schedule "synthByRandom", iStart, 2, iFreq
    iStart +=   2
    index  +=   1
od
endin

instr infiniteGlissando
iGlissandoTable  ftgen   0, 0, 8192, -5, 0.000976562, 8192, 1
iAmpTable  ftgen   0, 0, 8192, -19, 1, 0.5, 270, 0.5

iamp    =   p4
ifreq   =   p5
irate   =   0.1

kpit    oscili  ifreq, irate, iGlissandoTable, 0
kenv    oscil   iamp, irate, iAmpTable, 0
ares    oscili  kenv, kpit, giSine

index   =   1
while   index   < 13 do
    kpit    oscili  ifreq, irate, iGlissandoTable, 1/(index*10)
    kenv    oscil   iamp, irate, iAmpTable, 1/(index*10)
    asig    oscili  kenv, kpit, giSine
    ares    +=  asig
    index   +=   1
od

ares    =   ares / 5
outs    ares, ares
endin

; Modulation Synthesis

; Ring Modulation
; cosA * cosB = {cos(A+B)+cos(A-B)} / 2
; wave is bipolar
; Ring Modulation realize overtone composition with noise
instr rmSynth
ifreqA = p4
ifreqB = p5
kamp    oscili  1, ifreqA, giSine
ares    oscili  kamp, ifreqB, giSine
outs    ares, ares
endin

instr playRmSynth
index   =   0
iStart  =   0
iDur    =   2
while   index < lenarray(giNoteNumber) do
    iFreq   =   mtof:i(giNoteNumber[index])
    schedule "rmSynth", iStart, iDur, iFreq, iFreq
    iStart +=   iDur
    index  +=   1
od
endin

; Amplitude Modulation
; realize tremolo
instr amSynth
idur    =   p3
iacar   =   p4  ; career amplitude
ifcar   =   p5  ; career frequency
iamod   =   p6  ; modulation amplitude
ifmod   =   p7  ; modulation frequency

amod    oscili  iamod, ifmod, giSine    ; bipolar modulation signal
amod    =   amod + iamod                ; unipolar modulation signal
acar    oscili  iacar, ifcar, giSine    ; career signal
ares    =   acar * amod
outs    ares, ares
endin

instr playAmSynth
index   =   0
iStart  =   0
iDur    =   2
while   index < lenarray(giNoteNumber) do
    iFreq   =   mtof:i(giNoteNumber[index])
    schedule "amSynth", iStart, iDur, 0.6, iFreq, 0.01, 100
    iStart +=   iDur
    index  +=   1
od
endin

; Frequency Modulation
instr fmSynth

endin