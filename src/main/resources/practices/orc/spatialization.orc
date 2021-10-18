;sr 		= 		48000
;kr 		= 		48000 ; kontrol rate, k-cycle. idetical to sample rate in this orchestra
sr = 44100
kr = 44100
nchnls 	= 		2
0dbfs   =       1

; tables
giSine          ftgen       0, 0, 8192, 10, 1, 1, 1, 1, 1, 1, 1, 1
giEnv          ftgen       0, 0, 8192, 20, 2


; spatialization is to create acoustic spatiality
; source position is parametalized by the following parameters
; azimuth
; distance and its speed
; its height

; we perceive azimuth by
; time lag for the sound coming from the front to reach the left and right ears in low frequency
; amplitude shift felt by the left and right ears in middle frequency
; difference in frequency spectrum due to asymmetric reflection in high frequency

; we perceive distance by
; ratio of original signal and reverb
; high frequency attenuation
; attenuation of sound source details
instr panoramization
ipi             =           3.1416 ; Pi
kPos            linseg      0, p3, 1 ; from 0 to 1
kDeg            linseg      -ipi / 4, p3, ipi / 4 ; from -45 deg to 45 deg

kLeft           =           ( cos(kDeg) + sin(kDeg) ) * sqrt(2) / 2
kRight          =           ( cos(kDeg) - sin(kDeg) ) * sqrt(2) / 2

kEnv            oscil       1, 10, giEnv    ;envelope
asig            oscil       0.5 * kEnv, 400, giSine
                outs        asig * kLeft, asig * kRight
endin

instr panoramizationLinear
ipi             =           3.1416 ; Pi
kPos            linseg      0, p3, 1 ; from 0 to 1
kDeg            linseg      - ipi / 4, p3, ipi / 4 ; from -45 deg to 45 deg

kLeft           =           ( cos(kDeg) + sin(kDeg) ) * sqrt(2) / 2
kRight          =           ( cos(kDeg) - sin(kDeg) ) * sqrt(2) / 2

kEnv            oscil       1, 10, giEnv    ;envelope
asig            oscil       0.5 * kEnv, 400, giSine
                outs       asig * kPos, asig * (1 - kPos)
endin

instr panoramizationSqrt
ipi             =           3.1416 ; Pi
kPos            linseg      0, p3, 1 ; from 0 to 1
kDeg            linseg      - ipi / 4, p3, ipi / 4 ; from -45 deg to 45 deg

kLeft           =           ( cos(kDeg) + sin(kDeg) ) * sqrt(2) / 2
kRight          =           ( cos(kDeg) - sin(kDeg) ) * sqrt(2) / 2

kEnv            oscil       1, 10, giEnv    ;envelope
asig            oscil       0.5 * kEnv, 400, giSine
                outs       asig * sqrt(kPos), asig * sqrt(1 - kPos)
endin

;3D acoustic spatiality
; csound support 3d acoustic spatiality by hrtf opcode.
; hrtf(Head-Related Transfer Function)
instr threeDim
kaz             linseg      0, p3, -360  ; move the sound in circle
kel             linseg      -40, p3, 45  ; around the listener, changing
                                ; elevation as its turning
kEnv            oscil       1, 10, giEnv    ;envelope
asig            oscil       0.5 * kEnv, 400, giSine
aleft,aright    hrtfer      asig, kaz, kel, "HRTFcompact"
aleftscale      =           200 * aleft
arightscale     =           200 * aright
                outs        aleftscale, arightscale
endin