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

import java.util.Optional;

public class StateAndFrequency {

  public State state;
  public int frequency; 


  static public State getState(Optional<StateAndFrequency> next) {
    if(next.isPresent()){
      return next.get().state;
    }else{
      return null;
    }
  }

  static public int getFrequency(StateAndFrequency stateAndFrequency) {
    if(stateAndFrequency!=null){
      return stateAndFrequency.frequency;
    }else{
      return 0;
    }
  }	

  public StateAndFrequency(State state, int frequency){
    this.state=state;
    this.frequency=frequency;
  }

  public boolean equals(Object obj){
    if (obj instanceof StateAndFrequency){
      StateAndFrequency other = (StateAndFrequency)obj;
      return state.equals(other.state) && frequency==other.frequency;
    }else{
      return false;
    }
  }

  public int hashCode(){
    return state.hashCode()+31*frequency;
  }

  public static int getFrequency(Optional<StateAndFrequency> next) {
    if(next.isPresent()){
      return next.get().frequency;
    }else{
      return 0;
    }
  }

  public String toString(){
    return "["+state+", "+frequency+"]";
  }

}
