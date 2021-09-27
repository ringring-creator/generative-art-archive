sr = 48000
kr = 24000
nchnls = 2
0dbfs = 1


giEngine     fluidEngine; start fluidsynth engine
; load a soundfont
iSfNum1      fluidLoad          "/usr/share/sounds/sf2/default-GM.sf2", giEngine, 1
; load a different soundfont
iSfNum2      fluidLoad          "/usr/share/sounds/sf2/default-GM.sf2", giEngine, 1
; direct each midi channels to a particular soundfonts
 fluidProgramSelect giEngine, 1, iSfNum1, 0, 0
 fluidProgramSelect giEngine, 3, iSfNum1, 0, 0
 fluidProgramSelect giEngine, 5, iSfNum1, 0, 0
 fluidProgramSelect giEngine, 7, iSfNum1, 0, 0
 fluidProgramSelect giEngine, 9, iSfNum1, 0, 0
 fluidProgramSelect giEngine, 11, iSfNum1, 0, 0
 fluidProgramSelect giEngine, 13, iSfNum1, 0, 0
 fluidProgramSelect giEngine, 15, iSfNum1, 0, 0
 fluidProgramSelect giEngine, 2, iSfNum2, 0, 0
 fluidProgramSelect giEngine, 4, iSfNum2, 0, 0
 fluidProgramSelect giEngine, 6, iSfNum2, 0, 0
 fluidProgramSelect giEngine, 8, iSfNum2, 0, 0
 fluidProgramSelect giEngine, 10, iSfNum2, 0, 0
 fluidProgramSelect giEngine, 12, iSfNum2, 0, 0
 fluidProgramSelect giEngine, 14, iSfNum2, 0, 0
 fluidProgramSelect giEngine, 16, iSfNum2, 0, 0


instr 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16 ;fluid synths for channels 1-16
iKey         notnum                 ; read in midi note number
iVel         ampmidi            127 ; read in key velocity
; create a note played by the soundfont for this instrument
fluidNote          giEngine, p1, iKey, iVel
endin

instr 99 ; gathering of fluidsynth audio and audio output
aSigL, aSigR fluidOut           giEngine      ; read all audio from soundfont
         outs               aSigL, aSigR  ; send audio to outputs
endin
