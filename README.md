# MapReduce : Movie Script Analysis

This repository is designed to test MapReduce jobs using a simple word count dataset.

## Objectives


1. **Understand Hadoop's Architecture:** Learn how Hadoop's distributed file system (HDFS) and MapReduce framework work together to process large datasets.
2. **Build and Deploy a MapReduce Job:** Gain experience in compiling a Java MapReduce program, deploying it to a Hadoop cluster, and running it using Docker.
3. **Interact with Hadoop Ecosystem:** Practice using Hadoop commands to manage HDFS and execute MapReduce jobs.
4. **Work with Docker Containers:** Understand how to use Docker to run and manage Hadoop components and transfer files between the host and container environments.
5. **Analyze MapReduce Job Outputs:** Learn how to retrieve and interpret the results of a MapReduce job.

## Setup and Execution

### 1. **Start the Hadoop Cluster**

Run the following command to start the Hadoop cluster:

```bash
docker compose up -d
```

### 2. **Build the Code**

Build the code using Maven:

```bash
mvn install
```

### 3. **Move JAR File to Shared Folder**

Move the generated JAR file to a shared folder for easy access:

```bash
mv target/*.jar input/code/
```

### 4. **Copy JAR to Docker Container**

Copy the JAR file to the Hadoop ResourceManager container:

```bash
docker cp input/code/hands-on2-movie-script-analysis-1.0-SNAPSHOT.jar resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/
```

### 5. **Move Dataset to Docker Container**

Copy the dataset to the Hadoop ResourceManager container:

```bash
docker cp input/data/movie_dialogues.txt resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/
```

### 6. **Connect to Docker Container**

Access the Hadoop ResourceManager container:

```bash
docker exec -it resourcemanager /bin/bash
```

Navigate to the Hadoop directory:

```bash
cd /opt/hadoop-3.2.1/share/hadoop/mapreduce/
```

### 7. **Set Up HDFS**

Create a folder in HDFS for the input dataset:

```bash
hadoop fs -mkdir -p /input/dataset
```

Copy the input dataset to the HDFS folder:

```bash
hadoop fs -put ./movie_dialogues.txt /input/dataset
```

### 8. **Execute the MapReduce Job**

Run your MapReduce job using the following command:

```bash
hadoop jar /opt/hadoop-3.2.1/share/hadoop/mapreduce/hands-on2-movie-script-analysis-1.0-SNAPSHOT.jar com.movie.script.analysis.MovieScriptAnalysis /input/dataset/movie_dialogues.txt /output
```

### 9. **View the Output**

To view the output of your MapReduce job, use:

```bash
hadoop fs -cat /output/*
```

### 10. **Copy Output from HDFS to Local OS**

To copy the output from HDFS to your local machine:

1. Use the following command to copy from HDFS:
    ```bash
    hdfs dfs -get /output /opt/hadoop-3.2.1/share/hadoop/mapreduce/
    ```

2. use Docker to copy from the container to your local machine:
   ```bash
   exit 
   ```
    ```bash
    docker cp resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/output/ output/
    ```
3. Commit and push to your repo so that we can able to see your output

## Project Overview

The goal of this project is to implement a Hadoop MapReduce program that processes a movie script dataset and extracts key insights from the dialogues. The program performs the following tasks:

1. Identifies the most frequently spoken words by characters.
2. Calculates the total dialogue length for each character.
3. Extracts unique words used by each character.
4. Tracks key statistics using Hadoop Counters:
    - Total number of lines processed
    - Total words processed
    - Total characters processed
    - Total unique words identified
    - Number of characters speaking dialogues

The input dataset is in the format where each line represents a movie dialogue in the format `Character: Dialogue`.

## Approach and Implementation

### Mapper Logic:
The mapper processes each line of the input data, extracting the character's name and dialogue. For each word in the dialogue:
- It updates the word frequency for the corresponding character.
- It keeps track of the unique words used by each character.
- It counts the total number of words and characters in the dialogue.

Additionally, Hadoop Counters are incremented to track the following:
- Total lines processed
- Total words processed
- Total characters processed
- Total unique words identified
- Number of characters speaking dialogues

### Reducer Logic:
The reducer aggregates the data for each character:
- For the most frequent words, it merges word frequencies from the mappers and calculates the most frequently spoken words.
- For total dialogue length, it sums the lengths of all the dialogues from each character.
- For unique words, it merges the sets of words from each line to ensure uniqueness.

The final output includes the results for the most frequently spoken words, total dialogue length, unique words used by each character, and the Hadoop Counters.


### Sample Input and Output
### Sample Input:
```
JACK: The ship is sinking! I have to go now.
ROSE: I won’t leave without you.
JACK: I don’t have time, Rose!
```
### Expected Output:
**Most Frequently Spoken Words by Characters**
```
the 3
I 3
have 2
to 2
now 1
without 1
```
**Total Dialogue Length per Character**
```
JACK 54
ROSE 25
```
**Unique Words Used by Each Character**
```
JACK [the, ship, is, sinking, I, have, to, go, now, don’t, time, rose]
ROSE [i, won’t, leave, without, you]
```
**Hadoop Counter Output**
```
Total Lines Processed: 3
Total Words Processed: 18
Total Characters Processed: 79
Total Unique Words Identified: 13
Number of Characters Speaking: 2
```



