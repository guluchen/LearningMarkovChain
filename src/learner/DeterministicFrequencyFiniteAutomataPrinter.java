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
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.Stack;

public class DeterministicFrequencyFiniteAutomataPrinter {
  DeterministicFrequencyFiniteAutomata ffa;
  int num=0;
  HashMap<State,Integer> stateMap;
  Queue<State> queue;

  public DeterministicFrequencyFiniteAutomataPrinter(DeterministicFrequencyFiniteAutomata ffa){
    this.ffa=ffa;
  }
  public void printFrequencyFiniteAutomata(){
    System.out.println(toString());
  }
  public String toString(){
    HashSet<State> processed=new HashSet<State>();
    Stack<State> workList=new Stack<State>();
    workList.push(ffa.getInitialState());
    processed.add(ffa.getInitialState());
    StringBuffer sb=new StringBuffer();
    sb.append("Total Number Of States: ").append(ffa.getNumberOfStates()).append("\n");

    while(!workList.isEmpty()){
      State source=workList.pop();
      sb.append(source).append(" (").append(ffa.getStateFrequency(source)).append(")\n");

      for(String symbol:ffa.getAlphabet()){
        Optional<StateAndFrequency> stateAndFrequencyOpt=ffa.getOutgoingTransition(source, symbol);
        if(stateAndFrequencyOpt.isPresent()){
          State destination = stateAndFrequencyOpt.get().state;
          int frequency = stateAndFrequencyOpt.get().frequency;
          sb.append(source).append(" -- (").append(symbol).append(", ").
          append(frequency).append(") --> ").append(destination).append("\n");
          if(!processed.contains(destination)){
            workList.push(destination);
            processed.add(destination);
          }
        }
      }
      sb.append("\n");
    }

    return sb.toString();
  }



  private void printPrismInitialString(){
    System.out.println("dtmc");
    System.out.println();
    System.out.println("module car");
    System.out.println("s : [0.."+ffa.getNumberOfStates()+"] init 0;");
    System.out.println("mode : [0..1] init 0;");
    System.out.println("lff : [0..1] init 0;");
    System.out.println("lf : [0..1] init 0;");
    System.out.println("l : [0..1] init 0;");
    System.out.println("lb : [0..1] init 0;");
    System.out.println("ff : [0..1] init 0;");
    System.out.println("f : [0..1] init 0;");
    System.out.println("b : [0..1] init 0;");
    System.out.println("rff : [0..1] init 0;");
    System.out.println("rf : [0..1] init 0;");
    System.out.println("r : [0..1] init 0;");
    System.out.println("rb : [0..1] init 0;");
    System.out.println("action : [0..4] init 0;");
    System.out.println("lane : [0..2] init 0;");

  }

  void initializeDataStructure(){
    num=0;
    stateMap=new HashMap<State,Integer>();
    stateMap.put(State.initial(), 0);
    queue =new LinkedList<State>();
    queue.add(State.initial());
  }

  /* Output a DTMC in Prism format, for weight to stop in a state, 
   * we normalize it to all outgoing transitions
   * 
   */
  public void printDTMCinPrism(){

    printPrismInitialString();
    initializeDataStructure();

    while(!queue.isEmpty()){
      State cur=queue.remove();
      printDTMCTransitionFromCurToSymbol(cur);
      for(String c:ffa.getAlphabet()){
        createNextAndPrintTransitionFromSymbolToNext(cur,c);
      }
    }

    System.out.println("endmodule");
  }

  /* Output a finite DTMC in Prism format, for weight to stop in a state, 
   * we redirect it to a sink state with a special symbol
   * 
   */	
  public void printFiniteDTMCinPrism(){
    printPrismInitialString();
    initializeDataStructure();
    printSinkStateSelfloop();

    while(!queue.isEmpty()){
      State cur=queue.remove();
      printFiniteDTMCTransitionFromCurToSymbol(cur);
      printFiniteDTMCTransitionFromCurToSink(cur);
      for(String c:ffa.getAlphabet()){
        createNextAndPrintTransitionFromSymbolToNext(cur,c);
      }
    }

    System.out.println("endmodule");
  }

  private void printFiniteDTMCTransitionFromCurToSink(State cur) {
    int sinkNum=ffa.getNumberOfStates();
    int curNum=stateMap.get(cur);
    StringBuffer sb=new StringBuffer();
    sb.append("[] (s=").append(curNum).append(") & (mode =0)-> ");
    int totalAmount=getOutgoingTransitionFrequency(cur);
    totalAmount+=ffa.getStateFrequency(cur);
    if(ffa.getStateFrequency(cur)!=0){
      sb.append(((float)ffa.getStateFrequency(cur))/totalAmount 
          +": (s'="+sinkNum+") & "
          + "(mode'=2) & (lff'=0) & "
          + "(lf'=0) & (l'=0) & "
          + "(lb'=0) & (ff'=0) & "
          + "(f'=0) & (b'=0) & "
          + "(rff'=0) & (rf'=0) & "
          + "(r'=0) & (rb'=0) & "
          + "(action'=0) & (lane'=0) ");
    }
  }

  private void printFiniteDTMCTransitionFromCurToSymbol(State cur) {
    int curNum=stateMap.get(cur);
    StringBuffer sb=new StringBuffer();
    sb.append("[] (s=").append(curNum).append(") & (mode =0)-> ");
    int totalAmount=getOutgoingTransitionFrequency(cur);
    totalAmount+=ffa.getStateFrequency(cur);
    boolean hasNoOutgoingTransition=true;

    if(ffa.getStateFrequency(cur)!=0){
      hasNoOutgoingTransition=false;
    }

    for(String c:ffa.getAlphabet()){
      int destFreq = StateAndFrequency.getFrequency(ffa.getOutgoingTransition(cur,c));				
      AbstractAlphabet a=AbstractAlphabet.fromSymbol(c);
      if(destFreq==0)
        continue;
      if(hasNoOutgoingTransition){
        hasNoOutgoingTransition=false;
      }else{
        sb.append(" + ");
      }	
      sb.append(((float)destFreq)/totalAmount 
          +": (mode'=1) & "
          + "(lff'="+(a.lff?1:0)+") & "
          + "(lf'="+(a.lf?1:0)+") & "
          + "(l'="+(a.l?1:0)+") & "
          + "(lb'="+(a.lb?1:0)+") & "
          + "(ff'="+(a.ff?1:0)+") & "
          + "(f'="+(a.f?1:0)+") & "
          + "(b'="+(a.b?1:0)+") & "
          + "(rff'="+(a.rff?1:0)+") & "
          + "(rf'="+(a.rff?1:0)+") & "
          + "(r'="+(a.r?1:0)+") & "
          + "(rb'="+(a.rb?1:0)+") & "
          + "(action'="+a.action+") & "
          + "(lane'="+a.lane+") ");					

    }
    sb.append(";");
    System.out.println(sb.toString());		
  }

  private void printSinkStateSelfloop() {
    int sinkNum=ffa.getNumberOfStates();
    System.out.println("[] s="+sinkNum+" -> 1 : (s'="+sinkNum+");");	
  }

  private void printDTMCTransitionFromCurToSymbol(State cur) {
    int curNum=stateMap.get(cur);
    StringBuffer sb=new StringBuffer();
    sb.append("[] (s=").append(curNum).append(") & (mode =0)-> ");
    int totalAmount=getOutgoingTransitionFrequency(cur);
    boolean hasNoOutgoingTransition=true;
    for(String c:ffa.getAlphabet()){
      int destFreq = StateAndFrequency.getFrequency(ffa.getOutgoingTransition(cur,c));				
      AbstractAlphabet a=AbstractAlphabet.fromSymbol(c);
      if(destFreq==0)
        continue;
      if(hasNoOutgoingTransition){
        hasNoOutgoingTransition=false;
      }else{
        sb.append(" + ");
      }
      sb.append(((float)destFreq)/totalAmount
          + ": (mode'=1) & "
          + "(lff'="+(a.lff?1:0)+") & "
          + "(lf'="+(a.lf?1:0)+") & "
          + "(l'="+(a.l?1:0)+") & "
          + "(lb'="+(a.lb?1:0)+") & "
          + "(ff'="+(a.ff?1:0)+") & "
          + "(f'="+(a.f?1:0)+") & "
          + "(b'="+(a.b?1:0)+") & "
          + "(rff'="+(a.rff?1:0)+") & "
          + "(rf'="+(a.rff?1:0)+") & "
          + "(r'="+(a.r?1:0)+") & "
          + "(rb'="+(a.rb?1:0)+") & "
          + "(action'="+a.action+") & "
          + "(lane'="+a.lane+") ");
    }
    if(hasNoOutgoingTransition)
      sb.append("1 : (s'=").append(curNum).append(") & (mode' =0)");

    sb.append(";");
    System.out.println(sb.toString());
  }

  private int getOutgoingTransitionFrequency(State cur) {
    int count=0;
    for(String c:ffa.getAlphabet()){
      count+=StateAndFrequency.getFrequency(ffa.getOutgoingTransition(cur,c));
    }
    return count;
  }

  private void createNextAndPrintTransitionFromSymbolToNext(State cur, String c) {
    State destination=StateAndFrequency.getState(ffa.getOutgoingTransition(cur, c));
    if(destination!=null){
      int curNum=stateMap.get(cur);
      AbstractAlphabet a=AbstractAlphabet.fromSymbol(c);

      if(!stateMap.containsKey(destination)){
        stateMap.put(destination, num++);
        queue.add(destination);
      }
      int destNum=stateMap.get(destination);
      System.out.println("[] (s="+curNum+") & (mode =1) & (lff="+(a.lff?1:0)+") & (lf="+(a.lf?1:0)+") & (l="+(a.l?1:0)+") & (lb="+(a.lb?1:0)+") & (ff="+(a.ff?1:0)+") & (f="+(a.f?1:0)+") & (b="+(a.b?1:0)+") & (rff="+(a.rff?1:0)+") & (rf="+(a.rf?1:0)+") & (r="+(a.r?1:0)+") & (rb="+(a.rb?1:0)+") & (action="+a.action+") & (lane="+a.lane+") -> 1 : (s'="+destNum+") & (mode'=0) & (lff'=0) & (lf'=0) & (l'=0) & (lb'=0) & (ff'=0) & (f'=0) & (b'=0) & (rff'=0) & (rf'=0) & (r'=0) & (rb'=0) & (action'=0) & (lane'=0) ;");	
    }
  }

}
