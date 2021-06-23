package practice

import com.jsyn.JSyn
import com.jsyn.Synthesizer
import com.jsyn.instruments.DualOscillatorSynthVoice
import com.jsyn.scope.AudioScope
import com.jsyn.swing.JAppletFrame
import com.jsyn.swing.SoundTweaker
import com.jsyn.unitgen.LineOut
import com.jsyn.unitgen.UnitSource
import java.awt.BorderLayout
import javax.swing.JApplet


class CircuitSound : JApplet() {
    private val synth: Synthesizer
    private val lineOut: LineOut
    private val tweaker: SoundTweaker
    private val unitSource: UnitSource
    private val scope: AudioScope

    init {
        layout = BorderLayout()
        synth = JSyn.createSynthesizer()
        synth.add(LineOut().also { lineOut = it })
        unitSource = createUnitSource()
        synth.add(unitSource.getUnitGenerator())

        // Connect the source to both left and right speakers.
        unitSource.getOutput().connect(0, lineOut.input, 0)
        unitSource.getOutput().connect(0, lineOut.input, 1)
        tweaker = SoundTweaker(
            synth, unitSource.getUnitGenerator()::class.simpleName,
            unitSource
        )
        add(tweaker, BorderLayout.CENTER)

        // Use a scope to see the output.
        scope = AudioScope(synth)
        scope.addProbe(unitSource.getOutput())
        scope.setTriggerMode(AudioScope.TriggerMode.AUTO)
        scope.getView().setControlsVisible(false)
        add(BorderLayout.SOUTH, scope.getView())
        validate()
    }

    /**
     * Override this to test your own circuits.
     *
     * @return
     */
    fun createUnitSource(): UnitSource {
        //return new SampleHoldNoteBlaster();
        //return new com.syntona.exported.FMVoice();
        return DualOscillatorSynthVoice()
        //return new WindCircuit();
        //return new WhiteNoise();
        //return new BrownNoise();
    }

    override fun start() {
        // Start synthesizer using default stereo output at 44100 Hz.
        synth.start()
        // Start the LineOut. It will pull data from the other units.
        lineOut.start()
        scope.start()
    }

    override fun stop() {
        scope.stop()
        synth.stop()
    }
}

/* Can be run as either an application or as an applet. */
fun main(args: Array<String>) {
    val applet = CircuitSound()
    val frame = JAppletFrame("JSyn Circuit", applet)
    frame.setSize(600,600)
    frame.isVisible = true
    frame.test()
}
