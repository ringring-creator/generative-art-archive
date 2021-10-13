sr 		= 		48000
kr 		= 		48000 ; kontrol rate, k-cycle. idetical to sample rate in this orchestra
nchnls 	= 		2

; constant
giNoteNumber[]         fillarray 60, 62, 64, 65, 67, 69, 71, 72

; tables
giSine    ftgen       0, 0, 8192, 10, 1
giSine2   ftgen       0, 0, 8192, 10, 1, 1, 1, 1, 1, 1, 1, 1

; delay less than 10ms result in low pass filter
; delay between 10ms and 50ms result in com filter
; delay more than 50ms result in echo (The reflected sound is double reflected)
instr variableDelay
gaportL, gaportR     soundin  "../in/wav/nightingale.wav" ; career signals
idelay               =        p4
adel                 delay    gaportL, idelay
                     outs     (adel + gaportL) * 0.8, (adel + gaportL) * 0.8
endin

instr flanger
imax        =       100     ; max frame num of delay
a1          oscil   10000, 700, giSine2
a2          oscil   imax, 0.25, giSine ; low frequency of lfo
a2          =       a2 + imax / 2            ; change unipolar
;                   original signal, delay time (ms), max delay time
a3          vdelay  a1,              a2,              imax
            outs    a3 + a1, a3 + a1
endin

instr vdelayTransposition
ifeed       =       0.99875  ; factor for feedback to Resonator(short delay)
ifeed2      =       0.457    ; factor for feedback to Resonator(long delay)
icenter     =       5        ; average of short delay

ares[]          init        6
adres[]         init        6
kran[]          fillarray   0, 0, 0, 0, 0, 0
iranamp[]       fillarray   1.65,  2.65,   3.65,   2.265,  1.965,  4.65
irancps[]       fillarray   876.2, 3876.2, 8476.2, 5876.2, 6876.2, 8876.2

ishortfactor[]  fillarray   0.5, 0.8, 0.4, 0.9, 0.3, 0.7
ilongfactor[]   fillarray    100.5, 220.8, 320.4, 400.9, 80.4, 145.7

adL, adR          diskin      "../in/wav/nightingale.wav", p4, 0, 1
ad          dcblock     adL  ; block db elements

index   =   0
while   index < lenarray(kran) do
    kran[index]   randi       0.01, iranamp[index], irancps[index]
    ares[index]   vdelay      ad + ares[5 - index] * ifeed, icenter + ishortfactor[index] * (1 + kran[index]), 1000
    adres[index]  vdelay      ares[index] + adres[index] * ifeed2, ilongfactor[index], 1000
    index         +=          1
od

adressum             sum   adres[0], adres[1], adres[2], adres[3], adres[4], adres[5]
aout                 =     ad * 0.2 + adressum * 0.01
                      outs aout, aout
endin

instr DopplerEffect
asrc        buzz        1, 200, 20, giSine      ; input signal
atime       linseg      1, p3/2, 0.01, p3/2, 1  ; distance
ampfac      =           200 / atime             ; amplitude factor
adump       delayr      1                       ; value written by delayw before 1s
amove       deltapi     atime                   ; move input source
            delayw      asrc
            outs        amove * ampfac, amove * ampfac
endin

instr modulationInstr
imix        =           0.5
idepth      =           0.05
ifreq       =           3

kmoda       oscil       idepth, ifreq, giSine
kmodb       oscil       idepth, ifreq * 0.9, giSine
adL, adR    diskin      "../in/wav/nightingale.wav", p4, 0, 1
asig        =           adL

adump       delayr      2
adel1       deltapi     p4 * (1 + kmoda)
            delayw      asig + (adel1 * imix)

adump       delayr      2
adel2       deltapi     p4 * (1 + kmodb)
            delayw      asig + (adel2 * imix)

            outs        (asig + adel1) * 0.5, (asig + adel2) * 0.5
endin