sr 		= 		48000
kr 		= 		48000 ; kontrol rate, k-cycle. idetical to sample rate in this orchestra
nchnls 	= 		2
0dbfs   =       1

; tables
giSine         ftgen       0, 0, 8192, 10, 1
giEnv          ftgen       0, 0, 8192, 20, 2 ;Hanning window table
                                             ;its table is suitable for granulation
giLongSine         ftgen       0, 0, 65535, 10, 1
giAttack           ftgen       2, 0, 1024, 19, .5, .5, 270, .5

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

instr nightingale
gaportL, gaportR     soundin  "../in/wav/nightingale.wav" ; career signals
outs gaportL, gaportR
endin


gSampleFile = "../in/wav/nightingale.wav"
;varname                ifn itime   isize   igen    Sfilename       iskip   iformat ichn
giFile    ftgen       0,  0,      2^23,      1,      gSampleFile,    0,      0,      0


instr granulationFileSynth
ifphas         =            ftsr(giFile) / ftlen(giFile)  ; phasor reding speed
aidx           phasor       ifphas     ; sequence by current pos + ifphas
ares           tablei       aidx, giFile, 1 ; table access by aidx (index)
               outs         ares, ares
endin

instr granuDynamicSpeed
ifphas         =            ftsr(giFile) / ftlen(giFile)  ; phasor reding speed
aidx           phasor       ifphas * p4     ; sequence by current pos + ifphas
ares           tablei       aidx, giFile, 1 ; table access by aidx (index)
               outs         ares, ares
endin

; csound manual

instr csoundGranul
;k1      linseg 0,0.5,1,(p3-p2-1),1,0.5,0
igskip=i(p10)
printf_i    "igskip = %f, \n", 1, igskip
ilength=i(p12)
printf_i    "ilength = %f, \n", 1, ilength
a1      granule p4,p5,p6,p7,p8,giLongSine,p9,p10,p11,p12,p13,p14,p15,p16,p17,p18,p19,p20,p21,p22,p23
a2      granule p4,p5,p6,p7,p8,giLongSine,p9,p10,p11,p12,p13,p14,p15,p16,p17,p18,p19,p20+0.17,p21,p22,p23
outs a1,a2
endin

instr fogInstr
aenv    linseg  1, p3 * 0.01, 1, p3 * 0.9, 0
i1      =       sr / ftlen(giLongSine)
a1      phasor  i1 * p5
;               xamp, xdens, xtrans, aspd, koct, kband, kris, kdur, kdec, iolaps, ifna,       ifnb,     itotdur
a2      fog     0.5,  10,    p4,     a1,   0,    0,     0.01, 0.02, 2,    30,     giSine, giAttack, p3, 0, 1
ares    =       aenv * a2
        outs    ares, ares
endin