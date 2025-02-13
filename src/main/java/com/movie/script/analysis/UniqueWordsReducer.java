package com.movie.script.analysis;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashSet;

public class UniqueWordsReducer extends Reducer<Text, Text, Text, Text> {

    @Override
    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        // Using a HashSet to store unique words
        HashSet<String> uniqueWords = new HashSet<>();
        
        for (Text val : values) {
            uniqueWords.add(val.toString());
        }

        // Convert the unique words to a space-separated string
        StringBuilder uniqueWordsList = new StringBuilder("[");
        for (String word : uniqueWords) {
            uniqueWordsList.append(word).append(", ");
        }
        
        // Remove trailing comma and space, and close the bracket
        if (uniqueWordsList.length() > 1) {
            uniqueWordsList.setLength(uniqueWordsList.length() - 2);
        }
        uniqueWordsList.append("]");

        // Emit character and their unique words
        context.write(key, new Text(uniqueWordsList.toString()));
    }
}
