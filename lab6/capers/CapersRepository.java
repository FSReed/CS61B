package capers;

import java.io.File;
import java.io.IOException;

import static capers.Utils.*;

/**
 * A repository for Capers
 * 
 * @author FSReed
 *         The structure of a Capers Repository is as follows:
 *
 *         .capers/ -- top level folder for all persistent data in your lab12
 *         folder
 *         - dogs/ -- folder containing all of the persistent data for dogs
 *         - story -- file containing the current story
 *
 *         TODO: change the above structure if you do something different.
 */
public class CapersRepository {
    /** Current Working Directory. */
    static final File CWD = new File(System.getProperty("user.dir"));

    /** Main metadata folder. */
    static final File CAPERS_FOLDER = join(CWD, ".capers"); // TODO Hint: look at the `join`
    // function in Utils

    /**
     * Does required filesystem operations to allow for persistence.
     * (creates any necessary folders or files)
     * Remember: recommended structure (you do not have to follow):
     *
     * .capers/ -- top level folder for all persistent data in your lab12 folder
     * - dogs/ -- folder containing all of the persistent data for dogs
     * - story -- file containing the current story
     */
    public static void setupPersistence() throws IOException {
        // TODO
        CAPERS_FOLDER.mkdir();
        Dog.DOG_FOLDER.mkdir();
        File story = join(CAPERS_FOLDER, "story");
        story.createNewFile();
    }

    /**
     * Appends the first non-command argument in args
     * to a file called `story` in the .capers directory.
     * 
     * @param text String of the text to be appended to the story
     */
    public static void writeStory(String text) {
        // TODO
        File story = Utils.join(CAPERS_FOLDER, "story");
        if (story.exists()) {
            String prevStory = Utils.readContentsAsString(story);
            Utils.writeContents(story, prevStory, text, "\n");
            System.out.println(prevStory + text);
        }
    }

    /**
     * Creates and persistently saves a dog using the first
     * three non-command arguments of args (name, breed, age).
     * Also prints out the dog's information using toString().
     */
    public static void makeDog(String name, String breed, int age) throws IOException {
        // TODO
        Dog d = new Dog(name, breed, age);
        d.saveDog();
    }

    /**
     * Advances a dog's age persistently and prints out a celebratory message.
     * Also prints out the dog's information using toString().
     * Chooses dog to advance based on the first non-command argument of args.
     * 
     * @param name String name of the Dog whose birthday we're celebrating.
     */
    public static void celebrateBirthday(String name) {
        // TODO
        File tmp = new File(name);
        if (tmp.exists()) {
            Dog currentDog = Dog.fromFile(name);
            currentDog.haveBirthday();
            writeObject(tmp, currentDog);
        }
    }
}
