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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class SetToTreeShapedFFA {
  HashMap<List<String>,Integer> rawdata;
  HashSet<String> alphabet;

  public SetToTreeShapedFFA(){
    rawdata=new HashMap<List<String>,Integer>();
    alphabet=new HashSet<String>();

  }

  public DeterministicFrequencyFiniteAutomata generateFPTA() throws AutomataDeterminismException{
    DeterministicFrequencyFiniteAutomata ret=new DeterministicFrequencyFiniteAutomata(alphabet);
    ret.setInitialState(State.initial());

    for(List<String> input:rawdata.keySet()){
      addWordToFPTA(ret,input,rawdata.get(input));
    }
    return ret;
  }

  private void addWordToFPTA(DeterministicFrequencyFiniteAutomata ffa, List<String> input, int inputFreq) throws AutomataDeterminismException{
    int curFreq=ffa.getStateFrequency(new State(input));
    ffa.setStateFrequency(new State(input), curFreq+inputFreq);

    State source=ffa.getInitialState();
    for(int i=1;i<=input.size();i++){
      if(!ffa.hasState(source)){
        ffa.setStateFrequency(source, 0);
      }

      State destination=new State(input.subList(0, i));
      String symbol=input.get(i-1);

      Optional<StateAndFrequency> next=ffa.getOutgoingTransition(source, symbol);

      int nextFreq=StateAndFrequency.getFrequency(next);
      ffa.setTransition(source, symbol, inputFreq+nextFreq, destination);
      source =destination;
    }

  }

  public void fromFile(String fileName) throws IOException {
    HashMap<List<String>,Integer> inputStrings=readInputStrings(fileName);
    inputStrings=extractSubstrings(inputStrings,100);
    filterOutRareEvent(inputStrings,2);		
  }

  private void filterOutRareEvent(HashMap<List<String>, Integer> inputStrings, int minOccurrence) {
    for(List<String> key:inputStrings.keySet()){
      if(inputStrings.get(key)>minOccurrence){
        rawdata.put(key, inputStrings.get(key));
      }
    }
  }

  private HashMap<List<String>, Integer> extractSubstrings(HashMap<List<String>, Integer> inputStrings, int bound) {
    HashMap<List<String>,Integer> substrings=new HashMap<List<String>,Integer>();
    for(List<String> input:inputStrings.keySet()){
      for(int i=0;i<input.size();i++){
        for(int j=i;j<Math.min(input.size(),i+bound);j++){
          List<String> substring=input.subList(i, j);
          if(substrings.containsKey(substring)){
            substrings.put(substring, substrings.get(substring)+inputStrings.get(input));
          }else{
            substrings.put(substring, inputStrings.get(input));
          }
        }
      }
    }
    return substrings;
  }

  private HashMap<List<String>, Integer> readInputStrings(String fileName) throws IOException {
    BufferedReader br = null;
    br = new BufferedReader(new FileReader(fileName));
    String sCurrentLine;
    List<String> newWord=new ArrayList<String>();
    HashMap<List<String>,Integer> inputStrings=new HashMap<List<String>,Integer>();

    while ((sCurrentLine = br.readLine()) != null) {
      if(sCurrentLine.contains("Start")){
        if(!inputStrings.containsKey(newWord))
          inputStrings.put(newWord, 1);
        else
          inputStrings.put(newWord, inputStrings.get(newWord)+1);
        newWord=new ArrayList<String>();
      }else if(sCurrentLine.contains(",")){ 				
        String a=AbstractAlphabet.fromChihhongParser(sCurrentLine);	
        alphabet.add(a);
        newWord.add(a);
      }
    }
    br.close();
    return inputStrings;

  }

  private void showSampleStats(HashMap<List<String>, Integer> substrings) {
    for(List<String> s:substrings.keySet())
      System.out.println("|w|="+s.size()+" : "+substrings.get(s));
  }


  private ArrayList<String> toArrayList(String input){
    ArrayList<String> ret =new ArrayList<String>();
    for(int i=0;i<input.length();i++){
      ret.add(""+input.charAt(i));
    }
    return ret;
  }
  public void useDefaultRawdata(){
    alphabet.add("a");
    alphabet.add("b");

    rawdata.put(new ArrayList<String>(),490);
    rawdata.put(toArrayList("a"), 128);
    rawdata.put(toArrayList("b"), 170);
    rawdata.put(toArrayList("aa"), 31);
    rawdata.put(toArrayList("ab"), 42);
    rawdata.put(toArrayList("ba"), 38);
    rawdata.put(toArrayList("bb"), 14);
    rawdata.put(toArrayList("aaa"), 8);
    rawdata.put(toArrayList("aab"), 10);
    rawdata.put(toArrayList("aba"), 10);
    rawdata.put(toArrayList("abb"), 4);
    rawdata.put(toArrayList("baa"), 9);
    rawdata.put(toArrayList("bab"), 4);
    rawdata.put(toArrayList("bba"), 3);
    rawdata.put(toArrayList("bbb"),6);
    rawdata.put(toArrayList("aaaa"), 2);
    rawdata.put(toArrayList("aaab"), 2);
    rawdata.put(toArrayList("aaba"), 3);
    rawdata.put(toArrayList("aabb"), 2);
    rawdata.put(toArrayList("abaa"), 2);
    rawdata.put(toArrayList("abab"), 2);
    rawdata.put(toArrayList("abba"), 2);
    rawdata.put(toArrayList("abbb"), 1);
    rawdata.put(toArrayList("baaa"), 2);
    rawdata.put(toArrayList("baab"), 2);
    rawdata.put(toArrayList("baba"), 1);
    rawdata.put(toArrayList("babb"), 1);
    rawdata.put(toArrayList("bbaa"), 1);
    rawdata.put(toArrayList("bbab"), 1);
    rawdata.put(toArrayList("bbba"), 1);
    rawdata.put(toArrayList("aaaaa"), 1);
    rawdata.put(toArrayList("aaaab"), 1);
    rawdata.put(toArrayList("aaaba"), 1);
    rawdata.put(toArrayList("aabaa"), 1);
    rawdata.put(toArrayList("aabab"), 1);
    rawdata.put(toArrayList("aabba"), 1);
    rawdata.put(toArrayList("abbaa"), 1);
    rawdata.put(toArrayList("abbab"), 1);
  }


}
