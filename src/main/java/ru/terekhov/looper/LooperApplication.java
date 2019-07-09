package ru.terekhov.looper;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class LooperApplication {
    public static void main(String[] args) {
        Sample.sample("Hello world!");
/*        LooperApplication la = new LooperApplication();
        try {
            byte[] sound = loadSoundIntoMemory(la);
            play(sound);
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }*/
    }

    private static void play(byte[] sound) throws LineUnavailableException {
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, stdFormat);
        SourceDataLine sdl = (SourceDataLine)AudioSystem.getLine(info);
        sdl.open();
        sdl.start();
        // BPM = 60
        int BPM = 180   ;
        int CHANNELS = 2;
        int SAMPLE_RATE = 44100;
        int samplesPerBeat = (60 * SAMPLE_RATE * CHANNELS / BPM);
        System.out.println("Samples per beat: " + samplesPerBeat);
        while (true) {
            if (samplesPerBeat > sound.length) {
                int length = samplesPerBeat-sound.length;
                sdl.write(sound, 0, sound.length);
                sdl.write(new byte[length], 0, length);
            } else {
                sdl.write( sound, 0, samplesPerBeat);
            }
        }

    }

    private static AudioFormat stdFormat =
            new AudioFormat( AudioFormat.Encoding.PCM_SIGNED, 44100.0f, 16, 1, 2,
                    44100.0f, false );



    private static byte[] loadSoundIntoMemory(LooperApplication la) throws UnsupportedAudioFileException, IOException {
        int totalFramesRead = 0;

        try {
            URL file = la.getSoundFile("metronome-16.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            int bytesPerFrame =
                    audioInputStream.getFormat().getFrameSize();
            if (bytesPerFrame == AudioSystem.NOT_SPECIFIED) {
                // some audio formats may have unspecified frame size
                // in that case we may read any amount of bytes
                bytesPerFrame = 1;
            }
            // Set an arbitrary buffer size of 1024 frames.
            int numBytes = (int)(audioInputStream.getFrameLength() * bytesPerFrame);
            byte[] audioBytes = new byte[numBytes];
            try {
                int numBytesRead = 0;
                int numFramesRead = 0;
                // Try to read numBytes bytes from the file.
                while ((numBytesRead =
                        audioInputStream.read(audioBytes)) != -1) {
                    // Calculate the number of frames actually read.
                    numFramesRead = numBytesRead / bytesPerFrame;
                    totalFramesRead += numFramesRead;
                    // Here, do something useful with the audio data that's
                    // now in the audioBytes array...
                }
                return audioBytes;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    private URL getSoundFile(String resourceName) throws NullPointerException {
        return Thread.currentThread().getContextClassLoader().getResource(resourceName);
    }
}
