package  refactoringminer.python;

import io.vavr.control.Try;
import refactoringminer.Configuration;
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
import  refactoringminer.api.RefactoringHandler;
import  refactoringminer.rm1.GitHistoryRefactoringMinerImpl;
import  refactoringminer.util.GitServiceImpl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class GetPythonRefactoring {
    private String projectGitHubName;
    private Repository repository;
    List<String> goodCommits;
    public GetPythonRefactoring(String GitRepo, String PythonRepo, String projectName) throws Exception {
        Configuration.PROJECT_REPO = GitRepo;
        Configuration.TYPE_REPOSITORY = PythonRepo;
        projectGitHubName=projectName;
        GitService gitService = new GitServiceImpl();
        repository = gitService.cloneIfNotExists(
                Configuration.PROJECT_REPO + projectGitHubName,
                GitRepo);
        goodCommits=getCommitsForAnalysis(repository,projectGitHubName);

    }

    public List<Refactoring> getPythonRefactoringInCommit(String commitHex) throws Exception {
//        GitService gitService = new GitServiceImpl();
        GitHistoryRefactoringMiner miner = new GitHistoryRefactoringMinerImpl();
////        String projectName = projectUrl.split("/")[projectUrl.split("/").length-2]+ "/"+
////                projectUrl.split("/")[projectUrl.split("/").length - 1].split(".git")[0];
//
//        Repository repo = gitService.cloneIfNotExists(
//                Configuration.PROJECT_REPO + projectGitHubName,
//                projectUrl);
        List<Refactoring> refs =  new ArrayList<>();
        if (goodCommits.contains(commitHex)){
            miner.detectAtCommit(repository, commitHex, new RefactoringHandler() {
                @Override
                public void handle(String commitId, List<Refactoring> refactorings) {
                    refs.addAll(refactorings);
                }
            });
        }
        return refs;
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
}
