package learner;

/**
 *
 * Copyright (c) 2017 Chih-Hong Cheng
 * 
 */
import java.util.StringTokenizer;

public class AbstractAlphabet {


  // Abstract sensor map
  boolean lff;
  boolean lf;
  boolean l;
  boolean lb;

  boolean ff;
  boolean f;
  boolean b;

  boolean rff;
  boolean rf;
  boolean r;
  boolean rb;

  // Lane indicator [0, 2]
  int lane=0;

  // Action indicator (output alphabet)
  int action=0;


  static String toSymbol(AbstractAlphabet a){
    String ret="";
    ret+=(a.lff)?"1":"0";
    ret+=(a.lf)?"1":"0";
    ret+=(a.l)?"1":"0";
    ret+=(a.lb)?"1":"0";
    ret+=(a.ff)?"1":"0";
    ret+=(a.f)?"1":"0";
    ret+=(a.b)?"1":"0";
    ret+=(a.rff)?"1":"0";
    ret+=(a.rf)?"1":"0";
    ret+=(a.r)?"1":"0";
    ret+=(a.rb)?"1":"0";
    ret+=(a.action);
    ret+=(a.lane);    	
    return ret;
  }

  static String fromChihhongParser(String input){
    StringTokenizer st=new StringTokenizer(input,",");
    AbstractAlphabet ret=new AbstractAlphabet();
    ret.lff=st.nextToken().equals("1");
    ret.lf=st.nextToken().equals("1");
    ret.l=st.nextToken().equals("1");
    ret.lb=st.nextToken().equals("1");
    ret.ff=st.nextToken().equals("1");
    ret.f=st.nextToken().equals("1");
    ret.b=st.nextToken().equals("1");
    ret.rff=st.nextToken().equals("1");
    ret.rf=st.nextToken().equals("1");
    ret.r=st.nextToken().equals("1");
    ret.rb=st.nextToken().equals("1");
    ret.lane=Integer.parseInt(st.nextToken());    	
    ret.action=Integer.parseInt(st.nextToken());

    return toSymbol(ret);
  }

  static AbstractAlphabet fromSymbol(String input){

    AbstractAlphabet ret=new AbstractAlphabet();
    int index=0;
    ret.lff=(input.charAt(index)=='1');
    index++;
    ret.lf=(input.charAt(index)=='1');
    index++;
    ret.l=(input.charAt(index)=='1');
    index++;
    ret.lb=(input.charAt(index)=='1');
    index++;
    ret.ff=(input.charAt(index)=='1');
    index++;
    ret.f=(input.charAt(index)=='1');
    index++;
    ret.b=(input.charAt(index)=='1');
    index++;
    ret.rff=(input.charAt(index)=='1');
    index++;
    ret.rf=(input.charAt(index)=='1');
    index++;
    ret.r=(input.charAt(index)=='1');
    index++;
    ret.rb=(input.charAt(index)=='1');
    index++;
    ret.lane=Integer.parseInt(input.charAt(index)+"");
    index++;
    ret.action=Integer.parseInt(input.charAt(index)+"");
    return ret;
  }

  public String toString(){
    return "(lff,lf,l,lb,ff,f,b,rff,rf,r,rb,lane,action)="
        + lff + ","
        + lf + ","
        + l + ","
        + lb + ","
        + ff + ","
        + f + ","
        + b + ","
        + rff + ","
        + rf + ","
        + r + ","
        + rb + ","
        + lane + ","
        + action;
  }

  public void printAlphabet() {
    System.out.println("(lff,lf,l,lb,ff,f,b,rff,rf,r,rb,lane,action)="
        + lff + ","
        + lf + ","
        + l + ","
        + lb + ","
        + ff + ","
        + f + ","
        + b + ","
        + rff + ","
        + rf + ","
        + r + ","
        + rb + ","
        + lane + ","
        + action);
  }

}
