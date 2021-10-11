sr 		= 		48000
kr 		= 		48000 ; kontrol rate, k-cycle. idetical to sample rate in this orchestra
nchnls 	= 		2
0dbfs   =       1

; tables
giSine   ftgen       0, 0, 8192, 10, 1

instr aToZ
gamodL, gamodR       soundin  "../in/wav/a-z.wav" ; modulation signals
outs gamodL, gamodR
endin

instr nightingale
gaportL, gaportR     soundin  "../in/wav/nightingale.wav" ; career signals
outs gaportL, gaportR
endin


; Linear Prediction Coding
; The number of poles is filter dimensions
; csound -U lpanal -s 48000 -c 2 -p 50 a-z.wav ../lpc/a-z.lpc for preparation
; lpc record rms amplitude, with or without multiple voices, pitch, filter coefficient.
instr lpc
kind                        line        0, p3, p3 * p4 ; analysis window func index
; analysis surplus rms, original rms, normalized error signal, pitch
krmsr,                  krmso,        kerr,                    kcps    lpread      kind, "../in/lpc/a-z.lpc"
arand                       rand        krmso / 2 ; white noise
arobot                      buzz        0.5, 220*p5, 60, giSine; impulse train
printk  1, krmso
abuzz                       buzz        krmso/32767, kcps*p5, 60, giSine
abuzz                       butterlp   abuzz, 2000
ares                        lpreson     abuzz
outs    ares, ares
endin

instr lpcFilter
gamod, gamodR       soundin  "../in/wav/a-z.wav" ; modulation signals
gaport, gaportR     soundin  "../in/wav/nightingale.wav" ; career signals

aext        butterbp    gaport, p4, p4 * 0.05   ; analysis filter
aport       butterbp    gamod, p4, p4 * 0.05   ; synthsis filter
ares        balance     aport, aext
outs        ares * 5, ares * 5
endin