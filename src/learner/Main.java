package learner;

import java.io.IOException;

public class Main {

  public static void main(String[] args) throws IOException, AutomataDeterminismException {
    //The main function demonstrate how to use this library

    SetToTreeShapedFFA test=new SetToTreeShapedFFA();
    test.fromFile("./result.txt");
    DeterministicFrequencyFiniteAutomata fpta=test.generateFPTA();
    DeterministicFrequencyFiniteAutomataLearner learner=new DeterministicFrequencyFiniteAutomataLearner(fpta, 25, 0.2f);
    learner.runAlergia();
    DeterministicFrequencyFiniteAutomata result=learner.getFFA();
    DeterministicFrequencyFiniteAutomataPrinter printer=new DeterministicFrequencyFiniteAutomataPrinter(result);
    printer.printDTMCinPrism();

  }

}
