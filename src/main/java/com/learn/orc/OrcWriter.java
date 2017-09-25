package com.learn.orc;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.exec.vector.LongColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.VectorizedRowBatch;
import org.apache.orc.OrcFile;
import org.apache.orc.TypeDescription;
import org.apache.orc.Writer;

public class OrcWriter {
  
 private String fileToWrite;
 private String schema = "struct<>";
 public OrcWriter (String fileToWrite) {
   this.fileToWrite = fileToWrite;
 }
 
  public void writeORC() throws IllegalArgumentException, IOException {
    Configuration conf = new Configuration();
    TypeDescription schema = TypeDescription.fromString("struct<x:int,y:int>");
    Writer writer = OrcFile.createWriter(new Path(this.fileToWrite),
        OrcFile.writerOptions(conf).setSchema(schema));
    VectorizedRowBatch batch = schema.createRowBatch();
    LongColumnVector x = (LongColumnVector) batch.cols[0];
    LongColumnVector y = (LongColumnVector) batch.cols[1];
    for (int r = 0; r < 10000; ++r) {
      int row = batch.size++;
      x.vector[row] = r;
      y.vector[row] = r * 3;
      if (batch.size == batch.getMaxSize()) {
        writer.addRowBatch(batch);
        batch.reset();
      }

    }

    if (batch.size != 0) {
      writer.addRowBatch(batch);
      batch.reset();
    }
    writer.close();
  }
}
