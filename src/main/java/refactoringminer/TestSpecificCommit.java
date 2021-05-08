package refactoringminer;

import org.eclipse.jgit.lib.Repository;
import  refactoringminer.api.GitHistoryRefactoringMiner;
import  refactoringminer.api.GitService;
import  refactoringminer.api.Refactoring;
import  refactoringminer.api.RefactoringHandler;
import  refactoringminer.rm1.GitHistoryRefactoringMinerImpl;
import  refactoringminer.util.GitServiceImpl;

import java.util.List;

public class TestSpecificCommit {
    public static void main(String[] args) throws Exception {

        GitService gitService = new GitServiceImpl();
        GitHistoryRefactoringMiner miner = new GitHistoryRefactoringMinerImpl();

        String url = "/IDSIA/sacred/";
        String projectName = url.split("/")[url.split("/").length-2]+ "/"+
                url.split("/")[url.split("/").length - 1].split(".git")[0];

        Repository repo = gitService.cloneIfNotExists(
                Configuration.PROJECT_REPO + projectName,
                url);
        miner.detectAtCommit(repo, "6923d114ef24951f991eb90cbfc8a28dae0f111d", new RefactoringHandler() {
            @Override
            public void handle(String commitId, List<Refactoring> refactorings) {
                System.out.println("Refactorings at " + commitId);
                for (Refactoring ref : refactorings) {
                    System.out.println(ref.toString());
                }
            }
        });
    }
}
