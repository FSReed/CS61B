package gh2;
import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

/**
 * A client that uses the synthesizer package to replicate a plucked guitar string sound
 */
public class GuitarHero {
    public static final double BASIC = 440.0;
    public static final String KEYBOARD = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
    public static final int SIZE = 37;

    public static void main(String[] args) {
        /* create 37 guitar strings */
        GuitarString[] stringSet = new GuitarString[SIZE];
        for (int i = 0; i < SIZE; i += 1) {
            double frequency = BASIC * Math.pow(2, i / 12.0);
            stringSet[i] = new GuitarString(frequency);
        }

        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int index = KEYBOARD.indexOf(key);
                if (index > -1) {
                    stringSet[index].pluck();
                }

            }

            /* compute the superposition of samples */
            double sample = 0;
            for (int i = 0; i < SIZE; i += 1) {
                sample += stringSet[i].sample();
            }

            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            for (int i = 0; i < SIZE; i += 1) {
                stringSet[i].tic();
            }
        }
    }
}
