package learner;

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

import java.util.ArrayList;
import java.util.HashSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import learner.AutomataDeterminismException;
import learner.DeterministicFrequencyFiniteAutomata;
import learner.State;

public class DeterministicFrequencyFiniteAutomataTest {

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

  private State toState(String input){
    ArrayList<String> ret =new ArrayList<String>();
    for(int i=0;i<input.length();i++){
      if(input.charAt(i)=='a'){
        ret.add("1,1,1,1,1,1,1,1,1,1,1,2,2");
      }else{
        ret.add("1,1,1,1,1,1,1,1,1,1,1,1,1");
      }
    }
    return new State(ret);
  }

  @Test
  public void testFFA() throws AutomataDeterminismException {
    HashSet<String> alphabet=new HashSet<String>();
    alphabet.add("1,1,1,1,1,1,1,1,1,1,1,2,2");
    alphabet.add("1,1,1,1,1,1,1,1,1,1,1,1,1");
    DeterministicFrequencyFiniteAutomata a=new DeterministicFrequencyFiniteAutomata(alphabet);
    a.setStateFrequency(toState(""), 10);
    a.setTransition(toState(""), "1,1,1,1,1,1,1,1,1,1,1,2,2", 
        5, toState("a"));
    a.setStateFrequency(toState("a"), 5);
    a.setTransition(toState(""), "1,1,1,1,1,1,1,1,1,1,1,1,1", 
        3, toState("b"));
    a.setStateFrequency(toState("b"), 3);
    a.setTransition(toState("a"), "1,1,1,1,1,1,1,1,1,1,1,2,2", 
        3, toState("aa"));
    a.setStateFrequency(toState("aa"), 3);

  }

}
