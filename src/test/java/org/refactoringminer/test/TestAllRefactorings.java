package  org.refactoringminer.test;


import  refactoringminer.rm1.GitHistoryRefactoringMinerImpl;
import org.junit.Test;


public class TestAllRefactorings {

	@Test
	public void testAllRefactorings() throws Exception {
		GitHistoryRefactoringMinerImpl detector = new GitHistoryRefactoringMinerImpl();
		TestBuilder test = new TestBuilder(detector, "tmp1", RefactoringPopulator.Refactorings.All.getValue());
		RefactoringPopulator.feedRefactoringsInstances(RefactoringPopulator.Refactorings.All.getValue(), RefactoringPopulator.Systems.FSE.getValue(), test);
		test.assertExpectations(9153, 38, 396);
	}
}
