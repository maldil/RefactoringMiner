
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.maldil/python-refactoring-miner/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.maldil/python-refactoring-miner)
[![DOI](https://zenodo.org/badge/347796887.svg)](https://zenodo.org/badge/latestdoi/347796887)


Table of Contents
=================
   * [General info](#general-info)
   * [How to build Py-RefactoringMiner](#how-to-build-py-refactoringminer)
   * [How to use Py-RefactoringMiner as a maven dependency](#how-to-use-py-refactoringminer-as-a-maven-dependency)
   * [How to use Py-RefactoringMiner as a Docker container](#how-to-use-py-refactoringminer-as-a-docker-container)
   * [Research](#research)
      * [How to cite RefactoringMiner](#how-to-cite-refactoringminer)
   * [Contributors](#contributors)
   * [API usage guidelines](#api-usage-guidelines)
      * [With a locally cloned git repository](#with-a-locally-cloned-git-repository)
   * [Location information for the detected refactorings](#location-information-for-the-detected-refactorings)

# General info
[RefactoringMiner](https://github.com/tsantalis/RefactoringMiner) (developed by Nikolaos Tsantali et al.) is a Java library that can detects refactorings applied in the commit history of a Java project. We extend the RefactoringMiner to Python. Now, you can use *Py-RefactoringMiner to detect refactorings applied in Python projects.


Technically, it should support all the refactoring detected by the original RefacotringMiner (please refer [this](https://github.com/tsantalis/RefactoringMiner#general-info)). However, we manually valiaded only 19 kinds of refacotrings. All the validation results are available in our [website](https://mlcodepatterns.github.io).

# How to build Py-RefactoringMiner
To have build RefactoringMiner, you first need to build two dependencies, i.e., 1) [EclipseJDT](https://github.com/maldil/JavaFyPy/tree/master/CustomizedEclipseJDT) and, 2) [JPyParser](https://github.com/maldil/JPythonParser), locally and install them to your local repository.
Building Python-adapted RefactoringMiner could be slidly complex due to Eclipse-JDT paser. 

* [JPyParser](https://github.com/maldil/JPythonParser) 
  * Run `git clone https://github.com/maldil/JPythonParser.git`
  * Run `cd JPythonParser` and `mvn clean package` in the project's root directory. 
  * Install the binaries to your local maven repository using `mvn install:install-file -Dfile=./Your Path/target/JPyParser-1.0-SNAPSHOT.jar -DgroupId=org.mal.python -DartifactId=JPyParser -Dversion=1.0-SNAPSHOT.jar -Dpackaging=jar -DgeneratePom=true` 

* [EclipseJDT](https://github.com/maldil/JavaFyPy/tree/master/CustomizedEclipseJDT) 
  * Run `git clone https://github.com/maldil/JavaFyPy.git`
  * Run `cd  CustomizedEclipseJDT`
  * Follow the instructions in the [repository](https://github.com/maldil/JavaFyPy/tree/master/CustomizedEclipseJDT)  to build the project.  
  * Install the binaries to your local maven repository using `mvn install:install-file -Dfile= /You_Path/target/org.eclipse.jdt.core-3.24.0-SNAPSHOT.jar -DgroupId=org.eclipse.jdt -DartifactId=org.eclipse.jdt.core -Dversion=3.24.0-SNAPSHOT -Dpackaging=jar -DgeneratePom=true`

Once you complete installing the above dependencies, run `mvn clean package' to build the project.  

# How to use Py-RefactoringMiner as a maven dependency
Python-adapted RefactoringMiner is available in the [Maven Central Repository](https://mvnrepository.com/artifact/io.github.maldil/python-refactoring-miner).
In order to use RefactoringMiner as a maven dependency in your project, add the following snippet to your project's build configuration file:

	<dependency>
	    <groupId>io.github.maldil</groupId>
	    <artifactId>python-refactoring-miner</artifactId>
	    <version>1.0.6</version>
	</dependency>

# How to use Py-RefactoringMiner as a Docker container

**Step 1**: This [folder](https://drive.google.com/file/d/1mWy046yjHrywRUf_g_wklwiyGtb5Ggtn/view?usp=sharing) should be downloaded, unzipped, and saved to a directory, let's call the absolute path to the directory is $FOLDER PATH.  


**Step 2**:To download the docker images, execute the following command in your terminal - 
`docker pull malindadoo1/python_refactoring_miner:r13`. Once the download is completed, run the command `docker images` and make sure that the image `python_refactoring_miner` with tag `r13` is available.

**Step 3**: To start the docker container in interactive mode, execute the following command in your terminal - 
`docker run -v $FOLDER_PATH/ArtifactEvaluation:/user/local/rminer/ArtifactEvaluation -it malindadoo1/python_refactoring_miner:r13 /bin/bash`
You have to update the variable `$FOLDER_PATH` correctly. It should be the absolute path to the parent folder of the downloaded folder. We have to mount it to the docker container. The binaries in Docker containers will use the folder to read and write data related to refactoring inference. Once you execute the above command you will be entered to the docker container. 

**Step 4**- This step is to check whether the container is started correctly.  
Execute `python3 test_container.py` 
If this command prints the message, `You've done an excellent job mounting the folders` appears after running this command, you've successfully finished step 3. You can go to the next step now. If not, make sure the variable `$FOLDER PATH` is set to the absolute path of the download folder's parent folder.


**Step 5**- Let’s run the refactoring miner and extract some refactorings. First, use the command `pwd` to check whether your current working folder is `/user/local/rminer`. If not, you should first navigate back to the folder `/user/local/rminer`. Then, execute the following command
 `java -jar target/python-refactoring-miner-1.0.6.jar -dc` (Ignore the `log4j` warnings.)

The `Jar` file is preconfigured to read the file `$FOLDER PATH/ArtifactEvaluation/RefactoringMiner/repo_data.csv` which has the repository and commit hex of the commit that we want to extract refactorings. If you want to add more projects and hex you can edit the file and add more projects and commit hexes.  However, you must download inferred type information from the [type repository](https://github.com/mlcodepatterns/PythonTypeInformation) and add it to the subdirectory `TYPE_REPO` if you wish to analyze more commits and projects than the ones in `repo data.csv`.

**Step 6**- The step 2.4 extracts all the refactoring information to individual .json files to the folder '$FOLDER PATH/ArtifactEvaluation/RefactoringMiner/Refactoring'. Now we have to gather all this scattered information into one file. To do that, navigate inside the folder /user/local/rminer, execute the following command. 

'python3 conver_to_csv.py ./ArtifactEvaluation/RefactoringMiner/Refactoring/'

*Observation-1*
This will generate the file `$FOLDER_PATH/ArtifactEvaluation/RefactoringMiner/Refactoring/refactoring.csv` . This file is generated in the folder that you downloaded and mounted to the docker container. 

The file `refactoring.csv` contains a summary of all the refactoring of the commits specified in the file `/ArtifactEvaluation/RefactoringMiner/repo_data.csv`. 
This file described only a little information. Additional informations are available in the .json files in the subdirectories of  `$FOLDER_PATH/ArtifactEvaluation/RefactoringMiner/Refactoring`

**Step 3.6** - Execute exit to terminate the container. 


# Research
## How to cite Py-RefactoringMiner

If you are using RefactoringMiner in your research, please cite the following papers:


Malinda Dilhara, Ameya Ketkar, Nikhith Sannidhi, and Danny Dig, [Discovering Repetitive Code Changes in Python ML Systems](https://danny.cs.colorado.edu/papers/ICSE2022_Repetitive_Changes_in_Python_ML_Systems.pdf)," *44th International Conference on Software Engineering* (ICSE 2022), Pittsburgh, PA, USA, May 21--29, 2022.

    @inproceedings{Dilhara:ICSE:2022:RepetitiveChanges,
	author = {Dilhara, Malinda and Ketkar, Ameya and Sannidhi, Nikhith, Dig, Danny},
	title = {Discovering Repetitive Code Changes in Python ML Systems},
	booktitle = {Proceedings of the 44th International Conference on Software Engineering},
	series = {ICSE '22},
	year = {2022},
	isbn = {978-1-4503-9221-1/22/05},
	location = {PA, USA},
	numpages = {13},
	url = {http://doi.acm.org/10.1145/3510003.3510225},
	doi = {10.1145/3510003.3510225},
	publisher = {ACM},
	address = {New York, NY, USA},
    }


Do not foget to cite Java RefactoringMiner as well. 

Nikolaos Tsantalis, Matin Mansouri, Laleh Eshkevari, Davood Mazinanian, and Danny Dig, "[Accurate and Efficient Refactoring Detection in Commit History](https://users.encs.concordia.ca/~nikolaos/publications/ICSE_2018.pdf)," *40th International Conference on Software Engineering* (ICSE 2018), Gothenburg, Sweden, May 27 - June 3, 2018.

    @inproceedings{Tsantalis:ICSE:2018:RefactoringMiner,
	author = {Tsantalis, Nikolaos and Mansouri, Matin and Eshkevari, Laleh M. and Mazinanian, Davood and Dig, Danny},
	title = {Accurate and Efficient Refactoring Detection in Commit History},
	booktitle = {Proceedings of the 40th International Conference on Software Engineering},
	series = {ICSE '18},
	year = {2018},
	isbn = {978-1-4503-5638-1},
	location = {Gothenburg, Sweden},
	pages = {483--494},
	numpages = {12},
	url = {http://doi.acm.org/10.1145/3180155.3180206},
	doi = {10.1145/3180155.3180206},
	acmid = {3180206},
	publisher = {ACM},
	address = {New York, NY, USA},
	keywords = {Git, Oracle, abstract syntax tree, accuracy, commit, refactoring},
    }


# Contributors
The code in package **gr.uom.java.xmi.*** is developed by [Nikolaos Tsantalis](https://github.com/tsantalis).

The code in package **org.refactoringminer.*** was initially developed by [Danilo Ferreira e Silva](https://github.com/danilofes) and later extended by [Nikolaos Tsantalis](https://github.com/tsantalis).

Python extention of RefactoringMiner is developed by Malinda Dilhara.

# API usage guidelines
Please note that Py-RefactoringMiner uses Type inference to infer type information of Python program elements. We have already inferred the Type information of 1000 Python projects (for each commit) and uploaded it to [https://github.com/mlcodepatterns/PythonTypeInformation](https://github.com/mlcodepatterns/PythonTypeInformation). Please download the repository and update the variable `Configuration.TYPE_REPOSITORY` with the Path to the directory `TYPE_REPO` in the repository. If the repository doesn't already have the Type information of your project, you may use the steps mentioned in the [repository](https://github.com/mlcodepatterns/PythonTypeInformation) to infer type information. 
## With a locally cloned git repository
RefactoringMiner can automatically detect refactorings in the entire history of 
git repositories, between specified commits or tags, or at specified commits.

In the code snippet below we demonstrate how to print all refactorings performed
in the project NLTK https://github.com/nltk/nltk.


```java
        GitService gitService = new GitServiceImpl();
        GitHistoryRefactoringMiner miner = new GitHistoryRefactoringMinerImpl();
        Configuration.PROJECT_REPO = "/PROJECT_DOWNLOAD_PATH";
        Repository repo = gitService.cloneIfNotExists(
                Configuration.PROJECT_REPO+"nltk/nltk",
                "https://github.com/nltk/nltk.git");
        Configuration.TYPE_REPOSITORY = "../PATH_FOR_PythonTypeInformation/"; //clone Type Information from https://github.com/mlcodepatterns/PythonTypeInformation

        miner.detectAll(repo, repo.getBranch(), new RefactoringHandler() {
            @Override
            public void handle(String commitId, List<Refactoring> refactorings) {
                System.out.println("Refactorings at " + commitId);
                for (Refactoring ref : refactorings) {
                    System.out.println(ref.toString());
                }
            }
        });
    
```

You can also analyze between commits using `detectBetweenCommits` or between tags using `detectBetweenTags`. RefactoringMiner will iterate through all *non-merge* commits from **start** commit/tag to **end** commit/tag.

```java
// start commit: 819b202bfb09d4142dece04d4039f1708735019b
// end commit: d4bce13a443cf12da40a77c16c1e591f4f985b47
miner.detectBetweenCommits(repo, 
    "819b202bfb09d4142dece04d4039f1708735019b", "d4bce13a443cf12da40a77c16c1e591f4f985b47",
    new RefactoringHandler() {
  @Override
  public void handle(String commitId, List<Refactoring> refactorings) {
    System.out.println("Refactorings at " + commitId);
    for (Refactoring ref : refactorings) {
      System.out.println(ref.toString());
    }
  }
});
```

```java
// start tag: 1.0
// end tag: 1.1
miner.detectBetweenTags(repo, "1.0", "1.1", new RefactoringHandler() {
  @Override
  public void handle(String commitId, List<Refactoring> refactorings) {
    System.out.println("Refactorings at " + commitId);
    for (Refactoring ref : refactorings) {
      System.out.println(ref.toString());
    }
  }
});
```

It is possible to analyze a specifc commit using `detectAtCommit` instead of `detectAll`. The commit
is identified by its SHA key, such as in the example below:

```java
miner.detectAtCommit(repo, "05c1e773878bbacae64112f70964f4f2f7944398", new RefactoringHandler() {
  @Override
  public void handle(String commitId, List<Refactoring> refactorings) {
    System.out.println("Refactorings at " + commitId);
    for (Refactoring ref : refactorings) {
      System.out.println(ref.toString());
    }
  }
});
```

# Location information for the detected refactorings

All classes implementing the `Refactoring` interface include refactoring-specific location information.
For example, `ExtractOperationRefactoring` offers the following methods:

1. `getSourceOperationCodeRangeBeforeExtraction()` : Returns the code range of the source method in the **parent** commit
2. `getSourceOperationCodeRangeAfterExtraction()` : Returns the code range of the source method in the **child** commit
3. `getExtractedOperationCodeRange()` : Returns the code range of the extracted method in the **child** commit
4. `getExtractedCodeRangeFromSourceOperation()` : Returns the code range of the extracted code fragment from the source method in the **parent** commit
5. `getExtractedCodeRangeToExtractedOperation()` : Returns the code range of the extracted code fragment to the extracted method in the **child** commit
6. `getExtractedOperationInvocationCodeRange()` : Returns the code range of the invocation to the extracted method inside the source method in the **child** commit

Each method returns a `CodeRange` object including the following properties:
```java
String filePath
int pythonStartLine
int endLine
int startColumn
int endColumn
```
Alternatively, you can use the methods `List<CodeRange> leftSide()` and `List<CodeRange> rightSide()` to get a list of `CodeRange` objects for the left side (i.e., **parent** commit) and right side (i.e., **child** commit) of the refactoring, respectively.
