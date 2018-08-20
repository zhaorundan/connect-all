package demo;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class MyController implements Initializable {
	@FXML
	private Button myButton;
	@FXML
	private TextField myTextField;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	public void showDateTime(ActionEvent event) {
		System.out.println("Button Clicked");
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-dd-mm HH:mm:ss");
		String dateTimeString = format.format(date);
		myTextField.setText(dateTimeString);
	}

}
