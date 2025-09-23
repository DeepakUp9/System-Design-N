
# Create project directory
mkdir long-polling-demo
cd long-polling-demo

# Initialize as a Maven project
mvn archetype:generate -DgroupId=com.example -DartifactId=long-polling -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false

# Move into the project directory
cd long-polling


#### Compile and run the server in one terminal
mvn compile exec:java -Dexec.mainClass="com.example.BasicLongPollingServer"

#### In another terminal, run the client
mvn compile exec:java -Dexec.mainClass="com.example.BasicLongPollingClient"


<hr>

#### Compile and run the server in one terminal
mvn compile exec:java -Dexec.mainClass="com.example.IntermediateLongPollingServer"

#### In another terminal, run the client
mvn compile exec:java -Dexec.mainClass="com.example.IntermediateLongPollingClient"