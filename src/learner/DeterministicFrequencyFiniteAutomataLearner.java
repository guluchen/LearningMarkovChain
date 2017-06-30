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
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

public class DeterministicFrequencyFiniteAutomataLearner {
  DeterministicFrequencyFiniteAutomata ffa;
  Queue<State> red,blue;
  int t0;
  float alpha;
  public DeterministicFrequencyFiniteAutomataLearner(DeterministicFrequencyFiniteAutomata fpta,int t0, float alpha){
    this.t0=t0;
    this.alpha=alpha;
    ffa=fpta;
    red=new LinkedList<State>();
    blue=new LinkedList<State>();

    red.add(State.initial());
    for(String c:ffa.getAlphabet()){
      ArrayList<String> qa=new ArrayList<String>();
      qa.add(c);
      if(ffa.getStateFrequency(new State(qa))!=null){
        blue.add(new State(qa));
      }
    }
  }

  public DeterministicFrequencyFiniteAutomata runAlergia() throws AutomataDeterminismException{
    while(true){
      State qb=getNextBlueState();
      if(qb==null)
        return ffa;
      mergeOrBecomeRed(qb);
      updateBlueStates();

    }
  }

  public State getNextBlueState() {
    while(!blue.isEmpty()){
      State qb=blue.remove();
      final int freq_qb=ffa.getStateFrequency(qb)+getOutgoingTransitionFrequency(qb);
      if(freq_qb>=t0)
        return qb;
    }
    return null;
  }

  public void mergeOrBecomeRed(State qb) throws AutomataDeterminismException {
    boolean compatiblePairExist=false;
    for(State qr:red){
      if(alergiaCompatible(qr,qb)){
        compatiblePairExist=true;
        stochasticMerge(qr,qb);
        break;
      }
    }
    if(!compatiblePairExist)
      red.add(qb);
  }

  public void updateBlueStates() {
    blue.clear();
    for(State qu:red){
      for(String a:ffa.getAlphabet()){
        State qua=qu.extend(a);
        if(ffa.hasState(qua) && !red.contains(qua))
          blue.add(qua);
      }
    }
  }

  private void stochasticMerge(State qr, State qb) throws AutomataDeterminismException {
    for(String symbol:ffa.getAlphabet()){
      for(StateAndFrequency stateAndFrequency:ffa.getIncomingTransition(qb, symbol)){
        State source=stateAndFrequency.state;
        int frequency=stateAndFrequency.frequency;
        ffa.removeTransition(source, symbol);
        ffa.setTransition(source, symbol, frequency, qr);
      }
    }
    stochasticFlod(qr,qb);
  }

  private void stochasticFlod(State q1, State q2) throws AutomataDeterminismException {
    ffa.setStateFrequency(q1, ffa.getStateFrequency(q1)+ffa.getStateFrequency(q2));

    for(String symbol:ffa.getAlphabet()){
      Optional<StateAndFrequency> q1NextOpt=ffa.getOutgoingTransition(q1, symbol);
      Optional<StateAndFrequency> q2NextOpt=ffa.getOutgoingTransition(q2, symbol);

      if(q2NextOpt.isPresent()){
        int q2NextFrequency=q2NextOpt.get().frequency;
        State q2Destination=q2NextOpt.get().state;

        if(q1NextOpt.isPresent()){
          int q1NextFrequency=q1NextOpt.get().frequency;
          State q1Destination=q1NextOpt.get().state;

          ffa.setTransition(q1, symbol, q1NextFrequency+q2NextFrequency, q1Destination);
          stochasticFlod(q1Destination,q2Destination);
        }else{
          assert !q2Destination.equals(q2):"blue state should not have selfloop";
          ffa.setTransition(q1, symbol, q2NextFrequency, q2Destination);
        }
      }
    }
    ffa.removeState(q2);
  }

  private int getOutgoingTransitionFrequency(State cur) {
    int count=0;
    for(String c:ffa.getAlphabet()){
      count+=StateAndFrequency.getFrequency(ffa.getOutgoingTransition(cur,c));
    }
    return count;
  }

  private boolean alergiaCompatible(State qr, State qb) {
    final int freq_qu=ffa.getStateFrequency(qr)+getOutgoingTransitionFrequency(qr);
    final int freq_qv=ffa.getStateFrequency(qb)+getOutgoingTransitionFrequency(qb);

    if(!alergiaTest(ffa.getStateFrequency(qr),freq_qu,ffa.getStateFrequency(qb),freq_qv)){
      return false;
    }
    for(final String symbol:ffa.getAlphabet()){
      if(!alergiaTest(StateAndFrequency.getFrequency(ffa.getOutgoingTransition(qr, symbol)),freq_qu,
          StateAndFrequency.getFrequency(ffa.getOutgoingTransition(qb, symbol)),freq_qv)){
        return false;
      }
    }

    return true;
  }

  private boolean alergiaTest(float f1, float n1, float f2, float n2) {
    float gamma= Math.abs(f1/n1-f2/n2);
    return (gamma<(Math.sqrt(1/n1)+Math.sqrt(1/n2))*Math.sqrt(0.5*Math.log(2/alpha)));
  }


  public DeterministicFrequencyFiniteAutomata getFFA(){
    return ffa;
  }

}
