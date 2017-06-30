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
import java.util.Optional;

public class DeterministicTransitionMap {

  private HashMap<String, StateAndFrequency> symbolToStateAndFrequencyMap ;

  public DeterministicTransitionMap(){
    symbolToStateAndFrequencyMap = new HashMap<String, StateAndFrequency>();
  };

  public void set(String symbol, State state, int frequency) {
    symbolToStateAndFrequencyMap.put(symbol, new StateAndFrequency(state,frequency));
  }
  public Optional<StateAndFrequency> get(String symbol){
    return Optional.ofNullable(symbolToStateAndFrequencyMap.get(symbol));
  }

  public void remove(String symbol){
    symbolToStateAndFrequencyMap.remove(symbol);
  }

  public String toString(){
    String ret="[";
    for(String symbol:symbolToStateAndFrequencyMap.keySet()){
      StateAndFrequency stateAndFrequency=
          symbolToStateAndFrequencyMap.get(symbol);
      if(stateAndFrequency!=null){
        State state=stateAndFrequency.state;
        int frequency=stateAndFrequency.frequency;
        ret+=("--("+symbol+","+frequency+")-->"+state+" ");
      }
    }
    ret+="]";
    return ret;
  }

}
