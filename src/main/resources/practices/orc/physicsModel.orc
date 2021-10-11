sr 		= 		48000
kr 		= 		48000 ; kontrol rate, k-cycle. idetical to sample rate in this orchestra
nchnls 	= 		2
0dbfs   =       1

; constant
giNoteNumber[]         fillarray 60, 62, 64, 65, 67, 69, 71, 72

; tables
giSine   ftgen       0, 0, 8192, 10, 1

; MSW Synthesis
; Non-Linear Exciation > Linear Resonator > Feedback

instr wgpluck2Instr
;                   pick position, amp, pitch, mic position, reflection
ares    wgpluck2    p4,            0.5, p5,    0.75,         p6
        outs        ares, ares
endin

instr playWgpluck2Instr
index   =   0
iStart  =   0
iDur    =   2
while   index < lenarray(giNoteNumber) do
    iFreq   =   mtof:i(giNoteNumber[index])
    schedule "wgpluck2Instr", iStart, iDur, p4,iFreq, p5
    iStart +=   iDur
    index  +=   1
od
endin

instr repluckInstr
axcite oscil 1, 1, giSine
;                   pick position, amp, pitch, mic position, reflection, excites signal
ares    repluck    p4,            0.5, p5,    0.75,         p6,          axcite
        outs        ares, ares
endin

instr playRepluckInstr
index   =   0
iStart  =   0
iDur    =   2
while   index < lenarray(giNoteNumber) do
    iFreq   =   mtof:i(giNoteNumber[index])
    schedule "repluckInstr", iStart, iDur, p4,iFreq, p5
    iStart +=   iDur
    index  +=   1
od
endin

instr wgpluck2StresonInstr
;                       pick position, amp, pitch, mic position, reflection
asig        wgpluck2    p4,            0.5, p5,    0.75,         0
;                       in  , basic frequency, Iterative gain
ares        streson     asig, p5,              0.5              ; Resonant vibration
            outs        ares, ares
endin

instr playWgpluck2StresonInstr
index   =   0
iStart  =   0
iDur    =   2
while   index < lenarray(giNoteNumber) do
    iFreq   =   mtof:i(giNoteNumber[index])
    schedule "wgpluck2StresonInstr", iStart, iDur, p4,iFreq, p5
    iStart +=   iDur
    index  +=   1
od
endin

; waveguide synthesis
; to getting sound of stringed instrument
; waveguide synthesis consists of two delay mechanisms
; delay mechanisms make phase determination and frequency attenuation

instr waveguideInstr
asig    noise       0.5, 0.5
;                   in  , delay of iterate, cut frequency, iteration factor
ares    wguide1     asig, 200,              2000,          0.8
; iteration factor G is multiplied at each iteration
; decay 60 dB in X seconds, basic frequency fo
; G = 1000^(-1/(X x fo))
outs    ares, ares
endin

instr waveguide2Instr
asig        noise       0.5, 0.5
afreq1       init 200
afreq2       init 800
kcutoff1    init 1000
kcutoff2    init 2000
kfeedback1    init 0.2
kfeedback2    init 0.1

ares    wguide2     asig, afreq1, afreq2, kcutoff1, kcutoff2, kfeedback1, kfeedback2
        outs    ares, ares
endin

instr wgbowInstr
kv1         linseg      2, p3, 4
kv2         linseg      0, p3, 7
irat        init        0.09
;                       amplitude, frequency note, bow pressure, string position, vibrato frequency, vibrato amplitude,
ares        wgbow       0.7,       p4,             kv1,          irat,            kv2,                0.01,               giSine
            outs    ares, ares
endin

instr playWgbowInstr
index   =   0
iStart  =   0
iDur    =   2
while   index < lenarray(giNoteNumber) do
    iFreq   =   mtof:i(giNoteNumber[index])
    schedule "wgbowInstr", iStart, iDur, iFreq
    ;schedule "wgbowInstr", iStart, iDur, giNoteNumber[index]
    iStart +=   iDur
    index  +=   1
od
endin

instr wgbrassInstr
iatt        init        0.95
idetk       init        0.1
kvibf       init        6
kvibamp     init        0.05
;                       amplitude, frequency note, tension of the player, time taken, vibrato frequency, vibrato amplitude,
ares        wgbrass     0.7,       p4,             iatt,                  idetk,      kvibf,             kvibamp,               giSine
            outs    ares, ares
endin

instr playWgbrassInstr
index   =   0
iStart  =   0
iDur    =   2
while   index < lenarray(giNoteNumber) do
    iFreq   =   mtof:i(giNoteNumber[index])
    schedule "wgbrassInstr", iStart, iDur, iFreq
    iStart +=   iDur
    index  +=   1
od
endin
