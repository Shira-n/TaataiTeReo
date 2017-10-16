package application.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import application.confirmation.QuitConfirmationController;
import application.model.CustomManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class CustomCreatePageController {


	@FXML
	private TextField _equation;

	@FXML
	private ListView<ListCell> _list;

	@FXML
	private Button _preview;

	@FXML
	private Button _add;

	@FXML
	private Button _create;

	@FXML
	private Button _return;

	@FXML
	private TextField _description;
	
	@FXML
	private CheckBox _public;
	
	@FXML
	private Label _warningMessage;
	
	@FXML
	private Label _equationMessage;

	private ObservableList<ListCell> _data;

	private String[] _qs;

	//private ArrayList<String> _qs = new ArrayList<>();

	private String _id;

	private CustomManager _manager = CustomDoPageController.getManager();

	@FXML
	private Stage _popUp;

	@FXML
	public void initialize() {
		_data= FXCollections.observableArrayList ();
		_list.setItems(_data);
		_warningMessage.setVisible(false);
		_equationMessage.setVisible(false);
		//TODO if private limit, force public & versa vice
		_qs = new String[10];
		for (int i = 0; i<_qs.length; i++){
			_qs[i] = "";
		}
		generateID();
	}

	@FXML
	public void handlePressCreate(MouseEvent event) {
		String description = _description.getText();
		if (description.equals("")) {
			errorMsg("Please enter a description before continuing");
		} else {
			if (_data.isEmpty()) {
				errorMsg("The question list cannot be empty");
			}
			else {
				//TODO confirmation ask
				//Store _data in the file and report question suite created.
				ArrayList<String> delivery = new ArrayList<>();
				for (String s : _qs){
					if (s.length() > 1){
						delivery.add(s);
					}
				}
				//Create a new Question suite
				_manager.writeCustomSuite(_id, description, delivery, _public.isSelected());

				//Update GUI
				//Return to custom page
				try {
		        	Parent parent = FXMLLoader.load(getClass().getResource("/application/view/CustomDoPage.fxml"));
		        	Scene scene = new Scene(parent);
		        	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		        	stage.setScene(scene);
		        	stage.show();

		        } catch (IOException e) {
		            e.printStackTrace();
		        }
				//Successfully created message
				try {
					Parent parent = FXMLLoader.load(getClass().getResource("/application/confirmation/CustomCreateSuccessConfirmation.fxml"));
					Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
					Scene scene = new Scene(parent);
					_popUp = new Stage();
					_popUp.setScene(scene);
					_popUp.initOwner(stage);
					_popUp.initModality(Modality.WINDOW_MODAL);
					_popUp.showAndWait();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@FXML
	public void handlePressAdd(MouseEvent event) {
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("JavaScript");
		//Ensure the given input is not empty
		String equation = _equation.getText();
		if (equation.length() < 1){
			//Empty input notification
			errorMsg("Please enter an equation before add");
		}else {
			boolean validEquation = true;
			int value = -1;
			try {
				value = Integer.parseInt(engine.eval(equation).toString());
			} catch (ScriptException e) {
				validEquation = false;
				// Wrong format, equation can not be evaluated
				errorMsg("Please enter an equation");
			} catch (NumberFormatException e) {
				validEquation = false;
				//Answer is not an integer
				errorMsg("Please ensure the answer is an integer");
			}
			if (validEquation) {
				if (value < 1 || value > 99) {
					errorMsg("Result is out of range (1~99)");
				} else {
					//If valid equation, then add it to data
					//int index = _data.size() + 1;
					_qs[_data.size()] = value + "#" + equation;
					//TODO format is 32#2+30
					_data.add(new ListCell(equation));
				}
				if (_qs[9].length() > 0){
					//Disable add btn when the list is full
					_add.setDisable(true);
				}
			}
		}


/*

		if (equation.length() > 0){
			try {
				int value = Integer.parseInt(engine.eval(equation).toString());
			} catch (ScriptException e) {
				//e.printStackTrace();
				// wrong format, equation cant run
				Service delay = new TimedMessage();
				_equationMessage.setText("Please enter an equation");
				_equationMessage.setVisible(true);
				//disable the message after a few seconds
				if (!delay.isRunning()){
					delay.start();
				}
				delay.setOnSucceeded(b -> {
					_equationMessage.setVisible(false);
					delay.reset();
				});


				//Ensure the given input is within the range
				if( value < 1 || value > 99){
					Service delay = new TimedMessage();
					_equationMessage.setText("Result is out of range (1~99)");
					_equationMessage.setVisible(true);
					//disable the message after a few seconds
					if (!delay.isRunning()){
						delay.start();
					}
					delay.setOnSucceeded(ec -> {
			            _equationMessage.setVisible(false);
			            delay.reset();
			        });
				}else{
					//If valid equation, then add it to data
					_questionIndex++;
					_qs.add(_questionIndex + "#" + value + "#"+ equation);
					//TODO format is 1#32#2+30
					_data.add(new ListCell(equation));
				}

				} catch (NumberFormatException e){
					//TODO wrong answer, not int
					Service delay = new TimedMessage();
					_equationMessage.setText("Please enter numbers");
					_equationMessage.setVisible(true);
					//disable the message after a few seconds
					if (!delay.isRunning()){
						delay.start();
					}
					delay.setOnSucceeded(a -> {
			            _equationMessage.setVisible(false);
			            delay.reset();
			        });
				}
			}else{
				//TODO empty input notification
				Service delay = new TimedMessage();
				_equationMessage.setText("Please enter an equation before add");
				_equationMessage.setVisible(true);
				//disable the message after a few seconds
				if (!delay.isRunning()){
					delay.start();
				}
				delay.setOnSucceeded(eg -> {
		            _equationMessage.setVisible(false);
		            delay.reset();
		        });
			}
			*/
	}

	@FXML
	public void handlePressReturn(MouseEvent event) {
		//opens a window that confirms if the user wants to quit 
		try {
			Parent parent = FXMLLoader.load(getClass().getResource("/application/confirmation/QuitConfirmation.fxml"));
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene scene = new Scene(parent);
			_popUp = new Stage();
			_popUp.setScene(scene);
			_popUp.initOwner(stage);
			_popUp.initModality(Modality.WINDOW_MODAL);

			_popUp.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//get user result, if the user does want to quit, then quit
		boolean confirm = QuitConfirmationController.getQuit();
		if (confirm){
			try {
	        	Parent parent = FXMLLoader.load(getClass().getResource("/application/view/CustomDoPage.fxml"));
	        	Scene scene = new Scene(parent);
	        	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	        	stage.setScene(scene);
	        	stage.show();

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		}
	}

	class ListCell extends HBox {
		Label label = new Label();
		Button delete = new Button();

		ListCell(String equation) {
			super();
			label.setText(equation);
			label.setMaxWidth(Double.MAX_VALUE);
			HBox.setHgrow(label, Priority.ALWAYS);

			delete.setOnMouseClicked(this::clickDelete);
			Image deleteImage = new Image(getClass().getResourceAsStream("../image/custom/delete.png"));
			ImageView img = new ImageView(deleteImage);
			img.setFitWidth(20);
			img.setFitHeight(20);
			delete.setGraphic(img);

			this.getChildren().addAll(label, delete);
		}

		public void clickDelete(MouseEvent event) {
			System.out.println("delete");
			//TODO get the row number and remove the corresponding question
			_qs[_data.indexOf(this)]= "-";
			_data.remove(this);
		}

	}


	/*
    Support methods
 */
	private void errorMsg(String msg){
		Service delay = new TimedMessage();
		_equationMessage.setText(msg);
		_equationMessage.setVisible(true);
		//disable the message after a few seconds
		if (!delay.isRunning()){
			delay.start();
		}
		delay.setOnSucceeded(eg -> {
			_equationMessage.setVisible(false);
			delay.reset();
		});
	}

	/**
	 * Generate a unique ID
	 */
	private void generateID(){
		String root = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
		String id = "";
		for (int i = 0; i < 7; i++) {
			id = id + root.toCharArray()[(int) (Math.random() * (root.length()))];
		}
		//Recursively check if the id is unique
		if ((_manager.getPublicSuites().keySet().contains(id))
			|| (_manager.getPrivateSuites().keySet().contains(id))){
			generateID();
		}
		_id = id;
	}
}


