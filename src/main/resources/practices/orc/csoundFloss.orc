sr 		= 		48000
kr 		= 		24000; kontrol rate, k-cycle
nchnls 	= 		2
0dbfs   =       1


instr sineWave     ; sine wave
ares    poscil  p4, p5     ;p4 hz sine wave
outs    ares, ares
endin


instr linearAmp     ;db amp
kamp    line    0, p3, 1    ;amp 0 to 1
asig    poscil  1, p4     ;p4 hz sine wave
ares    =       asig * kamp
outs    ares, ares
endin

instr dbAmp   ;linear amp
kdb    line    -80, p3, 0    ;amp -80 to 0
asig    poscil  1, p4     ;p4 hz sine wave
kamp    =       ampdb(kdb)  ; db to amp
printk   1, kamp
ares    =       asig * kamp
outs    ares, ares
endin

giGlobalSample      =       1/2; global const variable

instr variableCheck ;
iLocal      =   1/4     ; local const variable
prints       "giGlobalSample: %f, iLocal: %f\n" ,giGlobalSample, iLocal
kCount      init    0   ; local variable
kCount      =   kCount + 1
printk      0, kCount
;printks    "kCount: %d\n", 0, kCount

endin

giNoteNumber[]  fillarray 60, 62, 64, 65, 67, 69, 71, 72

instr arrayCheck
index   =   0
iStart  =   0
while   index < lenarray(giNoteNumber) do
    schedule "playMidiNote", iStart, 2, giNoteNumber[index]
    iStart +=   2
    index  +=   1
od
endin

instr playMidiNote
iMidiNote   =   p4
iFreq   =   mtof:i(iMidiNote)
printf_i    "midi note = %d, frequency: %f\n", 1, iMidiNote, iFreq
ares    poscil  0.5, iFreq     ;p4 hz sine wave
outs    ares, ares
endin

gSampleFile = "../out/wav/Pluck.wav"
;varname                ifn itime   isize   igen    Sfilename       iskip   iformat ichn
giSample    ftgen       0,  0,      2^23,      1,      gSampleFile,    0,      0,      0

instr gen01Sample
    ares    poscil3     0.5, 1/p3,  giSample
    outs    ares, ares
endin

giSine  ftgen    0,  0,  2^10,   10, 1

instr sine
ares    poscil  0.5, 1/p3, giSine
outs    ares, ares
endin

giCurve     ftgen   0, 0, 1024, 8, 0, 97, 1, 170, 0.583, 757, 0

instr gen08Sample
;gen08 creates a smooth curve table.
ares    poscil3  0.5, 1/p3, giCurve
outs    ares, ares
endin

instr gen16Sample
;                                   val, dur, type ... val, dur, type
giEnvelope  ftgen   0, 0, 1024, 16, 0, 512, 4, 1, 512, 4, 0
aCurve    poscil3  0.5, 1/p3, giCurve
ares    =   aCurve * giEnvelope
outs    ares, ares
endin

instr gen19Sample
giComplexWave  ftgen 0, 0, 1024, 19, 1, 1, 0, 0, 20, 0.1, 0, 0
ares    poscil3  0.5, 1/p3, giComplexWave
outs    ares, ares
endin

instr gen30Sample
giPluckFFT  ftgen   0, 0 , 2^23, 30, giSample, 512, 1024
ares    poscil3     0.5, 1/p3,  giPluckFFT
outs    ares, ares
endin

instr arraySort
iArr[] fillarray 1, 3, 9, 5, 6, -1, 17
printarray iArr, "%d", "Original array:"
iAsc[] sorta iArr
printarray iAsc, "%d", "Sorted ascending:"
iDesc[] sortd iArr
printarray iDesc, "%d", "Sorted descending:"
iSlice[] slicearray iArr, 1, 3
printarray iSlice, "%d", "Sliced array:"
trim_i iArr, 3
printarray iArr, "%d", "Trimed array:"

iSine ftgen 0, 0, 2^10, 10, 1
iArr[] init 2^10
copyf2array iArr, iSine
printarray iArr, "%f", "sine array:"

iTable ftgen 0, 0, 10, 2, 0
iArr[] genarray 1, 10
copya2ftab iArr, iTable
index = 0
while index < ftlen(iTable) do
    prints "%d ", table:i(index, iTable)
    index += 1
od
prints "\n"
endin

; define opcode

opcode test, 0, 0
    prints "test\ntest\ntest\n"
endop

instr useCustomOp
    test
endin