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

giNoteNumber[]         fillarray 60, 62, 64, 65, 67, 69, 71, 72
giSine   ftgen       0, 0, 8192, 10, 1

instr oscilInstr
ares       oscili   1, p4, giSine
outs ares, ares
endin

instr playOscilInstr
index   =   0
iStart  =   0
iDur    =   2
while   index < lenarray(giNoteNumber) do
    iFreq   =   mtof:i(giNoteNumber[index])
    schedule "oscilInstr", iStart, iDur, iFreq
    iStart +=   iDur
    index  +=   1
od
endin

; phase vocoder
; opcodes in csound are pvsadsyn, pvsanal, pvscross, pvsfread, pvsftr,
;                       pvsftw, pvsinfo, pvsmaska, pvsynth
instr pvsanalInstr ; does not work
ain       oscili   1, p4, giSine
;                  in,  fft size, overlap,  window size, analysis form(0=amplitude + frequency)
fftin     pvsanal  ain, 1024,     256,      2048,        0 ; phase vocoder
fftout    pvsmaska    fftin, giSine, 0.5; equalization based on table
ares      pvsynth     fftout ; fft to audio
outs ares, ares
endin


instr playPvsanalInstr
index   =   0
iStart  =   0
iDur    =   2
while   index < lenarray(giNoteNumber) do
    iFreq   =   mtof:i(giNoteNumber[index])
    schedule "pvsanalInstr", iStart, iDur, iFreq
    iStart +=   iDur
    index  +=   1
od
endin

instr pvscrossInstr
a1       oscili   1, p4, giSine
a2       rand    0.5
fft1     pvsanal  a1, 1024, 256, 2048, 0
fft2     pvsanal  a2, 1024, 256, 2048, 0
fftout   pvscross  fft1, fft2, 0.7, 0.3 ; synthesis based on amplitudes of spectrum
ares   pvsynth     fftout
outs ares, ares
endin

instr playPvscrossInstr
index   =   0
iStart  =   0
iDur    =   2
while   index < lenarray(giNoteNumber) do
    iFreq   =   mtof:i(giNoteNumber[index])
    schedule "pvscrossInstr", iStart, iDur, iFreq
    iStart +=   iDur
    index  +=   1
od
endin

; cross synthesis by using FFT
instr cross2Instr
giWinFunc   ftgen       0, 0, 2048, 20, 2

a1       oscili   1, p4, giSine
a2       rand    0.5
;              career, modulator, fft size, overlap(2 or 4), window function table num , ratio of cross synthsis
ares    cross2   a1,     a2,        2048,     4,             giWinFunc,                  0
outs ares, ares
endin

instr playCross2Instr
index   =   0
iStart  =   0
iDur    =   2
while   index < lenarray(giNoteNumber) do
    iFreq   =   mtof:i(giNoteNumber[index])
    schedule "cross2Instr", iStart, iDur, iFreq
    iStart +=   iDur
    index  +=   1
od
endin
