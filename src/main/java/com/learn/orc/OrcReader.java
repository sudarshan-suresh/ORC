package com.learn.orc;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.exec.vector.LongColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.VectorizedRowBatch;
import org.apache.orc.OrcFile;
import org.apache.orc.Reader;
import org.apache.orc.RecordReader;

public class OrcReader {
  private String fileToRead = null;
  public OrcReader(String fileToRead){
    this.fileToRead = fileToRead;
  }
  
  public void readORC() throws IllegalArgumentException, IOException {
    Configuration conf = new Configuration();
    Reader reader = OrcFile.createReader(new Path(this.fileToRead), OrcFile.readerOptions(conf));
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
