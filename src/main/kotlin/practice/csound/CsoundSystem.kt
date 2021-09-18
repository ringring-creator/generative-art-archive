package practice.csound

import csnd6.Csound
import csnd6.csnd6

class CsoundSystem(outDac: Boolean, outputFilePath: String) {
    private val c: Csound

    init {
        csnd6.csoundInitialize(
            csnd6.CSOUNDINIT_NO_ATEXIT or csnd6.CSOUNDINIT_NO_SIGNAL_HANDLER
        )
        // Create an instance of the Csound object
        c = Csound()
        val ext = getExt(outputFilePath)
        if (outDac.or(outputFilePath.isEmpty())) {
            c.SetOption("-odac")
        } else if (ext.equals("mid")) {
            c.SetOption("-n")
            c.SetOption("--midioutfile=${outputFilePath}")
        } else {
            c.SetOption("-o${outputFilePath}")
        }
    }

    private fun getExt(filePath: String): String {
        val point = filePath.lastIndexOf(".")
        if (point != -1) {
            return filePath.substring(point + 1)
        }
        return ""
    }

    fun compileOrc(orc: String) {
        c.CompileOrc(orc)
    }

    fun readSco(sco: String) {
        c.ReadScore(sco)
    }

    fun run() {
        // When compiling from strings, this call is necessary before doing
        // any performing
        c.Start()
        // This call runs Csound to completion
        c.Perform()
        //Post-processing
        c.Stop()
        c.Cleanup()
    }
}