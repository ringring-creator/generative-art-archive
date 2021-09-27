sr 		= 		48000
kr 		= 		48000 ; kontrol rate, k-cycle. idetical to sample rate in this orchestra
nchnls 	= 		2
0dbfs   =       1

; you should not use frequency more than sr / 2.

;const values
giNoteNumber[]         fillarray 60, 62, 64, 65, 67, 69, 71, 72
giNoteNumber2Octave[]  fillarray 84, 86, 88, 89, 91, 93, 95, 96


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
; Freqeuncy elements of ring modulation are A-B and A+B
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
; y = [1 + AMcos(wMt)] sin (wCt)
; Freqeuncy elements of amplitude modulation are C-M, C, C+M
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
    schedule "amSynth", iStart, iDur, p4, iFreq, p5, p6
    iStart +=   iDur
    index  +=   1
od
endin

; Frequency Modulation
; career signal is final output.
; frequency of career signal changed according to modulation signal
; fmMod = A sin[wCt + I sin(wMt)]
; s.t. A: career amplitude, wC: career angular frequency(=2 * pi * f * C)
;      wM: modulation angular frequency, I is modutation ratio (modulation index)
; Freqeuncy elements of frequency modulation are as following
; C - (I+1)M, C - IM, ... ,C, C + M, ..., C + (I+1)M
; Amplitude of frequency element is represented by Bessel function

instr fmSynth
idur    =   p3
iamp    =   p4
ifcar   =   p5  ; career frequency
icm     =   p6  ; Career frequency:Modulation frequency ratio
ifmod   =   ifcar/icm   ; modulation frequency
index   =   6   ;modulation index I

kindex  line    index, idur, 0 ; six to zero value
kdev    =       index * ifmod   ; max distortion value
kenv    linen   iamp, 0.001, idur, idur * 0.9   ; amplitude envelope
amod    oscili  kdev, ifmod, giSine     ; modulator signal
ares    oscili  kenv, ifcar + amod, giSine
outs    ares, ares
endin

instr playFmSynth
index   =   0
iStart  =   0
iDur    =   2
while   index < lenarray(giNoteNumber) do
    iFreq   =   mtof:i(giNoteNumber[index])
    schedule "fmSynth", iStart, iDur, p4, iFreq, p5
    iStart +=   iDur
    index  +=   1
od
endin

instr bellByFmSynth
giEnvAmpExp  ftgen   0, 0, 512, 5, 1, 512, 0.0001
giEnvIdx     ftgen   0, 0, 512, 5, 1, 512, 0.2

idur    =   p3
iamp    =   p4
ifreq   =   p5
kenv    oscil1  0, iamp, idur, giEnvAmpExp ; frequency modulation special opcode
kindex  oscil1  0, p8-p9, idur, giEnvIdx
kindex   =   kindex + p9
ares    foscili kenv, ifreq, p6, p7, kindex, giSine
outs    ares, ares
endin

instr playBellByFmSynth
index   =   0
iStart  =   0
iDur    =   2
while   index < lenarray(giNoteNumber) do
    iFreq   =   mtof:i(giNoteNumber[index])
    schedule "bellByFmSynth", iStart, iDur, p4, iFreq, p5, p6, p7, p8
    iStart +=   iDur
    index  +=   1
od
endin

; (a + sin(b+sinc))+sin(d+sin(e+sinf))
; To prevent typical sound patterns due to overtones
instr complexFmSynth
idur    =   p3
iamp    =   15000/32767
ifcar   =   p4 ;career frequency
iratio  =   p5
ifmod   =   ifcar/iratio
index   =   p6
kindex  line    index, idur, 0; modulation ratio
kdev    =   kindex * ifmod; distortion value
kenv    linen   iamp, 0.001, idur, idur * 0.9   ; career envelope
amod    oscili  kdev, ifmod, giSine;    modulator
ares    oscili  kenv, ifcar+amod, giSine; career
outs    ares, ares
endin

instr playComplexFmSynth
index   =   0
iStart  =   0
iDur    =   2
while   index < lenarray(giNoteNumber) do
    iFreq   =   mtof:i(giNoteNumber[index])
    schedule "complexFmSynth", iStart, iDur, iFreq, p4, p5
    iStart +=   iDur
    index  +=   1
od
endin

;degital filter and subtractive synthesis
;subtractive synthesis changes spectrum of sound material by filtering

; material need to include target frequency
; white noise and impulse train include all range of frequencies
; buzz opcode generates impulse train with same amplitude of overtunes

instr impulseTrain
ifo     =       p5  ; basic frequency
iinh     =       int(sr/2/ifo)   ; the max number of overtunes
kenv    linen   p4, p6, p3, p7  ; amplitude envelope
                                ; kamp, irise, idur, idec
atrain  buzz    kenv, ifo, iinh, giSine; impulse train
outs    atrain, atrain
endin

instr playImpulseTrain
index   =   0
iStart  =   0
iDur    =   2
while   index < lenarray(giNoteNumber) do
    iFreq   =   mtof:i(giNoteNumber2Octave[index])
    schedule "impulseTrain", iStart, iDur, p4, iFreq, p5, p6
    iStart +=   iDur
    index  +=   1
od
endin

; FIR(Finite impulse Response) is to mix duplicated and delay input signal with input signal
; Delay amount is a sample or multiple samples.
; tap is amount which indicates how many post-samples to use in a digital filter.


instr simpleFirLowPassFilter
ifo     =       p5  ; basic frequency
iinh     =       int(sr/2/ifo)   ; the max number of overtunes
kenv    linen   p4, p6, p3, p7  ; amplitude envelope
atrain  buzz    kenv, ifo, iinh, giSine; impulse train
ad      delay1  atrain
ares    =   (0.5 * atrain) + (0.5 * ad)
outs    ares, ares
endin

instr playSimpleFirLowPassFilter
index   =   0
iStart  =   0
iDur    =   2
while   index < lenarray(giNoteNumber) do
    iFreq   =   mtof:i(giNoteNumber2Octave[index])
    schedule "simpleFirLowPassFilter", iStart, iDur, p4, iFreq, p5, p6
    iStart +=   iDur
    index  +=   1
od
endin

instr simpleTwoTapFirLowPassFilter
ifo     =       p5  ; basic frequency
iinh     =       int(sr/2/ifo)   ; the max number of overtunes
kenv    linen   p4, p6, p3, p7  ; amplitude envelope
atrain  buzz    kenv, ifo, iinh, giSine; impulse train
ad1      delay1  atrain
ad2      delay1  ad1
ares    =   (0.5 * atrain) + (0.5 * ad2)
outs    ares, ares
endin

instr playSimpleTwoTapFirLowPassFilter
index   =   0
iStart  =   0
iDur    =   2
while   index < lenarray(giNoteNumber) do
    iFreq   =   mtof:i(giNoteNumber2Octave[index])
    schedule "simpleTwoTapFirLowPassFilter", iStart, iDur, p4, iFreq, p5, p6
    iStart +=   iDur
    index  +=   1
od
endin

; IIR(Infinite impulse Response) is to mix delay output signal with input signal
; low pass filter
; out = (in + out)/2
; high pass filter
; out = (in - out)/2

gaIirLowPassRes init 0

instr simpleIirLowPassFilter
ifo     =       p5  ; basic frequency
iinh     =       int(sr/2/ifo)   ; the max number of overtunes
kenv    linen   p4, p6, p3, p7  ; amplitude envelope
atrain  buzz    kenv, ifo, iinh, giSine; impulse train
ares    =   (0.5 * atrain) + (0.5 * gaIirLowPassRes)
gaIirLowPassRes = ares
outs    ares, ares
endin

instr playSimpleIirLowPassFilter
index   =   0
iStart  =   0
iDur    =   2
while   index < lenarray(giNoteNumber) do
    iFreq   =   mtof:i(giNoteNumber2Octave[index])
    schedule "simpleIirLowPassFilter", iStart, iDur, p4, iFreq, p5, p6
    iStart +=   iDur
    index  +=   1
od
endin

gaIirHighPassRes init 0

instr simpleIirHighPassFilter
ifo     =       p5  ; basic frequency
iinh     =       int(sr/2/ifo)   ; the max number of overtunes
kenv    linen   p4, p6, p3, p7  ; amplitude envelope
atrain  buzz    kenv, ifo, iinh, giSine; impulse train
ares    =   (0.5 * atrain) - (0.5 * gaIirHighPassRes)
gaIirHighPassRes = ares
outs    ares, ares
endin

instr playSimpleIirHighPassFilter
index   =   0
iStart  =   0
iDur    =   2
while   index < lenarray(giNoteNumber) do
    iFreq   =   mtof:i(giNoteNumber2Octave[index])
    schedule "simpleIirHighPassFilter", iStart, iDur, p4, iFreq, p5, p6
    iStart +=   iDur
    index  +=   1
od
endin

instr combFilter
ifo     =       p5  ; basic frequency
iinh     =       int(sr/2/ifo)   ; the max number of overtunes
kenv    linen   p4, p6, p3, p7  ; amplitude envelope
atrain  buzz    kenv, ifo, iinh, giSine; impulse train
ad  delay   atrain, 100/sr  ; delay 100 samples
ares    =   (0.5 * atrain) + (0.5 * ad)
outs ares, ares
endin

instr playCombFilter
index   =   0
iStart  =   0
iDur    =   2
while   index < lenarray(giNoteNumber) do
    iFreq   =   mtof:i(giNoteNumber2Octave[index])
    schedule "combFilter", iStart, iDur, p4, iFreq, p5, p6
    iStart +=   iDur
    index  +=   1
od
endin

instr whiteNoise
anoise  rand    0.3                  ; white noise
outs    anoise, anoise
endin

; butter** is Butterworth filter.

instr butterhpInstr
anoise  rand    0.3                  ; white noise
ares    butterhp    anoise, 1500 ; frequency more than 1500Hz
outs ares, ares
endin

instr butterlpInstr
anoise  rand    0.3                  ; white noise
ares    butterlp    anoise, 1500 ; frequency cutted less than 1500Hz
outs ares, ares
endin

instr butterbpInstr
anoise  rand    0.3                  ; white noise
ares    butterbp    anoise, 2000, 1000 ; frequency between 1000Hz and 2000Hz
outs ares, ares
endin

instr butterbrInstr
anoise  rand    0.3                  ; white noise
ares    butterbr    anoise, 2000, 1000 ; frequency cutted between 1000Hz and 2000Hz
outs ares, ares
endin

instr resonInstr
anoise  rand    0.3                  ; white noise
ares    reson   anoise, 1500, 500; center frequency 1500Hz, bandwidth 500Hz
outs ares, ares
endin

instr aresonInstr
anoise  rand    0.3                  ; white noise
ares    areson   anoise, 1500, 500; cutted based on center frequency 1500Hz, bandwidth 500Hz
outs ares, ares
endin

instr toneInstr ; 1st low pass filter
anoise  rand    0.3                  ; white noise
ares    tone    anoise, 2000
outs ares, ares
endin

instr atoneInstr ; 1st high pass filter
anoise  rand    0.3                  ; white noise
ares    tone    anoise, 2000
outs ares, ares
endin

instr noBalanceInstr
anoise  rand    0.3                  ; white noise
acut    areson   anoise, 1500, 500
acut    areson   acut, 1500, 500
acut    areson   acut, 1500, 500
ares    areson   acut, 1500, 500
outs ares, ares
endin

instr balanceInstr
anoise  rand    0.3                  ; white noise
acut    areson   anoise, 1500, 500
acut    areson   acut, 1500, 500
acut    areson   acut, 1500, 500
acut    areson   acut, 1500, 500
ares    balance acut, anoise
outs ares, ares
endin


; q value is the slope of the frequency to be filtered in a octave.
; q = centerFreq / BW s.t. BW is band width
