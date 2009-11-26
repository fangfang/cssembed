/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.nczonline.web.cssembed;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Nicholas C. Zakas
 */
public class CSSURLEmbedderTest {
    
    private static String folderDataURI = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACIAAAAbCAMAAAAu7K2VAAAABGdBTUEAAK/INwWK6QAAABl0RVh0U29mdHdhcmUAQWRvYmUgSW1hZ2VSZWFkeXHJZTwAAAAwUExURWxsbNbW1v/rhf/ge//3kf/Ub9/f3/b29oeHh/7LZv/0juazTktLS8WSLf//mf/////BPrAAAAB4SURBVHja3NLdCoAgDIbhqbXZz2f3f7eZWUpMO67nQEReBqK0vaLPJohYegnSYqSdYAtRGvUYVpJhPpx7z2piLSqsJQ73oY1ztGREuEwBpCUTwpAt7cRmncRlnWTMoCdcXxmrdiMxngpvtDcSNkX9AvTnv9uyCzAAgzAw+dNAwOQAAAAASUVORK5CYII=";
    private CSSURLEmbedder embedder;
    
    public CSSURLEmbedderTest() {
    }
    
    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
        embedder = null;
    }
    
    @Test
    public void testAbsoluteLocalFile() throws IOException {
        String filename = CSSURLEmbedderTest.class.getResource("folder.png").getPath().replace("%20", " ");
        String code = "background: url(folder.png);";
        
        StringWriter writer = new StringWriter();
        embedder = new CSSURLEmbedder(new StringReader(code), true);
        embedder.embedImages(writer, filename.substring(0, filename.lastIndexOf("/")+1));
        
        String result = writer.toString();
        assertEquals("background: url(" + folderDataURI + ");", result);
    }
    
    @Test
    public void testAbsoluteLocalFileMultipleOneLine() throws IOException {
        String filename = CSSURLEmbedderTest.class.getResource("folder.png").getPath().replace("%20", " ");
        String code = "background: url(folder.png); background: url(folder.png);";
        
        StringWriter writer = new StringWriter();
        embedder = new CSSURLEmbedder(new StringReader(code), true);
        embedder.embedImages(writer, filename.substring(0, filename.lastIndexOf("/")+1));
        
        String result = writer.toString();
        assertEquals("background: url(" + folderDataURI + "); background: url(" + folderDataURI + ");", result);
    }
    
    @Test
    public void testAbsoluteLocalFileWithDoubleQuotes() throws IOException {
        String filename = CSSURLEmbedderTest.class.getResource("folder.png").getPath().replace("%20", " ");
        String code = "background: url(\"folder.png\");";
        
        StringWriter writer = new StringWriter();
        embedder = new CSSURLEmbedder(new StringReader(code), true);
        embedder.embedImages(writer, filename.substring(0, filename.lastIndexOf("/")+1));
        
        String result = writer.toString();
        assertEquals("background: url(" + folderDataURI + ");", result);
    }
    
    @Test
    public void testAbsoluteLocalFileWithSingleQuotes() throws IOException {
        String filename = CSSURLEmbedderTest.class.getResource("folder.png").getPath().replace("%20", " ");
        String code = "background: url('folder.png');";
        
        StringWriter writer = new StringWriter();
        embedder = new CSSURLEmbedder(new StringReader(code), true);
        embedder.embedImages(writer, filename.substring(0, filename.lastIndexOf("/")+1));
        
        String result = writer.toString();
        assertEquals("background: url(" + folderDataURI + ");", result);
    }     
    
    @Test
    public void testReadFromAndWriteToSameFile() throws IOException {
        String filename = CSSURLEmbedderTest.class.getResource("samefiletest.css").getPath().replace("%20", " ");
        File file = new File(filename);
        Reader in = new InputStreamReader(new FileInputStream(file));
        
        embedder = new CSSURLEmbedder(in, true);
        in.close();
        
        Writer writer = new OutputStreamWriter(new FileOutputStream(file));        
        embedder.embedImages(writer, filename.substring(0, filename.lastIndexOf("/")+1));
        writer.close();
        
        in = new InputStreamReader(new FileInputStream(file));
        char[] chars = new char[(int)file.length()];
        in.read(chars, 0, (int)file.length());
        in.close();
        
        String result = new String(chars);
        assertEquals("background: url(" + folderDataURI + ");", result);
    }
    
}