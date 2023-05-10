package App;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.Stack;

import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.util.Duration;
public class Controller implements Initializable{
    @FXML
    public Label outputWin, outputWin2;
    @FXML
    private Button eqBtn, clearBtn, logBtn, expBtn, divBtn, mulBtn, plusBtn, minusBtn, delBtn;
    @FXML
    private Button numOne, numTwo, numThree, numFour, numFive, numSix, numSeven, numEight, numNine, numZero, 
    decimalBtn, signBtn, sqrtBtn;
    @FXML
    public Button trigBtn;
    
    private ScaleTransition transition;
    private double scaleAmt = -0.1;
    private int duration = 80;
    private Character operator='\0';
    private String currentOutput, currentOutput2;
    public String lastop="", currentop="";
    private Double num1=0.0, num2=0.0, result;
    private boolean signFlip = false;
    public boolean decimalUsed = false;
    public boolean opchange = false;
    private boolean opchanged = false;
    private boolean intermediate = false;
    public boolean unaryused = false;
    public boolean start = true;
    public boolean evaled = false;
    private Stack<Character> opStack = new Stack<>();
    public Stack<Double> numStack = new Stack<>();
    private boolean referenced = false;
    public String formatNumber(Double d)
    {
        int length = d.toString().length();
        BigDecimal bd = new BigDecimal(d).setScale(6, RoundingMode.HALF_DOWN);
        bd = bd.stripTrailingZeros();
        if(length > 14)
        {
            DecimalFormat f = new DecimalFormat("0.###E0");
            return f.format(d);
        }
        if(length <= 14)
        {
            return bd.toPlainString();
        }
        else{
            return bd.toString();
        }
    }
    private boolean isHigherPrecedence(Character op)
    {
        Character last_op = opStack.peek();
        switch(op){
            case '^':
                switch (last_op) {
                    case '^':
                        return false;
                    case '+':
                    case '-':
                    case '*':
                    case '/':
                        return true;
                }
            break;
            case '+':
            case '-':
                switch (last_op) {
                    case '^':
                    case '+':
                    case '-':
                    case '*':
                    case '/':
                        return false;
                }
                break;
            case '*':
            case '/':
                switch (last_op) {

                    case '+':
                    case '-':
                        return true;
                    case '^':
                    case '*':
                    case '/':
                        return false;
                }
                break;
        }
        return false;
    }
    private ScaleTransition Anim(double amt, int duration)
    {
        ScaleTransition transition = new ScaleTransition();
        transition.setDuration(Duration.millis(duration));
        transition.setByX(scaleAmt);
        transition.setByY(scaleAmt);
        transition.setInterpolator(Interpolator.EASE_OUT);
        transition.setCycleCount(2);
        transition.setAutoReverse(true);
        return transition;
    }
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        transition = Anim(scaleAmt, duration);
        outputWin.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
                Integer length = arg2.length();
                if(length <= 5)
                {
                    outputWin.setFont(Font.font("SansSerif Regular", 48));
                }
                else if(5<length && length <=11)
                {
                    outputWin.setFont(Font.font("SansSerif Regular", 41));
                }
                else if(11<length && length<=16)
                {
                    outputWin.setFont(Font.font("SansSerif Regular", 34));
                }
                else if(16<length && length <= 21)
                {
                    outputWin.setFont(Font.font("SansSerif Regular", 26));
                }
                else if(21<length && length <= 24)
                {
                    outputWin.setFont(Font.font("SansSerif Regular", 24));
                }

                }       
        });
        eqBtn.setOnAction(event -> {
            //System.out.println("PRESSED " + event.getSource());
            if(!evaled)
            {
                if (!referenced){
                    transition.setNode(eqBtn);
                    transition.play();
                    evaled = true;
                }
                currentOutput = outputWin2.getText();
                if(referenced)
                {
                    num2 = numStack.pop();
                    intermediate = true;
                }
                else{
                    num2 = Double.parseDouble(outputWin.getText());
                }
                while(!opStack.isEmpty()){
                    num1 = numStack.pop();
                    switch(opStack.pop()){
                        case '^':
                            try{
                                result = Math.pow(num1, num2);
                                num2 = result;
                                
                            }
                            catch(ArithmeticException e)
                            {
                                System.out.println(e.getMessage());
                                outputWin.setText("Math Error");
                            }
                            break;
                        case '+':
                            try {
                                result = num1 + num2;
                                num2 = result;
                            } catch (ArithmeticException e) {
                                System.out.println(e.getMessage());
                                outputWin.setText("Math Error");
                            }
                            break;
                        case '-':
                            try {
                                result = num1 - num2;
                                num2 = result;
                            } catch (ArithmeticException e) {
                                System.out.println(e.getMessage());
                                outputWin.setText("Math Error");
                            }
                            break;
                        case '*':
                            try {
                                result = num1 * num2;
                                num2 = result;
                            } catch (ArithmeticException e) {
                                System.out.println(e.getMessage());
                                outputWin.setText("Math Error");
                            }
                            break;
                        case '/':
                            try {
                                result = num1/num2;
                                num2 = result;
                            } 
                            catch (ArithmeticException e) {
                                System.out.println(e.getMessage());
                                outputWin.setText("Math Error");
                            }
                            break;
                    }
                }
                
                if (!referenced)
                {
                    if(unaryused)
                        outputWin2.setText(currentOutput+" = ");
                    else
                        outputWin2.setText(currentOutput+outputWin.getText()+" = ");
                }
                else{
                    outputWin2.setText(currentOutput);
                }
                currentop = lastop = "";
                if(!result.isInfinite())
                {
                    numStack.push(result);
                    outputWin.setText(formatNumber(result));
                }
                else
                {
                    evaled = true;
                    numStack.clear();
                    opStack.clear();
                    outputWin.setText("Infinite");
                }
               
                referenced = false;

            }
            else{
                transition.setNode(eqBtn);
                transition.play();
                outputWin2.setText(outputWin.getText()+" = ");;
            }
            
            
            //operator = '\0';
          });
        clearBtn.setOnAction(event -> {
            transition.setNode(clearBtn);
            transition.play();
            outputWin.setText("0");
            currentop = lastop = "";
            numStack.clear();
            opStack.clear();
            
          });
        delBtn.setOnAction(event -> {
            transition.setNode(delBtn);
            transition.play();
            currentop = lastop = "";
            boolean flag = false;
            if(!evaled && !intermediate){
                currentOutput = outputWin.getText();
                if(currentOutput.contains("."))
                {
                    flag = true;
                }
                currentOutput = currentOutput.substring(0, currentOutput.length()-1);
                if(flag && !currentOutput.contains(".")){
                    decimalUsed = false;
                }
                if(currentOutput.length() == 0)
                {
                    currentOutput = "0";
                }
    ;           outputWin.setText(currentOutput);
            }
         
          });
        expBtn.setOnAction(event -> {
            transition.setNode(expBtn);
            transition.play();
            opchange = false;
            currentop = lastop = "";
            currentOutput = outputWin.getText();
            decimalUsed = false;
            currentOutput2 = outputWin2.getText();
            if(currentOutput.contains("E"))
            {
                num1 = numStack.pop();
            }
            else
                num1 = Double.parseDouble(currentOutput);
            if(evaled)
            {
                outputWin2.setText("");
                evaled = false;
            }
            if(opchange)
            {
                currentOutput2 = currentOutput2.substring(0, currentOutput2.length()-1);
                opchanged = true;
                opStack.pop();
                
            }
            if(!opchanged)
            {
                numStack.push(num1);
            }
            opchange = true;
            operator = '^';
            if (outputWin2.getText().isEmpty())
                outputWin2.setText(currentOutput+' '+'^'+' ');
            else{
                if (opchanged){
                    outputWin2.setText(currentOutput2+' '+'^'+' ');
                    opchanged  = false;
                }
                else
                {
                    if(unaryused)
                    {
                        unaryused = false;
                        outputWin2.setText(currentOutput2+' '+'^'+' ');
                    }
                    else
                        outputWin2.setText(currentOutput2+currentOutput+' '+'^'+' ');
                }
            }
            if(!opStack.empty() && !isHigherPrecedence(operator))
            {
                referenced = true;
                eqBtn.fire();
                opStack.push(operator);
            }
            else{
                opStack.push(operator);
            }

          });
        logBtn.setOnAction(event -> {
            transition.setNode(logBtn);
            transition.play();
            unaryused = true;
            decimalUsed = false;
            currentOutput = outputWin.getText();
            if(currentOutput.contains("E"))
            {
                num1 = numStack.pop();
            }
            else
                num1 = Double.parseDouble(currentOutput);
            opchange = false;
            if(evaled)
            {
                outputWin2.setText("");
                evaled = false;
            }
            if(lastop.isEmpty() && start){
                lastop = outputWin2.getText();
                start = false;
                currentop = "log("+currentOutput+")";
            }
            else{
                currentop = "log("+currentop+")";
            }
            outputWin2.setText(lastop+currentop);
            result = Math.log10(num1);
            numStack.push(result);
            outputWin.setText(formatNumber(result));
          });
        trigBtn.setOnAction(event -> {
            transition.setNode(trigBtn);
            transition.play();
            popupController pController = new popupController(this);
            pController.showTrigFunc();
            
          }); 
        sqrtBtn.setOnAction(event -> {
            transition.setNode(sqrtBtn);
            transition.play();
            unaryused = true;
            currentOutput = outputWin.getText();
            if(currentOutput.contains("E"))
            {
                num1 = numStack.pop();
            }
            else
                num1 = Double.parseDouble(currentOutput);
            opchange = false;
            if(evaled)
            {
                outputWin2.setText("");
                evaled = false;
            }
            if(lastop.isEmpty() && start){
                lastop = outputWin2.getText();
                start = false;
                currentop = sqrtBtn.getText()+"("+currentOutput+")";
            }
            else{
                currentop = sqrtBtn.getText()+"("+currentop+")";
            }
            outputWin2.setText(lastop+currentop);
            result = Math.sqrt(num1);
            outputWin.setText(formatNumber(result));
          });
        divBtn.setOnAction(event -> {
            transition.setNode(divBtn);
            transition.play();
            decimalUsed = false;
            currentop = lastop = "";
            start = true;
            currentOutput = outputWin.getText();
            currentOutput2 = outputWin2.getText();
            if(currentOutput.contains("E"))
            {
                num1 = numStack.pop();
            }
            else
                num1 = Double.parseDouble(currentOutput);
            if(evaled)
            {
                outputWin2.setText("");
                evaled = false;
            }
            if(opchange)
            {
                currentOutput2 = currentOutput2.substring(0, currentOutput2.length()-3);
                opchanged = true;
                opStack.pop();
                
            }
            if(!opchanged)
            {
                numStack.push(num1);
            }
            opchange = true;
            operator = '/';
            if (outputWin2.getText().isEmpty())
                outputWin2.setText(currentOutput+' '+divBtn.getText()+' ');
            else{
                if (opchanged){
                    outputWin2.setText(currentOutput2+' '+divBtn.getText()+' ');
                    opchanged  = false;
                }
                else
                {
                    if(unaryused)
                    {
                        unaryused = false;
                        outputWin2.setText(currentOutput2+' '+divBtn.getText()+' ');
                    }
                    else
                        outputWin2.setText(currentOutput2+currentOutput+' '+divBtn.getText()+' ');

                }
                    
            }
                
            if(!opStack.empty() && !isHigherPrecedence(operator))
            {
                referenced = true;
                eqBtn.fire();
                opStack.push(operator);
            }
            else{
                opStack.push(operator);
            }

          });
        mulBtn.setOnAction(event -> {
            transition.setNode(mulBtn);
            transition.play();
            decimalUsed = false;
            start = true;
            currentop = lastop = "";
            currentOutput = outputWin.getText();
            currentOutput2 = outputWin2.getText();
            if(currentOutput.contains("E"))
            {
                num1 = numStack.pop();
            }
            else
                num1 = Double.parseDouble(currentOutput);
            if(evaled)
            {
                outputWin2.setText("");
                evaled = false;
            }
            if(opchange)
            {
                currentOutput2 = currentOutput2.substring(0, currentOutput2.length()-3);
                opStack.pop();
                opchanged = true;
                
            }
            if(!opchanged)
            {
                numStack.push(num1);
            }
            opchange = true;
            operator = '*';
            if (outputWin2.getText().isEmpty())
                outputWin2.setText(currentOutput+' '+mulBtn.getText()+' ');
            else{
                if (opchanged) 
                {
                    outputWin2.setText(currentOutput2+' '+mulBtn.getText()+' ');
                    opchanged = false;
                }
                    
                else
                {
                    if(unaryused)
                    {
                        unaryused = false;
                        outputWin2.setText(currentOutput2+' '+mulBtn.getText()+' ');
                    }
                    else
                        outputWin2.setText(currentOutput2+currentOutput+' '+mulBtn.getText()+' ');
                }
            }
            if(!opStack.empty() && !isHigherPrecedence(operator))
            {
                referenced = true;
                eqBtn.fire();
                opStack.push(operator);
            }
            else{
                opStack.push(operator);
            }

          });
        plusBtn.setOnAction(event -> {
            transition.setNode(plusBtn);
            transition.play();
            decimalUsed = false;
            start = true;
            currentop = lastop = "";
            currentOutput = outputWin.getText();
            currentOutput2 = outputWin2.getText();
            if(currentOutput.contains("E"))
            {
                num1 = numStack.pop();
            }
            else
                num1 = Double.parseDouble(currentOutput);
            if(evaled)
            {
                outputWin2.setText("");
                evaled = false;
            }
            if(opchange)
            {
                currentOutput2 = currentOutput2.substring(0, currentOutput2.length()-3);
                opStack.pop();
                opchanged = true;
                
            }
            if(!opchanged)
            {
                numStack.push(num1);
            }
            opchange = true;
            operator = '+';
            if (outputWin2.getText().isEmpty())
                outputWin2.setText(currentOutput+' '+plusBtn.getText()+' ');
            else{
                if (opchanged){
                    outputWin2.setText(currentOutput2+' '+plusBtn.getText()+' ');
                    opchanged = false;
                }
                    
                else
                {
                    if(unaryused)
                    {
                        unaryused = false;
                        outputWin2.setText(currentOutput2+' '+plusBtn.getText()+' ');
                    }
                    else
                        outputWin2.setText(currentOutput2+currentOutput+' '+plusBtn.getText()+' ');
                }
            }
            if(!opStack.empty() && !isHigherPrecedence(operator))
            {
                referenced = true;
                eqBtn.fire();
                opStack.push(operator);
            }
            else{
                opStack.push(operator);
            }
          });
        minusBtn.setOnAction(event -> {
            transition.setNode(minusBtn);
            transition.play();
            decimalUsed = false;
            start = true;
            currentop = lastop = "";
            currentOutput = outputWin.getText();
            currentOutput2 = outputWin2.getText();
                    if(currentOutput.contains("E"))
            {
                num1 = numStack.pop();
            }
            else
                num1 = Double.parseDouble(currentOutput);
            if(evaled)
            {
                outputWin2.setText("");
                evaled = false;
            }
            if(opchange)
            {
                currentOutput2 = currentOutput2.substring(0, currentOutput2.length()-3);
                opStack.pop();
                opchanged = true;
            
            }
            if(!opchanged)
            {
                numStack.push(num1);
            }
            opchange = true;
            operator = '-';
            if (outputWin2.getText().isEmpty())
                outputWin2.setText(currentOutput+' '+minusBtn.getText()+' ');
            else{
                if (opchanged) {
                    outputWin2.setText(currentOutput2+' '+minusBtn.getText()+' ');
                    opchanged = false;
                }
                else
                {
                    if(unaryused)
                    {
                        unaryused = false;
                        outputWin2.setText(currentOutput2+' '+minusBtn.getText()+' ');
                    }
                    else
                        outputWin2.setText(currentOutput2+currentOutput+' '+minusBtn.getText()+' ');
                }
            }
            if(!opStack.empty() &&!isHigherPrecedence(operator))
            {
                referenced = true;
                eqBtn.fire();
                opStack.push(operator);
            }
            else{
                opStack.push(operator);
            }
          });
        numZero.setOnAction(event -> {
            transition.setNode(numZero);
            transition.play();
            intermediate = false;
            currentOutput = outputWin.getText();
            opchange = false;
            if(evaled)
            {
                outputWin2.setText("");
                currentOutput="0";
                evaled = false;
            }
            if (Double.parseDouble(currentOutput) != 0)
            {
                if(operator != '\0'&& !decimalUsed){
                    outputWin.setText(numZero.getText());
                    operator='\0';
                }
                else
                    outputWin.setText( currentOutput+numZero.getText());
            }
            else if(decimalUsed)
            {
                outputWin.setText( currentOutput+numZero.getText());
            }
            else{
                outputWin.setText(numZero.getText());
            }
          });
        numOne.setOnAction(event -> {
            transition.setNode(numOne);
            transition.play();
            intermediate = false;
            opchange = false;
            currentOutput = outputWin.getText();
            if(evaled)
            {
                outputWin2.setText("");
                currentOutput="0";
                evaled = false;
            }
            if (Double.parseDouble(currentOutput) != 0)
            {
                if(operator != '\0'&& !decimalUsed){
                    outputWin.setText(numOne.getText());
                    operator='\0';
                }
                else
                    outputWin.setText( currentOutput+numOne.getText());
            }
            else if(decimalUsed)
            {
                outputWin.setText( currentOutput+numOne.getText());
            }
            else{
                outputWin.setText(numOne.getText());
            }
            
          });
        numTwo.setOnAction(event -> {
            transition.setNode(numTwo);
            transition.play();
            intermediate = false;
            opchange = false;
            currentOutput = outputWin.getText();
            if(evaled)
            {
                outputWin2.setText("");
                currentOutput="0";
                evaled = false;
            }
            if (Double.parseDouble(currentOutput) != 0)
            {
                if(operator != '\0'&& !decimalUsed){
                    outputWin.setText(numTwo.getText());
                    operator='\0';
                }
                else
                outputWin.setText( currentOutput+numTwo.getText());
            }
            else if(decimalUsed)
            {
                outputWin.setText( currentOutput+numTwo.getText());
            }
            else{
                outputWin.setText(numTwo.getText());
            }
          });
        numThree.setOnAction(event -> {
            transition.setNode(numThree);
            transition.play();
            intermediate = false;
            opchange = false;
            currentOutput = outputWin.getText();
            if(evaled)
            {
                outputWin2.setText("");
                currentOutput="0";
                evaled = false;
            }
            if (Double.parseDouble(currentOutput) != 0)
            {
                if(operator != '\0'&& !decimalUsed){
                    outputWin.setText(numThree.getText());
                    operator='\0';
                }
                else
                outputWin.setText( currentOutput+numThree.getText());
            }
            else if(decimalUsed)
            {
                outputWin.setText( currentOutput+numThree.getText());
            }
            else{
                outputWin.setText(numThree.getText());
            }
          });
        numFour.setOnAction(event -> {
            transition.setNode(numFour);
            transition.play();
            intermediate = false;
            opchange = false;
            currentOutput = outputWin.getText();
            if(evaled)
            {
                outputWin2.setText("");
                currentOutput="0";
                evaled = false;
            }
            if (Double.parseDouble(currentOutput) != 0)
            {
                if(operator != '\0'&& !decimalUsed){
                    outputWin.setText(numFour.getText());
                    operator='\0';
                }
                else
                outputWin.setText(currentOutput+numFour.getText());
            }
            else if(decimalUsed)
            {
                outputWin.setText( currentOutput+numFour.getText());
            }
            else{
                outputWin.setText(numFour.getText());
            }
          });
        numFive.setOnAction(event -> {
            transition.setNode(numFive);
            transition.play();
            intermediate = false;
            opchange = false;
            currentOutput = outputWin.getText();
            if(evaled)
            {
                outputWin2.setText("");
                currentOutput="0";
                evaled = false;
            }
            if (Double.parseDouble(currentOutput) != 0)
            {
                if(operator != '\0' && !decimalUsed){
                    outputWin.setText(numFive.getText());
                    operator='\0';
                }
                else
                outputWin.setText( currentOutput+numFive.getText());
            }

            else if(decimalUsed)
            {
                outputWin.setText( currentOutput+numFive.getText());
            }
            else{
                outputWin.setText(numFive.getText());
            }
          });
        numSix.setOnAction(event -> {
            transition.setNode(numSix);
            transition.play();
            intermediate = false;
            opchange = false;
            currentOutput = outputWin.getText();
            if(evaled)
            {
                outputWin2.setText("");
                currentOutput="0";
                evaled = false;
            }
            if (Double.parseDouble(currentOutput) != 0)
            {
                if(operator != '\0'&& !decimalUsed){
                    outputWin.setText(numSix.getText());
                    operator='\0';
                }
                else
                outputWin.setText( currentOutput+numSix.getText());
            }
            else if(decimalUsed)
            {
                outputWin.setText( currentOutput+numSix.getText());
            }
            else{
                outputWin.setText(numSix.getText());
            }
          });
        numSeven.setOnAction(event -> {
            transition.setNode(numSeven);
            transition.play();
            intermediate = false;
            opchange = false;
            currentOutput = outputWin.getText();
            if(evaled)
            {
                outputWin2.setText("");
                currentOutput="0";
                evaled = false;
            }
            if (Double.parseDouble(currentOutput) != 0)
            {
                if(operator != '\0'&& !decimalUsed){
                    outputWin.setText(numSeven.getText());
                    operator='\0';
                }
                else
                outputWin.setText( currentOutput+numSeven.getText());
            }
            else if(decimalUsed)
            {
                outputWin.setText( currentOutput+numSeven.getText());
            }
            else{
                outputWin.setText(numSeven.getText());
            }
          });
        numEight.setOnAction(event -> {
            transition.setNode(numEight);
            transition.play();
            intermediate = false;
            opchange = false;
            currentOutput = outputWin.getText();
            if(evaled)
            {
                outputWin2.setText("");
                currentOutput="0";
                evaled = false;
            }
            if (Double.parseDouble(currentOutput) != 0)
            {
                if(operator != '\0'&& !decimalUsed){
                    outputWin.setText(numEight.getText());
                    operator='\0';
                }
                else
                outputWin.setText( currentOutput+numEight.getText());
            }
            else if(decimalUsed)
            {
                outputWin.setText( currentOutput+numEight.getText());
            }
            else{
                outputWin.setText(numEight.getText());
            }
          });
        numNine.setOnAction(event -> {
            transition.setNode(numNine);
            transition.play();
            intermediate = false;
            opchange = false;
            currentOutput = outputWin.getText();
            if(evaled)
            {
                outputWin2.setText("");
                currentOutput="0";
                evaled = false;
            }
            if (Double.parseDouble(currentOutput) != 0)
            {
                if(operator != '\0'&& !decimalUsed){
                    outputWin.setText(numNine.getText());
                    operator='\0';
                }
                else
                outputWin.setText( currentOutput+numNine.getText());
            }
            else if(decimalUsed)
            {
                outputWin.setText( currentOutput+numNine.getText());
            }
            else{
                outputWin.setText(numNine.getText());
            }
          });
        decimalBtn.setOnAction(event -> {
            transition.setNode(decimalBtn);
            transition.play();
            if(!evaled && !intermediate)
            {
                intermediate = false;
                opchange = false;
                if(!decimalUsed)
                {
                    outputWin.setText(outputWin.getText()+".");
                    decimalUsed = true;
                }

            }
  
          });
        signBtn.setOnAction(event -> {
            transition.setNode(signBtn);
            transition.play();
            opchange = false;
            intermediate = false;
            result = Double.parseDouble(outputWin.getText());
            if (result != 0)
            {
                result *=-1;
                outputWin.setText(result.toString());
                signFlip = !signFlip;
            }
          });
          
        
    }
    
}
