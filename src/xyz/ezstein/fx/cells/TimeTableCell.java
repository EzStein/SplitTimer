package xyz.ezstein.fx.cells;

import java.util.concurrent.TimeUnit;

import javafx.scene.control.*;
import xyz.ezstein.backend.*;
import xyz.ezstein.backend.util.Util;

public class TimeTableCell extends TableCell<SplitEvent,Number> {
	private Label timeLabel;
	public TimeTableCell(){
		super();
		timeLabel=new Label("");
		setGraphic(timeLabel);
	}
	
	@Override
	public void updateItem(Number timeN, boolean empty){
		super.updateItem(timeN, empty);
		if(empty||timeN==null){
			setGraphic(null);
		} else {
			long time = (long) timeN;
			timeLabel.setText(Util.nanosToReadable(time));
			setGraphic(timeLabel);
		}
	}
}
