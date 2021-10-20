sr 		= 		48000
kr 		= 		48000 ; kontrol rate, k-cycle. idetical to sample rate in this orchestra
nchnls 	= 		2

; tables
giSine         ftgen       0, 0, 8192, 10, 1, 1, 1, 1, 1, 1, 1, 1
giSine2         ftgen       0, 0, 8192, 10, 1, 0.5, 0.2, 0.1
giEnv          ftgen       0, 0, 8192, 20, 2

instr aToZ
gamodL, gamodR       soundin  "../in/wav/a-z.wav" ; modulation signals
outs gamodL, gamodR
endin

; Removes noise at the moment of silence
instr noiseGate
;ithre          =       0.1 ; noise gate threshold
ithre          =       500 ; noise gate threshold
asig, asigR            soundin  "../in/wav/a-z.wav"

krms            rms         asig            ; analyze level

kng             =           (krms > ithre?1:0)
kng             port        kng, 0.005 ; low pass filter for smoothing the transition
ares            =           asig * kng
                outs        ares, ares
endin

; compress means shrinking volume of signal under tolerable level
instr compressedAtoZ
amodL, amodR       soundin  "../in/wav/a-z.wav" ; modulation signals

kthreshold = 0.2 ; threshold
icomp1 = 0.2     ; ratio of level more than threshold
icomp2 = 0.8     ; ratio of level less than threshold
irtime = 0.01    ; time when gain rise
iftime = 0.5     ; time when gain decline

asig               dam      amodL, kthreshold, icomp1, icomp2, irtime, iftime
                   outs     asig, asig
endin

; expand is reverse meaning of compress
instr expandedAtoZ
amodL, amodR       soundin  "../in/wav/a-z.wav" ; modulation signals

kthreshold = .5
icomp1 = 2
icomp2 = 3
irtime = 0.01
iftime = 0.1

asig                dam     amodL, kthreshold, icomp1, icomp2, irtime, iftime
                    outs    asig*.2, asig*.2
endin

instr followEx
kenv                linseg  0, p3/2, 20000, p3/2, 0
a1                  oscil   kenv, 440, giSine2
ak1                 follow  a1, .02 ; envelope generator, 0.02 is average amplitude period (seconds)
a2                  oscil   ak1, 1000, giSine2
                    outs    a1, a2
endin