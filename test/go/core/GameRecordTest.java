package go.core;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Collections;

public class GameRecordTest extends TestCase {

    private static final boolean deleteTestFile = true;
    private static final String savePath = "./test/test.json";
    private GameRecord record1 = null;
    private GameRecord record2 = null;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        record1 = new GameRecord(9,9);
        record2 = new GameRecord(9,9);
        GameTurn current1 = null;
        GameTurn current2 = null;
        for (int i = 0; i < 9 ; i++) {
            current1 = record1.getLastTurn().toNext(i,i,(i)%2+1, Collections.<Intersection>emptySet());
            record1.apply(current1);
            current2 = record2.getLastTurn().toNext(i,i,(i)%2+1, Collections.<Intersection>emptySet());
            record2.apply(current2);
        }
        record1.undo();
        record1.undo();
        record1.undo();
        record2.undo();
        record2.undo();
        record2.undo();
    }

    @After
    public void tearDown() throws Exception {
        if (deleteTestFile) {
            File testFile = new File(savePath);
            testFile.delete();
        }
    }

    @Test
    public void testWritingAndLoadingIsCoherent() throws Exception {
        assertTrue(record1.save(savePath));
        GameRecord loaded = GameRecord.load(savePath);
        assertEquals(record1, loaded);
    }

    @Test
    public void testSavingDoesNotCorruptRecord() throws Exception {
        record1.save(savePath);
        assertEquals(record1,record2);
    }

}