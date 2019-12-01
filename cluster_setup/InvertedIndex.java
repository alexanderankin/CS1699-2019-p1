import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.StringUtils;

public class InvertedIndex {
  public static class TokenizerMapper
       extends Mapper<Object, Text, Text, Text>{
    static enum CountersEnum { INPUT_WORDS }

    @Override
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
      String filename_ = ((FileSplit) context.getInputSplit()).getPath().getName().replaceAll("_", "");
      String line = value.toString().toLowerCase();
      StringTokenizer itr = new StringTokenizer(line);

      HashMap<String,Integer> map = new HashMap<String,Integer>();
      while (itr.hasMoreTokens()) {
        String word = itr.nextToken().replaceAll("[\\\",?;:!._'-()\\[\\]{]", "");
        map.put(word, map.getOrDefault(word, 0) + 1);
        Counter counter = context.getCounter(CountersEnum.class.getName(), CountersEnum.INPUT_WORDS.toString());
        counter.increment(1);
      }

      
      for (String key_ : map.keySet()) {
       System.out.println("FileSplit is: " + filename_ + " and mapper is putting key (" + key_ +") and fn ("+ filename_ + ")_(" + map.get(key_).toString()+ ") and the literal value being written to the context is `" + filename_ + "_" + map.get(key_).toString() + "`.");

        context.write(new Text(key_), new Text(filename_ + "_" + map.get(key_).toString()));
      }
    }
  }

  public static class IntSumReducer extends Reducer<Text,Text,Text,Text> {
    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
      HashMap<String,Integer> docCount = new HashMap<String, Integer>();
      System.out.println("reducer for term " + key.toString());
      ArrayList<String> values_ = new ArrayList<>();

      // copy all doc_1 into AList<String>
      for (Text val : values) { values_.add(val.toString()/*.trim()*/); }

      // for all [ doc_1, doc_1, doc1_1, ... ] do hash[doc] += 1;
      for (String val : values_) {
        System.out.println("val is " + val);

        // this was taken from a board on the internet
        // if (val.length() < 2) continue;
        String[] p;
        if (val.indexOf("_") > -1) {
          p = val.split("_");
        } else if (val.indexOf(",") > -1) {
          p = val.split(",");
        } else {
          System.out.println("val " + val + " of term " + key.toString() + " is bad");
          continue;
        }
        System.out.println("p is " + String.join("][", p) + " with length: " + p.length);
        try {
          docCount.put(p[0], docCount.getOrDefault(p[0], 0) + Integer.valueOf(p[1]));
        } catch (Exception e) {
          System.out.println("Problem happened when val was " + val);
        }

      }
      
      // This sorts the map from { b: 1, c: 2, a: 0 } to { a: 0, b: 1, c:2 }
      // TreeMap<String, Integer> map = new TreeMap<String, Integer>(docCount);
      HashMap<String, Integer> map = docCount;

      // list of ["doc,1", "doc2,30"]
      ArrayList<String> list = new ArrayList<>();
      for (String doc : map.keySet()) {
        System.out.println("last reduction in term reducer " + doc + "," + map.get(doc).toString());
        list.add(doc + "_" + map.get(doc).toString());
      }

      context.write(new Text(key.toString()), new Text(String.join(",", list)));
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    GenericOptionsParser optionParser = new GenericOptionsParser(conf, args);
    String[] remainingArgs = optionParser.getRemainingArgs();
    Job job = Job.getInstance(conf, "word count");
    job.setJarByClass(InvertedIndex.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);

    List<String> otherArgs = new ArrayList<String>();
    for (int i=0; i < remainingArgs.length; ++i) {
      otherArgs.add(remainingArgs[i]);
    }
    FileInputFormat.addInputPath(job, new Path(otherArgs.get(0)));
    FileOutputFormat.setOutputPath(job, new Path(otherArgs.get(1)));

    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
