package application;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.scene.control.Alert.AlertType;

public class ProgramController {
	private String file_path="";
	private String ip_address="";
	private String target_folder_path="ReceivedFiles/";
    @FXML
    private TabPane tabs;

    @FXML
    private TextField file_path_field;

    @FXML
    private TextField ip_destination;

    @FXML
    private ProgressIndicator prgrs_send;

    @FXML
    private Button btn_receive;
    
    @FXML
    private TextField destination_folder_path;

    @FXML
    private Label receive_status;

    @FXML
    private ProgressIndicator prgrs_receive;

    @FXML
    void destinationFolderBrowse(ActionEvent event) {
    	DirectoryChooser chooser = new DirectoryChooser();
    	File selectedDirectory = chooser.showDialog(tabs.getScene().getWindow());
    	if(selectedDirectory!=null)
    	{
    		destination_folder_path.setText(selectedDirectory.getPath());
    	}
    	
    }

    @FXML
    void onReceiveClicked(ActionEvent event) {
    	target_folder_path=destination_folder_path.getText();   	
    	if(target_folder_path.replaceAll("\\s+", "").length()>0)
    	{
    		receive_status.setVisible(true);
        	receive_status.setText("Waiting to receive ...");
    		ReceiveFile receive=new ReceiveFile(target_folder_path);
        	Thread t1=new Thread(new Runnable()
        			{
        		public void run()
        		{
        			btn_receive.setVisible(false);
        			receive.start();
        		}
        			});
        	Thread t2=new Thread(new Runnable()
        			{
        		public void run()
        		{
        			double downloaded=0.0;
        			while(downloaded <1)
        			{
        				try {
    						Thread.sleep(100);
    					} catch (InterruptedException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
        				downloaded=receive.getProgress();
        				prgrs_receive.setProgress(downloaded);
        			}
        			receive_status.setVisible(false);
        			btn_receive.setVisible(true);
        		}
        			});
        	
        	t1.start();
        	t2.start();
    	}
    	else
    	{
    		 Alert alert = new Alert(AlertType.ERROR);
             alert.setTitle("Error !");
             alert.setHeaderText("Destination folder is not available !");
             alert.setContentText("You must determine the destination folder");
             alert.showAndWait();
    	}

   
    
    }

    @FXML
    void onSendClicked(ActionEvent event) {
    	file_path=file_path_field.getText();
    	ip_address=ip_destination.getText();
    	if(file_path.replaceAll("\\s+", "").length()==0 || ip_address.replaceAll("\\s+", "").length()==0)
    	{
    		 Alert alert = new Alert(AlertType.ERROR);
             alert.setTitle("Error !");
             alert.setHeaderText("Fill all fields");
             alert.setContentText("You must fill all fields to send");
             alert.showAndWait();
    	}
    	else
    	{
    	  	SendFile send=new SendFile(file_path_field.getText(),ip_address);
    	  	Thread t1=new Thread(new Runnable(){
    			public void run(){
    				send.start();
    			}
    		});
    		Thread t2=new Thread(new Runnable()
    				{
    			public void run()
    			{
    				double percentage=0;
    					
    				while(percentage<1.0)
    				{
    					percentage=send.getProgress();
    					prgrs_send.setProgress(percentage);
    					
    					try {
    						Thread.sleep(100);
    					} catch (InterruptedException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
    					
    				}
    			}
    				});
    		
    		t1.start();
    		t2.start();
    }
    }

    @FXML
    void receiveTabOpen(ActionEvent event) {
    	SingleSelectionModel<Tab> selectionModel = tabs.getSelectionModel();
    	selectionModel.select(2);
    }

    @FXML
    void sendBrowseFile(ActionEvent event) {
    	FileChooser fileChooser = new FileChooser();
    	File file=fileChooser.showOpenDialog(tabs.getScene().getWindow());
    	if(file!=null)
    	{
    		file_path=file.getPath().toString();
        	file_path_field.setText(file_path);
    	}
    	
    }

    @FXML
    void sendTabOpen(ActionEvent event) {
    	SingleSelectionModel<Tab> selectionModel = tabs.getSelectionModel();
    	selectionModel.select(1);
    }

}
