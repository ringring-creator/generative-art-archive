sr 		= 		48000
kr 		= 		48000 ; kontrol rate, k-cycle. idetical to sample rate in this orchestra
nchnls 	= 		2
0dbfs   =       1

; tables
giSine         ftgen       0, 0, 8192, 10, 1
giEnv          ftgen       0, 0, 8192, 20, 2 ;Hanning window table
                                             ;its table is suitable for granulation

; Granulation is to create a high-density acoustic body by grain (short sound, 10ms-30ms)

instr granulationSynth
kfreq          rand         .5 * p4, p7 ; random frequency
kamp           rand         .30 * p5, .123 * p7 ; random amplitude
kdur           rand         .25 * p6, .345 * p7 ; random duration

NOUVEAU:
ipit           =            p4 + i(kfreq)
iamp           =            p5 + i(kamp)
idur           =            p6 + i(kdur)
               timout      0, idur, CONTINUE
               reinit       NOUVEAU

CONTINUE:
kenv           oscil1       0, iamp, idur, giEnv
ares           oscil        kenv, ipit, giSine
               outs         ares, ares
endin