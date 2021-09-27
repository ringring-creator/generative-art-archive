sr 		= 		48000
kr 		= 		24000
nchnls 	= 		2


instr 1
kamp = 10000
a1 oscil kamp, p4, 1
a2 oscil kamp, p5, 1
a3 oscil kamp, p6, 1
asum sum a1, a2, a3
ares = asum / 3
outs ares, ares
endin

instr 2
kamp = 10000
a1 oscil kamp, p4, 1
a2 oscil kamp, p5, 1
a3 oscil kamp, p6, 1
a4 oscil kamp, p6, 1
asum sum a1, a2, a3, a4
ares = asum / 4
outs ares, ares
endin