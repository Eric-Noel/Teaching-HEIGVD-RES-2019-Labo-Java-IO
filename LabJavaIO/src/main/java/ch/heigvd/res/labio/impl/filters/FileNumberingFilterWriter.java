package ch.heigvd.res.labio.impl.filters;

import ch.heigvd.res.labio.impl.Utils;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

/**
 * This class transforms the streams of character sent to the decorated writer.
 * When filter encounters a line separator, it sends it to the decorated writer.
 * It then sends the line number and a tab character, before resuming the write
 * process.
 *
 * Hello\n\World -> 1\tHello\n2\tWorld
 *
 * @author Olivier Liechti
 */
public class FileNumberingFilterWriter extends FilterWriter {

  private static final Logger LOG = Logger.getLogger(FileNumberingFilterWriter.class.getName());

  public FileNumberingFilterWriter(Writer out) {
    super(out);
  }

  private  int actual = 1;
  private boolean newLine = true;
  @Override
  public void write(String str, int off, int len) throws IOException {
    str = str.substring(off, off+len);
    String lines[] = Utils.getNextLine(str);

    while(!lines[0].equals("")){
      if (newLine) {
        out.write(actual++ + "\t" + lines[0]);
      }
      else {
        out.write(lines[0]);
      }
      newLine = true;
      lines = Utils.getNextLine(lines[1]);
    }
    if(newLine){
      out.write(actual++ + "\t" + lines[1]);
      newLine = false;
    }
    else{
      out.write(lines[1]);
    }
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    String str = new String();
    for (int k = 0; k < cbuf.length; k++){
      str = str + cbuf[k];
    }
    str = str.substring(off, off+len);
    String lines[] = Utils.getNextLine(str);

    while (!lines[0].equals("")){
      if (newLine) {
        out.write(actual++ + "\t" + lines[0]);
      }
      else {
        out.write(lines[0]);
      }
      newLine = true;
      lines = Utils.getNextLine(lines[1]);
    }
    if(newLine){
      out.write(actual++ + "\t" + lines[1]);
      newLine = false;
    }
    else{
      out.write(lines[1]);
    }
  }

  @Override
  public void write(int c) throws IOException {
    String str = String.valueOf((char)c);
    String lines[] = Utils.getNextLine(str);

    if(!lines[0].equals("")){
      out.write(lines[0]);
      newLine = true;
    }
    else if(newLine){
      out.write(actual++ + "\t" + lines[1]);
      newLine = false;
    }
    else{
      out.write(lines[1]);
      newLine = false;
    }
  }
}
