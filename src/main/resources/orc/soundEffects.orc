sr 		= 		48000
kr 		= 		24000
nchnls 	= 		2
0dbfs  = 1

gamix init 0

instr 1 ;generate assigned table sound: p4: amplitude (0-32767), p5: freqenty, p6: the number of table.
    ares oscil p4, p5, p6
    outs ares, ares
    gamix = gamix + ares
endin

instr 2 ; triangle wave
    kamp = p4
    kcps = p5
    iwave = p6
    kpw init 0.5
    ifn = p7
    ares vco kamp, kcps, iwave, kpw, ifn
    outs ares, ares
endin

instr 3
    krvt = p4
    ilpt = p5
    aleft comb gamix, krvt, ilpt
    aright comb gamix, krvt, ilpt
    outs aleft, aright

    clear gamix
endin

instr 4 ; rain sound
    kamp = p4
    kbeta = p5
    a1 noise p4, p5
    ares clfilt a1, 1000, 1, 2
    outs ares, ares
endin

instr 5 ; rain sound
    kamp = p4
    kbeta = p5
    a1 noise p4, p5
    ;ares clfilt a1, 1000, 1, 10
    aFilt clfilt a1, 1000, 1, 10
    ares reson aFilt, 1750, 750
    outs ares, ares
endin

instr 6 ; Snowstorm
    kamp = p4
    kcps = p5
    anoise fractalnoise kamp, 0
    alfo lfo 0.05, p5, 0
    aStorm sum anoise, alfo
    aSum sum anoise, alfo
    ares clfilt aSum, 4000, 0, 10
    outs ares, ares
endin

instr 7 ; Heartbeat
    aL, aR soundin "../out/wav/BassDrum.wav"
    aFilt clfilt aL, 900 , 0, 10
    aComb comb aFilt, 0.18, 0.02
    kenv adsr 0.04, 0.08, 0.4, 0.06
    ares = aComb * kenv
    outs ares, ares
endin

instr 8 ; Warning sound
    aL, aR loscil 1.0, 800, 80, 261.626, 1
    aLres, aRres freeverb aL, aR, 0.5, 0.5
    outs aLres, aRres
    ;outs aL, aR
endin

instr 9 ; Warning sound
    aL, aR loscil 1.0, 800, 80, 261.626, 2
    aLres, aRres freeverb aL, aR, 0.5, 0.5
    outs aLres, aRres
endin

instr 10; Laser gun
    kamp line p4, p3, p5
    aS  vco  0.5, 4186.01 * kamp, 2, 0.5, 110
    at1 vco  0.5, 2093.00 * kamp, 3, 0.5, 110
    at2 vco  0.5, 1046.50 * kamp, 3, 0.5, 110
    ares sum aS, at1, at2
    outs ares, ares
endin