sr = 48000
kr = 24000
nchnls = 2


instr 1
midiout	192, p4, p5, 0  ;program change to p5, channel is p4
midion p4, p6, p7       ; p6 is note number and p7 is velocity

endin
