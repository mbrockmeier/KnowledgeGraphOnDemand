# KnowledgeGraphOnDemand

## extraction-framework setup

In order to run the extraction-framework, a Java 8 installation is required. Building the extraction-framework from source requires the installation of Apache Maven. The source of the extraction-framework can then be retrieved by running

`git clone https://github.com/dbpedia/extraction-framework`

Then, two properties need to be set in the `core/src/main/resources/universal.properties file.`
`base-dir` needs to point to an empty folder and is used for the storage of the wikidumps (input) and for the extracted model (output) `log-dir` needs to point to an empty folder and is used by the extraction-framework to store its logs

In order to make the extraction-framework runnable without relying on Apache Maven, the following configuration code needs to be added in the `pom.xml` in the `dump` folder.

```
<build>
    <plugins>
        ...
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-assembly-plugin</artifactId>
            <version>2.3</version>
            <configuration>
                <finalName>extraction</finalName>
                <appendAssemblyId>false</appendAssemblyId>
                <archive>
                    <manifest>
                        <mainClass>org.dbpedia.extraction.dump.extract.Extraction</mainClass>
                    </manifest>
                </archive>
                <descriptorRefs>
                    <descriptorRef>jar-with-dependencies</descriptorRef>
                </descriptorRefs>
            </configuration>
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals>
                        <goal>single</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.1</version>
            <configuration>
                <source>1.8</source>
                <target>1.8</target>
            </configuration>
        </plugin>
    </plugins>
</build>
```

Then,

`mvn clean package -DskipTests=true` 
can be run to create an executable jar of the extraction framework. The `extraction.jar` will be created in the `dump/target` folder and needs to be copied to the `dump` folder. This will reduce the runtime of the extraction-framework when called by the server, as the overhead created by Maven is removed.

Additionally, a file called `extraction.kgod.properties` with the following content needs to be created in the `dump` folder.

```
require-download-complete=false
extractors.en=.PageIdExtractor,.LabelExtractor,.PageLinksExtractor,.InfoboxExtractor,.MappingExtractor
source=dump.xml
copyrightCheck=false
languages=en
extractors=
```

Finally, the ontology and mappings need to be downloaded. This can be achieved by running the following two commands while in the `core` directory.

`mvn scala:run "-Dlauncher=download-ontology"`
`mvn scala:run "-Dlauncher=download-mappings"`

## Knowledge Graph On Demand Setup

In order to run the backend, a Java 8 installation is required. Building the backend from source requires the installation of Apache Maven. If rebuilding from source is not desired, the server can be directly started using
`java -jar kgod_server.jar`
Otherwise, the execution of
`mvn clean package`
will build the project and create an executable jar-file.


The backend can be configured using the `kgod.properties` file in the main directory. 

`extractionFrameworkBaseDir` the `base-dir` property of the extraction-framework (String)

`includeBacklinks` whether to include any backlinks in the extraction process

`backlinksCount` number of backlinks to include in the knowledge graph (integer, 0 = unlimited)

`prefixesFile` Path to the file containing the namespace prefixes, predefined in namespaces.csv (String)

`retrieveExtract` whether to include the abstract from wikipedia pages (boolean)

`extractionFrameworkDir` path to the `dump` folder of the extraction-framework (String)

`cacheSize` number of models to store in cache (integer)

`lang` language code for the wikipage extraction and knowledge graph generation (String)
