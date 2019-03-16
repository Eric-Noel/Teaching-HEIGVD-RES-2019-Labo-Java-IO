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

  private  int line = 1;
  private boolean newLine = true;


  @Override
  public void write(String str, int off, int len) throws IOException {
    for(int i = off; i < len+off; i++){
      write((int)str.charAt(i));
    }
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    for(int i = off; i < len+off; i++){
      write((int)cbuf[i]);
    }
  }

  @Override
  public void write(int c) throws IOException {
    char car = (char)c;

    if(car == '\n'){
      out.write(car);
      out.write(line++ + "\t");
      newLine = false;
    }
    else if(car == '\r'){
      out.write(car);
      newLine = true;
    }
    else if(newLine){
      out.write(line++ + "\t" + car);
      newLine = false;
    }
    else {
      out.write(car);
    }
  }
}
