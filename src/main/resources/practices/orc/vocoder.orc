sr 		= 		48000
kr 		= 		48000 ; kontrol rate, k-cycle. idetical to sample rate in this orchestra
nchnls 	= 		2
0dbfs   =       1

instr aToZ
gamodL, gamodR       soundin  "../in/wav/a-z.wav" ; modulation signals
outs gamodL, gamodR
endin

instr nightingale
gaportL, gaportR     soundin  "../in/wav/nightingale.wav" ; career signals
outs gaportL, gaportR
endin

; speaking nightingale
instr vocoder
gamod, gamodR       soundin  "../in/wav/a-z.wav" ; modulation signals
gaport, gaportR     soundin  "../in/wav/nightingale.wav" ; career signals

amod    butterbp    gamod, p4, p4 * 0.05  ; analyze filter
aport   butterbp    gaport, p4, p4 * 0.05 ; synthsis filter
ares    balance     aport, amod
outs ares, ares
endin

; phase vocoder
; opcodes in csound are pvsadsyn, pvsanal, pvscross, pvsfread, pvsftr,
;                       pvsftw, pvsinfo, pvsmaska, pvsynth
instr pvsanalInstr
giSine          ftgen       0, 0, 8192, 10, 1
ain  in      ; directly input
fin pvsanal ain, 1024, 256, 2048, 0
fout    pvsmaska    fin, giSine, 0.75
aout   pvsynth     fout
endin