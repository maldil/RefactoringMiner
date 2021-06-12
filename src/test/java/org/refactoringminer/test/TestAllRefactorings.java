package  org.refactoringminer.test;


import org.eclipse.jgit.lib.Repository;
import refactoringminer.Configuration;
import refactoringminer.api.GitHistoryRefactoringMiner;
import refactoringminer.api.GitService;
import refactoringminer.api.Refactoring;
import refactoringminer.api.RefactoringHandler;
import  refactoringminer.rm1.GitHistoryRefactoringMinerImpl;
import org.junit.Test;
import refactoringminer.util.GitServiceImpl;

import java.util.List;


public class TestAllRefactorings {

	@Test
	public void testAllRefactorings() throws Exception {
//		GitHistoryRefactoringMinerImpl detector = new GitHistoryRefactoringMinerImpl();
//		TestBuilder test = new TestBuilder(detector, "tmp1", RefactoringPopulator.Refactorings.All.getValue());
//		RefactoringPopulator.feedRefactoringsInstances(RefactoringPopulator.Refactorings.All.getValue(), RefactoringPopulator.Systems.FSE.getValue(), test);
//		test.assertExpectations(10458, 36, 389);
	}

	@Test
	public void testAllPythonRefactorings() throws Exception {
		GitService gitService = new GitServiceImpl();
		GitHistoryRefactoringMiner miner = new GitHistoryRefactoringMinerImpl();

		Repository repo = gitService.cloneIfNotExists(
				Configuration.PROJECT_REPO+"maldil/MLEditsTest",
				"https://github.com/maldil/MLEditsTest.git");







		miner.detectAll(repo, repo.getBranch(), new RefactoringHandler() {
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
