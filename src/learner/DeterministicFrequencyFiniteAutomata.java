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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;

public class DeterministicFrequencyFiniteAutomata {
  HashMap<State, DeterministicTransitionMap> transitions;
  HashMap<State, TransitionMap> reverseTransitions;
  HashMap<State,Integer> stateFrequency;
  Set<String> alphabet;
  State initial=State.initial();


  public State getInitialState(){
    return initial;
  }

  public void setInitialState(State state){
    initial=state;
  }

  public DeterministicFrequencyFiniteAutomata(Set<String> alphabet){
    transitions = new HashMap<State,DeterministicTransitionMap>();
    reverseTransitions = new HashMap<State,TransitionMap>();
    stateFrequency = new HashMap<State,Integer>();
    this.alphabet = alphabet;
  }

  public Set<StateAndFrequency> getIncomingTransition(State destination, String symbol){
    return getStateAndFrequency(reverseTransitions.get(destination), symbol);
  }

  public Optional<StateAndFrequency> getOutgoingTransition(State source, String symbol){
    if(transitions.containsKey(source)){
      return transitions.get(source).get(symbol);
    }else
      return Optional.empty();
  }	

  private Set<StateAndFrequency> getStateAndFrequency(TransitionMap transitionToDestination, String symbol) {
    if(transitionToDestination==null){
      return new HashSet<StateAndFrequency>();
    }else{
      return transitionToDestination.get(symbol);
    }	
  }

  public void setTransition(State source, String symbol, int frequency, State destination) throws AutomataDeterminismException{
    StateAndFrequency forwardStateAndFrequency =new StateAndFrequency(destination,frequency);
    StateAndFrequency reverseStateAndFrequency =new StateAndFrequency(source,frequency);

    ensureReverseTransitionsHasDestinationState(destination);
    ensureNoTransitionFromSourceToOtherDestinationViaSymbol(source, symbol, destination);

    updateForwardTransition(source,symbol,forwardStateAndFrequency);
    updateReverseTransition(reverseTransitions.get(destination),symbol,reverseStateAndFrequency);
  }

  private void ensureNoTransitionFromSourceToOtherDestinationViaSymbol(State source, String symbol, State destination) throws AutomataDeterminismException {
    DeterministicTransitionMap dtm=transitions.get(source);
    if(dtm!=null){
      Optional<StateAndFrequency> stateAndFrequencyOpt=dtm.get(symbol);
      if(stateAndFrequencyOpt.isPresent() && 
          !stateAndFrequencyOpt.get().state.equals(destination)){
        throw new AutomataDeterminismException(source, symbol);
      }
    }	
  }

  public void removeState(State source){
    stateFrequency.remove(source);
    for(String symbol:alphabet){
      removeTransition(source,symbol);
    }

  }

  public void removeTransition(State source, String symbol) {
    if(transitions.containsKey(source)){
      DeterministicTransitionMap dtm=transitions.get(source);
      if(dtm!=null){
        Optional<StateAndFrequency> stateAndFrequencyOpt=dtm.get(symbol);
        if(stateAndFrequencyOpt.isPresent()){
          State destination=stateAndFrequencyOpt.get().state;
          dtm.remove(symbol);
          TransitionMap tm= reverseTransitions.get(destination);
          tm.remove(symbol, source);
          if(tm.size()==0)
            reverseTransitions.remove(destination);
        }
      }	
    }
  }

  private void updateForwardTransition(State source, String symbol,
      StateAndFrequency stateAndFrequency) {
    if(!transitions.containsKey(source)){
      transitions.put(source, new DeterministicTransitionMap());
    }
    DeterministicTransitionMap dtm=transitions.get(source);
    dtm.set(symbol, stateAndFrequency.state, stateAndFrequency.frequency);
  }

  private void updateReverseTransition(TransitionMap transitionMap, String symbol,
      StateAndFrequency stateAndFrequency) {
    transitionMap.set(symbol, stateAndFrequency.state, stateAndFrequency.frequency);
  }


  private void ensureReverseTransitionsHasDestinationState(State destination) {
    if(reverseTransitions.get(destination)==null){
      reverseTransitions.put(destination, new TransitionMap());
    }
  }

  public void setStateFrequency(State state, int freq){
    stateFrequency.put(state, freq);
  }
  public Integer getStateFrequency(State state){
    return stateFrequency.getOrDefault(state,0);
  }

  public boolean hasState(State state){
    return stateFrequency.containsKey(state);
  }

  public Set<String> getAlphabet(){
    return alphabet;
  }

  public int getNumberOfStates(){
    return stateFrequency.keySet().size();
  }

  public String toString(){
    DeterministicFrequencyFiniteAutomataPrinter printer=new DeterministicFrequencyFiniteAutomataPrinter(this);	
    return printer.toString();		
  }

  public boolean isTreeShaped(){
    Stack<State> workList = new Stack<State>();
    HashSet<State> processed = new HashSet<State>();
    workList.push(State.initial());
    processed.add(State.initial());
    while(!workList.isEmpty()){
      State cur=workList.pop();
      for(String symbol:getAlphabet()){
        Optional<StateAndFrequency> stateAndFrequency=getOutgoingTransition(cur, symbol);
        if(stateAndFrequency.isPresent()){
          State next=stateAndFrequency.get().state;
          if(!cur.extend(symbol).equals(next)){
            return false;
          }
          if(processed.contains(next)){
            processed.add(next);
            workList.push(next);
          }					
        }
      }
    }
    return true;
  }
}
