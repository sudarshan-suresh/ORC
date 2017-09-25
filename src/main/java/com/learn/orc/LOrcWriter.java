package com.learn.orc;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.exec.vector.LongColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.VectorizedRowBatch;
import org.apache.orc.OrcFile;
import org.apache.orc.Reader;
import org.apache.orc.RecordReader;
import org.apache.orc.TypeDescription;
import org.apache.orc.Writer;

public class LOrcWriter {

  public static void main(String[] args) throws IllegalArgumentException, IOException {
    readORC();
  }

  private static void writeORC() throws IllegalArgumentException, IOException {
    Configuration conf = new Configuration();
    TypeDescription schema = TypeDescription.fromString("struct<x:int,y:int>");
    Writer writer = OrcFile.createWriter(new Path("my-file.orc"),
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

  private static void readORC() throws IllegalArgumentException, IOException {
    Configuration conf = new Configuration();
    Reader reader = OrcFile.createReader(new Path("my-file.orc"), OrcFile.readerOptions(conf));
    RecordReader rows = reader.rows();
    VectorizedRowBatch batch = reader.getSchema().createRowBatch();
    while (rows.nextBatch(batch)) {
      LongColumnVector intVector = (LongColumnVector) batch.cols[0];
      LongColumnVector intVector1 = (LongColumnVector) batch.cols[1];
      for (int r = 0; r < batch.size; ++r) {
         int intValue = (int) intVector.vector[r];
         int intValue1 = (int) intVector1.vector[r];
         System.out.println(intValue + "," + intValue1);
      }
    }
    rows.close();
  }

}
