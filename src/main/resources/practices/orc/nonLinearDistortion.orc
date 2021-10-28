sr 		= 		48000
kr 		= 		48000 ; kontrol rate, k-cycle. idetical to sample rate in this orchestra
nchnls 	= 		2
0dbfs   =       1

; tables
giSine                   ftgen       1, 0, 8192, 10, 1
giNoDis                  ftgen       2, 0, 8192, 7, -1, 8192, 1 ; not distortion
giNoDis                  ftgen       3, 0, 8192, 7, -1, 8192, 1 ; not distortion
giSlightDis              ftgen       4, 0, 8192, 9, 0.5, 1, 270 ; Slightly distort
giChebyshevDis           ftgen       5, 0, 8192, 13, 1, 1, 0, 16, 0, 8, 0, 4, 0, 2, 0, 1; Chebyshev　distortion
giStrongDis              ftgen       6, 0, 8192, 7, -1, 2048, -1, 0, 0.3, 2048, 0, 0, -0.5, 2048, 0, 0, 0.8, 2048, 0.8

giAg                     ftgen       0, 0, 8192, 13, 1, 1, 0, 1, 0.7, 0.8, 0.3, 0.1, 0.8, 0.9, 1, 1
giNormalization          ftgen       0, 0, 4096, 4, 2, 1

giNoteNumber[]         fillarray 60, 62, 64, 65, 67, 69, 71, 72

; Granulation is to create a high-density acoustic body by grain (short sound, 10ms-30ms)

instr simpleDistortion
kenv            linen       p4, 0.1, p3, 0.1
ioffset         =           ftlen(p6) / 2 - 1
ain             oscil       ioffset, p5, giSine
awsh            tablei      ain, p6, 0, ioffset
ares            =           kenv * awsh
                outs        ares, ares
endin

instr ridgeDistortion
kenv            line        0.5, p3, 0
asig            oscil       kenv, p4, giSine
ag              table       asig, giAg, 1, 0.5

kcor            table       kenv * 2, giNormalization, 1
ares            =           ag * 0.5 * kcor
                outs        ares, ares
endin

instr playSimpleDistortion
index   =   0
iStart  =   0
iDur    =   2
while   index < lenarray(giNoteNumber) do
    iFreq   =   mtof:i(giNoteNumber[index])
    schedule "simpleDistortion", iStart, iDur, 0.5, iFreq, p4
    iStart +=   iDur
    index  +=   1
od
endin

instr playRidgeDistortion
index   =   0
iStart  =   0
iDur    =   2
while   index < lenarray(giNoteNumber) do
    iFreq   =   mtof:i(giNoteNumber[index])
    schedule "ridgeDistortion", iStart, iDur, iFreq
    iStart +=   iDur
    index  +=   1
od
endin
