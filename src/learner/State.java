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
import java.util.List;

public class State{
  private List<String> name;

  public State(List<String> name){
    this.name=new ArrayList<String>(name);
  }

  public State extend(String symbol){
    ArrayList<String> nextName = new ArrayList<String>(name);
    nextName.add(symbol);
    return new State(nextName);
  }

  public int hashCode(){
    return name.hashCode();
  }

  public static State initial(){
    return new State(new ArrayList<String>());
  }

  public String toString(){
    return name.toString();
  }

  public boolean equals(Object obj){
    if (obj instanceof State){
      State other = (State)obj;
      if(name.size()!=other.name.size()){
        return false;
      }else{
        for(int i=0;i<name.size();i++){
          if(!name.get(i).equals(other.name.get(i))){
            return false;
          }
        }
      }		
      return true;
    }else{
      return false;
    }
  }

}
