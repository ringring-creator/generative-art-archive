sr = 48000
kr = 24000
nchnls = 2
0dbfs  = 1

instr 1  ;STKSitar - has no controllers
asig pluck 0.5, p4, p4, 0, 1
     outs asig, asig
endin

instr 2  ;STKSitar - has no controllers
asig pluck 0.5, p4, p4, 0, 2, 5
     outs asig, asig
endin

instr 3  ;STKSitar - has no controllers
asig pluck 1.0, p4, p4, 0, 3, 0.5, 10
     outs asig, asig
endin

instr 4  ;STKSitar - has no controllers
asig pluck 1.0, p4, p4, 0, 4, 0.5, 10
     outs asig, asig
endin

instr 5  ;STKSitar - has no controllers
asig pluck 0.5, p4, p4, 0, 5, 0.3, 0.3
     outs asig, asig
endin

instr 6  ;STKSitar - has no controllers
asig pluck 1.0, p4, p4, 0, 6
     outs asig, asig
endin