package refactoringminer;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import gr.uom.java.xmi.diff.CodeRange;
import io.vavr.control.Try;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevSort;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.eclipse.jgit.treewalk.filter.PathSuffixFilter;
import org.eclipse.jgit.util.io.NullOutputStream;
import  refactoringminer.api.GitHistoryRefactoringMiner;
import  refactoringminer.api.GitService;
import  refactoringminer.api.Refactoring;

import refactoringminer.api.RefactoringHandler;
import  refactoringminer.rm1.GitHistoryRefactoringMinerImpl;
import  refactoringminer.util.GitServiceImpl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class TestPython {
    public static void main(String[] args) throws Exception {
        String content = readStringFromFile("selected-repos.csv");
        assert content != null;
        Scanner sc = new Scanner(content);
        while (sc.hasNextLine()) {
            analyseProject(sc.nextLine());
        }

    }
    public static String readStringFromFile(String inputFile) {
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(inputFile));
            byte[] bytes = new byte[(int) new File(inputFile).length()];
            in.read(bytes);
            in.close();
            return new String(bytes);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void analyseProject (String url) throws Exception{
        GitService gitService = new GitServiceImpl();
        GitHistoryRefactoringMiner miner = new GitHistoryRefactoringMinerImpl();


        String projectName = url.split("/")[url.split("/").length-2]+ "/"+
                url.split("/")[url.split("/").length - 1].split(".git")[0];

        String folder = Configuration.PROJECT_REPO + projectName;

        Repository repo = gitService.cloneIfNotExists(folder,url);

        List<String> lists = readExistingRefactoringDetections();

        CSVWriter writer = new CSVWriter(new FileWriter("../generations/refactorings.csv",true));

        List<String> commits= getCommitsForAnalysis(repo,projectName);

        System.out.println(commits.size()+"Commits are analysable");

        miner.detectAll(repo, repo.getBranch(), new RefactoringHandler() {
            @Override
            public void handle(String commitId, List<Refactoring> refactorings) {
                System.out.println("Refactorings at " + commitId);
                for (Refactoring ref : refactorings) {
                    int python_line = 0;
                    String pythonFile="";

                    for (CodeRange change : ref.leftSide()) {
                        if (python_line<change.getPythonStartLine()) {
                            python_line = change.getPythonStartLine();
                            pythonFile = change.getFilePath();

                        }
                    }
                    byte[] fileCode=null;
                    try {
                        byte[] bytesOfMessage = pythonFile.getBytes("UTF-8");
                        MessageDigest md = MessageDigest.getInstance("SHA-256");
                        fileCode = md.digest(bytesOfMessage);
                    } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {

                    }

                    String md5s = pythonFile;
                    if (fileCode != null) {
                        StringBuffer sbDigest = new StringBuffer();
                        for (int i = 0; i < fileCode.length; ++i)
                            sbDigest.append(Integer.toHexString((fileCode[i] & 0xFF) | 0x100).substring(1,3));
                        md5s = sbDigest.toString();
                    }
                    String[] data1 = { projectName,ref.getRefactoringType().getDisplayName(),"",
                            "https://github.com/"+projectName+"/commit/"+commitId+"#diff-"+md5s + "L"+python_line ,ref.toString(),"https://github.com/"+projectName+"/commit/"+commitId , pythonFile, commitId };
                    writer.writeNext(data1);

                    try {
                        writer.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println(ref.toJSON());
                    System.out.println("+++++++++++++++++++");
                }
            }

            @Override
            public boolean skipCommit(String commitId) {

                if (lists.contains("https://github.com/"+projectName+"/commit/"+commitId)) {
                    System.out.println("skipped ");
                    return true;
                }
                if (!commits.contains(commitId)){
                    System.out.println("skipped ");
                    return true;
                }
                return false;
            }


        });
    }


    public static List<String>  getCommitsForAnalysis(Repository repo,String projectName ){
        List<String> finals= new ArrayList<>();
        RevWalk rw = new RevWalk(repo);
        for (RevCommit commit : getCommits(repo, RevSort.REVERSE)) {
            RevCommit parent = null;
            if (commit.getParentCount() > 0) {
                try {
                    parent = rw.parseCommit(commit.getParent(0).getId());
                } catch (IOException e) {
                    continue;
                }
                DiffFormatter df = new DiffFormatter(NullOutputStream.INSTANCE);
                df.setRepository(repo);
                df.setDiffComparator(RawTextComparator.DEFAULT);
                df.setDetectRenames(true);
                df.setPathFilter(PathSuffixFilter.create(".py"));

                List<DiffEntry> diffs;
                try {
                    diffs = df.scan(parent.getTree(), commit.getTree());
                } catch (IOException e) {
                    continue;
                }
                if (diffs.isEmpty()) {
                    continue;
                }
                boolean allTypesAvailable = true;
                for (DiffEntry diff : diffs) {
                    if (diff.getChangeType() == DiffEntry.ChangeType.MODIFY &&
                            (!isTypeInfoFileAvailable(Configuration.TYPE_REPOSITORY, projectName, commit.getName(), "N", diff.getNewPath().replace('/', '_')) ||
                                    !isTypeInfoFileAvailable(Configuration.TYPE_REPOSITORY, projectName, commit.getName(), "O", diff.getOldPath().replace('/', '_')))) {
                        allTypesAvailable = false;
                        break;
                    }
                }
                if (allTypesAvailable)
                    finals.add(commit.getName());

            }
        }
        rw.close();
        return finals;
    }

    public static boolean isTypeInfoFileAvailable(String fileSource , String projectName, String commitHex, String postfix, String fileName){
        String json_file = fileSource +"/" +projectName+"/"+ commitHex +   postfix +"/" +  fileName.substring(0,fileName.length()-3) +".json";
        File f = new File(json_file);
        if (f.exists())
            return true;
        return false;
    }

    public static List<RevCommit> getCommits(Repository git, RevSort order) {
        List<RevCommit> commits = new ArrayList<>(Try.of(() -> {
            RevWalk walk = new RevWalk(git);
            walk.markStart(walk.parseCommit(git.resolve(Constants.HEAD)));
            walk.sort(order);
            walk.setRevFilter(RevFilter.NO_MERGES);
            return walk;
        })
                .map(walk -> {
                    Iterator<RevCommit> iter = walk.iterator();
                    List<RevCommit> l = new ArrayList<>();
                    while (iter.hasNext()) {
                        l.add(iter.next());
                    }
                    walk.dispose();
                    return l;
                })
                .onSuccess(l -> System.out.println(l.size() + " number of commits found for " + git.getDirectory().getParentFile().getName()))
                .onFailure(Throwable::printStackTrace)
                .getOrElse(new ArrayList<>()));
        Collections.reverse(commits);
        return commits;
    }

    public static List<String> readExistingRefactoringDetections(){
        List<String> records = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader("../generations/refactorings.csv"));) {
            String[] values = null;
            while ((values = csvReader.readNext()) != null) {
                records.add(values[5]);
            }
        } catch (CsvValidationException | IOException e) {
            e.printStackTrace();
        }
        return records;
    }

}
