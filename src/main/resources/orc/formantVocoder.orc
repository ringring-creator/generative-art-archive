sr = 48000
kr = 24000
nchnls = 2

; Define formant frequency
f00 = 110
f10 = 250
f20 = 2100
f30 = 2900
f40 = 3700

f01 = 160
f11 = 450
f21 = 1900
f31 = 2650
f41 = 3800

f02 = 110
f12 = 700
f22 = 1250
f32 = 2500
f42 = 3900

f03 = 110
f13 = 500
f23 = 1050
f33 = 2700
f43 = 3700

f04 = 110
f14 = 330
f24 = 1500
f34 = 2400
f44 = 3650

f05 = 110
f15 = 335
f25 = 1550
f35 = 2450
f45 = 3800
; Define pitch frequency
f0 = (f00 + f01) / 2.0
; Define Formant bandwidth
b0 = 50
b1 = 70
b2 = 110
b3 = 200



instr 1  ; Chant Scheme
asig pluck 0.5, p4, p4, 0, 1
     outs asig, asig
endin