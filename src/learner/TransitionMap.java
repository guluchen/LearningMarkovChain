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
import java.util.Set;

public class TransitionMap {

  private HashMap<String, HashMap<State, Integer>> symbolToStateAndFrequencyMap ;

  public TransitionMap(){
    symbolToStateAndFrequencyMap = new HashMap<String, HashMap<State, Integer>>();
  };

  public void set(String symbol, State state, int frequency){
    if(!symbolToStateAndFrequencyMap.containsKey(symbol)){
      symbolToStateAndFrequencyMap.put(symbol, new HashMap<State, Integer>());
    }
    HashMap<State, Integer> stateFrequencyMap=symbolToStateAndFrequencyMap.get(symbol);
    stateFrequencyMap.put(state,frequency);
  }	

  public Set<StateAndFrequency> get(String symbol){
    HashMap<State, Integer> stateFrequencyMap=symbolToStateAndFrequencyMap.get(symbol);
    Set<StateAndFrequency> ret=new HashSet<StateAndFrequency>();
    if(stateFrequencyMap!=null){
      for(State state:stateFrequencyMap.keySet()){
        int frequency=stateFrequencyMap.get(state);
        ret.add(new StateAndFrequency(state,frequency));
      }
    }
    return ret;
  }

  public void remove(String symbol, State source){
    if(symbolToStateAndFrequencyMap.get(symbol)!=null){
      symbolToStateAndFrequencyMap.get(symbol).remove(source);
      if(symbolToStateAndFrequencyMap.get(symbol).size()==0){
        symbolToStateAndFrequencyMap.remove(symbol);
      }
    }
  }

  public String toString(){
    String ret="[";
    for(String symbol:symbolToStateAndFrequencyMap.keySet()){
      HashMap<State, Integer> stateToFrequencyMap=
          symbolToStateAndFrequencyMap.get(symbol);
      if(stateToFrequencyMap!=null){
        for(State state:stateToFrequencyMap.keySet()){
          int frequency=stateToFrequencyMap.get(state);
          ret+=(state+"--("+symbol+","+frequency+")--> ");
        }
      }
    }
    ret+="]";
    return ret;
  }

  public int size() {
    return symbolToStateAndFrequencyMap.size();
  }

}
