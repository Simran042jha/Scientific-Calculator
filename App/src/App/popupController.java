package App;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Popup;
import javafx.util.Duration;

public class popupController implements Initializable{
    
    @FXML
    private Button popSin, popCos, popTan, popSec, popCsc, popCot;

    private Popup popup;
    private double scaleAmt = -0.1;
    private int duration = 80;
    private ScaleTransition transition;
    private Double num, result;
    private Controller cntrlr;
    private String output, output2;
    public popupController(){}
    public popupController(Controller controller)
    {
        this.cntrlr = controller;
        this.popup = new Popup();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("popup.fxml"));
            loader.setController(this);
            Parent parent = loader.load();
            popup.setAutoHide(true);
            popup.getContent().add(parent);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showTrigFunc(){

        popup.show(this.cntrlr.trigBtn.getScene().getWindow());
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
        Platform.runLater(() -> {
            popSin.setOnAction(event ->{
                transition = Anim(scaleAmt, duration);
                transition.setNode(popSin);
                transition.play();
                output = cntrlr.outputWin.getText();
                num = Double.parseDouble(output);
                output2 = cntrlr.outputWin2.getText();
                cntrlr.unaryused = true;
                cntrlr.decimalUsed = false;
                cntrlr.opchange = false;
                if(cntrlr.evaled)
                {
                    cntrlr.outputWin2.setText("");
                    cntrlr.evaled = false;
                }
                if(cntrlr.lastop.isEmpty() && cntrlr.start){
                    cntrlr.lastop = cntrlr.outputWin2.getText();
                    cntrlr.start = false;
                    cntrlr.currentop = "sin("+output+")";
                }
                else{
                    cntrlr.currentop = "sin("+cntrlr.currentop+")";
                }
                cntrlr.outputWin2.setText(cntrlr.lastop+cntrlr.currentop);
                result = Math.sin(num);
                cntrlr.numStack.push(result);
                cntrlr.outputWin.setText(cntrlr.formatNumber(result));
                popSin.getScene().getWindow().hide();
            });
            popCos.setOnAction(event ->{
                transition = Anim(scaleAmt, duration);
                transition.setNode(popCos);
                transition.play();
                output = cntrlr.outputWin.getText();
                num = Double.parseDouble(output);
                output2 = cntrlr.outputWin2.getText();
                cntrlr.unaryused = true;
                cntrlr.decimalUsed = false;
                cntrlr.opchange = false;
                if(cntrlr.evaled)
                {
                    cntrlr.outputWin2.setText("");
                    cntrlr.evaled = false;
                }
                if(cntrlr.lastop.isEmpty() && cntrlr.start){
                    cntrlr.lastop = cntrlr.outputWin2.getText();
                    cntrlr.start = false;
                    cntrlr.currentop = "cos("+output+")";
                }
                else{
                    cntrlr.currentop = "cos("+cntrlr.currentop+")";
                }
                cntrlr.outputWin2.setText(cntrlr.lastop+cntrlr.currentop);
                result = Math.cos(num);
                cntrlr.numStack.push(result);
                cntrlr.outputWin.setText(cntrlr.formatNumber(result));
                popCos.getScene().getWindow().hide();
            });
            popTan.setOnAction(event ->{
                transition = Anim(scaleAmt, duration);
                transition.setNode(popTan);
                transition.play();
                output = cntrlr.outputWin.getText();
                num = Double.parseDouble(output);
                output2 = cntrlr.outputWin2.getText();
                if(cntrlr.evaled)
                {
                    cntrlr.outputWin2.setText("");
                    cntrlr.evaled = false;
                }
                cntrlr.unaryused = true;
                cntrlr.decimalUsed = false;
                cntrlr.opchange = false;
                if(cntrlr.lastop.isEmpty() && cntrlr.start){
                    cntrlr.lastop = cntrlr.outputWin2.getText();
                    cntrlr.start = false;
                    cntrlr.currentop = "tan("+output+")";
                }
                else{
                    cntrlr.currentop = "tan("+cntrlr.currentop+")";
                }
                cntrlr.outputWin2.setText(cntrlr.lastop+cntrlr.currentop);
                result = Math.sin(num)/Math.cos(num);
                cntrlr.numStack.push(result);
                cntrlr.outputWin.setText(cntrlr.formatNumber(result));
                popTan.getScene().getWindow().hide();
            });
            popSec.setOnAction(event ->{
                transition = Anim(scaleAmt, duration);
                transition.setNode(popSec);
                transition.play();
                output = cntrlr.outputWin.getText();
                num = Double.parseDouble(output);
                output2 = cntrlr.outputWin2.getText();
                if(cntrlr.evaled)
                {
                    cntrlr.outputWin2.setText("");
                    cntrlr.evaled = false;
                }
                cntrlr.unaryused = true;
                cntrlr.decimalUsed = false;
                cntrlr.opchange = false;
                if(cntrlr.lastop.isEmpty() && cntrlr.start){
                    cntrlr.lastop = cntrlr.outputWin2.getText();
                    cntrlr.start = false;
                    cntrlr.currentop = "sec("+output+")";
                }
                else{
                    cntrlr.currentop = "sec("+cntrlr.currentop+")";
                }
                cntrlr.outputWin2.setText(cntrlr.lastop+cntrlr.currentop);
                result = 1/Math.cos(num);
                cntrlr.numStack.push(result);
                cntrlr.outputWin.setText(cntrlr.formatNumber(result));
                popSec.getScene().getWindow().hide();
            });
            popCsc.setOnAction(event ->{
                transition = Anim(scaleAmt, duration);
                transition.setNode(popCsc);
                transition.play();
                output = cntrlr.outputWin.getText();
                num = Double.parseDouble(output);
                output2 = cntrlr.outputWin2.getText();
                if(cntrlr.evaled)
                {
                    cntrlr.outputWin2.setText("");
                    cntrlr.evaled = false;
                }
                cntrlr.unaryused = true;
                cntrlr.decimalUsed = false;
                cntrlr.opchange = false;
                if(cntrlr.lastop.isEmpty() && cntrlr.start){
                    cntrlr.lastop = cntrlr.outputWin2.getText();
                    cntrlr.start = false;
                    cntrlr.currentop = "cosec("+output+")";
                }
                else{
                    cntrlr.currentop = "cosec("+cntrlr.currentop+")";
                }
                cntrlr.outputWin2.setText(cntrlr.lastop+cntrlr.currentop);
                result = 1/Math.sin(num);
                cntrlr.numStack.push(result);
                cntrlr.outputWin.setText(cntrlr.formatNumber(result));
                popCsc.getScene().getWindow().hide();
            });
            popCot.setOnAction(event ->{
                transition = Anim(scaleAmt, duration);
                transition.setNode(popCot);
                transition.play();
                output = cntrlr.outputWin.getText();
                num = Double.parseDouble(output);
                output2 = cntrlr.outputWin2.getText();
                cntrlr.unaryused = true;
                cntrlr.decimalUsed = false;
                cntrlr.opchange = false;
                if(cntrlr.evaled)
                {
                    cntrlr.outputWin2.setText("");
                    cntrlr.evaled = false;
                }
                if(cntrlr.lastop.isEmpty() && cntrlr.start){
                    cntrlr.lastop = cntrlr.outputWin2.getText();
                    cntrlr.start = false;
                    cntrlr.currentop = "cot("+output+")";
                }
                else{
                    cntrlr.currentop = "cot("+cntrlr.currentop+")";
                }
                cntrlr.outputWin2.setText(cntrlr.lastop+cntrlr.currentop);
                result = Math.cos(num)/Math.sin(num);
                cntrlr.numStack.push(result);
                cntrlr.outputWin.setText(cntrlr.formatNumber(result));
                popCot.getScene().getWindow().hide();
            });
        });
    }
    
}
