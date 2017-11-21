package testing;

import java.io.*;

/**
 * Created by Tay on 11/17/2017.
 */
public class Reader {


    void serializeStuff(StuffToRead stuff) {
        String projectLocation = System.getProperty("user.dir");

        // Creates File
        try {
            FileOutputStream fos = new FileOutputStream(projectLocation + "\\test\\test2");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(stuff);
            fos.close();
            oos.close();
            System.out.println("FINISHED MAKING FILE");

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Reads File
        try {
            FileInputStream fis = new FileInputStream(projectLocation + "\\test\\test2"); // THIS IS WHERE YOU ARE RIGHT NOW BRO!
            ObjectInputStream ois = new ObjectInputStream(fis);

            StuffToRead newstuff = (StuffToRead) ois.readObject();
            System.out.println(newstuff.testInt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public static void main(String[] args) {
        File testFolder = new File("test");
        testFolder.mkdir();

        Reader reader = new Reader();

        StuffToRead stuffToRead = new StuffToRead(5);

        reader.serializeStuff(stuffToRead);

    }


}
