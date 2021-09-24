
Table of Contents
=================
   * [General info](#general-info)
   * [How to build RefactoringMiner](#how-to-build-refactoringminer)
   * [How to use RefactoringMiner as a maven dependency](#how-to-use-refactoringminer-as-a-maven-dependency)
   * [Research](#research)
      * [How to cite RefactoringMiner](#how-to-cite-refactoringminer)
   * [Contributors](#contributors)
   * [API usage guidelines](#api-usage-guidelines)
      * [With a locally cloned git repository](#with-a-locally-cloned-git-repository)
   * [Location information for the detected refactorings](#location-information-for-the-detected-refactorings)

# General info
[RefactoringMiner](https://github.com/tsantalis/RefactoringMiner) (developed by Nikolaos Tsantali et al.) is a Java library that can detects refactorings applied in the commit history of a Java project. We extend the RefactoringMiner to Python. Now, you can use *Python-adapted RefactoringMiner* to detect refactorings applied in Python projects.


Technically, it should support all the refactoring detected by the original RefacotringMiner (please refer [this](https://github.com/tsantalis/RefactoringMiner#general-info)). However, we manually valiaded only 19 kinds of refacotrings. All the validation results are available in our [website]().


# How to use Python-adapted RefactoringMiner as a maven dependency
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.maldil/python-refactoring-miner/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.maldil/python-refactoring-miner)
Python-adapted RefactoringMiner is available in the [Maven Central Repository](https://mvnrepository.com/artifact/io.github.maldil/python-refactoring-miner).
In order to use RefactoringMiner as a maven dependency in your project, add the following snippet to your project's build configuration file:

	<dependency>
	    <groupId>io.github.maldil</groupId>
	    <artifactId>python-refactoring-miner</artifactId>
	    <version>1.0.6</version>
	</dependency>

# How to build Python-adapted RefactoringMiner
Building Python-adapted RefactoringMiner could be slidly complex due to Eclipse-JDT paser. You have to build the CustomizedEclipseJDT in [JavaFyPy
](https://github.com/maldil/JavaFyPy) locally and install it to the local Maven repository before building RefactoringMiner. 

# Research
## How to cite RefactoringMiner

If you are using RefactoringMiner in your research, please cite the following papers:

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

Nikolaos Tsantalis, Ameya Ketkar, and Danny Dig, "[RefactoringMiner 2.0](https://users.encs.concordia.ca/~nikolaos/publications/TSE_2020.pdf)," *IEEE Transactions on Software Engineering*, 2020.

    @article{Tsantalis:TSE:2020:RefactoringMiner2.0,
	author = {Tsantalis, Nikolaos and Ketkar, Ameya and Dig, Danny},
	title = {RefactoringMiner 2.0},
	journal = {IEEE Transactions on Software Engineering},
	year = {2020},
	numpages = {21},
	doi = {10.1109/TSE.2020.3007722},
    }

# Contributors
The code in package **gr.uom.java.xmi.*** is developed by [Nikolaos Tsantalis](https://github.com/tsantalis).

The code in package **org.refactoringminer.*** was initially developed by [Danilo Ferreira e Silva](https://github.com/danilofes) and later extended by [Nikolaos Tsantalis](https://github.com/tsantalis).

Python extention of RefactoringMiner is developed by MalDil.

# API usage guidelines
Please note that Python-adapted RefactoringMiner uses Type inference to infer type information of Python program elements. We have already inferred the Type information of 1000 Python projects (for each commit) and uploaded it to [https://github.com/maldil/PythonTypeInformation](https://github.com/maldil/PythonTypeInformation). Please download the repository and update the variable `Configuration.TYPE_REPOSITORY` with the Path to the repository. If the repository doesn't already have the Type information of your project, you may use the steps mentioned in the [repository](https://github.com/maldil/PythonTypeInformation]) to infer type information. 
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
        Configuration.TYPE_REPOSITORY = "../PATH_FOR_PythonTypeInformation/"; //clone Type Information from https://github.com/maldil/PythonTypeInformation

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
