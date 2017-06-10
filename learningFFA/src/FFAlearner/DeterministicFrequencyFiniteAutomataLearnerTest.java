package FFAlearner;

/**
*
* Copyright (c) 2017 Yu-Fang Chen
* 
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
* 
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
* 
*/

import static org.junit.Assert.*;

import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DeterministicFrequencyFiniteAutomataLearnerTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	private String getAlergiaResultForkIterations(DeterministicFrequencyFiniteAutomata fpta,int k) throws AutomataDeterminismException{
		DeterministicFrequencyFiniteAutomataLearner learner=new DeterministicFrequencyFiniteAutomataLearner(fpta, 30, 0.1f);

		for(int i=0;i<k;i++){
			State qb=learner.getNextBlueState();
			learner.mergeOrBecomeRed(qb);
			learner.updateBlueStates();
			
		}
		DeterministicFrequencyFiniteAutomata result=learner.getFFA();
		DeterministicFrequencyFiniteAutomataPrinter printer=new DeterministicFrequencyFiniteAutomataPrinter(result);	
		return printer.toString();
	}
	
	@Test
	public void LearnerTest1() throws AutomataDeterminismException {
		SetToTreeShapedFFA test=new SetToTreeShapedFFA();
		test.useDefaultRawdata();
		DeterministicFrequencyFiniteAutomata fpta=test.generateFPTA();
		String result=getAlergiaResultForkIterations(fpta,1);
		assertTrue(result.hashCode()==-298444604);
	}

	@Test
	public void LearnerTest2() throws AutomataDeterminismException {
		SetToTreeShapedFFA test=new SetToTreeShapedFFA();
		test.useDefaultRawdata();
		DeterministicFrequencyFiniteAutomata fpta=test.generateFPTA();
		String result=getAlergiaResultForkIterations(fpta,2);
		assertTrue(result.hashCode()==-298444604);
	}
	
	@Test
	public void LearnerTest3() throws AutomataDeterminismException {
		SetToTreeShapedFFA test=new SetToTreeShapedFFA();
		test.useDefaultRawdata();
		DeterministicFrequencyFiniteAutomata fpta=test.generateFPTA();
		String result=getAlergiaResultForkIterations(fpta,3);
		assertTrue(result.hashCode()==1142926983);
	}
	
	@Test
	public void LearnerTest4() throws AutomataDeterminismException {
		SetToTreeShapedFFA test=new SetToTreeShapedFFA();
		test.useDefaultRawdata();
		DeterministicFrequencyFiniteAutomata fpta=test.generateFPTA();
		String result=getAlergiaResultForkIterations(fpta,4);
		assertTrue(result.hashCode()==1217289335);
	}	

	@Test
	public void LearnerTest5() throws AutomataDeterminismException {
		SetToTreeShapedFFA test=new SetToTreeShapedFFA();
		test.useDefaultRawdata();
		DeterministicFrequencyFiniteAutomata fpta=test.generateFPTA();
		DeterministicFrequencyFiniteAutomataLearner learner=new DeterministicFrequencyFiniteAutomataLearner(fpta, 30, 0.1f);
		learner.runAlergia();
		DeterministicFrequencyFiniteAutomata result=learner.getFFA();
		DeterministicFrequencyFiniteAutomataPrinter printer=new DeterministicFrequencyFiniteAutomataPrinter(result);	
		String ret=printer.toString();
		assertTrue(ret.hashCode()==1217289335);
	}
	
	@Test	
	public void LearnerTest6() throws AutomataDeterminismException {
		try {
			SetToTreeShapedFFA test=new SetToTreeShapedFFA();
			test.fromFile("/Users/yfc/Documents/workspace/learningFFA/result.txt");
			DeterministicFrequencyFiniteAutomata fpta=test.generateFPTA();
			DeterministicFrequencyFiniteAutomataLearner learner=new DeterministicFrequencyFiniteAutomataLearner(fpta, 25, 0.2f);
			learner.runAlergia();
			DeterministicFrequencyFiniteAutomata result=learner.getFFA();
			DeterministicFrequencyFiniteAutomataPrinter printer=new DeterministicFrequencyFiniteAutomataPrinter(result);
			printer.printDTMCinPrism();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
}
